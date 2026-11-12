package zin.rashidi.boot.web.restclient.user;

import java.util.List;

/**
 * @author Rashidi Zin
 */
interface UserRepository {

    List<User> findAll();

    User findById(Long id);

}
