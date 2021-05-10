package messagechat.messagechatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableMongoRepositories(basePackages = "messagechat.messagechatservice.persistent")
@SpringBootApplication
public class MessageChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageChatServiceApplication.class, args);
    }

}
