package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.domain.model.Dialog;
import org.springframework.lang.Nullable;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface DialogService{

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
     * @return new (created) or existed (fetched from DB) Dialog.
     */
    Dialog getLinkedDialog(@Nullable String dialogId, String authorId, @Nullable String receiverId);

    Dialog createNewChanel(String chanelName, Set<String> memberIds);

    /**
     * For example add/remove user from Dialog, or rename Dialog.
     *
     * @param dialogData - data for comparing and changing of model.
     * @param authorId - member ID who makes changes.
     * @return updated Dialog.
     */
    Dialog updateDialog(Dialog dialogData, String authorId);

    Dialog findDialogByInternalIdRequired(String dialogId);
}