package zin.rashidi.boot.data.jdbc.locking.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Rashidi Zin
 */
@Table("users")
class User {

    @Id
    private Long id;

    @Version
    private Long version;

    private final String name;
    private String username;

    User(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public User username(String username) {
        this.username = username;
        return this;
    }

}
