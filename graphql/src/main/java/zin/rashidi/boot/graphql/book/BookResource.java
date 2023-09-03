package zin.rashidi.boot.graphql.book;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Rashidi Zin
 */
@Controller
class BookResource {

    private final BookRepository repository;

    BookResource(BookRepository repository) {
        this.repository = repository;
    }

    @QueryMapping
    public List<Book> findAll() {
        return repository.findAll();
    }

    @QueryMapping
    public Book findByTitle(@Argument String title) {
        return repository.findByTitle(title);
    }

}
