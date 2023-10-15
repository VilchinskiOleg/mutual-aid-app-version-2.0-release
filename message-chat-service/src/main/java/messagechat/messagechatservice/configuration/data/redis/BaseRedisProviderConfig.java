package messagechat.messagechatservice.configuration.data.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

@Slf4j
public abstract class BaseRedisProviderConfig {

    protected RedisConnectionFactory getConnectionFactory(RedisConfiguration config) {
        if (config instanceof RedisStandaloneConfiguration) {
            return getConnectionFactoryImpl((RedisStandaloneConfiguration) config);
        } else {
            log.warn("Can't handle represented config type!");
            return null;
        }
    }

    protected abstract RedisConnectionFactory getConnectionFactoryImpl(RedisStandaloneConfiguration config);
}