package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.persistent.entity.Message;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class DataMessageToMessageConverter extends BaseConverter<Message, messagechat.messagechatservice.domain.model.Message> {

    @Override
    public void convert(Message source, messagechat.messagechatservice.domain.model.Message destination) {
        destination.setId(source.getId());
        destination.setInternalId(source.getMessageId());
        destination.setDialog(mapper.map(source.getDialog(), Dialog.class));
        destination.setAuthor(mapper.map(source.getAuthor(), Member.class));
        destination.setDescription(source.getDescription());
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
    }
}