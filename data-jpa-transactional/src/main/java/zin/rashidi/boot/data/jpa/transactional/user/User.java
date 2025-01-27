package zin.rashidi.boot.data.jpa.transactional.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Rashidi Zin
 */
@Entity
@Table(name = "users")
class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String username;

    protected User() {}

    User(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public User username(String username) {
        this.username = username;
        return this;
    }

}
