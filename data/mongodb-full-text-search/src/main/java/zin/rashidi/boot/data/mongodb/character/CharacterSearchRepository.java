package zin.rashidi.boot.data.mongodb.character;

import java.util.List;

import org.springframework.data.domain.Sort;

/**
 * @author Rashidi Zin
 */
interface CharacterSearchRepository {

    List<Character> findByText(String text, Sort sort);

}
