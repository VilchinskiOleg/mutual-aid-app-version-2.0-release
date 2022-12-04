package org.tms.authservicerest.domain.service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tms.authservicerest.configuration.client.RapidEmailSenderApiClient;

@Component
@Slf4j
public class EmailSenderClientService {

    private RapidEmailSenderApiClient emailSenderApiClient;

    public void sendEmailForResetPassword(String defaultGeneratedPassword) {

        //TODO: send 1. current password 2. send linc to genearate new
    }
}