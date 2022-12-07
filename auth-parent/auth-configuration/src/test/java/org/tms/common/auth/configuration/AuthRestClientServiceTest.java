package org.tms.common.auth.configuration;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.google.gson.JsonObject;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.tms.common.auth.configuration.client.AuthRestClientProperties;
import org.tms.common.auth.configuration.client.AuthRestClientService;
import org.tms.common.auth.configuration.client.config.CustomResponseErrorHandler;
import org.tms.mutual_aid.auth.client.model.VerifyJWTResponse;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthRestClientServiceTest {

  private static final String AUTH_REST_URL = "http://localhost:8800/api/auth-service";
  private static final String JWT_MOCK = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

  @InjectMocks
  private AuthRestClientService authRestClientService;
  @Mock
  private AuthRestClientProperties authRestClientProperties;

  @Spy
  private RestTemplate restApi = new RestTemplate();
  @Mock
  private ClientHttpRequestFactory requestFactory;

  @BeforeEach
  void initRestApi() {
    restApi.setRequestFactory(requestFactory);
    restApi.setMessageConverters(singletonList(new MappingJackson2HttpMessageConverter()));
    restApi.setErrorHandler(new CustomResponseErrorHandler());
  }

  @Test
  @SneakyThrows
  void verify_token_successfully_by_external_API() {

    final var calledEndpointURI = new URI(AUTH_REST_URL.concat("/verify-token"));

    var MOCKResponseStr = buildSuccessfullyResponseMOCK();
    var MOCKResponse = new MockClientHttpResponse(MOCKResponseStr.getBytes(StandardCharsets.UTF_8), HttpStatus.OK);
    MOCKResponse.getHeaders().add(CONTENT_TYPE, APPLICATION_JSON_VALUE);

    var MOCKRequest = new MockClientHttpRequest(HttpMethod.POST, calledEndpointURI);
    MOCKRequest.setResponse(MOCKResponse);

    when(authRestClientProperties.getUrl()).thenReturn(AUTH_REST_URL);
    when(requestFactory.createRequest(calledEndpointURI, HttpMethod.POST)).thenReturn((ClientHttpRequest) MOCKRequest);
    VerifyJWTResponse result = authRestClientService.verifyJwt(JWT_MOCK);

    //verify:
    assertNotNull(result);
    assertEquals(true, result.isSuccess());
  }

  @Test
  @SneakyThrows
  void verify_token_fail_by_external_API() {

    final var calledEndpointURI = new URI(AUTH_REST_URL.concat("/verify-token"));

    var MOCKResponseStr = buildErrorResponseMOCK();
    var MOCKResponse = new MockClientHttpResponse(MOCKResponseStr.getBytes(StandardCharsets.UTF_8), HttpStatus.INTERNAL_SERVER_ERROR);
    MOCKResponse.getHeaders().add(CONTENT_TYPE, APPLICATION_JSON_VALUE);

    var MOCKRequest = new MockClientHttpRequest(HttpMethod.POST, calledEndpointURI);
    MOCKRequest.setResponse(MOCKResponse);

    when(authRestClientProperties.getUrl()).thenReturn(AUTH_REST_URL);
    when(requestFactory.createRequest(calledEndpointURI, HttpMethod.POST)).thenReturn((ClientHttpRequest) MOCKRequest);

    //verify:
    var throwable = assertThrows(RestClientResponseException.class, () -> authRestClientService.verifyJwt(JWT_MOCK));
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), throwable.getRawStatusCode());
  }

  private String buildSuccessfullyResponseMOCK() {
    final var MOCKVerifyJwtResponse = new JsonObject();
    MOCKVerifyJwtResponse.addProperty("profileId", "12345");
    MOCKVerifyJwtResponse.addProperty("success", true);
    return MOCKVerifyJwtResponse.toString();
  }

  private String buildErrorResponseMOCK() {
    final var MOCKVerifyJwtResponse = new JsonObject();
    final var error = new JsonObject();
    error.addProperty("code", "internal.service.error");
    error.addProperty("message", "Service temporary is not available. Call app later.");
    MOCKVerifyJwtResponse.add("error", error);
    return MOCKVerifyJwtResponse.toString();
  }
}