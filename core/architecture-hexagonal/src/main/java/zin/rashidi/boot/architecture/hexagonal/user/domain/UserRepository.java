package zin.rashidi.boot.architecture.hexagonal.user.domain;

import java.util.List;

public interface UserRepository {

    User save(User user);

    List<User> findAll();

}
