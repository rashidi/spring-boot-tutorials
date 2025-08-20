package zin.rashidi.boot.data.mongodb.character;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;

/**
 * @author Rashidi Zin
 */
class CharacterSearchRepositoryImpl implements CharacterSearchRepository {

    private final MongoOperations operations;

    CharacterSearchRepositoryImpl(MongoOperations operations) {
        this.operations = operations;
    }

    @Override
    public List<Character> findByText(String text, Sort sort) {
        operations.indexOps(Character.class)
                .createIndex(TextIndexDefinition.builder().onFields("name", "publisher").build());

        var parameters = text.split(" ");
        var query = TextQuery.queryText(new TextCriteria().matchingAny(parameters)).with(sort);

        return operations.find(query, Character.class);
    }

}
