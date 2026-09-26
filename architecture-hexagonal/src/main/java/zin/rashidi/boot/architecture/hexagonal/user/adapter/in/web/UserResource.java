package zin.rashidi.boot.architecture.hexagonal.user.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import zin.rashidi.boot.architecture.hexagonal.user.application.UserUseCase;
import zin.rashidi.boot.architecture.hexagonal.user.domain.User;

import java.util.List;

@RestController
@RequestMapping("/users")
class UserResource {

    private final UserUseCase useCase;

    public UserResource(UserUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody UserRequest request) {
        User user = new User(null, request.name());
        return useCase.create(user);
    }

    @GetMapping
    public List<User> retrieveAll() {
        return useCase.retrieveAll();
    }

    record UserRequest(String name) {}
}
