package zin.rashidi.datajpa.hibernatecache.customer;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import zin.rashidi.datajpa.hibernatecache.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

/**
 * @author Rashidi Zin
 */
@DataJpaTest(properties =  {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.generate_statistics=true"
})
@Import(TestcontainersConfiguration.class)
@Sql(statements = "INSERT INTO customer (id, name) VALUES (1, 'Rashidi Zin')", executionPhase = BEFORE_TEST_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customers;

    private Statistics statistics;

    @BeforeEach
    void setupStatistics(@Autowired EntityManagerFactory entityManagerFactory) {
        statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
    }

    @Test
    @Order(1)
    @Transactional(propagation = REQUIRES_NEW)
    @DisplayName("On initial retrieval data will be retrieved from the database and customer cache will be stored")
    void initial() {
        customers.findById(1L).orElseThrow();

        assertThat(statistics.getSecondLevelCachePutCount()).isEqualTo(1);
        assertThat(statistics.getSecondLevelCacheHitCount()).isZero();
    }

    @Test
    @Order(2)
    @Transactional(propagation = REQUIRES_NEW)
    @DisplayName("On subsequent retrieval data will be retrieved from the customer cache")
    void subsequent() {
        customers.findById(1L).orElseThrow();

        assertThat(statistics.getSecondLevelCacheHitCount()).isEqualTo(1);
    }

}
