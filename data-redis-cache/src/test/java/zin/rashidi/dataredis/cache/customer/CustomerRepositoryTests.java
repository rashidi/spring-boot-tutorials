package zin.rashidi.dataredis.cache.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import zin.rashidi.dataredis.cache.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@Sql(executionPhase = BEFORE_TEST_CLASS, statements = "INSERT INTO customer (id, name) VALUES (1, 'Rashidi Zin')")
@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customers;

    @Autowired
    private CacheManager caches;

    @Test
    @DisplayName("Given the method name is configured as the cache's key Then subsequent retrieval should return the same value as initial retrieval")
    void findAll() {
        var persisted = customers.findAll();
        var cached = caches.getCache("customers").get("findAll").get();

        assertThat(cached).isEqualTo(persisted);
    }

    @Test
    @DisplayName("Given the cache is configured Then subsequent retrieval with the same key should return the same value as initial retrieval")
    void findById() {
        var persisted = customers.findById(1L).get();
        var cached = caches.getCache("customerById").get(1L).get();

        assertThat(cached).isEqualTo(persisted);
    }

}