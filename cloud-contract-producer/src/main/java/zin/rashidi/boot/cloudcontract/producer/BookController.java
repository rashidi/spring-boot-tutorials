package zin.rashidi.boot.cloudcontract.producer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
class BookController {

    private final BookRepository repository;

    BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/books/{id}")
    ResponseEntity<BookResponse> getBook(@PathVariable UUID id) {
        return repository.findById(id)
                .map(book -> new BookResponse(book.getId(), book.getTitle(), book.getAuthor()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
