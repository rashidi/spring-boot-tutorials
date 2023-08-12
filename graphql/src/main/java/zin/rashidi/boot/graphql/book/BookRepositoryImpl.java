package zin.rashidi.boot.graphql.book;

import org.springframework.stereotype.Repository;
import zin.rashidi.boot.graphql.book.Book.Author;
import zin.rashidi.boot.graphql.book.Book.Author.Name;
import zin.rashidi.boot.graphql.book.Book.Isbn;

import java.util.List;

/**
 * @author Rashidi Zin
 */
@Repository
class BookRepositoryImpl implements BookRepository {

    @Override
    public List<Book> findAll() {
        return List.of(
                new Book(new Isbn(9780132350884L, 978, 0, 13235088, 4), "Clean Code", new Author(new Name("Robert", "Martin"))),
                new Book(new Isbn(9780201633610L, 978, 0, 20163361, 0), "Design Patterns", new Author(new Name("Erich", "Gamma"))),
                new Book(new Isbn(9780132350884L, 978, 0, 13235088, 4), "The Hobbit", new Author(new Name("J.R.R.", "Tolkien")))
        );
    }

    @Override
    public Book findByTitle(String title) {
        return findAll().stream().filter(book -> book.title().equalsIgnoreCase(title)).findFirst().orElse(null);
    }

}
