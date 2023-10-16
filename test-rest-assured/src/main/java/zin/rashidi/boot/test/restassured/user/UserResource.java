package zin.rashidi.boot.test.restassured.user;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/users/{username}")
    public UserReadOnly findByUsername(@PathVariable String username) {
        return repository.findByUsername(username).orElseThrow();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public void handleIllegalArgumentException(IllegalArgumentException ignored) {
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public void handleNoSuchElementException(NoSuchElementException ignored) {
    }

}
