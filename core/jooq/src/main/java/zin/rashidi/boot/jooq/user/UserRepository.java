package zin.rashidi.boot.jooq.user;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
interface UserRepository {

   Optional<User> findByUsername(String username);

}
