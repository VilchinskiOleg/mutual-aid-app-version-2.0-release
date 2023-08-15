package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.persistent.cache.CachedMessage;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MessageToCachedMessageConverter extends BaseConverter<Message, CachedMessage> {

    @Override
    public void convert(Message source, CachedMessage destination) {
        destination.setId(source.getId());
        destination.setInternalId(source.getInternalId());

        destination.setDialogId(source.getDialogId());
        destination.setAuthorId(source.getAuthorId());

        destination.setDescription(source.getDescription());
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
    }
}