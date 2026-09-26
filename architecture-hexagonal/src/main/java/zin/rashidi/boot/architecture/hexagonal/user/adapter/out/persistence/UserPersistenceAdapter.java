package zin.rashidi.boot.architecture.hexagonal.user.adapter.out.persistence;

import org.springframework.stereotype.Component;
import zin.rashidi.boot.architecture.hexagonal.user.domain.User;
import zin.rashidi.boot.architecture.hexagonal.user.domain.UserRepository;

import java.util.List;

@Component
class UserPersistenceAdapter implements UserRepository {

    private final JpaUserRepository repository;

    public UserPersistenceAdapter(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity(user.id(), user.name());
        UserEntity savedEntity = repository.save(entity);
        return new User(savedEntity.getId(), savedEntity.getName());
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream()
                .map(entity -> new User(entity.getId(), entity.getName()))
                .toList();
    }
}
