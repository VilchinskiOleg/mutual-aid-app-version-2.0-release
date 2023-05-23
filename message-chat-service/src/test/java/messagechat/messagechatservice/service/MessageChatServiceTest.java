package messagechat.messagechatservice.service;

import messagechat.messagechatservice.MessageChatServiceApplication;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.service.MessageChatService;
import messagechat.messagechatservice.domain.service.client.ProfileClientService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tms.mutual_aid.profile_service.client.model.Name;
import org.tms.mutual_aid.profile_service.client.model.Profile;

import javax.annotation.Resource;

import java.util.List;

import static java.lang.Math.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("local")
@SpringBootTest(
        classes = {MessageChatServiceApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(SpringExtension.class)
public class MessageChatServiceTest extends DatabaseSourceTestConfig {

    private static final String[] PROFILE_FIRST_NAMES = {"John", "Sarah", "Jeims", "Rik", "Jesika", "Piter"};
    private static final String[] PROFILE_LAST_NAMES = {"Smith", "Parker", "Malfoy", "Potter", "Stark", "Gibson"};

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("datasource.message-chat.jdbc-url",postgresDB::getJdbcUrl);
        registry.add("datasource.message-chat.username", () -> TEST_DB_USERNAME);
        registry.add("datasource.message-chat.password", () -> TEST_DB_PASSWORD);
        registry.add("message-chat-properties.translation-message.enabled", () -> false);
    }


    @Resource
    private MessageChatService messageChatService;
    @MockBean
    private ProfileClientService profileClientService;


    @BeforeAll
    static void initAll() {
        showTestDbUrl();
    }


    @Test
    void test() {
        final String authorId = "1296234-assdfgsdf-230914";
        final String receiverId = "1231234-asdfsdf-234";

        var newMessage = Message.builder()
                .author(Member.builder().profileId(authorId).build())
                .description("Some test message description.")
                .build();

        when(profileClientService.getProfileById((String) any())).thenAnswer(args -> generateProfile(args.getArgument(0)));
        var message = messageChatService.addMessageToDialog(newMessage, receiverId);

        System.out.println("OK");
    }

    private Profile generateProfile(String profileId) {
        var profile = new Profile();
        profile.setId(profileId);
        var name = new Name();
        name.setFirstName(PROFILE_FIRST_NAMES[abs(5 - (int)(random()*10))]);
        name.setLastName(PROFILE_LAST_NAMES[abs(5 - (int)(random()*10))]);
        name.setLocale("en");
        profile.setNames(List.of(name));
        return profile;
    }
}