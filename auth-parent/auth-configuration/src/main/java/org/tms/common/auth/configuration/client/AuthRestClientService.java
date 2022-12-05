package org.tms.common.auth.configuration.client;

import static java.util.Objects.nonNull;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.tms.mutual_aid.auth.client.model.JwtRequest;
import org.tms.mutual_aid.auth.client.model.VerifyJWTResponse;

@Component
@ConditionalOnBean(name = "authRestClientProperties")

@Slf4j
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
      log.info("Successful called external API by URL = {}. Status code = {}", urlStr, response.getStatusCodeValue());
      return response.getBody();
    } catch (Exception ex) {
      log.error("Unexpected error while execute request by: URL = {}; with body = {}",
          urlStr, nonNull(entity) ? entity.getBody() : null, ex);
      throw ex;
    }
  }

  private <T> HttpEntity<T> buildHttpEntity(T request){
    var headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE);
    return new HttpEntity(request, headers);
  }
}