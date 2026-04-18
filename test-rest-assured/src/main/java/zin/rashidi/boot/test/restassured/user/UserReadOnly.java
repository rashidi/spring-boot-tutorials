package zin.rashidi.boot.test.restassured.user;

import org.bson.types.ObjectId;

import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

/**
 * @author Rashidi Zin
 */
record UserReadOnly(@JsonSerialize(using = ToStringSerializer.class) ObjectId id, String name, String username) {
}
