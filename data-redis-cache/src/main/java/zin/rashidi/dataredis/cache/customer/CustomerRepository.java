package zin.rashidi.dataredis.cache.customer;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Rashidi Zin
 */
interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Override
    @Cacheable(cacheNames = "customers", key = "#root.methodName")
    List<Customer> findAll();

    @Override
    @Cacheable(cacheNames = "customerById")
    Optional<Customer> findById(Long id);

}
