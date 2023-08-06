package messagechat.messagechatservice.persistent.cache;

import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

public class ExtendedMessageCacheImpl implements ExtendedMessageCache {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private Mapper mapper;

    @Override
    public Set<CachedMessage> getCachedMessagesByPattern(String pattern) {
        Set<String> cachedMessageKeys = redisTemplate.keys(pattern);
        if (isEmpty(cachedMessageKeys)) return null;

        var hashOperations = redisTemplate.opsForHash();
        return cachedMessageKeys.stream()
                .map(hashOperations::entries)
                .map(entry -> mapper.map(entry, CachedMessage.class))
                .collect(toSet());
    }
}