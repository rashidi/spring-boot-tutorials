package zin.rashidi.boot.testcontainers.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @GetMapping("/api/greetings")
    public Greeting greeting() {
        return new Greeting("Hello from Testcontainers Server!");
    }
}
