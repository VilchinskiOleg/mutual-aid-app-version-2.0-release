package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.persistent.repository.DialogRepository;
import org.common.http.autoconfiguration.service.IdGeneratorService;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tms.common.auth.configuration.model.JwtAuthenticationToken;

import javax.annotation.Resource;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;
import static messagechat.messagechatservice.domain.model.Dialog.Status.ACTIVE;
import static messagechat.messagechatservice.domain.model.Dialog.Status.NOT_ACTIVE;
import static messagechat.messagechatservice.domain.model.Dialog.Type.CHANNEL;
import static messagechat.messagechatservice.domain.model.Dialog.Type.FACE_TO_FACE_DIALOG;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.data.domain.PageRequest.of;

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
    public Dialog createNewChanel(String chanelName, Set<String> memberIds) {
        Dialog chanel = createNewDialog(null, chanelName);
        populateMembersIfNeed(chanel, memberIds);
        refreshChanges(chanel, fetchAuthorIdFromAuthContext());
        return saveDialog(chanel);
    }

    /**
     * That method have to ensure NON_REPEATABLE_READING level of isolation.
     * Otherwise, if several NEW users try to write messages in one chanel simultaneously,
     * we might lose some of them (as a Member) during update 'DialogByMember.class' because of Last Commit Win behavior.
     *
     * @param dialogId -
     * @param authorId -
     * @param receiverId -
     * @return Dialog -
     */
    @Override
    @Transactional
    public Dialog getLinkedDialog(@Nullable String dialogId, String authorId, @Nullable String receiverId) {
        Dialog dialog = isNull(dialogId) ? createNewDialog(receiverId, null) : findDialogByInternalIdRequired(dialogId);
        checkDialogIsActive(dialog);
        if (populateMembersIfNeed(dialog, authorId, receiverId)) {
            populateDefaultDialogNameIfNeed(dialog);
            refreshChanges(dialog, authorId);
            try {
                return saveDialog(dialog);
            } catch (Exception ex) {
                //todo: refactor:
                System.out.println("OK!");
                return null;
            }
        }
        return dialog;
    }

    @Override
    public Dialog findDialogByInternalIdRequired(String dialogId) {
        var dataDialog = dialogRepository.findByDialogId(dialogId)
                                                .orElseThrow(() -> new ConflictException("DIALOG_NOT_FOUND"));
        return mapper.map(dataDialog, Dialog.class);
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
        if (!memberIds.isEmpty()) {
            dialog.setMembers(
                    memberIds.stream()
                            .map(memberId -> memberServiceImpl.getMemberByIdRequired(memberId))
                            .collect(toSet())
            );
        }
    }

    private void refreshChanges(Dialog dialog, String authorId) {
        if (isNull(dialog.getCreateAt())) {
            dialog.setCreateByMemberId(authorId);
        } else {
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

    private String fetchAuthorIdFromAuthContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) auth).getProfileId();
        }
        // If you call that method from TEST account (not like usual user), method will write your login as a authorId:
        return auth.getPrincipal().toString();
    }
}