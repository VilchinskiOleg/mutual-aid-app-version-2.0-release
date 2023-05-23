package messagechat.messagechatservice.domain.service;

import lombok.RequiredArgsConstructor;
import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.service.proessor.IdGeneratorService;
import messagechat.messagechatservice.domain.service.proessor.TranslateMessageService;
import messagechat.messagechatservice.persistent.repository.MessageRepository;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static messagechat.messagechatservice.util.Constant.Errors.MESSAGE_NOT_FOUND;
import static org.springframework.data.domain.PageRequest.of;

@Component
@RequiredArgsConstructor
public class MessageChatServiceImpl implements MessageChatService {

    private final MessageRepository messageRepository;
    private final DialogService dialogService;
    private final TranslateMessageService translateMessageService;
    private final IdGeneratorService idGeneratorService;
    private final Mapper mapper;


    @Override
    // All inner Transactions will be merged into one:
    @Transactional
    public void addMessageToDialog(Message message, String receiverId) {
        message.setInternalId(idGeneratorService.generate());
        linkDialogToMessage(message, receiverId);
        linkAuthorToMessage(message);
        translateMessageService.translateSavedMessage(message);
        saveMessage(message);
    }

    @Override
    public Message updateMessage(Message message) {
        Message currentMessage = getMessageByIdRequired(message.getInternalId());
        translateMessageService.translateSavedMessage(message);
        mapper.map(message, currentMessage);
        saveMessage(currentMessage);
        return getMessageByIdRequired(message.getInternalId());
    }

    @Override
    public Page<Message> getPageMessagesFromDialog(Integer pageNumber,
                                                   Integer size,
                                                   String dialogId,
                                                   @Nullable String dialogName) {
        PageRequest pageRequest = of(pageNumber, size);
        var dataMessagesPage = messageRepository.findAllByDialogIdOrName(pageRequest, dialogId, dialogName);
        var messagesPage = dataMessagesPage.map(message -> mapper.map(message, Message.class));
        translateMessageService.translateReturnedMessages(messagesPage.getContent());
        return messagesPage;
    }

    private void linkDialogToMessage(Message message, String receiverId) {
        Dialog linkedDialog = dialogService.getLinkedDialog(message.getDialogId(), message.getAuthorId(), receiverId);
        message.setDialog(linkedDialog);
    }

    private void linkAuthorToMessage(Message message) {
        Dialog dialog = message.getDialog();
        Member retrievedAuthor = dialog.getMemberById(message.getAuthorId());
        message.setAuthor(retrievedAuthor);
    }

    private void saveMessage(Message message) {
        if (isNull(message.getCreateAt())) {
            message.setCreateAt(now());
        } else {
            message.setModifyAt(now());
        }
        var dataMessage = mapper.map(message, messagechat.messagechatservice.persistent.entity.Message.class);
        messageRepository.save(dataMessage);
    }

    /**
     * Get Message without any related (inner) entities info.
     *
     * @param massageId - message ID.
     * @return 'messagechat.messagechatservice.domain.model.Message'.
     */
    private Message getMessageByIdRequired(String massageId) {
        return mapper.map(messageRepository.findByMessageId(massageId)
                                           .orElseThrow(() -> new ConflictException(MESSAGE_NOT_FOUND)),
                     Message.class);
    }
}