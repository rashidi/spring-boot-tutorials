package zin.rashidi.boot.data.de.availability;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
interface BookAvailabilityRepository extends JpaRepository<BookAvailability, Long> {

    Optional<BookAvailability> findByIsbn(Long isbn);

}
