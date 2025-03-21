package zin.rashidi.boot.data.de.availability;

import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
@RepositoryDefinition(domainClass = BookAvailability.class, idClass = Long.class)
interface BookAvailabilityRepository {

    Optional<BookAvailability> findByIsbn(Long isbn);

    BookAvailability save(BookAvailability entity);

}
