package zin.rashidi.data.mongodb.tc.dataload.user;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Rashidi Zin
 */
@Document
record User(@Id ObjectId id, String username, String name) {

}
