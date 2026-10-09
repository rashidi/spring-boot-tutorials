package zin.rashidi.boot.observability.opentelemetry.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserResource {

    @GetMapping("/users")
    String getUsers() {
        return "Users";
    }
}
