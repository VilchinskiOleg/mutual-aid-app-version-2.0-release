package messagechat.messagechatservice.configuration.data.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Thread save implementation.
 */
public abstract class LettuceConfig extends BaseRedisProviderConfig {

    @Override
    protected RedisConnectionFactory getConnectionFactoryImpl(RedisStandaloneConfiguration config) {
        var connectionFactory = new LettuceConnectionFactory(config);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }
}