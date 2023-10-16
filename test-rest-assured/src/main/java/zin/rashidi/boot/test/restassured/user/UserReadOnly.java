package zin.rashidi.boot.test.restassured.user;

import org.bson.types.ObjectId;

/**
 * @author Rashidi Zin
 */
record UserReadOnly(ObjectId id, String name, String username) {
}
