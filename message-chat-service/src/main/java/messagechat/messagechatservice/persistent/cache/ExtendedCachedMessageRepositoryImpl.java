package messagechat.messagechatservice.persistent.cache;

import org.mapper.autoconfiguration.mapper.Mapper;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.toCollection;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

public class ExtendedCachedMessageRepositoryImpl implements ExtendedCachedMessageRepository {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private Mapper mapper;

    @Override
    public NavigableSet<CachedMessage> getMessagesByKeyPattern(String pattern) {
        Set<String> cachedMessageKeys = redisTemplate.keys(pattern);
        if (isEmpty(cachedMessageKeys)) return null;

        var hashOperations = redisTemplate.opsForHash();
        return cachedMessageKeys.stream()
                .map(key -> {
                    Map<Object, Object> entries = hashOperations.entries(key);
                    Integer serialNumberDesc = 1;//todo!
                    entries.put("serialNumberDesc", serialNumberDesc);
                    return entries;
                })
                .map(entry -> mapper.map(entry, CachedMessage.class))
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
}