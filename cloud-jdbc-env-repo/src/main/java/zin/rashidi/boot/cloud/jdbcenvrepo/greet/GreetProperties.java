package zin.rashidi.boot.cloud.jdbcenvrepo.greet;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Rashidi Zin, GfK
 */
@ConfigurationProperties(prefix = "app.greet")
record GreetProperties(String name) {
}
