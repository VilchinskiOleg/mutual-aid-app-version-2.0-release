package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.rest.model.ShirtMessage;
import messagechat.messagechatservice.rest.model.Message;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import java.util.List;

public interface HateoasService {

    EntityModel<Message> wrapMessage(Message message);
    CollectionModel<EntityModel<ShirtMessage>> wrapAllMessages(List<ShirtMessage> messages);
}
