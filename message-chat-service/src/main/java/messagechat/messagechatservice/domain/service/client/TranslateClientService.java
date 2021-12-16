package messagechat.messagechatservice.domain.service.client;

import java.util.List;
import java.util.Map;

public interface TranslateClientService {

    String translateMessage(String message, String sourceLang, String targetLang);

    Map<String, String> translateMessages(List<String> messages, String sourceLang, String targetLang);
}