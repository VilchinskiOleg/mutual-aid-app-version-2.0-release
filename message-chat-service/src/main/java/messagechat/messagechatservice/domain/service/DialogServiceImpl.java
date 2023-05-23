package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.service.proessor.IdGeneratorService;
import messagechat.messagechatservice.persistent.repository.DialogRepository;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.jetbrains.annotations.Nullable;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static java.util.Objects.isNull;
import static messagechat.messagechatservice.domain.model.Dialog.Status.ACTIVE;
import static messagechat.messagechatservice.domain.model.Dialog.Status.NOT_ACTIVE;
import static messagechat.messagechatservice.domain.model.Dialog.Type.CHANNEL;
import static messagechat.messagechatservice.domain.model.Dialog.Type.FACE_TO_FACE_DIALOG;
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
        var dataDialogsPage = dialogRepository.findAllByMemberId(memberId, pageRequest);
        return dataDialogsPage.map(dialog -> mapper.map(dialog, Dialog.class));
    }

    @Override
    public Dialog getLinkedDialog(String dialogId, String authorId, String consumerId) {
        Dialog dialog = isNull(dialogId) ? createNewDialog(consumerId) : findDialogByInternalIdRequired(dialogId);
        checkDialogIsActive(dialog);
        populateMembersIfNeed(dialog, authorId, consumerId);
        refreshChanges(dialog, authorId);
        return saveDialog(dialog);
    }

    public Dialog createNewDialog(@Nullable String consumerId){
        var dialog = new Dialog();
        dialog.setInternalId(idGeneratorService.generate());
        if (isNull(consumerId)) {
            dialog.setType(CHANNEL);
        } else {
            dialog.setType(FACE_TO_FACE_DIALOG);
        }
        dialog.setStatus(ACTIVE);
        return dialog;
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
    public Dialog getLinkedDialog(String dialogId, String authorId, String receiverId) {
        Dialog dialog = isNull(dialogId) ? createNewDialog(receiverId) : findDialogByInternalIdRequired(dialogId);
        checkDialogIsActive(dialog);
        if (populateMembersIfNeed(dialog, authorId, receiverId)) {
            refreshChanges(dialog, authorId);
            return saveDialog(dialog);
        } else {
            return dialog;
        }
    }

    @Override
    public Dialog findDialogByInternalIdRequired(String dialogId) {
        var dataDialog = dialogRepository.findByDialogId(dialogId)
                                                .orElseThrow(() -> new ConflictException("DIALOG_NOT_FOUND"));
        return mapper.map(dataDialog, Dialog.class);
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
}