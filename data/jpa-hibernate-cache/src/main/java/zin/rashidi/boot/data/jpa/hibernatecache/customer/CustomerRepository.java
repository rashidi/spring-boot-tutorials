package zin.rashidi.boot.data.jpa.hibernatecache.customer;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rashidi Zin
 */
interface CustomerRepository extends JpaRepository<Customer, Long> {
}
