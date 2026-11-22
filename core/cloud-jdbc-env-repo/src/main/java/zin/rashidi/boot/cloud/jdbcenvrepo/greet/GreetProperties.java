package zin.rashidi.boot.cloud.jdbcenvrepo.greet;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Rashidi Zin
 */
@ConfigurationProperties(prefix = "app.greet")
class GreetProperties {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
