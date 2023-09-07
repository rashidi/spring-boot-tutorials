package zin.rashidi.boot.data.mongodb.user;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Rashidi Zin
 */
@Document
class User {

    @Id
    private ObjectId id;

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

    public ObjectId id() {
        return id;
    }

    public User name(String name) {
        this.name = name;
        return this;
    }

    public User username(String username) {
        this.username = username;
        return this;
    }

    public Instant modified() {
        return modified;
    }
}
