package zin.rashidi.boot.test.slices.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rashidi Zin
 */
interface UserRepository extends JpaRepository<User, Long> {
}
