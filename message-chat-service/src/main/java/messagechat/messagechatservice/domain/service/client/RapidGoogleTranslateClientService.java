package messagechat.messagechatservice.domain.service.client;

import static java.lang.String.join;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static messagechat.messagechatservice.util.Constant.Errors.TRANSLATION_ERROR;
import static messagechat.messagechatservice.util.Constant.Service.GoogleTranslateApi.*;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.common.http.autoconfiguration.utils.Constant.OK_HTTP_CODE;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import java.util.ArrayList;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import messagechat.messagechatservice.configuration.client.translate.RapidGoogleTranslateApiClient;
import messagechat.messagechatservice.configuration.client.translate.RapidGoogleTranslateApiProperties;
import messagechat.messagechatservice.configuration.client.translate.message.GoogleTranslateResponse;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class RapidGoogleTranslateClientService implements TranslateClientService {

    @Resource
    private RapidGoogleTranslateApiClient translateApi;
    @Resource
    private RapidGoogleTranslateApiProperties translateApiProperties;

    @Override
    public String translateMessage(String message, String sourceLang, String targetLang) {
        var messageRequest = buildRequest(message, sourceLang, targetLang);
        return translate(messageRequest);
    }

    @Override
    public Map<String, String> translateMessages(List<String> messages, String sourceLang, String targetLang) {
        var messageRequest = buildRequest(messages, sourceLang, targetLang);
        String result = translate(messageRequest);
        List<String> translatedMessages = separateMessages(result);
        //todo: not very good solution:
        return IntStream.range(INTEGER_ZERO, messages.size())
                        .boxed()
                        .collect(toMap(messages::get, translatedMessages::get));
    }

    private String buildRequest(String message, String sourceLang, String targetLang) {
        String text = replaceSpaces(message);
        return  TEXT_FORM_BODY_KEY + text +
                TARGET_LANG_FORM_BODY_KEY + targetLang +
                SOURCE_LANG_FORM_BODY_KEY + sourceLang;
    }

    private String buildRequest(List<String> messages, String sourceLang, String targetLang) {
        List<String> processedMessages = messages.parallelStream()
                                                 .collect(ArrayList::new,
                                                          (messagesList, currentMessage) -> messagesList.add(replaceSpaces(currentMessage)),
                                                          ArrayList::addAll);
        String text = join(MESSAGES_DELIMITER, processedMessages);
        return  TEXT_FORM_BODY_KEY + text +
                TARGET_LANG_FORM_BODY_KEY + targetLang +
                SOURCE_LANG_FORM_BODY_KEY + sourceLang;
    }

    private String translate(String messageRequest) {
        var response = translateApi.translate(translateApiProperties.getHost(),
                                              translateApiProperties.getToken(),
                                              translateApiProperties.getEncoding(),
                                              messageRequest);
        checkResponse(response);
        GoogleTranslateResponse responseData = deserializeResponse(response.body());
        return responseData.getPayload().getFirstTranslation();
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

    private String replaceSpaces(String message) {
        return SPACES_DELIMITING_PATTERN.matcher(message)
                                        .replaceAll(TEXT_DELIMITER);
    }

    private List<String> separateMessages(String message) {
        return asList(message.split("\\" + MESSAGES_DELIMITER));
    }
}