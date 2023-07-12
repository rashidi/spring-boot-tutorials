package zin.rashidi.boot.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rashidi Zin, GfK
 */
interface UserRepository extends JpaRepository<User, Long> {
}
