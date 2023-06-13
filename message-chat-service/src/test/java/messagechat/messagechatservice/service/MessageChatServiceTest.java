package messagechat.messagechatservice.service;

import messagechat.messagechatservice.MessageChatServiceApplication;
import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.service.DialogService;
import messagechat.messagechatservice.domain.service.MessageChatService;
import messagechat.messagechatservice.domain.service.client.ProfileClientService;
import messagechat.messagechatservice.domain.service.proessor.TranslateMessageService;
import messagechat.messagechatservice.persistent.repository.DialogRepository;
import messagechat.messagechatservice.persistent.repository.MessageRepository;
import messagechat.messagechatservice.service.hibernate_listener.PostInsertDialogListener;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.verification.Times;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tms.mutual_aid.profile_service.client.model.Name;
import org.tms.mutual_aid.profile_service.client.model.Profile;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.random;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.PageRequest.of;

@ActiveProfiles("local")
@SpringBootTest(
        classes = {MessageChatServiceApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(SpringExtension.class)
public class MessageChatServiceTest extends DatabaseSourceTestConfig {

    public static String DIALOG_ID;
    private static final String[] PROFILE_FIRST_NAMES = {"John", "Sarah", "Jeims", "Rik", "Jesika", "Piter"};
    private static final String[] PROFILE_LAST_NAMES = {"Smith", "Parker", "Malfoy", "Potter", "Stark", "Gibson"};

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("datasource.message-chat.jdbc-url",postgresDB::getJdbcUrl);
        registry.add("datasource.message-chat.username", () -> TEST_DB_USERNAME);
        registry.add("datasource.message-chat.password", () -> TEST_DB_PASSWORD);
//        registry.add("message-chat-properties.translation-message.enabled", () -> false);
    }


    @Resource
    private MessageChatService messageChatService;
    @Resource
    private DialogService dialogService;
    @Resource
    private DialogRepository dialogRepository;
    @Resource
    private MessageRepository messageRepository;
    @Resource
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private TranslateMessageService translateMessageService;
    @MockBean
    private ProfileClientService profileClientService;


    @BeforeAll
    static void initAll() {
        showTestDbUrl();
    }


    @Test
    void add_new_messages_to_dialog_successfully() {
        final String firstUserId = "1296234-assdfgsdf-230914";
        final String secondUserId = "1231234-asdfsdf-234sd637";
        registerAdditionalHibernateListenersForTests();

        // Create new Dialog (automatically) and fetch Members from profile-service(MOCK):
        when(profileClientService.getProfileById((String) any())).thenAnswer(args -> generateProfile(args.getArgument(0)));
        var newMessage_1 = Message.builder()
                .author(Member.builder().profileId(firstUserId).build())
                .description("Some test message description. (From firstUser)")
                .build();
        messageChatService.addMessageToDialog(newMessage_1, secondUserId);

        // Find and update existed Dialog and read existed Members from DB:
        var newMessage_2 = Message.builder()
                .author(Member.builder().profileId(secondUserId).build())
                .dialog(Dialog.builder().internalId(DIALOG_ID).build())
                .description("Some test message description. (From secondUser)")
                .build();
        messageChatService.addMessageToDialog(newMessage_2, firstUserId);

        // Verify:
        var dialog = dialogRepository.findByDialogId(DIALOG_ID).orElseThrow();
        var messages = messageRepository.findAllByDialogIdOrName(of(0,2), DIALOG_ID, null).getContent();
        assertEquals(2, messages.size());
        assertThat(messages.size()).isEqualTo(2);
        verify(profileClientService, new Times(2)).getProfileById((String) any());
    }

    /**
     * Within adding new Message, system will try to create and save new Dialog (because Message, we provide, doesn't have reference to existed Dialog),
     * but after saving (flush 'INSERT' to DB) Dialog we will be provided by RuntimeException and system
     * will Roll Back all changes within current transaction, so that we must obtain null value when we try to retrieve
     * Dialog by dialogId later.
     */
    @Test
    void fail_and_rollback_all_operation_within_adding_new_message_to_dialog_if_something_go_wrong_within_transaction() {
        final String firstUserId = "1296234-assdfgsdf-230914";
        final String secondUserId = "1231234-asdfsdf-234sd637";
        registerAdditionalHibernateListenersForTests();
        final String errorMessage = "RollBackTestException";

        when(profileClientService.getProfileById((String) any())).thenAnswer(args -> generateProfile(args.getArgument(0)));
        doThrow(new RuntimeException("RollBackTestException")).when(translateMessageService).translateSavedMessage((Message) any());
        // The same model like in previous test:
        var newMessage_1 = Message.builder()
                .author(Member.builder().profileId(firstUserId).build())
                .description("Some test message description. (From firstUser)")
                .build();

        // Verify:
        assertThrows(RuntimeException.class,() -> messageChatService.addMessageToDialog(newMessage_1, secondUserId), errorMessage);
        assertTrue(dialogRepository.findByDialogId(DIALOG_ID).isEmpty());
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

    private void registerAdditionalHibernateListenersForTests() {
        var sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        var registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

        // Register required Listeners:
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(new PostInsertDialogListener());
    }
}