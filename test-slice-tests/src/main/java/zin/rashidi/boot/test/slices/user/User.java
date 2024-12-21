package zin.rashidi.boot.test.slices.user;

import jakarta.persistence.*;

import static zin.rashidi.boot.test.slices.user.User.Status.ACTIVE;

/**
 * @author Rashidi Zin
 */
@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(name = "uniqueUsername", columnNames = "username")
)
class User {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Name name;
    private String username;
    private Status status;

    protected User() {}

    User(Name name, String username) {
        this.name = name;
        this.username = username;
        this.status = ACTIVE;
    }

    @Embeddable
    record Name(String first, String last) {}

    enum Status {
        ACTIVE, DORMANT
    }

}
