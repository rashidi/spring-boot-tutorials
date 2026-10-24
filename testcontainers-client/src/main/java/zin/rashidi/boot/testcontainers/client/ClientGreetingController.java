package zin.rashidi.boot.testcontainers.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientGreetingController {

    private final GreetingService greetingService;

    public ClientGreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/client/greetings")
    public Greeting getGreeting() {
        return greetingService.fetchGreeting();
    }
}
