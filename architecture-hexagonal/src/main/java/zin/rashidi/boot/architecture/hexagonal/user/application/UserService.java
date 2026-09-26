package zin.rashidi.boot.architecture.hexagonal.user.application;

import org.springframework.stereotype.Service;
import zin.rashidi.boot.architecture.hexagonal.user.domain.User;
import zin.rashidi.boot.architecture.hexagonal.user.domain.UserRepository;

import java.util.List;

@Service
class UserService implements UserUseCase {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public List<User> retrieveAll() {
        return repository.findAll();
    }

}
