package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.domain.model.Dialog;
import org.springframework.data.domain.Page;

public interface DialogService {

    /**
     * User opens message-chat app and look at his chats (Dialogs).
     *
     * @param memberId -
     * @param pageNumber -
     * @param size -
     * @return Page<Dialog> - page of Dialogs.
     */
    Page<Dialog> getPageDialogsByMemberId(String memberId, Integer pageNumber, Integer size);


    /**
     * Finds linked dialog by dialogId or creates new one (if dialogId == null) for this author and consumer.
     *
     * Creation new dialog by that method expects that it will be FACE_TO_FACE_DIALOG. If dialog by this (received)
     * dialogId already exists it can be FACE_TO_FACE_DIALOG or CHANEL as well.
     *
     * @param dialogId -
     * @param authorId -
     * @param receiverId -
     * @return Dialog -
     */
    Dialog getLinkedDialog(String dialogId, String authorId, String receiverId);

    Dialog findDialogByInternalIdRequired(String dialogId);
}