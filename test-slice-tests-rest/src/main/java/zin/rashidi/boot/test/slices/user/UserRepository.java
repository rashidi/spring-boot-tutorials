package zin.rashidi.boot.test.slices.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
interface UserRepository extends JpaRepository<User, Long> {

    @NativeQuery(
            name = "User.findByUsername",
            value = "SELECT CONCAT_WS(' ', first, last) as name,  username, status FROM users WHERE username = ?1",
            sqlResultSetMapping = "User.WithoutId"
    )
    Optional<UserWithoutId> findByUsername(String username);

}
