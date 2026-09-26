package zin.rashidi.boot.architecture.hexagonal.user.application;

import zin.rashidi.boot.architecture.hexagonal.user.domain.User;

import java.util.List;

public interface UserUseCase {

    User create(User user);

    List<User> retrieveAll();

}
