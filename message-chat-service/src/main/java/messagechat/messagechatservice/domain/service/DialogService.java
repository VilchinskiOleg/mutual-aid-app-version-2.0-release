package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.domain.model.Dialog;
import org.springframework.data.domain.Page;

public interface DialogService {

    /**
     * User opens message-chat app and look at his chats (Dialogs).
     *
     * @param memberId
     * @param pageNumber
     * @param size
     * @return Page<Dialog> - page of Dialogs.
     */
    Page<Dialog> getPageDialogsByMemberId(String memberId, Integer pageNumber, Integer size);

    Dialog getLinkedDialog(String dialogId, String authorId, String consumerId);

    Dialog findDialogByInternalIdRequired(String dialogId);
}