package messagechat.messagechatservice.domain.service.client;

import static messagechat.messagechatservice.util.Constant.Errors.TRANSLATION_ERROR;
import static messagechat.messagechatservice.util.Constant.Service.GoogleTranslateApi.*;
import static org.common.http.autoconfiguration.utils.Constant.OK_HTTP_CODE;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import messagechat.messagechatservice.configuration.client.GoogleTranslateApiClient;
import messagechat.messagechatservice.configuration.client.GoogleTranslateApiProperties;
import messagechat.messagechatservice.configuration.client.message.GoogleTranslateResponse;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
@Component
public class GoogleTranslateClientService {

    @Resource
    private GoogleTranslateApiClient translateApi;
    @Resource
    private GoogleTranslateApiProperties translateApiProperties;

    public String translateMessage(String message, String sourceLang, String targetLang) {
        var request = buildRequest(message, sourceLang, targetLang);
        var response = translateApi.translate(translateApiProperties.getHost(),
                                                        translateApiProperties.getToken(),
                                                        translateApiProperties.getEncoding(),
                                                        request);
        checkResponse(response);
        GoogleTranslateResponse responseData = deserializeResponse(response.body());
        return responseData.getPayload().getFirstTranslation();
    }

    private String buildRequest(String message, String sourceLang, String targetLang) {
        String text = TEXT_DELIMITING_PATTERN.matcher(message).replaceAll(TEXT_DELIMITER);
        return  TEXT_FORM_BODY_KEY + text +
                TARGET_LANG_FORM_BODY_KEY + targetLang +
                SOURCE_LANG_FORM_BODY_KEY + sourceLang;
    }

    private void checkResponse(Response response) {
        if (OK_HTTP_CODE != response.status()) {
            throw new ConflictException(TRANSLATION_ERROR);
        }
    }

    private GoogleTranslateResponse deserializeResponse(Response.Body responseBody) {
        ObjectMapper jsonRider = new ObjectMapper();
        try {
            return jsonRider.readValue(responseBody.asInputStream(), GoogleTranslateResponse.class);
        } catch (IOException ex) {
            log.error("Unexpected error: cannot read response body", ex);
            throw new ConflictException(TRANSLATION_ERROR);
        }
    }
}