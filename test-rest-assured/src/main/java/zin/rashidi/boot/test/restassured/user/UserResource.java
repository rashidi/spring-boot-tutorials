package zin.rashidi.boot.test.restassured.user;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rashidi Zin
 */
@RestController
class UserResource {

    private final UserRepository repository;

    UserResource(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/users")
    @ResponseStatus(CREATED)
    public void create(@RequestBody UserRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        repository.save(new User(request.name(), request.username()));
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public void handleIllegalArgumentException(IllegalArgumentException ignored) {
    }

}
