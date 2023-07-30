package zin.rashidi.boot.data.de.availability;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import zin.rashidi.boot.data.de.book.BookPurchaseEvent;

/**
 * @author Rashidi Zin
 */
@Service
class BookAvailabilityManagement {

    private final BookAvailabilityRepository repository;

    BookAvailabilityManagement(BookAvailabilityRepository repository) {
        this.repository = repository;
    }

    @TransactionalEventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void updateTotal(BookPurchaseEvent event) {
        var book = event.getSource();

        repository.findByIsbn(book.getIsbn()).map(BookAvailability::reduceTotal).ifPresent(repository::save);
    }

}
