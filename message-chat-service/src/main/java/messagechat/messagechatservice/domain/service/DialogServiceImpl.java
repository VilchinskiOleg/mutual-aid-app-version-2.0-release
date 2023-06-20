package messagechat.messagechatservice.domain.service;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.persistent.repository.DialogRepository;
import org.common.http.autoconfiguration.service.IdGeneratorService;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.OptimisticLockException;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;
import static messagechat.messagechatservice.domain.model.Dialog.Status.ACTIVE;
import static messagechat.messagechatservice.domain.model.Dialog.Status.NOT_ACTIVE;
import static messagechat.messagechatservice.domain.model.Dialog.Type.CHANNEL;
import static messagechat.messagechatservice.domain.model.Dialog.Type.FACE_TO_FACE_DIALOG;
import static messagechat.messagechatservice.mapper.DialogToDialogUpdateByPatchConverter.UPDATE_DIALOG_BY_PATCH;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.data.domain.PageRequest.of;
import static org.tms.common.auth.configuration.utils.AuthenticationUtils.fetchAuthorOfRequestUserIdFromAuthContext;

@Slf4j
@Component
public class DialogServiceImpl implements DialogService {

    @Resource
    private DialogRepository dialogRepository;
    @Resource
    private IdGeneratorService idGeneratorService;
    @Resource
    private MemberServiceImpl memberServiceImpl;
    @Resource
    private Mapper mapper;


    @Override
    public Page<Dialog> getPageDialogsByMemberId(String memberId, Integer pageNumber, Integer size) {
        PageRequest pageRequest = of(pageNumber, size);
//        var dataDialogsPage = dialogRepository.findAllByMemberId(memberId, pageRequest);
//        return dataDialogsPage.map(dialog -> mapper.map(dialog, Dialog.class));
        return null;
    }

    @Override
    @Transactional
    public Dialog createNewChanel(String chanelName, Set<String> memberIds) {
        Dialog chanel = createNewDialog(null, chanelName);
        populateMembersIfNeed(chanel, memberIds);
        refreshChanges(chanel, fetchAuthorOfRequestUserIdFromAuthContext());
        return saveDialog(chanel);
    }

    @Override
    @Transactional
    public Dialog getLinkedDialog(@Nullable String dialogId, String authorId, @Nullable String receiverId) {
        Dialog dialog = isNull(dialogId) ? createNewDialog(receiverId, null) : findDialogByInternalIdRequired(dialogId);
        checkDialogIsActive(dialog);
        if (populateMembersIfNeed(dialog, authorId, receiverId)) {
            populateDefaultDialogNameIfNeed(dialog);
            refreshChanges(dialog, authorId);
            return saveDialog(dialog);
        }
        return dialog;
    }

    @Override
    public Dialog findDialogByInternalIdRequired(String dialogId) {
        var dataDialog = dialogRepository.findByDialogIdWithOptimisticLock(dialogId)
                                                .orElseThrow(() -> new ConflictException("DIALOG_NOT_FOUND"));
        return mapper.map(dataDialog, Dialog.class);
    }

    @Override
    @Transactional
    @Retryable(
            value = {OptimisticLockingFailureException.class, OptimisticLockException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 100))
    public Dialog updateDialog(Dialog dialogData, String authorId) {
        Dialog dialog = findDialogByInternalIdRequired(dialogData.getInternalId());
        checkDialogIsActive(dialog);
        // join/live dialog:
        Set<Member> membersChangeData = dialogData.getMembers();
        if (isNotEmpty(membersChangeData)) {
            updateMembers(membersChangeData, dialog, authorId);
        }
        // modify name or status:
        mapper.map(dialogData, dialog, UPDATE_DIALOG_BY_PATCH);
        refreshChanges(dialog, authorId);
        return saveDialog(dialog);
    }

    @Recover
    void recoverDialogUpdating(Exception ex, Dialog dialogData, String authorId) {
        log.error("Max attempts for update Dialog with ID= {} by Dialog Data= {} was surpassed!",
                dialogData.getInternalId(), dialogData, ex);
        throw new ConflictException("Try one more little beat later.");
    }

    private Dialog createNewDialog(@Nullable String consumerId, @Nullable String dialogName){
        var dialog = new Dialog();
        dialog.setInternalId(idGeneratorService.generate());
        dialog.setName(dialogName);
        if (isNull(consumerId)) {
            dialog.setType(CHANNEL);
        } else {
            dialog.setType(FACE_TO_FACE_DIALOG);
        }
        dialog.setStatus(ACTIVE);
        return dialog;
    }

    private Dialog saveDialog(Dialog dialog) {
        var dataDialog = dialogRepository.save(mapper.map(dialog,
                messagechat.messagechatservice.persistent.entity.Dialog.class));
        return mapper.map(dataDialog, Dialog.class);
    }

    private boolean populateMembersIfNeed(Dialog dialog, String authorId, String consumerId) {
        boolean anyMemberWasPopulated = false;
        if (dialog.hasNotMember(authorId)) {
            dialog.addMember(memberServiceImpl.getMemberByIdRequired(authorId));
            anyMemberWasPopulated = true;
        }
        if (FACE_TO_FACE_DIALOG == dialog.getType() && dialog.hasNotMember(consumerId)) {
            dialog.addMember(memberServiceImpl.getMemberByIdRequired(consumerId));
            anyMemberWasPopulated = true;
        }
        return anyMemberWasPopulated;
    }

    private void populateMembersIfNeed(Dialog dialog, Set<String> memberIds) {
        if (isNotEmpty(memberIds)) {
            dialog.setMembers(
                    memberIds.stream()
                            .map(memberId -> memberServiceImpl.getMemberByIdRequired(memberId))
                            .collect(toSet())
            );
        }
    }

    private void refreshChanges(Dialog dialog, String authorId) {
        if (isNull(dialog.getCreateAt())) {
            dialog.setCreateAt(now());
            dialog.setCreateByMemberId(authorId);
        } else {
            dialog.setModifyAt(now());
            dialog.setModifyByMemberId(authorId);
        }
    }

    private void checkDialogIsActive(Dialog dialog) {
        if (NOT_ACTIVE == dialog.getStatus()) {
            throw new ConflictException("DIALOG_NOT_ACTIVE");
        }
    }

    private void populateDefaultDialogNameIfNeed(Dialog dialog) {
        if (isBlank(dialog.getName())) {
            String dialogName = dialog.getMembers().stream()
                    .map(member -> isBlank(member.getNickName()) ? member.getFirstName() : member.getNickName())
                    .collect(Collectors.joining("_"));
            dialog.setName(dialogName);
        }
    }

    private void updateMembers(Set<Member> membersChangeData, Dialog dialog, String authorId) {
        Set<Member> membersWereModified;
        Member author = new Member(authorId);

        if ((membersWereModified = Sets.difference(membersChangeData, dialog.getMembers())).size() == 1
                && membersWereModified.contains(author)) {
            dialog.addMember(memberServiceImpl.getMemberByIdRequired(authorId));
        } else if ((membersWereModified = Sets.difference(dialog.getMembers(), membersChangeData)).size() == 1
                && membersWereModified.contains(author)) {
            dialog.removeMember(author);
        } else {
            log.error("DialogData with members= {} contains additional Members to make changes in current Dialog= {} unless current User with ID= {}.",
                    membersChangeData, dialog, authorId);
            throw new ConflictException("Any user must update only his own status toward Dialog, and mustn't bring along any additional Members!");
        }
    }
}