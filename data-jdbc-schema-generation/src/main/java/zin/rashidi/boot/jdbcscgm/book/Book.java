package zin.rashidi.boot.jdbcscgm.book;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Rashidi Zin
 */
@Table
class Book {

    @Id
    private Long isbn;
    private String title;

    @MappedCollection
    private Author author;

}
