package zin.rashidi.boot.ai.redis;

import com.redis.testcontainers.RedisStackContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection("redis")
    RedisStackContainer redisContainer() {
        return new RedisStackContainer(RedisStackContainer.DEFAULT_IMAGE_NAME.withTag("latest"));
    }

}
