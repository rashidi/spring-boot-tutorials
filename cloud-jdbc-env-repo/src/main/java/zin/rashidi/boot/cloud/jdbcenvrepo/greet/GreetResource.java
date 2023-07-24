package zin.rashidi.boot.cloud.jdbcenvrepo.greet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rashidi Zin
 */
@RestController
class GreetResource {

    private final GreetProperties properties;

    GreetResource(GreetProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/greet")
    public String greet(@RequestParam String greeting) {
        return String.format("%s, my name is %s", greeting, properties.name());
    }

}
