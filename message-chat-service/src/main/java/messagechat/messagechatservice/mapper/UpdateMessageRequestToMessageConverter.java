package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.rest.message.request.UpdateMessageRequest;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class UpdateMessageRequestToMessageConverter extends BaseConverter<UpdateMessageRequest, Message> {

    @Override
    protected Message getDestination() {
        return new Message();
    }

    @Override
    public void convert(UpdateMessageRequest source, Message destination) {
        destination.setInternalId(source.getMessageId());
        destination.setDescription(source.getDescription());
    }
}