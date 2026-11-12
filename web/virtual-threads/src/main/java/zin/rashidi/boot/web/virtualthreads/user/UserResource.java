package zin.rashidi.boot.web.virtualthreads.user;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Transactional(readOnly=true)
class UserResource {

    private final UserRepository repository;

    public UserResource(UserRepository repository) {
        this.repository = repository;
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
