package messagechat.messagechatservice.domain.service;

import static java.time.LocalDateTime.now;
import static messagechat.messagechatservice.domain.model.Dialog.Status.OPEN;
import static messagechat.messagechatservice.util.Constant.Errors.MESSAGE_NOT_FOUND;
import static org.springframework.data.domain.PageRequest.of;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.model.page.PageMessages;
import messagechat.messagechatservice.persistent.repository.DialogRepository;
import messagechat.messagechatservice.persistent.repository.MessageRepository;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class MessageChatServiceImpl implements MessageChatService {

    @Resource
    private MessageRepository massageRepository;
    @Resource
    private DialogRepository dialogRepository;
    @Resource
    private Mapper mapper;

    public Message addMessageToDialog(Message messageData, String dialogId) {
        Message currentMessage = populateMetaDataToMessage(messageData);
        dialogId = createDialogIfNeed(dialogId);
        addMemberToDialogIfNeed(messageData.getAuthor(), dialogId);
        currentMessage.setDialogId(dialogId);
        var savedMessage = massageRepository.save(mapper.map(currentMessage, messagechat.messagechatservice.persistent.entity.Message.class));
        return mapper.map(savedMessage, Message.class);
    }

    public Message getMessageById(String massageId) {
        return mapper.map(massageRepository.findById(massageId)
                                           .orElseThrow(() -> new ConflictException(MESSAGE_NOT_FOUND)),
                          Message.class);
    }

    public PageMessages getPageMessagesFromDialog(Integer pageNumber, Integer size, String dialogId) {
        PageRequest pageRequest = of(pageNumber, size);
        var messagesPageData = massageRepository.findAllByDialogId(dialogId, pageRequest);
        return PageMessages.builder()
                           .messages(mapper.map(messagesPageData.getContent(), new ArrayList<>(), Message.class))
                           .allPages(messagesPageData.getTotalPages())
                           .currentPage(messagesPageData.getNumber())
                           .sizeOfPage(messagesPageData.getSize())
                           .build();
    }

    /**
     * Get dialog by id.
     * @param dialogId
     * @return dialog by id or empty dialog if such dialog doesn't exist.
     */
    public Dialog getDialogById(String dialogId) {
        var dialogData = dialogRepository.findByInternalId(dialogId);
        if (dialogData.isEmpty()) {
            return new Dialog();
        }
        return mapper.map(dialogData.get(), Dialog.class);
    }



    private String createDialogIfNeed(String dialogId) {
        if (dialogRepository.existsByInternalId(dialogId)) {
            return dialogId;
        }
        Dialog newDialog = populateMetaDataToDialog(new Dialog());
        dialogRepository.save(mapper.map(newDialog, messagechat.messagechatservice.persistent.entity.Dialog.class));
        return newDialog.getDialogId();
    }

    private void addMemberToDialogIfNeed(Member currentMessageAuthor, String dialogId) {
        Dialog dialog = findRequiredDialogByInternalId(dialogId);
        if (dialog.hasMember(currentMessageAuthor)) {
            return;
        }
        dialog.addMember(currentMessageAuthor);
        dialogRepository.save(mapper.map(dialog, messagechat.messagechatservice.persistent.entity.Dialog.class));
    }

    private Dialog findRequiredDialogByInternalId(String internalId) {
        var dialogData = dialogRepository.findByInternalId(internalId);
        if (dialogData.isEmpty()) {
            throw new RuntimeException("Can not found such dialog");
        }
        return mapper.map(dialogData.get(), Dialog.class);
    }

    private Dialog populateMetaDataToDialog(Dialog dialog){
        dialog.setDialogId(generateId());
        dialog.setCreateAt(now());
        dialog.setStatus(OPEN);
        //todo
        return dialog;
    }

    private Message populateMetaDataToMessage(Message message) {
        message.setId(generateId());
        message.setCreateAt(now());
        //todo
        return message;
    }

    private String generateId() {
        //todo
        return "123456";
    }
}
