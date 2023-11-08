package messagechat.messagechatservice.data.repository;

import com.redis.testcontainers.RedisContainer;
import messagechat.messagechatservice.configuration.MessageChatConfigProps;
import messagechat.messagechatservice.configuration.data.MessageChatRedisConfig;
import messagechat.messagechatservice.persistent.cache.CachedMessage;
import messagechat.messagechatservice.persistent.cache.repository.CachedMessageRepository;
import messagechat.messagechatservice.persistent.cache.repository.ExtendedCachedMessageRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DirtiesContext

@ActiveProfiles("local")
@ExtendWith({SpringExtension.class})
@DataRedisTest
@ContextConfiguration(classes = {
        MessageChatRedisConfig.class, ExtendedCachedMessageRepositoryImpl.class, MessageChatConfigProps.class,
})
public class CachedMessageRepositoryTest {

    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("redis-connection.host", REDIS_CONTAINER::getHost);
        registry.add("redis-connection.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }


    @Resource
    private CachedMessageRepository cachedMessageRepository;
    @Resource
    private MessageChatConfigProps configProps;


    @Test
    void save_and_read_cached_message_by_key_pattern() {
        String keyPattern = "%s/%s/%s/%s";
        String dialogId = "1-dialog-UUID-id";
        String messageId = "1-message-UUID-id";
        Integer serialNumberDesc = 1;

        var cachedMessage = CachedMessage.builder()
                .id(1)
                .internalId(messageId)
                .serialNumberDesc(serialNumberDesc)
                .dialogId(dialogId)
                .authorId("1-member-UUID-id")
                .authorNickName("Jon Caron 93")
                .description("Hi man! How are you?")
                .createAt(now()).build();

        // Save:
        String key = format(keyPattern, dialogId, "EN", serialNumberDesc, messageId);
        cachedMessageRepository.saveMessageByKey(key, cachedMessage);

        // Read:
        var messages = cachedMessageRepository.getMessagesByKeyPattern(format(keyPattern, dialogId, "EN", "*", "*"));

        // Validate:
        assertEquals(1, messages.size());
        var msg = messages.first();
        assertEquals(dialogId, msg.getDialogId());
        assertEquals(messageId, msg.getInternalId());
    }
}