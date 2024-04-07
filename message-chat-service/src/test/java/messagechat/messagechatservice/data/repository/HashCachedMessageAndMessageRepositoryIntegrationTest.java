package messagechat.messagechatservice.data.repository;

import com.redis.testcontainers.RedisContainer;
import messagechat.messagechatservice.MessageChatServiceApplication;
import messagechat.messagechatservice.domain.service.proessor.CacheManagerImpl;
import messagechat.messagechatservice.persistent.cache.model.HashCachedMessage;
import messagechat.messagechatservice.persistent.cache.repository.HashCachedMessageRepository;
import messagechat.messagechatservice.persistent.entity.Dialog;
import messagechat.messagechatservice.persistent.entity.Message;
import messagechat.messagechatservice.persistent.repository.DialogRepository;
import messagechat.messagechatservice.persistent.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.verification.Times;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.Resource;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static messagechat.messagechatservice.domain.service.proessor.CacheManagerImpl.CACHE_MESSAGE_ID_PATTERN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@Testcontainers
@DirtiesContext

@ActiveProfiles("local")
@SpringBootTest(
        classes = {MessageChatServiceApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(SpringExtension.class)
public class HashCachedMessageAndMessageRepositoryIntegrationTest {

    private static final String TEST_DB_NAME = "messageChatServiceTEST";
    private static final String TEST_DB_USERNAME = "postgres";
    private static final String TEST_DB_PASSWORD = "postgres";

    @Container
    public static final PostgreSQLContainer<?> POSTGRES_DB = new PostgreSQLContainer<>("postgres")
                                                                        .withDatabaseName(TEST_DB_NAME)
                                                                        .withUsername(TEST_DB_USERNAME)
                                                                        .withPassword(TEST_DB_PASSWORD);

    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("redis-connection.host", REDIS_CONTAINER::getHost);
        registry.add("redis-connection.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());

        registry.add("datasource.message-chat.jdbc-url", POSTGRES_DB::getJdbcUrl);
        registry.add("datasource.message-chat.username", () -> TEST_DB_USERNAME);
        registry.add("datasource.message-chat.password", () -> TEST_DB_PASSWORD);
    }


    private static final String DIALOG_ID_MOCK = "1-dialog-UUID-id";
    private static final String MESSAGE_ID_POSTFIX = "-message-UUID-id";
    private static final String LANG_MOCK = "EN";


    @Resource
    private HashCachedMessageRepository cachedMessageRepository;
    @Resource
    private DialogRepository dialogRepository;
    @Resource
    private MessageRepository messageRepository;
    @SpyBean
    private CacheManagerImpl cacheManager;

    @Test
    void remove_cached_messages_if_new_message_was_inserted_to_current_dialog() {
        generateAndSaveCachedMessage(DIALOG_ID_MOCK, "1" + MESSAGE_ID_POSTFIX, 1);
        generateAndSaveCachedMessage(DIALOG_ID_MOCK, "2" + MESSAGE_ID_POSTFIX, 2);
        generateAndSaveCachedMessage(DIALOG_ID_MOCK, "3" + MESSAGE_ID_POSTFIX, 3);

        // Validate that all Messages were cached :
        var messages = cachedMessageRepository.findAllByDialogIdAndLang(DIALOG_ID_MOCK, LANG_MOCK);
        assertEquals(3, messages.size());
        var msg = messages.first();
        assertEquals(DIALOG_ID_MOCK, msg.getDialogId());
        assertEquals("1" + MESSAGE_ID_POSTFIX, msg.getInternalId());

        // Save new Message into current Dialog (for test it doesn't matter, we just can create a new Dialog
        // and Hibernate will insert new Message within such Dialog thanks to Cascades) in DB :
        String msgId = "test-message-1";
        var dialog = Dialog.builder()
                .dialogId(DIALOG_ID_MOCK)
                .name("name of " + DIALOG_ID_MOCK)
                .status("ACTIVE")
                .type("FACE_TO_FACE_DIALOG").build();
        var message = Message.builder()
                .messageId(msgId)
                .description("description of test-message-1")
                .createAt(now()).build();
        message.setDialog(dialog);
        dialogRepository.save(dialog);

        // Validate that new message was realy saved in DB :
        var savedMessage = messageRepository.findByMessageId(msgId);
        assertTrue(savedMessage.isPresent());

        // Validate that Cached Messages were deleted from Cache :
        messages = cachedMessageRepository.findAllByDialogIdAndLang(DIALOG_ID_MOCK, LANG_MOCK);
        assertEquals(0, messages.size());
        verify(cacheManager, new Times(1)).removeAllMessagesByDialogId(DIALOG_ID_MOCK);
    }

    private void generateAndSaveCachedMessage(String dialogId, String messageId, Integer serialNumberDesc) {
        String id = format(CACHE_MESSAGE_ID_PATTERN, dialogId, LANG_MOCK, serialNumberDesc, messageId);
        var cachedMessage = HashCachedMessage.builder()
                .id(id)
                .lang(LANG_MOCK)
                .internalId(messageId)
                .serialNumberDesc(serialNumberDesc)
                .dialogId(dialogId)
                .authorId(serialNumberDesc + "-member-UUID-id")
                .authorNickName("Jon Caron 93")
                .description("Hi man! How are you?")
                .createAt(now()).build();
        cachedMessageRepository.save(cachedMessage);
    }
}
