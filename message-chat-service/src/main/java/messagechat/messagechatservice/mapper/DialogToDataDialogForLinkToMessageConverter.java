package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Dialog;
import org.mapper.autoconfiguration.annotation.ModelMapperContext;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

import static messagechat.messagechatservice.mapper.DialogToDataDialogForLinkToMessageConverter.LINK_DIALOG_TO_MESSAGE;

@Component
@ModelMapperContext(value = LINK_DIALOG_TO_MESSAGE)
public class DialogToDataDialogForLinkToMessageConverter extends BaseConverter<Dialog, messagechat.messagechatservice.persistent.entity.Dialog> {

    public static final String LINK_DIALOG_TO_MESSAGE = "link_dialog_to_message";

    @Override
    public void convert(Dialog source, messagechat.messagechatservice.persistent.entity.Dialog destination) {
        destination.setId(source.getId());
        destination.setVersion(source.getVersion());
        destination.setDialogId(source.getInternalId());
    }
}