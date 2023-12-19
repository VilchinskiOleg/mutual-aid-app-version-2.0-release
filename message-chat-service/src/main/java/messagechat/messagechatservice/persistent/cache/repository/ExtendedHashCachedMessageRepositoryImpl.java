package messagechat.messagechatservice.persistent.cache.repository;

import lombok.extern.slf4j.Slf4j;
import messagechat.messagechatservice.persistent.cache.model.HashCachedMessage;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toCollection;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Slf4j
public class ExtendedHashCachedMessageRepositoryImpl implements ExtendedHashCachedMessageRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, Object, Object> hashOperations;


    public ExtendedHashCachedMessageRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }


    @Override
    public void saveMessageByKey(String key, HashCachedMessage message) {
        var payload = message.getMap();
        hashOperations.putAll(key, payload);
    }

    @Override
    public NavigableSet<HashCachedMessage> getMessagesByKeyPattern(String pattern) {
        Set<String> cachedMessageKeys = redisTemplate.keys(pattern);
        if (isEmpty(cachedMessageKeys)) return null;

        return cachedMessageKeys.stream()
                .map(hashOperations::entries)
                .map(this::convertToCachedMsg)
                .collect(toCollection(TreeSet::new));
    }

    @Override
    public Long removeMessagesByKeyPattern(String pattern) {
        Set<String> cachedMessageKeys = redisTemplate.keys(pattern);
        if (isEmpty(cachedMessageKeys)) return 0L;

        return redisTemplate.delete(cachedMessageKeys);
    }

    @Override
    public Boolean removeMessageByKeyPattern(String pattern) {
        Set<String> cachedMessageKeys = redisTemplate.keys(pattern);
        if (isEmpty(cachedMessageKeys)) return false;

        return redisTemplate.delete(new TreeSet<>(cachedMessageKeys).first());
    }


    private HashCachedMessage convertToCachedMsg(Map<Object, Object> data) {
        var result = new HashCachedMessage();
        try {
            for(Field field : HashCachedMessage.class.getDeclaredFields()) {
                Object val = data.get(field.getName());
                if (nonNull(val)) {
                    field.setAccessible(true);
                    field.set(result, val);
                }
            }
        } catch (Exception ex) {
            log.error("Get fail trying to map entries properties to CachedMessage entity.", ex);
            throw new RuntimeException(ex);
        }
        return result;
    }
}
