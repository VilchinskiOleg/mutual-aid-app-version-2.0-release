package org.tms.authservicerest.domain.service.client;

import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.exception.handling.autoconfiguration.throwable.ConflictException;
import org.springframework.stereotype.Component;
import org.tms.authservicerest.configuration.client.RapidEmailSenderApiClient;
import org.tms.authservicerest.configuration.client.model.Contact;
import org.tms.authservicerest.configuration.client.model.Content;
import org.tms.authservicerest.configuration.client.model.EmailLetter;
import org.tms.authservicerest.configuration.client.model.Personalization;

import javax.annotation.Resource;
import java.util.regex.Pattern;

import static java.lang.String.valueOf;
import static java.util.Collections.singletonList;
import static java.util.regex.Pattern.compile;
import static org.tms.authservicerest.utils.Constant.Errors.RESET_PASSWORD_FAILED;

@Component
@Slf4j
public class EmailSenderClientService {

    private static final String SENDER_EMAIL = "vin76423@gmail.com";
    private static final String SUBJECT = "Resending password";
    private static final String CONTENT_TYPE = "text/plain"; // text/plain ; application/json
    private static final Pattern SUCCESS_HTTP_CODE = compile("^2\\d+$");

    @Resource
    private RapidEmailSenderApiClient emailSenderApiClient;

    public void sendEmailForResetPassword(String temporaryPassword) {
        try {
            var personalization = new Personalization(singletonList(new Contact(getRecipientsEmail())), SUBJECT);
            var letter = new EmailLetter(
                    singletonList(personalization),
                    new Contact(SENDER_EMAIL),
                    singletonList(buildContent(temporaryPassword)));
            Response response = emailSenderApiClient.sendEmail(letter);
            checkResponse(response);
        } catch (Exception ex) {
            log.error("Unexpected error during send email to resend password for user email = {}", getRecipientsEmail(), ex);
            throw ex;
        }
    }

    private String getRecipientsEmail() {
        return "test$user#2@mailsac.com"; // MOCK
    }

    private String generateUrlToResetPassword() {
        return "https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/"; // MOCK
    }

    private Content buildContent(String temporaryPassword) {
        return new Content(CONTENT_TYPE, "Hi there is your default password : " + temporaryPassword); // MOCK
    }

    private void checkResponse(Response response) {
        if (!SUCCESS_HTTP_CODE.matcher(valueOf(response.status())).matches()) {
            throw new ConflictException(RESET_PASSWORD_FAILED);
        }
    }
}