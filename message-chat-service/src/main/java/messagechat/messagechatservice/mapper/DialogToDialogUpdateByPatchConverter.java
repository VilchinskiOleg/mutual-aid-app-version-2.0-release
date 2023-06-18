package messagechat.messagechatservice.mapper;

import messagechat.messagechatservice.domain.model.Dialog;
import org.mapper.autoconfiguration.annotation.ModelMapperContext;
import org.mapper.autoconfiguration.converter.BaseConverter;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;
import static messagechat.messagechatservice.mapper.DialogToDialogUpdateByPatchConverter.UPDATE_DIALOG_BY_PATCH;

@Component
@ModelMapperContext(UPDATE_DIALOG_BY_PATCH)
public class DialogToDialogUpdateByPatchConverter extends BaseConverter<Dialog, Dialog> {

    public static final String UPDATE_DIALOG_BY_PATCH = "UPDATE_DIALOG_BY_PATCH";

    @Override
    public void convert(Dialog source, Dialog destination) {
        if (nonNull(source.getName())) {
            destination.setName(source.getName());
        }
        if (nonNull(source.getStatus())) {
            destination.setStatus(source.getStatus());
        }
    }
}