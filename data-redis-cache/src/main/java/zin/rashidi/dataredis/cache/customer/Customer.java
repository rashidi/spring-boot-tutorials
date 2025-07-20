package zin.rashidi.dataredis.cache.customer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serializable;

/**
 * @author Rashidi Zin
 */
@Entity
class Customer implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Override
    public final boolean equals(Object o) {
        return o instanceof Customer another && this.id.equals(another.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
