package zin.rashidi.boot.data.jpa.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rashidi Zin
 */
interface UserRepository extends JpaRepository<User, UUID> {
}
