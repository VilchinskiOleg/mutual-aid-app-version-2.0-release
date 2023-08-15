package messagechat.messagechatservice.configuration.data;

import messagechat.messagechatservice.configuration.MessageChatConfigProps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@Configuration
@EnableRedisRepositories(basePackages = "messagechat.messagechatservice.persistent.cache")
public class MessageChatRedisConfig {

    @Resource
    private MessageChatConfigProps configProps;


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(getConnectionConfiguration());
    }

//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(getConnectionConfiguration());
//    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
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