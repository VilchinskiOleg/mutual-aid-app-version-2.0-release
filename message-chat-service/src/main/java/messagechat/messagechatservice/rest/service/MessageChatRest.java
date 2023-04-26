package messagechat.messagechatservice.rest.service;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import messagechat.messagechatservice.domain.service.DialogService;
import messagechat.messagechatservice.domain.service.MessageChatService;
import messagechat.messagechatservice.rest.message.request.CreateDialogRequest;
import messagechat.messagechatservice.rest.message.request.CreateMessageRequest;
import messagechat.messagechatservice.rest.message.request.PageRequest;
import messagechat.messagechatservice.rest.message.request.UpdateMessageRequest;
import messagechat.messagechatservice.rest.message.response.DialogsPageResponse;
import messagechat.messagechatservice.rest.message.response.MessagesPageResponse;
import org.common.http.autoconfiguration.annotation.Api;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/api/massages-chat-service")
public class MessageChatRest {

    @Resource
    private MessageChatService messageChatService;
    @Resource
    private DialogService dialogService;
    @Resource
    private Mapper mapper;

    /**
     * Step 1:
     * User opens message-chat app and look at his chats (Dialogs).
     *
     * @param pageRequest - how many dialogs you want to pull from DB.
     * @param memberId - user ID, who must be provided by dialogs.
     * @return DialogsPageResponse - set of Dialogs.
     */
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

    /**
     * Step 2:
     * User chooses certain particular chat (Dialog) and must be provided by all messages (within received pageRequest)
     * from that chat (Dialog).
     *
     * @param pageRequest - how many messages you want to pull from DB.
     * @param dialogId - dialog ID, which messages you mast show from.
     * @return MessagesPageResponse - list of Messages with current dialog id and name.
     */
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

    /**
     * Step 3.1:
     * User writes messages.
     *
     * He can do it within 3 different context:
     * - 1. New message for particular user-consumer (FACE_TO_FACE_DIALOG, dialogId == null, consumerId != null)
     * - 2. Message for particular user-consumer within existed dialog (FACE_TO_FACE_DIALOG, dialogId != null, consumerId != null)
     * - 3. Message to the chanel within existed dialog (CHANNEL, dialogId != null, consumerId == null)
     *
     * @param createMessageRequest - create message request details.
     * @param consumerId - member (user) id who you want to send a message (must be null if you send message to the chanel).
     * @return MessagesPageResponse - list of Messages with current dialog id and name.
     */
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

    /**
     * Step 3.2:
     * User wants to create chanel for group of people.
     *
     * @param createDialogRequest -
     * @return MessagesPageResponse -
     */
    @Api
    @ApiOperation(value = "${message-chat.operation.create-dialog}",
            nickname = "createDialog")
    @PostMapping(path = "/create-dialog")
    @ResponseStatus(CREATED)
    public MessagesPageResponse createDialog(@RequestBody @Valid CreateDialogRequest createDialogRequest) {
        //TODO:..
        return null;
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
}