package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.persistent.cache.CachedMessage;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class CachedMessageToMessageConverter extends BaseConverter<CachedMessage, Message> {

    @Override
    public void convert(CachedMessage source, Message destination) {
        destination.setId(source.getId());
        destination.setInternalId(source.getInternalId());

        Dialog dialog = new Dialog();
        dialog.setInternalId(source.getDialogId());
        destination.setDialog(dialog);

        destination.setAuthor(new Member(source.getAuthorId()));
        destination.setDescription(source.getDescription());
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
    }
}