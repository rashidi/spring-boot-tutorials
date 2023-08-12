package zin.rashidi.boot.graphql.book;

import java.util.List;

/**
 * @author Rashidi Zin
 */
interface BookRepository {

    List<Book> findAll();

    Book findByTitle(String title);

}
