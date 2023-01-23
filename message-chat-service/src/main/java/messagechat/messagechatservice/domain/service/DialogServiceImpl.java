package messagechat.messagechatservice.domain.service;

import static java.util.Objects.isNull;
import static messagechat.messagechatservice.domain.model.Dialog.Status.ACTIVE;
import static messagechat.messagechatservice.domain.model.Dialog.Status.NOT_ACTIVE;
import static messagechat.messagechatservice.domain.model.Dialog.Type.CHANNEL;
import static messagechat.messagechatservice.domain.model.Dialog.Type.FACE_TO_FACE_DIALOG;
import static org.springframework.data.domain.PageRequest.of;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.service.DialogService;
import messagechat.messagechatservice.domain.service.proessor.IdGeneratorService;
import messagechat.messagechatservice.domain.service.proessor.ProfileService;
import messagechat.messagechatservice.persistent.repository.DialogRepository;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

@Component
public class DialogServiceImpl implements DialogService {

    @Resource
    private DialogRepository dialogRepository;
    @Resource
    private IdGeneratorService idGeneratorService;
    @Resource
    private ProfileService profileService;
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

    @Override
    public Dialog findDialogByInternalIdRequired(String dialogId) {
        var dataDialog = dialogRepository.findByInternalId(dialogId)
                                                .orElseThrow(() -> new ConflictException("DIALOG_NOT_FOUND"));
        return mapper.map(dataDialog, Dialog.class);
    }


    private Dialog createNewDialog(String consumerId){
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

    private Dialog saveDialog(Dialog dialog) {
        var dataDialog = dialogRepository.save(mapper.map(dialog,
                messagechat.messagechatservice.persistent.entity.Dialog.class));
        return mapper.map(dataDialog, Dialog.class);
    }

    private void populateMembersIfNeed(Dialog dialog, String authorId, String consumerId) {
        if (dialog.hasNotMember(authorId)) {
            dialog.addMember(profileService.getMemberByIdRequired(authorId));
        }
        if (FACE_TO_FACE_DIALOG == dialog.getType() && dialog.hasNotMember(consumerId)) {
            dialog.addMember(profileService.getMemberByIdRequired(consumerId));
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
}