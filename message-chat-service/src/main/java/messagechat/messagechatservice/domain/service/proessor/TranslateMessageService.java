package messagechat.messagechatservice.domain.service.proessor;

import static org.common.http.autoconfiguration.utils.Constant.DEFAULT_LANG;

import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.service.client.GoogleTranslateClientService;
import org.common.http.autoconfiguration.model.CommonData;
import javax.annotation.Resource;

public abstract class TranslateMessageService {

    @Resource
    private GoogleTranslateClientService translateClientService;
    @Resource
    private CommonData commonData;

    public void translateMessageForSave(Message message) {
        String sourceLang = commonData.getLocale().getLanguage();
        if (DEFAULT_LANG.equals(sourceLang)) {
            return;
        }
        message.setDescription(translateClientService.translateMessage(message.getDescription(), sourceLang, DEFAULT_LANG));
    }

    public void translateMessageForReturn(Message message) {
        String targetLang = commonData.getLocale().getLanguage();
        if (DEFAULT_LANG.equals(targetLang)) {
            return;
        }
        message.setDescription(translateClientService.translateMessage(message.getDescription(), DEFAULT_LANG, targetLang));
    }
}