package zin.rashidi.boot.test.restassured.user;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @author Rashidi Zin
 */
record UserReadOnly(@JsonSerialize(using = ToStringSerializer.class) ObjectId id, String name, String username) {
}
