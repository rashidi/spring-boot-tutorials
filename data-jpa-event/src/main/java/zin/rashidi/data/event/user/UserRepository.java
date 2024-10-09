package zin.rashidi.data.event.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rashidi Zin
 */
interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

}
