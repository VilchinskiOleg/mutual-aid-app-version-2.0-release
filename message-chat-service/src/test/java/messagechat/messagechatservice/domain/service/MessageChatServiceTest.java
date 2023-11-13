package messagechat.messagechatservice.domain.service;

import messagechat.messagechatservice.MessageChatServiceApplication;
import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.service.client.ProfileClientService;
import messagechat.messagechatservice.domain.service.common.DatabaseSourceTestConfig;
import messagechat.messagechatservice.domain.service.common.ProfileMockTestExtension;
import messagechat.messagechatservice.domain.service.listener.PostInsertDialogListener;
import messagechat.messagechatservice.domain.service.proessor.ExternalCacheManager;
import messagechat.messagechatservice.domain.service.proessor.TranslateMessageService;
import messagechat.messagechatservice.persistent.repository.DialogRepository;
import messagechat.messagechatservice.persistent.repository.MessageRepository;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.verification.Times;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.List;

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
@EnableAutoConfiguration(
        exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class, RedisReactiveAutoConfiguration.class}
)
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MessageChatServiceTest extends DatabaseSourceTestConfig implements ProfileMockTestExtension {

    public static String DIALOG_ID;

    private static final String FIRST_USER_ID = "1296234-assdfgsdf-230914";
    private static final String SECOND_USER_ID = "1231234-asdfsdf-234sd637";


    /**
     * In this case we must run and stop testcontainer manually because we use one class instance per each test method.
     */
    static {
        postgresDB.start();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("datasource.message-chat.jdbc-url", postgresDB::getJdbcUrl);
        registry.add("datasource.message-chat.username", () -> TEST_DB_USERNAME);
        registry.add("datasource.message-chat.password", () -> TEST_DB_PASSWORD);
        registry.add("message-chat-properties.translation-message.enabled", () -> true);
    }


    @SpyBean
    private MessageChatServiceImpl messageChatService;
    @SpyBean
    private MessageRepository messageRepository;

    @Resource
    private DialogService dialogService;
    @Resource
    private DialogRepository dialogRepository;
    @Resource
    private EntityManagerFactory entityManagerFactory;

    @MockBean
    private TranslateMessageService translateMessageService;
    @MockBean
    private ProfileClientService profileClientService;
    @MockBean
    private ExternalCacheManager cacheManager;


    @BeforeAll
    void initAll() {
        registerAdditionalHibernateListenersForTests();
    }


    // Test "addMessageToDialog" method:
    @Test
    void add_new_messages_to_dialog_successfully() {
        // Create new Dialog (automatically) and fetch Members from profile-service(MOCK):
        when(profileClientService.getProfileById((String) any())).thenAnswer(args -> generateProfile(args.getArgument(0)));
        var newMessage_1 = Message.builder()
                .author(Member.builder().profileId(FIRST_USER_ID).build())
                .description("Some test message description. (From firstUser)")
                .build();
        messageChatService.addMessageToDialog(newMessage_1, SECOND_USER_ID);

        // Find and update existed Dialog and read existed Members from DB:
        var newMessage_2 = Message.builder()
                .author(Member.builder().profileId(SECOND_USER_ID).build())
                .dialog(Dialog.builder().internalId(DIALOG_ID).build())
                .description("Some test message description. (From secondUser)")
                .build();
        messageChatService.addMessageToDialog(newMessage_2, FIRST_USER_ID);

        // Verify:
        var dialog = dialogRepository.findByDialogId(DIALOG_ID).orElseThrow();
        var messages = messageRepository.findAllByDialogIdOrName(of(0,2), DIALOG_ID, null).getContent();
        assertEquals(2, messages.size());
        assertThat(messages.size()).isEqualTo(2);
        verify(profileClientService, new Times(2)).getProfileById((String) any());
    }

    /**
     * Within adding new Message, system will try to create and save new Dialog (because Message, we provided, doesn't have reference to existed Dialog),
     * but after saving (flush 'INSERT' to DB) Dialog we will be provided by RuntimeException and system
     * will Roll Back all changes within current transaction, so that we must obtain null value when we try to retrieve
     * Dialog by dialogId later.
     */
    @Test
    void fail_and_rollback_all_operation_within_adding_new_message_to_dialog_if_something_go_wrong_within_transaction() {
        final String errorMessage = "RollBack Exception test message.";

        when(profileClientService.getProfileById((String) any())).thenAnswer(args -> generateProfile(args.getArgument(0)));
        doThrow(new RuntimeException(errorMessage)).when(messageChatService).linkAuthorToMessage((Message) any());
        // The same model lake in previous test:
        var newMessage = Message.builder()
                .author(Member.builder().profileId(FIRST_USER_ID).build())
                .description("Some test message description. (From firstUser)")
                .build();

        // Verify:
        assertThrows(RuntimeException.class,() -> messageChatService.addMessageToDialog(newMessage, SECOND_USER_ID), errorMessage);
        assertTrue(dialogRepository.findByDialogId(DIALOG_ID).isEmpty());
    }



    // Test "GetPageMessagesFromDialog" method:
    @Test
    void get_messages_from_cache_If_all_of_them_were_found_into_cache() {
        int pageNumber = 0, size = 2;

        var cachedMessage_1 = Message.builder()
                .author(Member.builder().profileId(FIRST_USER_ID).build())
                .description("Some test message description. (From firstUser)")
                .build();
        var cachedMessage_2 = Message.builder()
                .author(Member.builder().profileId(SECOND_USER_ID).build())
                .dialog(Dialog.builder().internalId(DIALOG_ID).build())
                .description("Some test message description. (From secondUser)")
                .build();

        when(cacheManager.readMessagesFromCache(DIALOG_ID, pageNumber, size)).thenReturn(List.of(cachedMessage_1, cachedMessage_2));

        List<Message> cachedMessages = messageChatService.getPageMessagesFromDialog(pageNumber, size, DIALOG_ID, null);

        // Verify:
        assertEquals(size, cachedMessages.size());
        verify(messageChatService, new Times(0)).findAllByDialogIdOrName(any(), any(), any());
        verify(translateMessageService, new Times(0)).translateReturnedMessages(any());
    }

    @Test
    void get_messages_from_DB_If_all_of_them_were_not_found_into_cache() {
        int pageNumber = 0, size = 2;

        var cachedMessage_1 = Message.builder()
                .author(Member.builder().profileId(FIRST_USER_ID).build())
                .description("Some test message description. (From firstUser)")
                .build();
        var cachedMessage_2 = Message.builder()
                .author(Member.builder().profileId(SECOND_USER_ID).build())
                .dialog(Dialog.builder().internalId(DIALOG_ID).build())
                .description("Some test message description. (From secondUser)")
                .build();
        List<Message> messagesMock = List.of(cachedMessage_1, cachedMessage_2);

        when(cacheManager.readMessagesFromCache(DIALOG_ID, pageNumber, size)).thenReturn(Collections.emptyList());
        // [1.] It's necessary to use 'doReturn()..' instead of 'when()..' for Spy Mock (see docs):
        // [2.] Also sometime you might be provided with issue when you try to provide real value and 'any()' to mock-method simultaneously.
        // Use 'eq()' instead of real value to fix it:
        doReturn(messagesMock).when(messageChatService).findAllByDialogIdOrName(eq(DIALOG_ID), (PageRequest) any(), (String) any());
        doNothing().when(translateMessageService).translateReturnedMessages(messagesMock);
        doNothing().when(cacheManager).cacheTranslatedMessages(messagesMock, pageNumber, size);

        List<Message> cachedMessages = messageChatService.getPageMessagesFromDialog(pageNumber, size, DIALOG_ID, null);

        // Verify:
        assertEquals(size, cachedMessages.size());
        verify(messageChatService, new Times(1)).findAllByDialogIdOrName(eq(DIALOG_ID), any(), any());
        verify(translateMessageService, new Times(1)).translateReturnedMessages(messagesMock);
        verify(cacheManager, new Times(1)).cacheTranslatedMessages(messagesMock, pageNumber, size);
    }


    @AfterAll
    void afterAll() {
        postgresDB.stop();
    }


    private void registerAdditionalHibernateListenersForTests() {
        var sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        var registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

        // Register required Listeners:
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(new PostInsertDialogListener());
    }
}