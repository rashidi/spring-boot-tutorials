package zin.rashidi.dataredis.cache.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import zin.rashidi.dataredis.cache.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@ImportAutoConfiguration({ RedisAutoConfiguration.class, CacheAutoConfiguration.class })
@Sql(executionPhase = BEFORE_TEST_CLASS, statements = "INSERT INTO customer (id, name) VALUES (1, 'Rashidi Zin')")
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop", includeFilters = @Filter(EnableCaching.class))
class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customers;

    @Autowired
    private CacheManager caches;

    @Test
    @Transactional(readOnly = true)
    @DisplayName("Given the method name is configured as the cache's key Then subsequent retrieval should return the same value as initial retrieval")
    void findAll() {
        var persisted = customers.findAll();
        var cached = caches.getCache("customers").get("findAll").get();

        assertThat(cached).isEqualTo(persisted);
    }

    @Test
    @Transactional(readOnly = true)
    @DisplayName("Given the cache is configured Then subsequent retrieval with the same key should return the same value as initial retrieval")
    void findById() {
        var persisted = customers.findById(1L).get();
        var cached = caches.getCache("customerById").get(1L).get();

        assertThat(cached).isEqualTo(persisted);
    }

}