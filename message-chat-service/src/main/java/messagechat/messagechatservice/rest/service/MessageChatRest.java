package messagechat.messagechatservice.rest.service;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import messagechat.messagechatservice.domain.service.DialogService;
import messagechat.messagechatservice.domain.service.MessageChatService;
import messagechat.messagechatservice.rest.message.request.CreateDialogRequest;
import messagechat.messagechatservice.rest.message.request.CreateMessageRequest;
import messagechat.messagechatservice.rest.message.request.PageRequest;
import messagechat.messagechatservice.rest.message.request.UpdateMessageRequest;
import messagechat.messagechatservice.rest.message.response.DialogResponse;
import messagechat.messagechatservice.rest.message.response.DialogsPageResponse;
import messagechat.messagechatservice.rest.message.response.MessagesPageResponse;
import messagechat.messagechatservice.rest.model.Dialog;
import org.common.http.autoconfiguration.annotation.Api;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.ArrayList;

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
    @PostMapping(path = "/get-page-messages/{dialog-id}/{dialog-name}")
    @ResponseStatus(OK)
    public MessagesPageResponse getPageMassagesFromDialog(@RequestBody @Valid PageRequest pageRequest,
                                                          @PathVariable("dialog-id") String dialogId,
                                                          @PathVariable(value = "dialog-name", required = false) String dialogName) {
        var pageMessages = messageChatService.getPageMessagesFromDialog(
                pageRequest.getPageNumber(),
                pageRequest.getSize(),
                dialogId,
                dialogName);
        var messages = mapper.map(pageMessages.getContent(), new ArrayList<>(), messagechat.messagechatservice.rest.model.Message.class);
        return new MessagesPageResponse(dialogId, dialogName, messages);
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
     * @param request - create message request details.
     * @return MessagesPageResponse - list of Messages with current dialog id and name.
     */
    @Api
    @ApiOperation(value = "${message-chat.operation.create-message}",
                  nickname = "createMessage")
    @PostMapping(path = "/create-massage")
    @ResponseStatus(CREATED)
    public MessagesPageResponse createMassage(@RequestBody @Valid CreateMessageRequest request) {
        var message = mapper.map(request, messagechat.messagechatservice.domain.model.Message.class);
        messageChatService.addMessageToDialog(message, request.getReceiverId());
        return getPageMassagesFromDialog(
                new PageRequest(request.getPageNumber(), request.getSize()),
                request.getDialogId(),
                request.getDialogName());
    }

    /**
     * Step 3.2:
     * User wants to create chanel for group of people.
     *
     * @param request - model of request to create new Chanel, that contains all necessary data.
     * @return DialogResponse - created Dialog model.
     */
    @Api
    @ApiOperation(value = "${message-chat.operation.create-chanel}",
            nickname = "createChanel")
    @PostMapping(path = "/create-chanel")
    @ResponseStatus(CREATED)
    public DialogResponse createChanel(@RequestBody @Valid CreateDialogRequest request) {
        var dialog = dialogService.createNewChanel(request.getDialogName(), request.getMemberIds());
        return new DialogResponse(mapper.map(dialog, Dialog.class));
    }

    /**
     * Step 4.1:
     * User want to join/leave chanel or rename current Dialog.
     *
     * @param dialogData - dialog changes to update.
     * @return updated Dialog.
     */
    @Api
    @ApiOperation(value = "${message-chat.operation.update-dialog}",
            nickname = "updateDialog")
    @PatchMapping("/update-dialog/{author-id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasRole('UPDATE_DIALOG') or #authorId == authentication.profileId")
    public DialogResponse updateDialog(@RequestBody @Valid Dialog dialogData,
                                       @PathVariable("author-id") String authorId) {
        var updatedDialog = dialogService.updateDialog(
                mapper.map(dialogData, messagechat.messagechatservice.domain.model.Dialog.class),
                authorId);
        return new DialogResponse(mapper.map(updatedDialog, Dialog.class));
    }

    @Api
    @ApiOperation(value = "${message-chat.operation.update-message}",
                  nickname = "updateMessage")
    @PutMapping(path = "/update-message/{author-id}")
    @ResponseStatus(OK)
    @PreAuthorize("hasRole('UPDATE_MESSAGE') or #authorId == authentication.profileId")
    public MessagesPageResponse updateMassage(@RequestBody @Valid UpdateMessageRequest updateMessageRequest,
                                              @PathVariable("author-id") String authorId) {
        var message = mapper.map(updateMessageRequest, messagechat.messagechatservice.domain.model.Message.class);
        message = messageChatService.updateMessage(message);
        //TODO: check :
//        var pageMessages = messageChatService.getPageMessagesFromDialog(updateMessageRequest.getPageNumber(),
//                                                                                       updateMessageRequest.getSize(),
//                                                                                       message.getDialogId());
//        return mapper.map(pageMessages, MessagesPageResponse.class);
        return null;
    }
}