package zin.rashidi.boot.data.rest.book;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * @author Rashidi Zin
 */
@Entity
class Author {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private Status status;

    @OneToMany(mappedBy = "author")
    private Set<Book> books;

    enum Status {

        ACTIVE,

        INACTIVE

    }

    public Status getStatus() {
        return status;
    }

}
