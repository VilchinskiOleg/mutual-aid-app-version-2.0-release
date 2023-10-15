package messagechat.messagechatservice.domain.service.proessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.persistent.cache.CachedMessage;
import messagechat.messagechatservice.persistent.cache.repository.CachedMessageRepository;
import org.common.http.autoconfiguration.model.CommonData;
import org.mapper.autoconfiguration.mapper.Mapper;
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
public class ExternalCacheManager {

    private static final String ONE_OR_MORE_ANY_SYMBOLS = "*";
    private static final String DELIMITER = "/";
    private static final String CACHE_MESSAGE_KEY_PATTERN = "%s/%s/%s/%s";

    private final CachedMessageRepository cachedMessageRepository;
    private final CommonData commonData;
    private final Mapper mapper;

//    private ExecutorService executorService = Executors.newFixedThreadPool(3);


//    @Async("threadPoolTaskExecutor")
    public void cacheTranslatedMessages(List<Message> messages, Integer pageNumber, Integer size) {
//        executorService.submit(() -> {
            // in this case 1th - is a last message in a chat:
            String lang = commonData.getLocale().getLanguage().toUpperCase();
            AtomicInteger currentSerialNumberDesc = new AtomicInteger(size * pageNumber + 1);
            messages.forEach(message -> saveMessageToCache(message, currentSerialNumberDesc, lang));
//        });
    }

    /**
     * ...
     *
     * @param dialogId - ID of dialog which cached messages belong to
     * @param pageNumber - number of page
     * @param size - size of page
     * @return [1.] cached messages or [2.] empty list if there isn't such long continuous sequence of cached messages in a CACHE.
     */
    public List<Message> readMessagesFromCache(String dialogId, Integer pageNumber, Integer size) {
        String lang = commonData.getLocale().getLanguage().toUpperCase();
        String pattern = buildCacheKeyPattern(dialogId, lang, null, null);
        var cachedMessages = cachedMessageRepository.getMessagesByKeyPattern(pattern);
        if (isEmpty(cachedMessages)) {
            log.info("Didn't find any cached message for dialogId= {} and lang= {}",
                    dialogId, commonData.getLocale().getLanguage().toUpperCase());
            return null;
        }
        return mapper.map(trimResultAccordingPage(cachedMessages, pageNumber, size), new ArrayList<>(), Message.class);
    }

//    @Async("threadPoolTaskExecutor")
    public void removeAllMessagesByDialogId(String dialogId) {
//        executorService.submit(() -> {
            String pattern = buildCacheKeyPattern(dialogId, null, null, null);
            var numbers = cachedMessageRepository.removeMessagesByKeyPattern(pattern);
            log.info("Was removed {} messages from the cache for dialog= {}", numbers, dialogId);
//        });
    }

    public void removeMessageByMessageId(String messageId) {
        String pattern = buildCacheKeyPattern(null, null, null, messageId);
        cachedMessageRepository.removeMessageByKeyPattern(pattern);
    }

    private void saveMessageToCache(Message message, AtomicInteger serialNumber, String lang) {
        var cachedMessage = mapper.map(message, CachedMessage.class);
        cachedMessage.setSerialNumberDesc(serialNumber.get());
        String dialogId = ofNullable(message.getDialogId()).orElseThrow(() -> new RuntimeException("Dialog ID can't be null"));
        String messageId = ofNullable(message.getInternalId()).orElseThrow(() -> new RuntimeException("Message ID can't be null"));
        String key = buildCacheKeyPattern(dialogId, lang, serialNumber.getAndIncrement(), messageId);
        cachedMessageRepository.saveMessageByKey(key, cachedMessage);
    }

    private NavigableSet<CachedMessage> trimResultAccordingPage(NavigableSet<CachedMessage> messages,
                                                                Integer pageNumber,
                                                                Integer size) {
        int from = size * pageNumber + 1;
        int to = size * pageNumber + size;
        var matchedMessages = messages.subSet(new CachedMessage(from), true, new CachedMessage(to), true);
        // validation matchedCachedMessages, that no one was missed :
        if (matchedMessages.size() != size) {
            log.info("Expected number= {}, retrieved number= {} of messages from cache.", size, matchedMessages.size());
            return null;
        }
        return matchedMessages;
    }

    private String buildCacheKeyPattern(String dialogId, String lang, Integer serialNumberDesc, String messageId) {
        return format(
                CACHE_MESSAGE_KEY_PATTERN,
                nonNull(dialogId) ? dialogId : ONE_OR_MORE_ANY_SYMBOLS,
                nonNull(lang) ? lang : ONE_OR_MORE_ANY_SYMBOLS,
                nonNull(serialNumberDesc) ? serialNumberDesc : ONE_OR_MORE_ANY_SYMBOLS,
                nonNull(messageId) ? messageId : ONE_OR_MORE_ANY_SYMBOLS);
    }
}