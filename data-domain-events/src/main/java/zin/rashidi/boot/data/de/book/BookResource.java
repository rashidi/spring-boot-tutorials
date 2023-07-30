package zin.rashidi.boot.data.de.book;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rashidi Zin
 */
@RestController
class BookResource {

    private final BookRepository repository;

    BookResource(BookRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @DeleteMapping("/books/{id}/purchase")
    public void purchase(@PathVariable Long id) {
        repository.findById(id).map(Book::purchase).ifPresent(repository::delete);
    }

}
