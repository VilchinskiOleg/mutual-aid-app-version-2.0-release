package messagechat.messagechatservice.domain.service.proessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messagechat.messagechatservice.domain.model.Message;
import messagechat.messagechatservice.persistent.cache.CachedMessage;
import messagechat.messagechatservice.persistent.cache.CachedMessageRepository;
import org.common.http.autoconfiguration.model.CommonData;
import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalCacheManager {

    private static final String CACHE_MESSAGE_KEY_PATTERN = "%s:%s:%s:%s";

    private final CachedMessageRepository cachedMessageRepository;
    private final CommonData commonData;
    private final Mapper mapper;

    private ExecutorService executorService = Executors.newFixedThreadPool(3);


    //Async
    public void cacheTranslatedMessages(List<Message> messages) {
        executorService.submit(() -> {
            messages.forEach(message -> {
                var cachedMessage = mapper.map(message, CachedMessage.class);
                cachedMessageRepository.save(cachedMessage); //??? key
            });
        });
    }

    public List<Message> readMessagesFromCache(String dialogId, Integer pageNumber, Integer size) {
        String lang = commonData.getLocale().getLanguage().toUpperCase();
        String pattern = buildCacheKeyPattern(dialogId, lang, null, null);
        var cachedMessages = cachedMessageRepository.getMessagesByKeyPattern(pattern);
        if (isEmpty(cachedMessages)) {
            log.info("Didn't find any cached message for dialogId= {} and lang= {}",
                    dialogId, commonData.getLocale().getLanguage().toUpperCase());
            return null;
        }
        int from = size * pageNumber + 1;
        int to = size * pageNumber + size;
        var matchedCachedMessages = cachedMessages.subSet(new CachedMessage(from), true, new CachedMessage(to), true);
        // validation matchedCachedMessages, that no one was missed :
        if (matchedCachedMessages.size() != size) {
            log.info("Expected number= {}, retrieved number= {} of messages from cache.", size, matchedCachedMessages.size());
            return null;
        }
        return mapper.map(matchedCachedMessages, new ArrayList<>(), Message.class);
    }

    //Async
    public void removeAllMessagesByDialogId(String dialogId) {
        executorService.submit(() -> {
            String pattern = buildCacheKeyPattern(dialogId, null, null, null);
            var numbers = cachedMessageRepository.removeMessagesByKeyPattern(pattern);
            log.info("Was removed {} messages from the cache for dialog= {}", numbers, dialogId);
        });
    }

    public void removeMessageByMessageId(String messageId) {
        String pattern = buildCacheKeyPattern(null, null, null, messageId);
        cachedMessageRepository.removeMessageByKeyPattern(pattern);
    }


    private String buildCacheKeyPattern(String dialogId, String lang, Integer serialNumberDesc, String messageId) {
        return format(
                CACHE_MESSAGE_KEY_PATTERN,
                nonNull(dialogId) ? dialogId : "*",
                nonNull(lang) ? lang : "*",
                nonNull(serialNumberDesc) ? serialNumberDesc : "*",
                nonNull(messageId) ? messageId : "*");
    }
}