package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.persistent.entity.Dialog;
import messagechat.messagechatservice.persistent.entity.Member;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

import static messagechat.messagechatservice.mapper.DialogToDataDialogForLinkToMessageConverter.LINK_DIALOG_TO_MESSAGE;
import static messagechat.messagechatservice.mapper.MemberToDataMemberForLinkToMessageConverter.LINK_AUTHOR_TO_MESSAGE;

@Component
public class MessageToDataMessageConverter extends BaseConverter<Message, messagechat.messagechatservice.persistent.entity.Message> {

    @Override
    public void convert(Message source, messagechat.messagechatservice.persistent.entity.Message destination) {
        destination.setId(source.getId());
        destination.setMessageId(source.getInternalId());

        // Put into persistent entities only ID-s or Version for find existed appropriate entities
        // and sync with them current changes of message:
        destination.setDialog(mapper.map(source.getDialog(), Dialog.class, LINK_DIALOG_TO_MESSAGE));
        destination.setAuthor(mapper.map(source.getAuthor(), Member.class, LINK_AUTHOR_TO_MESSAGE));

        destination.setDescription(source.getDescription());
        destination.setCreateAt(source.getCreateAt());
        destination.setModifyAt(source.getModifyAt());
    }
}