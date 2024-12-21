package zin.rashidi.boot.test.slices.user;

import jakarta.persistence.*;

/**
 * @author Rashidi Zin
 */
@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(name = "uniqueUsername", columnNames = "username")
)
@SqlResultSetMapping(name = "User.WithoutId", classes = {
        @ConstructorResult(targetClass = UserWithoutId.class, columns = {
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "username", type = String.class),
                @ColumnResult(name = "status", type = User.Status.class)
        })
})
class User {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Name name;
    private String username;
    private Status status;

    enum Status {
        ACTIVE, DORMANT
    }

}
