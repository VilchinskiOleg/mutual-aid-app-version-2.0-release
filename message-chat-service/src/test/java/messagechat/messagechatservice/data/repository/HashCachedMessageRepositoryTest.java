package messagechat.messagechatservice.data.repository;

import com.redis.testcontainers.RedisContainer;
import messagechat.messagechatservice.configuration.MessageChatConfigProps;
import messagechat.messagechatservice.configuration.data.MessageChatRedisConfig;
import messagechat.messagechatservice.persistent.cache.model.HashCachedMessage;
import messagechat.messagechatservice.persistent.cache.repository.ExtendedHashCachedMessageRepositoryImpl;
import messagechat.messagechatservice.persistent.cache.repository.HashCachedMessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.Resource;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static messagechat.messagechatservice.domain.service.proessor.CacheManagerImpl.CACHE_MESSAGE_ID_PATTERN;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DirtiesContext

@ActiveProfiles("local")
@ExtendWith({SpringExtension.class})
@DataRedisTest
@ContextConfiguration(classes = {
        MessageChatRedisConfig.class, MessageChatConfigProps.class,
        ExtendedHashCachedMessageRepositoryImpl.class
})
public class HashCachedMessageRepositoryTest {

    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("redis-connection.host", REDIS_CONTAINER::getHost);
        registry.add("redis-connection.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    private static final String DIALOG_ID_MOCK = "1-dialog-UUID-id";
    private static final String MESSAGE_ID_MOCK = "1-message-UUID-id";
    private static final String LANG_MOCK = "EN";


    @Resource
    private HashCachedMessageRepository cachedMessageRepository;
    @Resource
    private MessageChatConfigProps configProps;


    /**
     * Test of more lowe level Redis implementation which using RedisTemplate and HashOperations:
     *
     * NOTE:
     * If you save Entities by RedisTemplate (and HashOperations) directly , you also must read them by RedisTemplate ,
     * because RedisTemplate and Spring Data (repository) use different mechanisms of Serialization.
     *
     * Otherwise you will be provided with Deserialization error (for example if I would use 'save(cachedMessage)'
     * instead of 'saveMessageByKey(key, cachedMessage)' in method below).
     */
    @Test
    void save_and_read_cached_message_by_key_pattern() {
        Integer serialNumberDesc = 1;
        String id = format(CACHE_MESSAGE_ID_PATTERN, DIALOG_ID_MOCK, LANG_MOCK, serialNumberDesc, MESSAGE_ID_MOCK);

        var cachedMessage = HashCachedMessage.builder()
                .id(id)
                .internalId(MESSAGE_ID_MOCK)
                .serialNumberDesc(serialNumberDesc)
                .dialogId(DIALOG_ID_MOCK)
                .authorId("1-member-UUID-id")
                .authorNickName("Jon Caron 93")
                .description("Hi man! How are you?")
                .createAt(now()).build();

        // Save:
        String key = HashCachedMessage.class.getAnnotation(RedisHash.class).value() + ":"
                + format(CACHE_MESSAGE_ID_PATTERN, DIALOG_ID_MOCK, LANG_MOCK, serialNumberDesc, MESSAGE_ID_MOCK);
        cachedMessageRepository.saveMessageByKey(key, cachedMessage);

        // Read:
        String keyPrefix = HashCachedMessage.class.getAnnotation(RedisHash.class).value();
        String keyPattern = keyPrefix + ":" + format(CACHE_MESSAGE_ID_PATTERN, DIALOG_ID_MOCK, LANG_MOCK, "*", "*");
        var messages = cachedMessageRepository.getMessagesByKeyPattern(keyPattern);

        // Validate:
        assertEquals(1, messages.size());
        var msg = messages.first();
        assertEquals(DIALOG_ID_MOCK, msg.getDialogId());
        assertEquals(MESSAGE_ID_MOCK, msg.getInternalId());
    }

    /**
     * Test of more high level Redis implementation which using Spring Data:
     */
    @Test
    void save_and_read_cached_message_by_dialog_id_and_lang() {
        Integer serialNumberDesc = 1;
        String id = format(CACHE_MESSAGE_ID_PATTERN, DIALOG_ID_MOCK, LANG_MOCK, serialNumberDesc, MESSAGE_ID_MOCK);

        var cachedMessage = HashCachedMessage.builder()
                .id(id)
                .lang(LANG_MOCK)
                .internalId(MESSAGE_ID_MOCK)
                .serialNumberDesc(serialNumberDesc)
                .dialogId(DIALOG_ID_MOCK)
                .authorId("1-member-UUID-id")
                .authorNickName("Jon Caron 93")
                .description("Hi man! How are you?")
                .createAt(now()).build();

        // Save:
        cachedMessageRepository.save(cachedMessage);

        // Read:
        var messages = cachedMessageRepository.findAllByDialogIdAndLang(DIALOG_ID_MOCK, LANG_MOCK);

        // Validate:
        assertEquals(1, messages.size());
        var msg = messages.first();
        assertEquals(DIALOG_ID_MOCK, msg.getDialogId());
        assertEquals(MESSAGE_ID_MOCK, msg.getInternalId());
    }
}
