package messagechat.messagechatservice.rest.service;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "${message-chat.operation.create-message}",
                  nickname = "createMessage")
    @PostMapping(path = "/create-massage/{dialog-id}")
    @ResponseStatus(CREATED)
    public EntityModel<Message> createMassage(@RequestBody MessageRequest messageRequest,
                                              @PathVariable("dialog-id") String dialogId) {
        var massage = mapper.map(messageRequest, messagechat.messagechatservice.domain.model.Message.class);
        Message addedMessage = mapper.map(messageChatService.addMessageToDialog(massage, dialogId), Message.class);
        return hateoasService.wrapMessage(addedMessage);
    }

    @ApiOperation(value = "${message-chat.operation.get-message}",
                  nickname = "getMessage")
    @ApiImplicitParam(name = "massage-id", dataType = "string", paramType = "path", defaultValue = "12345")
    @GetMapping(path = "/get-massage/{massage-id}")
    @ResponseStatus(OK)
    public EntityModel<Message> getMassageById(@PathVariable("massage-id") String messageId) {
        Message message = mapper.map(messageChatService.getMessageById(messageId), Message.class);
        return hateoasService.wrapMessage(message);
    }

    @ApiOperation(value = "${message-chat.operation.get-page-messages}",
                  nickname = "getPageMessages")
    @GetMapping(path = "/get-page-messages/{dialog-id}")
    @ResponseStatus(OK)
    public ResultMessagesResponse getPageMassagesFromDialog(@PathVariable("dialog-id") String dialogId,
                                                            @RequestParam("pageNumber") Integer pageNumber,
                                                            @RequestParam("size") Integer size) {
        PageMessages pageMessages = messageChatService.getPageMessagesFromDialog(pageNumber, size, dialogId);
        var messages = hateoasService.wrapAllMessages(mapper.map(pageMessages.getMessages(), new ArrayList<>(), ShirtMessage.class));
        return ResultMessagesResponse.builder()
                                     .shirtMessages(messages)
                                     .allPages(pageMessages.getAllPages())
                                     .currentPage(pageMessages.getCurrentPage())
                                     .sizeOfPage(pageMessages.getSizeOfPage())
                                     .build();
    }

    @ApiOperation(value = "${message-chat.operation.get-dialog}",
                  nickname = "getDialog")
    @ApiImplicitParam(name = "dialog-id", dataType = "string", paramType = "path", defaultValue = "123")
    @GetMapping(path = "/get-dialog/{dialog-id}")
    @ResponseStatus(OK)
    public Dialog getDialog(@PathVariable("dialog-id") String dialogId) {
        var dialog = messageChatService.getDialogById(dialogId);
        return mapper.map(dialog, Dialog.class);
    }
}
