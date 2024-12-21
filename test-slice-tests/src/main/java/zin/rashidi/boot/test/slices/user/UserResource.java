package zin.rashidi.boot.test.slices.user;

import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

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
    public User add(@RequestBody UserRequest request) {
        return repository.save(new User(request.name(), request.username()));
    }

    @GetMapping("/users/{id}")
    public User read(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    record UserRequest(Name name, String username) {
    }

}
