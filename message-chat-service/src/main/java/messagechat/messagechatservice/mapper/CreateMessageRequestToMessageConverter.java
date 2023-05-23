package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.rest.message.request.CreateMessageRequest;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

@Component
public class CreateMessageRequestToMessageConverter extends BaseConverter<CreateMessageRequest, Message> {

    @Override
    protected Message getDestination() {
        return new Message();
    }

    @Override
    public void convert(CreateMessageRequest source, Message destination) {
        destination.setAuthor(new Member(source.getAuthorId()));
        destination.setDialog(Dialog.builder().internalId(source.getDialogId()).build());
        destination.setDescription(source.getDescription());
    }
}