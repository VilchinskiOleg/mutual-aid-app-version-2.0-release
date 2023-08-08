package messagechat.messagechatservice.domain.service.proessor;

import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.service.client.TranslateClientService;
import org.common.http.autoconfiguration.model.CommonData;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.common.http.autoconfiguration.utils.Constant.DEFAULT_LANG;

public abstract class TranslateMessageService {

    @Resource(name = "rapidGoogleTranslateClientService")
    private TranslateClientService translateClientService;
    @Resource
    private CommonData commonData;

    public void translateSavedMessage(Message message) {
        //REMOVE:
        String sourceLang = commonData.getLocale().getLanguage();
        if (DEFAULT_LANG.equals(sourceLang)) {
            return;
        }
        message.setDescription(translateClientService.translateMessage(message.getDescription(), sourceLang, DEFAULT_LANG));
    }

    public void translateReturnedMessages(List<Message> messages) {
        String targetLang = commonData.getLocale().getLanguage();
        if (DEFAULT_LANG.equals(targetLang)) {
            return;
        }
        List<String> messageDescriptions = messages.stream()
                                                   .map(Message::getDescription)
                                                   .collect(toList());
        Map<String, String> resultDetails = translateClientService.translateMessages(messageDescriptions, DEFAULT_LANG, targetLang);
        messages.forEach(message -> message.setDescription(resultDetails.get(message.getDescription())));
    }
}