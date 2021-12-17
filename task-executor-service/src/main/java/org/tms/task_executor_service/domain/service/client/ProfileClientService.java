package org.tms.task_executor_service.domain.service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tms.task_executor_service.config.client.ProfileRestClient;
import javax.annotation.Resource;

@Component
@Slf4j
public class ProfileClientService {

    @Resource
    private ProfileRestClient profileRestClient;


}