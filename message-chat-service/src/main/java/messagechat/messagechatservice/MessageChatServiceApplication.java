package messagechat.messagechatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "messagechat.messagechatservice.persistent")
@EnableFeignClients(basePackages = "messagechat.messagechatservice.configuration.client")
@SpringBootApplication
public class MessageChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageChatServiceApplication.class, args);
    }

}