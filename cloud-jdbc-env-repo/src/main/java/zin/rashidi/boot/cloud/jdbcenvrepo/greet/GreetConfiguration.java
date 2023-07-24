package zin.rashidi.boot.cloud.jdbcenvrepo.greet;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rashidi Zin
 */
@Configuration
@EnableConfigurationProperties(GreetProperties.class)
class GreetConfiguration {
}
