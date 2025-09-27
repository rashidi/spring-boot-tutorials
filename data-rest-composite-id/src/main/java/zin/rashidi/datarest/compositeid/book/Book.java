package zin.rashidi.datarest.compositeid.book;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * @author Rashidi Zin
 */
@Entity
class Book {

    @EmbeddedId
    private Isbn isbn = new Isbn();

    @ManyToOne(optional = false)
    private Author author;

    private String title;

    public void setIsbn(Isbn isbn) {
        this.isbn = isbn;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    @Embeddable
    static class Isbn implements Serializable {

        private Integer prefix;

        @Column(name = "registration_group")
        private Integer group;

        private Integer registrant;
        private Integer publication;

        @Column(name = "check_digit")
        private Integer check;

        protected Isbn() {}

        public Isbn(String isbn) {
            this.prefix = Integer.parseInt(isbn.substring(0, 3));
            this.group = Integer.parseInt(isbn.substring(3, 4));
            this.registrant = Integer.parseInt(isbn.substring(4, 7));
            this.publication = Integer.parseInt(isbn.substring(7, 12));
            this.check = Integer.parseInt(isbn.substring(12));
        }

        @Override
        public String toString() {
            return "%d%d%d%d%d".formatted(prefix, group, registrant, publication, check);
        }

    }

}
