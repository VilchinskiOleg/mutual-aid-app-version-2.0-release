package messagechat.messagechatservice.mapper.update;

import messagechat.messagechatservice.domain.model.Message;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MessageToMessageConverter extends BaseConverter<Message, Message> {

    @Override
    protected Message getDestination() {
        throw new UnsupportedOperationException("Such converter can be used with existed destinations only, not new");
    }

    @Override
    public void convert(Message source, Message destination) {
        destination.setDescription(source.getDescription());
    }
}