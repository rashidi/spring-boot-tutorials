package zin.rashidi.boot.data.mongodb.tm.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Rashidi Zin
 */
@Document
class User {
    private ObjectId id;
    private final String name;
    private final String username;

    private Status status;

    User(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public User status(Status status) {
        this.status = status;
        return this;
    }

    enum Status {

        ACTIVE,

        INACTIVE

    }
}
