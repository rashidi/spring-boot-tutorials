package zin.rashidi.boot.data.user;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * @author Rashidi Zin
 */
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String username;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant created;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private Instant modified;

    protected User() {}

    User(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
