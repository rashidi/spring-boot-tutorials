package zin.rashidi.data.event.user;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Rashidi Zin
 */
@Entity
@Table(name = "users")
@EntityListeners(UserEventPublisher.class)
class User {

    @Id
    @GeneratedValue
    private Long id;
    private String username;

    protected User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
