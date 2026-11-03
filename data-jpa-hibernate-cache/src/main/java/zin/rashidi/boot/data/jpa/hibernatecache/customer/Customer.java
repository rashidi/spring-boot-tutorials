package zin.rashidi.boot.data.jpa.hibernatecache.customer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.Cache;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

/**
 * @author Rashidi Zin
 */
@Entity
@Cache(usage = READ_WRITE, region = "customer")
class Customer {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

}
