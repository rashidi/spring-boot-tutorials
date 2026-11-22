package zin.rashidi.boot.cloud.jdbcenvrepo.greet;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rashidi Zin
 */
@RestController
@RefreshScope
class GreetResource {

    private final GreetProperties properties;

    GreetResource(GreetProperties properties) {
        this.properties = properties;
    }

    @GetMapping(value = "/greet", produces = MediaType.TEXT_PLAIN_VALUE)
    public String greet(@RequestParam String greeting) {
        return "%s, my name is %s".formatted(greeting, properties.getName());
    }

}
