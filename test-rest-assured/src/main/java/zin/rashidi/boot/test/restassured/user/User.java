package zin.rashidi.boot.test.restassured.user;

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

    public User(String name, String username) {
        this.name = name;
        this.username = username;
    }

}
