package zin.rashidi.boot.data.de.availability;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * @author Rashidi Zin
 */
@Entity
class BookAvailability {

    @Id
    @GeneratedValue
    private Long id;

    private Long isbn;

    private Integer total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public BookAvailability reduceTotal() {
        this.total--;
        return this;
    }

}
