package zin.rashidi.boot.cloud.jdbcenvrepo.greet;

import org.springframework.stereotype.Service;

/**
 * @author Rashidi Zin, GfK
 */
@Service
public class GreetService {

    private final GreetProperties properties;

    GreetService(GreetProperties properties) {
        this.properties = properties;
    }

    public String greet(String greet) {
        return String.format("%s, my name is %s", greet, properties.name());
    }

}
