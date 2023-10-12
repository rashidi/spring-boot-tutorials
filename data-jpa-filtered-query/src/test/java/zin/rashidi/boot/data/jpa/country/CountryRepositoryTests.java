package zin.rashidi.boot.data.jpa.country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.context.annotation.FilterType.ANNOTATION;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest(
        properties = "spring.jpa.hibernate.ddl-auto=create-drop",
        includeFilters = @Filter(type = ANNOTATION, classes = EnableJpaRepositories.class)
)
class CountryRepositoryTests {

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:latest");

    @Autowired
    private CountryRepository countries;

    @BeforeEach
    void setup() {
        countries.saveAll(List.of(
                new Country("DE", "Germany"),
                new Country("MY", "Malaysia")
        ));
    }

    @Test
    @DisplayName("Given there are two countries, when findAll is invoked, then both countries are returned")
    void findAll() {
        assertThat(countries.findAll())
                .hasSize(2)
                .extracting("isoCode")
                .containsOnly("DE", "MY");
    }

}
