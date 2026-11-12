package zin.rashidi.boot.test.user;

import static zin.rashidi.boot.test.user.User.Status.ACTIVE;

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
    private Status status = ACTIVE;

    public User(String name, String username) {
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
