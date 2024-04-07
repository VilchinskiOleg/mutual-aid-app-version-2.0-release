package messagechat.messagechatservice.configuration.data;

import messagechat.messagechatservice.configuration.MessageChatConfigProps;
import messagechat.messagechatservice.configuration.data.redis.JedisConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * In order to test only that service without docker-compose file -> just run that command in docker CLI:
 * docker run --name redis-container -p 6379:6379 -it redis
 */
@Configuration
@EnableRedisRepositories(basePackages = "messagechat.messagechatservice.persistent.cache")
public class MessageChatRedisConfig extends JedisConfig {

    @Resource
    private MessageChatConfigProps configProps;

    /**
     * Method create Redis Client implementation to interact with redis server.
     *
     * @return RedisTemplate - Redis Client Spring framework implementation.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisConnectionFactory connectionFactory = getConnectionFactory(getConnectionConfiguration());

        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

//    @Bean
//    public RedisCacheManager cacheManager() {
//        return RedisCacheManager.builder(jedisConnectionFactory())
//                .cacheDefaults(defaultCacheConfig().entryTtl(Duration.ofSeconds(1))
//                // Expects as param: Map of cache name/RedisCacheConfiguration pairs to be pre initialized.
//                .withInitialCacheConfigurations("cache-name", defaultCacheConfig().entryTtl(Duration.ofSeconds(3)))
//                .transactionAware().build();
//    }


    private RedisStandaloneConfiguration getConnectionConfiguration() {
        return new RedisStandaloneConfiguration(configProps.getRedisHost(), configProps.getRedisPort());
    }
}