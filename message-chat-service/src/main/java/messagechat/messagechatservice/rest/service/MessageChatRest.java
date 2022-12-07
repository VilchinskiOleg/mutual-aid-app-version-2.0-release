package messagechat.messagechatservice.rest.service;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import messagechat.messagechatservice.domain.service.MessageChatService;
import messagechat.messagechatservice.domain.service.proessor.DialogService;
import messagechat.messagechatservice.rest.message.request.PageRequest;
import messagechat.messagechatservice.rest.message.request.UpdateMessageRequest;
import messagechat.messagechatservice.rest.message.response.DialogsPageResponse;
import messagechat.messagechatservice.rest.message.request.CreateMessageRequest;
import messagechat.messagechatservice.rest.message.response.MessagesPageResponse;
import org.common.http.autoconfiguration.annotation.Api;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/massages-chat-service")
public class MessageChatRest {

    @Resource
    private MessageChatService messageChatService;
    @Resource
    private DialogService dialogService;
    @Resource
    private Mapper mapper;

    @Api
    @ApiOperation(value = "${message-chat.operation.create-message}",
                  nickname = "createMessage")
    @PostMapping(path = "/create-massage/{consumer-id}")
    @ResponseStatus(CREATED)
    public MessagesPageResponse createMassage(@RequestBody @Valid CreateMessageRequest createMessageRequest,
                                              @PathVariable(value = "consumer-id", required = false) String consumerId) {
        var message = mapper.map(createMessageRequest, messagechat.messagechatservice.domain.model.Message.class);
        message = messageChatService.addMessageToDialog(message, consumerId);
        var pageMessages = messageChatService.getPageMessagesFromDialog(createMessageRequest.getPageNumber(),
                                                                                       createMessageRequest.getSize(),
                                                                                       message.getDialogId());
        return mapper.map(pageMessages, MessagesPageResponse.class);
    }

    @Api
    @ApiOperation(value = "${message-chat.operation.update-message}",
                  nickname = "updateMessage")
    @PutMapping(path = "/update-massage")
    @ResponseStatus(OK)
    public MessagesPageResponse updateMassage(@RequestBody @Valid UpdateMessageRequest updateMessageRequest) {
        var message = mapper.map(updateMessageRequest, messagechat.messagechatservice.domain.model.Message.class);
        message = messageChatService.updateMessage(message);
        var pageMessages = messageChatService.getPageMessagesFromDialog(updateMessageRequest.getPageNumber(),
                                                                                       updateMessageRequest.getSize(),
                                                                                       message.getDialogId());
        return mapper.map(pageMessages, MessagesPageResponse.class);
    }

    @Api
    @ApiOperation(value = "${message-chat.operation.get-page-messages}",
                  nickname = "getPageMessages")
    @PostMapping(path = "/get-page-messages/{dialog-id}")
    @ResponseStatus(OK)
    public MessagesPageResponse getPageMassagesFromDialog(@RequestBody @Valid PageRequest pageRequest,
                                                          @PathVariable("dialog-id") String dialogId) {
        var pageMessages = messageChatService.getPageMessagesFromDialog(pageRequest.getPageNumber(),
                                                                                       pageRequest.getSize(),
                                                                                       dialogId);
        return mapper.map(pageMessages, MessagesPageResponse.class);
    }

    @Api
    @ApiOperation(value = "${message-chat.operation.get-dialog}",
                  nickname = "getDialog")
    @ApiImplicitParam(name = "member-id", dataType = "string", paramType = "path", defaultValue = "123")
    @PostMapping(path = "/get-page-dialogs/{member-id}")
    @ResponseStatus(OK)
    public DialogsPageResponse getPageDialogsForMember(@RequestBody @Valid PageRequest pageRequest,
                                                       @PathVariable("member-id") String memberId) {
        var pageDialogs = dialogService.getPageDialogsByMemberId(
                memberId, pageRequest.getPageNumber(), pageRequest.getSize());
        return mapper.map(pageDialogs, DialogsPageResponse.class);
    }
}