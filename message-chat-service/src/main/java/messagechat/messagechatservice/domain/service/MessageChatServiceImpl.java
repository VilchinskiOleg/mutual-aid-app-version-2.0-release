package messagechat.messagechatservice.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messagechat.messagechatservice.configuration.MessageChatConfigProps;
import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.service.proessor.CacheManagerImpl;
import messagechat.messagechatservice.domain.service.proessor.TranslateMessageService;
import messagechat.messagechatservice.persistent.repository.MessageRepository;
import org.common.http.autoconfiguration.service.IdGeneratorService;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static messagechat.messagechatservice.util.Constant.Errors.MESSAGE_NOT_FOUND;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.data.domain.PageRequest.of;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageChatServiceImpl implements MessageChatService {

    private final MessageRepository messageRepository;
    private final DialogService dialogService;
    private final TranslateMessageService translateMessageService;
    private final CacheManagerImpl cacheManager;
    private final IdGeneratorService idGeneratorService;
    private final MessageChatConfigProps messageChatConfigProps;
    private final Mapper mapper;


    @Override
    // All inner Transactions will be merged into one:
    @Transactional
    public void addMessageToDialog(Message message, String receiverId) {
        message.setInternalId(idGeneratorService.generate());
        linkDialogToMessage(message, receiverId);
        linkAuthorToMessage(message);
        saveMessage(message);
    }

    @Override
    public Message updateMessage(Message message) {
        Message currentMessage = getMessageByIdRequired(message.getInternalId());
        mapper.map(message, currentMessage);
        saveMessage(currentMessage);
        return getMessageByIdRequired(message.getInternalId());
    }

    @Override
    public List<Message> getPageMessagesFromDialog(Integer pageNumber,
                                                   Integer size,
                                                   String dialogId,
                                                   @Nullable String dialogName) {
        List<Message> messages;

        if (messageChatConfigProps.isTranslationEnabled()) {
            log.info("Try to find translated messages into cache.");
            messages = cacheManager.readMessagesFromCache(dialogId, pageNumber, size);
            if (isNotEmpty(messages)) {
                log.info("Have retrieved messages from cache successfully.");
                return messages;
            }
            log.info("Didn't manage to read messages from cache. Gonna use usual flow with DB and result handling.");
        }

        messages = findAllByDialogIdOrName(dialogId, of(pageNumber, size), dialogName);
        translateMessageService.translateReturnedMessages(messages);

        if (messageChatConfigProps.isTranslationEnabled()) {
            cacheManager.cacheTranslatedMessages(messages, pageNumber, size);
        }

        return messages;
    }

    void linkDialogToMessage(Message message, String receiverId) {
        Dialog linkedDialog = dialogService.getLinkedDialog(message.getDialogId(), message.getAuthorId(), receiverId);
        message.setDialog(linkedDialog);
    }

    void linkAuthorToMessage(Message message) {
        Dialog dialog = message.getDialog();
        Member retrievedAuthor = dialog.getMemberById(message.getAuthorId());
        message.setAuthor(retrievedAuthor);
    }

    List<Message> findAllByDialogIdOrName(String dialogId, PageRequest pageRequest, @Nullable String dialogName) {
        var dataMessagesPage = messageRepository.findAllByDialogIdOrName(pageRequest, dialogId, dialogName);
        return dataMessagesPage.map(message -> mapper.map(message, Message.class)).getContent();
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