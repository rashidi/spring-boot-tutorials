package zin.rashidi.boot.test.user;

import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import zin.rashidi.boot.test.user.User.Status;

/**
 * @author Rashidi Zin
 */
interface UserRepository extends MongoRepository<User, ObjectId> {

    Stream<User> findByStatus(Status status);

}
