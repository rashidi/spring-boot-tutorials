package zin.rashidi.boot.data.jdbc.user;

import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

/**
 * @author Rashidi Zin
 */
@Table("users")
class User {

    @Id
    private Long id;

    @CreatedDate
    private Instant created;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Instant lastModified;

    @LastModifiedBy
    private String lastModifiedBy;

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
