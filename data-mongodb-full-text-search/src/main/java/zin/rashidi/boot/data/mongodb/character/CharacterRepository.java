package zin.rashidi.boot.data.mongodb.character;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Rashidi Zin
 */
interface CharacterRepository extends MongoRepository<Character, ObjectId>, CharacterSearchRepository {

    List<Character> findAllBy(TextCriteria criteria, Sort sort);

}
