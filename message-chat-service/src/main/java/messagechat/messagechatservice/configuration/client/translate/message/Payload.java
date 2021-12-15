package messagechat.messagechatservice.configuration.client.translate.message;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.springframework.util.CollectionUtils.isEmpty;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Payload {

    private List<Translation> translations;

    public String getFirstTranslation() {
        if (isEmpty(translations)) {
            return null;
        }
        return translations.get(INTEGER_ZERO)
                           .getTranslatedText();
    }
}