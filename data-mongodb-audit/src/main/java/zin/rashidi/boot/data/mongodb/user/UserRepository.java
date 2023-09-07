package zin.rashidi.boot.data.mongodb.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Rashidi Zin
 */
interface UserRepository extends MongoRepository<User, ObjectId> {
}
