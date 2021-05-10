package messagechat.messagechatservice.domain.service;

import static java.time.LocalDateTime.now;
import static messagechat.messagechatservice.domain.model.Dialog.Status.OPEN;
import static org.springframework.data.domain.PageRequest.of;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.model.page.PageMessages;
import messagechat.messagechatservice.persistent.repository.DialogRepository;
import messagechat.messagechatservice.persistent.repository.MessageRepository;
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

    public Message addMessage(Message messageData, String dialogId) {
        Message currentMessage = populateMetaDataToMessage(messageData);
        dialogId = createDialogIfNeed(dialogId);
        addMemberToDialogIfNeed(messageData.getAuthor(), dialogId);
        currentMessage.setDialogId(dialogId);
        var savedMessage = massageRepository.save(mapper.map(currentMessage, messagechat.messagechatservice.persistent.entity.Message.class));
        return mapper.map(savedMessage, Message.class);
    }

    /**
     * Get message by message id and dialog id.
     * @param dialogId
     * @param massageId
     * @return message by id or empty message if such message doesn't exist.
     */
    public Message getMessageById(String dialogId, String massageId) {
        return mapper.map(massageRepository.findById(massageId).orElse(new messagechat.messagechatservice.persistent.entity.Message()),
                          Message.class);
    }

    public PageMessages getNextPackMessageFromDialog(Integer page, Integer size, String dialogId) {
        PageRequest pageRequest = of(page, size);
        var allByDialogId = massageRepository.findAllByDialogId(pageRequest, dialogId);
        return PageMessages.builder()
                           .messages(mapper.map(allByDialogId.getContent(), new ArrayList<>(), Message.class))
                           .allPages(allByDialogId.getTotalPages())
                           .currentPage(allByDialogId.getNumber())
                           .sizeOfPage(allByDialogId.getSize())
                           .build();
    }

    /**
     * Get dialog by id.
     * @param dialogId
     * @return dialog by id or empty dialog if such dialog doesn't exist.
     */
    public Dialog getDialogById(String dialogId) {
        var dialog = dialogRepository.findById(dialogId);
        if (dialog.isEmpty()) {
            Dialog emptyDialog = new Dialog();
            return emptyDialog;
        }
        return mapper.map(dialog.get(), Dialog.class);
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
