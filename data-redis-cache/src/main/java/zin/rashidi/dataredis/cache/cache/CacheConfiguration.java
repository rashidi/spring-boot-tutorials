package zin.rashidi.dataredis.cache.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author Rashidi Zin
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        var cacheWriter = RedisCacheWriter.create(connectionFactory,
                configurer -> configurer.immediateWrites());
        return RedisCacheManager.builder(cacheWriter).build();
    }

}
