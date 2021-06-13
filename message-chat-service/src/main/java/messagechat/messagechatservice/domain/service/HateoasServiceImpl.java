package messagechat.messagechatservice.domain.service;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.EntityModel.of;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import messagechat.messagechatservice.rest.model.Message;
import messagechat.messagechatservice.rest.model.ShirtMessage;
import messagechat.messagechatservice.rest.service.MessageChatRest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HateoasServiceImpl implements HateoasService {

    public EntityModel<Message> wrapMessage(Message message) {
        Link linkToAllMessages = linkTo(methodOn(MessageChatRest.class).getDialog(message.getDialogId())).withRel("dialog-for-this-message");
        return of(message, linkToAllMessages);
    }

    public CollectionModel<EntityModel<ShirtMessage>> wrapAllMessages(List<ShirtMessage> messages) {
        var wrappageMessages = messages.stream()
                            .map(shirtMessage -> {
                                Link selfLink = linkTo(methodOn(MessageChatRest.class).getMassageById(shirtMessage.getId())).withSelfRel();
                                return of(shirtMessage, selfLink);
                            })
                            .collect(toList());
        return CollectionModel.of(wrappageMessages);
    }
}
