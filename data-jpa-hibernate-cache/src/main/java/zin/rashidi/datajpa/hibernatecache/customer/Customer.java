package zin.rashidi.datajpa.hibernatecache.customer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * @author Rashidi Zin
 */
@Entity
class Customer {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

}
