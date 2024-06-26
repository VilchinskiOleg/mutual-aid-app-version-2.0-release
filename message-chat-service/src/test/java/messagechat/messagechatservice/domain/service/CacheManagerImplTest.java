package messagechat.messagechatservice.domain.service;

import com.redis.testcontainers.RedisContainer;
import messagechat.messagechatservice.configuration.MessageChatConfigProps;
import messagechat.messagechatservice.configuration.data.MessageChatRedisConfig;
import messagechat.messagechatservice.domain.model.Dialog;
import messagechat.messagechatservice.domain.model.Member;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.domain.service.proessor.CacheManagerImpl;
import messagechat.messagechatservice.mapper.CachedMessageToMessageConverter;
import messagechat.messagechatservice.mapper.MessageToCachedMessageConverter;
import messagechat.messagechatservice.persistent.cache.repository.ExtendedHashCachedMessageRepositoryImpl;
import org.common.http.autoconfiguration.model.CommonData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapper.autoconfiguration.ModelMapperConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static java.time.LocalDateTime.now;
import static messagechat.messagechatservice.domain.model.Dialog.Status.ACTIVE;
import static messagechat.messagechatservice.domain.model.Dialog.Type.CHANNEL;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@Testcontainers
@DirtiesContext

@ActiveProfiles("local")
@ExtendWith({SpringExtension.class})
@DataRedisTest
@ContextConfiguration(classes = {
        MessageChatRedisConfig.class, MessageChatConfigProps.class,
        ExtendedHashCachedMessageRepositoryImpl.class,
        ModelMapperConfig.class, CachedMessageToMessageConverter.class, MessageToCachedMessageConverter.class,
        CacheManagerImpl.class //,          ThreadPoolTaskExecutionConfig.class
})
public class CacheManagerImplTest {

    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("redis-connection.host", REDIS_CONTAINER::getHost);
        registry.add("redis-connection.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
        LOG.info("Redis service has started on host= {} and port= {} .",
                REDIS_CONTAINER.getHost(), REDIS_CONTAINER.getMappedPort(6379));
    }


    private static final String DIALOG_ID = "1-dialog-UUID-id";
    private static final String MESSAGE_ID_POSTFIX = "-message-UUID-id";
    private static final String MEMBER_ID_POSTFIX = "-member-UUID-id";
    private static final Logger LOG = LoggerFactory.getLogger(CacheManagerImplTest.class);
    private static final List<Message> messagesMOCK = generateMessagesMock();

    @Resource
    private CacheManagerImpl cacheManager;
    @MockBean
    private CommonData commonData;


    @BeforeAll
    static void beforeAll() {
        // Reverse order, the way them will be requested from DB (some N last messages):
        Collections.reverse(messagesMOCK);
    }

    @Test
    void read_subset_of_last_three_messages_when_cache_contain_five() {
        final int lastMsgInChatId = messagesMOCK.size();
        final int size = 3;

        when(commonData.getLocale()).thenReturn(new Locale("EN"));

        cacheManager.cacheTranslatedMessages(messagesMOCK, 0, 10);
        List<Message> cachedMessagesSubList = cacheManager.readMessagesFromCache(DIALOG_ID, 0, size);

        // Validate:
        assertEquals(size, cachedMessagesSubList.size());
        assertEquals(lastMsgInChatId + MESSAGE_ID_POSTFIX, cachedMessagesSubList.get(0).getInternalId());
        assertEquals((lastMsgInChatId - size + 1) + MESSAGE_ID_POSTFIX, cachedMessagesSubList.get(2).getInternalId());
    }

    @Test
    void read_subset_of_two_messages_in_middle_of_cache_when_cache_contain_five() {
        when(commonData.getLocale()).thenReturn(new Locale("EN"));

        final int size = 2;
        cacheManager.cacheTranslatedMessages(messagesMOCK, 0, 5);
        List<Message> cachedMessagesSubList = cacheManager.readMessagesFromCache(DIALOG_ID, 1, size);

        // Validate:
        assertEquals(size, cachedMessagesSubList.size());
        assertEquals(3 + MESSAGE_ID_POSTFIX, cachedMessagesSubList.get(0).getInternalId());
        assertEquals(2 + MESSAGE_ID_POSTFIX, cachedMessagesSubList.get(1).getInternalId());
    }

    @Test
    void return_empty_list_of_messages_When_requested_range_of_messages_run_out_amount_of_cached_messages_in_general() {
        when(commonData.getLocale()).thenReturn(new Locale("EN"));

        cacheManager.cacheTranslatedMessages(messagesMOCK, 0, 5);
        List<Message> cachedMessagesSubList = cacheManager.readMessagesFromCache(DIALOG_ID, 0, 7);

        // Validate:
        assertTrue(isEmpty(cachedMessagesSubList));
    }


    @AfterEach
    void afterEach() {
        cleanCacheAndValidate();
    }


    private static List<Message>  generateMessagesMock() {
        Dialog dialog = new Dialog();
        dialog.setId(1);
        dialog.setInternalId(DIALOG_ID);
        dialog.setName("Our test chanel");
        dialog.setStatus(ACTIVE);
        dialog.setType(CHANNEL);

        List<Message> messages = new ArrayList<>();
        for (int n = 1; n <= 5; n++) {
            messages.add(Message.builder()
                    .id(n)
                    .internalId(n + MESSAGE_ID_POSTFIX)
                    .dialog(dialog)
                    .author(new Member(n + MEMBER_ID_POSTFIX))
                    .description("Hello guys!")
                    .createAt(now()).build());
        }
        return messages;
    }

    private void cleanCacheAndValidate() {
        cacheManager.removeAllMessagesByDialogId(DIALOG_ID);
        List<Message> cachedMessagesSubListAfterDeleting = cacheManager.readMessagesFromCache(DIALOG_ID, 0, 5);

        // Validate:
        assertTrue(isEmpty(cachedMessagesSubListAfterDeleting));
    }
}