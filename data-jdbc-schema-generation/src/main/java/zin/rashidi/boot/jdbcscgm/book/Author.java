package zin.rashidi.boot.jdbcscgm.book;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Rashidi Zin
 */
@Table
class Author {

    @Id
    private Long id;
    private String name;

}
