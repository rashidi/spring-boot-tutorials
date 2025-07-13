package zin.rashidi.datarest.compositeid.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Rashidi Zin
 */
@RepositoryRestResource
interface AuthorRepository extends JpaRepository<Author, Author.Id> {
}
