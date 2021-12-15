package messagechat.messagechatservice.configuration.client.translate.message;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleTranslateResponse {

    @JsonProperty("data")
    private Payload payload;

    public Payload getPayload() {
        if (isNull(payload)) {
            payload = new Payload();
        }
        return payload;
    }
}