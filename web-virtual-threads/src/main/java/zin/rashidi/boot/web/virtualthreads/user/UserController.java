package zin.rashidi.boot.web.virtualthreads.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody Map<String, String> request) {
        var user = new User(request.get("name"));
        return repository.save(user);
    }

    @GetMapping("/thread-info")
    public Map<String, Object> getThreadInfo() {
        var count = repository.count(); // Simulated blocking DB IO
        var thread = Thread.currentThread();

        return Map.of(
                "isVirtual", thread.isVirtual(),
                "threadName", thread.getName(),
                "userCount", count
        );
    }
}
