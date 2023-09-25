package zin.rashidi.boot.data.mongodb.character;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Rashidi Zin
 */
@Document
class Character {

    @Id
    private ObjectId id;

    @TextIndexed
    private final String name;

    @TextIndexed
    private final String publisher;

    public Character(String name, String publisher) {
        this.name = name;
        this.publisher = publisher;
    }

}
