package messagechat.messagechatservice.rest.service;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import messagechat.messagechatservice.domain.model.page.PageMessages;
import messagechat.messagechatservice.domain.service.HateoasService;
import messagechat.messagechatservice.domain.service.MessageChatService;
import messagechat.messagechatservice.rest.model.Dialog;
import messagechat.messagechatservice.rest.model.Message;
import messagechat.messagechatservice.rest.model.ShirtMessage;
import messagechat.messagechatservice.rest.model.request.MessageRequest;
import messagechat.messagechatservice.rest.model.response.ResultMessagesResponse;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;


@RestController
@RequestMapping(path = "/api/massages-chat-service")
public class MessageChatRest {

    @Resource
    private MessageChatService messageChatService;
    @Resource
    private HateoasService hateoasService;
    @Resource
    private Mapper mapper;


    @PostMapping(path = "/create-massage/{dialog-id}")
    @ResponseStatus(CREATED)
    public EntityModel<Message> createMassage(@RequestBody MessageRequest messageRequest,
                                              @PathVariable("dialog-id") String dialogId) {
        var massage = mapper.map(messageRequest, messagechat.messagechatservice.domain.model.Message.class);
        Message addedMessage = mapper.map(messageChatService.addMessage(massage, dialogId), Message.class);
        return hateoasService.wrapMessage(addedMessage);
    }

    @GetMapping(path = "/get-massage/{dialog-id}/{massage-id}")
    @ResponseStatus(OK)
    public EntityModel<Message> getMassageById(@PathVariable("dialog-id") String dialogId,
                                               @PathVariable("massage-id") String messageId) {
        Message message = mapper.map(messageChatService.getMessageById(dialogId, messageId), Message.class);
        return hateoasService.wrapMessage(message);
    }

    @GetMapping(path = "/get-pack-messages/{dialog-id}")
    @ResponseStatus(OK)
    public ResultMessagesResponse getPackMassagesFromDialog(@PathVariable("dialog-id") String dialogId,
                                                            @RequestParam("page") Integer page,
                                                            @RequestParam("size") Integer size) {
        PageMessages pageMessages = messageChatService.getNextPackMessageFromDialog(page, size, dialogId);
        var messages = hateoasService.wrapAllMessages(mapper.map(pageMessages.getMessages(), new ArrayList<>(), ShirtMessage.class));
        return ResultMessagesResponse.builder()
                                     .shirtMessages(messages)
                                     .allPages(pageMessages.getAllPages())
                                     .currentPage(pageMessages.getCurrentPage())
                                     .sizeOfPage(pageMessages.getSizeOfPage())
                                     .build();
    }

    @GetMapping(path = "/get-dialog/{dialog-id}")
    @ResponseStatus(OK)
    public Dialog getDialog(@PathVariable("dialog-id") String dialogId) {
        var dialog = messageChatService.getDialogById(dialogId);
        return mapper.map(dialog, Dialog.class);
    }
}
