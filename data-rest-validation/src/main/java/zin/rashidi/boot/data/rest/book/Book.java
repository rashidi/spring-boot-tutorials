package zin.rashidi.boot.data.rest.book;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * @author Rashidi Zin
 */

@Entity
class Book {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    @JoinColumn
    @ManyToOne(optional = false)
    private Author author;

    public Author getAuthor() {
        return author;
    }
}
