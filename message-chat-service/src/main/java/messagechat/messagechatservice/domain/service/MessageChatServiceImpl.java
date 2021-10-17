package messagechat.messagechatservice.domain.service;

import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static messagechat.messagechatservice.util.Constant.Errors.MESSAGE_NOT_FOUND;
import static org.springframework.data.domain.PageRequest.of;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.service.proessor.DialogService;
import messagechat.messagechatservice.domain.service.proessor.TranslateMessageService;
import messagechat.messagechatservice.persistent.repository.MessageRepository;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class MessageChatServiceImpl implements MessageChatService {

    @Resource
    private MessageRepository messageRepository;
    @Resource
    private DialogService dialogService;
    @Resource
    private TranslateMessageService translateMessageService;
    @Resource
    private Mapper mapper;

    @Override
    public Message addMessageToDialog(Message message, String consumerId) {
        linkMessageToDialog(message, consumerId);
        translateMessageService.translateMessageForSave(message);
        return saveMessage(message);
    }

    @Override
    public Message updateMessage(Message message) {
        Message currentMessage = getMessageByIdRequired(message.getId());
        translateMessageService.translateMessageForSave(message);
        mapper.map(message, currentMessage);
        return saveMessage(currentMessage);
    }

    @Override
    public Page<Message> getPageMessagesFromDialog(Integer pageNumber, Integer size, String dialogId) {
        PageRequest pageRequest = of(pageNumber, size);
        var dataMessagesPage = messageRepository.findAllByDialogId(dialogId, pageRequest);
        return dataMessagesPage.map(message -> mapper.map(message, Message.class));
    }

    private void linkMessageToDialog(Message message, String consumerId) {
        Dialog linkedDialog = dialogService.getLinkedDialog(message.getDialogId(), message.getAuthorId(), consumerId);
        message.setDialogId(linkedDialog.getId());
        Member author = linkedDialog.getMemberById(message.getAuthorId());
        mapper.map(author, message.getAuthor());
    }

    private Message saveMessage(Message message) {
        if (isNull(message.getCreateAt())) {
            message.setCreateAt(now());
        } else {
            message.setModifyAt(now());
        }
        var dataMessage = messageRepository.save(mapper.map(message,
                messagechat.messagechatservice.persistent.entity.Message.class));
        return mapper.map(dataMessage, Message.class);
    }

    private Message getMessageByIdRequired(String massageId) {
        return mapper.map(messageRepository.findById(massageId)
                                           .orElseThrow(() -> new ConflictException(MESSAGE_NOT_FOUND)),
                     Message.class);
    }
}