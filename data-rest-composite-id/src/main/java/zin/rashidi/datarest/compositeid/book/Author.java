package zin.rashidi.datarest.compositeid.book;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * @author Rashidi Zin
 */
@Entity
class Author {

    @EmbeddedId
    private Id id = new Id();

    @Embedded
    private Name name;

    @Embeddable
    static class Id implements Serializable {

        private Long id;

        public Long id() {
            return id;
        }

        public Id id(Long id) {
            this.id = id;
            return this;
        }

    }

    @Embeddable
    record Name(@Column(name = "first_name") String first, @Column(name = "last_name") String last) { }

}
