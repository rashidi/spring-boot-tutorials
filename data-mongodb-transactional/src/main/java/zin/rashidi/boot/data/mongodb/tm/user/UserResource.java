package zin.rashidi.boot.data.mongodb.tm.user;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public User add(@RequestBody User user) {
        return repository.save(user);
    }

}
