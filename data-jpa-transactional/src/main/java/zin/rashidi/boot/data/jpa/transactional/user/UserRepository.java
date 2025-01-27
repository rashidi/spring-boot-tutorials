package zin.rashidi.boot.data.jpa.transactional.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
