package messagechat.messagechatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "messagechat.messagechatservice.persistent.repository")
@EnableFeignClients(basePackages = "messagechat.messagechatservice.configuration.client")
@SpringBootApplication
public class MessageChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageChatServiceApplication.class, args);
    }

}