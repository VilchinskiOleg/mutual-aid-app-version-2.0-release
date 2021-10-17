package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Message;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class MessageToApiMessageConverter extends BaseConverter<Message, messagechat.messagechatservice.rest.model.Message> {

    @Override
    protected messagechat.messagechatservice.rest.model.Message getDestination() {
        return new messagechat.messagechatservice.rest.model.Message();
    }

    @Override
    public void convert(Message source, messagechat.messagechatservice.rest.model.Message destination) {
        destination.setId(source.getId());

        destination.setDialogId(source.getDialogId());
        destination.setDescription(source.getDescription());

        destination.setAuthorId(source.getAuthorId());
        destination.setAuthorNickName(source.getAuthorNickName());

        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
        destination.setIsModified(source.isModified());
    }
}