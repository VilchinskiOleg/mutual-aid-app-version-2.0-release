package org.tms.common.auth.configuration.client.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.model.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
public class CustomResponseErrorHandler extends DefaultResponseErrorHandler {

  /**
   * That method MUST cover/handle all negative cases (only unless success 2XX or 3XX answers).
   * In case if some negative case will not be processed by that method -> check/override "public boolean hasError()" implementation.
   */
  @Override
  protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
    try {
      var responseBody = new ObjectMapper().readValue(response.getBody(), BaseResponse.class);
      var error = responseBody.getError();
      log.error("Unexpected error: request was failed on external API by reason = {}", error);
      throw new RestClientResponseException(
          error.toString(), statusCode.value(), statusCode.getReasonPhrase(), null, null, null);
    } catch (JsonParseException | JsonMappingException ex) {
      log.error("Unexpected error while calling external API.");
      super.handleError(response, statusCode);
    }
  }
}