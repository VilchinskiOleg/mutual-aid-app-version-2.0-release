package messagechat.messagechatservice.domain.mapper;

import static java.util.Objects.nonNull;

import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.rest.model.ShirtMessage;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MessageToShirtMessageConverter extends BaseConverter<Message, ShirtMessage> {

    @Override
    protected ShirtMessage getDestination() {
        return new ShirtMessage();
    }

    @Override
    public void convert(Message source, ShirtMessage destination) {
        destination.setId(source.getId());
        destination.setDialogId(source.getDialogId());
        if (nonNull(source.getAuthor())) {
            destination.setAuthor(source.getAuthor()
                                        .getNickName());
        }
        destination.setDescription(source.getDescription());
    }
}
