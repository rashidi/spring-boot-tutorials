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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

}
