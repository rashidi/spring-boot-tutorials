package zin.rashidi.boot.data.jdbc.locking.user;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Rashidi Zin
 */
interface UserRepository extends CrudRepository<User, Long> {
}
