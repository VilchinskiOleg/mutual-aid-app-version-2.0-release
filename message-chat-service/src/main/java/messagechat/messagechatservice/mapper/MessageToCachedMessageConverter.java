package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.persistent.cache.model.HashCachedMessage;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MessageToCachedMessageConverter extends BaseConverter<Message, HashCachedMessage> {

    @Override
    public void convert(Message source, HashCachedMessage destination) {
        destination.setInternalId(source.getInternalId());

        destination.setDialogId(source.getDialogId());
        destination.setAuthorId(source.getAuthorId());

        destination.setDescription(source.getDescription());
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
    }
}