package zin.rashidi.boot.test.restassured.user;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Rashidi Zin
 */
interface UserRepository extends MongoRepository<User, ObjectId> {

    boolean existsByUsername(String username);

    Optional<UserReadOnly> findByUsername(String username);

}
