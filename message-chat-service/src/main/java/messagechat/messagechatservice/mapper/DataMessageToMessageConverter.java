package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.persistent.entity.Message;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class DataMessageToMessageConverter extends BaseConverter<Message, messagechat.messagechatservice.domain.model.Message> {

    @Override
    protected messagechat.messagechatservice.domain.model.Message getDestination() {
        return new messagechat.messagechatservice.domain.model.Message();
    }

    @Override
    public void convert(Message source, messagechat.messagechatservice.domain.model.Message destination) {
        destination.setId(source.getId());
        destination.setDialogId(source.getDialogId());
        destination.setAuthor(mapper.map(source.getAuthor(), Member.class));
        destination.setDescription(source.getDescription());
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
    }
}