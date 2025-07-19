package zin.rashidi.datajpa.hibernatecache.customer;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import zin.rashidi.datajpa.hibernatecache.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Import(TestcontainersConfiguration.class)
class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customers;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    @DisplayName("Given Customer is configured for caching When I make first retrieval Then its cache is available")
    @Sql(statements = "INSERT INTO customer (id, name) VALUES (1, 'Rashidi Zin')")
    void findById() {
        customers.findById(1L).orElseThrow();

        assertThat(entityManagerFactory.getCache().contains(Customer.class, 1L)).isTrue();
    }
}