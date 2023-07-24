package zin.rashidi.data.mongodb.tc.dataload.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Rashidi Zin
 */
interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByUsername(String username);

}
