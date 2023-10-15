package messagechat.messagechatservice.configuration.data.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

public abstract class JedisConfig extends BaseRedisProviderConfig {

    @Override
    protected RedisConnectionFactory getConnectionFactoryImpl(RedisStandaloneConfiguration config) {
        var connectionFactory = new JedisConnectionFactory(config);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }
}