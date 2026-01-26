package zin.rashidi.boot.data.de.availability;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;

import zin.rashidi.boot.data.de.TestDataDomainEventsApplication;
import zin.rashidi.boot.data.de.book.Book;
import zin.rashidi.boot.data.de.book.BookRepository;

/**
 * @author Rashidi Zin
 */
@AutoConfigureTestRestTemplate
@SpringBootTest(
        classes = TestDataDomainEventsApplication.class,
        properties = "spring.jpa.hibernate.ddl-auto=create",
        webEnvironment = RANDOM_PORT
)
class BookPurchaseTests {

    @Autowired
    private BookAvailabilityRepository availabilities;

    @Autowired
    private BookRepository books;

    @Autowired
    private TestRestTemplate client;

    private Book book;

    @BeforeEach
    void setup() {
        book = books.save(book());

        availabilities.save(availability());
    }

    @Test
    @DisplayName("Given total book availability is 100 When a book is purchased Then total book availability should be 99")
    void purchase() {
        client.delete("/books/{id}/purchase", book.getId());

        var availability = availabilities.findByIsbn(book.getIsbn());

        assertThat(availability).get()
                .extracting("total")
                .isEqualTo(99);
    }

    private Book book() {
        var book = new Book();

        book.setTitle("Say Nothing: A True Story of Murder and Memory in Northern Ireland");
        book.setAuthor("Patrick Radden Keefe");
        book.setIsbn(9780385543378L);

        return book;
    }

    private BookAvailability availability() {
        var availability =  new BookAvailability();

        availability.setIsbn(9780385543378L);
        availability.setTotal(100);

        return availability;
    }

}
