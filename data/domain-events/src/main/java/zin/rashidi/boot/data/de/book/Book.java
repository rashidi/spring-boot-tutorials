package zin.rashidi.boot.data.de.book;

import org.springframework.data.domain.AbstractAggregateRoot;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * @author Rashidi Zin
 */
@Entity
public class Book extends AbstractAggregateRoot<Book> {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String author;
    private Long isbn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public Book purchase() {
        registerEvent(new BookPurchaseEvent(this));
        return this;
    }

}
