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

    public Name getName() {
        return name;
    }

    @Embeddable
    static class Id implements Serializable {

        @GeneratedValue
        private Long id;

        public Long id() {
            return id;
        }

        public Id id(Long id) {
            this.id = id;
            return this;
        }

        @Override
        public final boolean equals(Object o) {
            return o instanceof Id another && this.id.equals(another.id);
       }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

    }

    @Embeddable
    record Name(@Column(name = "first_name") String first, @Column(name = "last_name") String last) {

        public Name() {
            this(null, null);
        }

    }

}
