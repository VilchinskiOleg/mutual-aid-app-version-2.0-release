package messagechat.messagechatservice.domain.service.proessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messagechat.messagechatservice.configuration.MessageChatConfigProps;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.persistent.cache.model.HashCachedMessage;
import messagechat.messagechatservice.persistent.cache.repository.HashCachedMessageRepository;
import org.common.http.autoconfiguration.model.CommonData;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheManagerImpl {

    public static final String CACHE_MESSAGE_ID_PATTERN = "%s/%s/%s/%s";
    private static final String ONE_OR_MORE_ANY_SYMBOLS = "*";

    private final HashCachedMessageRepository cachedMessageRepository;
    private final CommonData commonData;
    private final Mapper mapper;
    private final MessageChatConfigProps configProps;

//    private ExecutorService executorService = Executors.newFixedThreadPool(3);


    /**
     * Save translated Message into Redis Cache.
     *
     * @param messages - Message to cache
     * @param pageNumber - number of page of translated Messages
     * @param size - size of page of translated Messages
     */
//    @Async("threadPoolTaskExecutor")
    public void cacheTranslatedMessages(List<Message> messages, Integer pageNumber, Integer size) {
//        executorService.submit(() -> {
            String lang = commonData.getLocale().getLanguage().toUpperCase();
            // in this case first message in a DB table - is a last message in a cache:
            AtomicInteger currentSerialNumber = new AtomicInteger(size * pageNumber + 1);

            messages.forEach(message -> saveMessageToCache(message, currentSerialNumber, lang));
//        });
    }

    /**
     * Try to find and read Messages from cache.
     *
     * @param dialogId - ID of dialog which cached messages belong to
     * @param pageNumber - number of page
     * @param size - size of page
     * @return [1.] cached messages or [2.] empty list if there isn't such long continuous sequence of cached messages in a CACHE.
     */
    public List<Message> readMessagesFromCache(String dialogId, Integer pageNumber, Integer size) {
        String lang = commonData.getLocale().getLanguage().toUpperCase();
        var cachedMessages = cachedMessageRepository.findAllByDialogIdAndLang(dialogId, lang);
        if (isEmpty(cachedMessages)) {
            log.info("Didn't find any cached message for dialogId= {} and lang= {}",
                    dialogId, commonData.getLocale().getLanguage().toUpperCase());
            return null;
        }
        return mapper.map(trimResultAccordingPage(cachedMessages, pageNumber, size), new ArrayList<>(), Message.class);
    }

    /**
     * Try to remove all Messages belonging to this dialog.
     *
     * @param dialogId - ID of dialog which cached messages belong to
     */
//    @Async("threadPoolTaskExecutor")
    public void removeAllMessagesByDialogId(String dialogId) {
//        executorService.submit(() -> {
            String pattern = buildCacheKeyPattern(dialogId, null, null, null);
            var numbers = cachedMessageRepository.removeMessagesByKeyPattern(pattern);
            log.info("Was removed {} messages from the cache for dialog= {}", numbers, dialogId);
//        });
    }

    /**
     * Try to remove Message by ID.
     *
     * @param messageId - ID of Message to delete
     */
    public void removeMessageByMessageId(String messageId) {
        String pattern = buildCacheKeyPattern(null, null, null, messageId);
        cachedMessageRepository.removeMessageByKeyPattern(pattern);
    }


    private void saveMessageToCache(Message message, AtomicInteger serialNumber, String lang) {
        var cachedMessage = mapper.map(message, HashCachedMessage.class);

        cachedMessage.setLang(lang);
        cachedMessage.setTtl(configProps.getTtl());
        // in this case first message in a DB table - is a last message in a cache:
        cachedMessage.setSerialNumberDesc(serialNumber.get());

        String dialogId = ofNullable(message.getDialogId()).orElseThrow(() -> new RuntimeException("Dialog ID can't be null"));
        String messageId = ofNullable(message.getInternalId()).orElseThrow(() -> new RuntimeException("Message ID can't be null"));
        cachedMessage.setId(buildCachedMessageId(dialogId, lang, serialNumber.getAndIncrement(), messageId));

        cachedMessageRepository.save(cachedMessage);
    }

    private NavigableSet<HashCachedMessage> trimResultAccordingPage(NavigableSet<HashCachedMessage> messages,
                                                                    Integer pageNumber,
                                                                    Integer size) {
        int from = size * pageNumber + 1;
        int to = size * pageNumber + size;
        var matchedMessages = messages.subSet(new HashCachedMessage(from), true, new HashCachedMessage(to), true);
        // validation matchedCachedMessages, that no one was missed :
        if (matchedMessages.size() != size) {
            log.info("Expected number= {}, retrieved number= {} of messages from cache.", size, matchedMessages.size());
            return null;
        }
        return matchedMessages;
    }

    private String buildCacheKeyPattern(String dialogId, String lang, Integer serialNumberDesc, String messageId) {
        String keyPrefix = HashCachedMessage.class.getAnnotation(RedisHash.class).value();
        String id = buildCachedMessageId(dialogId, lang, serialNumberDesc, messageId);
        return keyPrefix + ":" + id;
    }

    private String buildCachedMessageId(String dialogId, String lang, Integer serialNumberDesc, String messageId) {
        return format(
                CACHE_MESSAGE_ID_PATTERN,
                nonNull(dialogId) ? dialogId : ONE_OR_MORE_ANY_SYMBOLS,
                nonNull(lang) ? lang : ONE_OR_MORE_ANY_SYMBOLS,
                nonNull(serialNumberDesc) ? serialNumberDesc : ONE_OR_MORE_ANY_SYMBOLS,
                nonNull(messageId) ? messageId : ONE_OR_MORE_ANY_SYMBOLS);
    }
}