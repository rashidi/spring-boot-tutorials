package zin.rashidi.boot.data.de.availability;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rashidi Zin
 */
interface BookAvailabilityRepository extends JpaRepository<BookAvailability, Long> {

    Optional<BookAvailability> findByIsbn(Long isbn);

}
