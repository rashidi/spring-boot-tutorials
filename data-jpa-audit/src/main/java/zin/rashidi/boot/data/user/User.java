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

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
