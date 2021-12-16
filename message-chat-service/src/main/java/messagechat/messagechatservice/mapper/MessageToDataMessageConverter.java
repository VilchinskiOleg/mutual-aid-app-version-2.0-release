package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.persistent.entity.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MessageToDataMessageConverter extends BaseConverter<Message, messagechat.messagechatservice.persistent.entity.Message> {

    @Override
    protected messagechat.messagechatservice.persistent.entity.Message getDestination() {
        return new messagechat.messagechatservice.persistent.entity.Message();
    }

    @Override
    public void convert(Message source, messagechat.messagechatservice.persistent.entity.Message destination) {
        destination.setId(source.getId());
        destination.setDialogId(source.getDialogId());
        destination.setAuthor(mapper.map(source.getAuthor(), Member.class));
        destination.setDescription(source.getDescription());
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
    }
}