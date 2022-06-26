package org.tms.common.auth.configuration.client;

import static java.util.Objects.nonNull;
import static org.common.http.autoconfiguration.utils.Constant.OK_HTTP_CODE;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.model.BaseResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.tms.mutual_aid.auth.client.model.JwtRequest;
import org.tms.mutual_aid.auth.client.model.VerifyJWTResponse;

@Slf4j
@Component
public class AuthRestClientService {

  private static final String VERIFY_JWT_PATH = "/verify-token";

  @Resource
  private AuthRestClientProperties authRestClientProperties;
  @Resource
  private RestTemplate restApi;

  public VerifyJWTResponse verifyJwt(String jwt) {
    String url = authRestClientProperties.getUrl() + VERIFY_JWT_PATH;

    JwtRequest request = new JwtRequest();
    request.setJwt(jwt);

    var entity = buildHttpEntity(request);
    return executeRequest(url, HttpMethod.POST, entity, VerifyJWTResponse.class);
  }

  private <T> T executeRequest(String urlStr, HttpMethod method, @Nullable HttpEntity<?> entity, Class<T> responseType) {
    try {
      ResponseEntity<T> response = restApi.exchange(urlStr, method, entity, responseType);
      HttpStatus statusCode = response.getStatusCode();

      if (OK_HTTP_CODE == statusCode.value()) {
        return response.getBody();
      } else {
        var error = ((BaseResponse) response.getBody()).getError();
        log.error("Request by: URL = {}; req body = {} was failed. Error = {}",
            urlStr, nonNull(entity) ? entity.getBody() : null, error);
      }
    } catch (RestClientException ex) {
      log.error("Unexpected error while execute request by: URL = {}; req body = {}",
          urlStr, nonNull(entity) ? entity.getBody() : null, ex);
    }
    //todo: create custom ex:
    throw new RuntimeException("CUSTOM EX like EXTERNAL API FAIL");
  }

  private <T> HttpEntity<T> buildHttpEntity(T request){
    var headers = new HttpHeaders();
    //todo: can add hiders if need:..
    return new HttpEntity(request, headers);
  }
}