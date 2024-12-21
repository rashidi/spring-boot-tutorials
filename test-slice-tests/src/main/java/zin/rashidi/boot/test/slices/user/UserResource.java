package zin.rashidi.boot.test.slices.user;

import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Rashidi Zin
 */
@RestController
class UserResource {

    private final UserRepository repository;

    UserResource(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/users/{username}", produces = APPLICATION_JSON_VALUE)
    public UserWithoutId findByUsername(@PathVariable String username) {
        return repository.findByUsername(username).orElseThrow(InvalidUserException::new);
    }

    @ExceptionHandler(InvalidUserException.class)
    @ResponseStatus(NOT_FOUND)
    public void handleInvalidUserException() {}

    static class InvalidUserException extends RuntimeException {}

}
