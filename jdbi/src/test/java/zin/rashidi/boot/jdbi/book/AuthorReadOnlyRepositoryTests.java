package zin.rashidi.boot.jdbi.book;

import org.assertj.core.api.Assertions;
import org.jdbi.v3.spring.EnableJdbiRepositories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import zin.rashidi.boot.jdbi.TestcontainersConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase
@Transactional
@SpringBootTest(webEnvironment = NONE)
@Sql(scripts = "classpath:schema.sql")
class AuthorReadOnlyRepositoryTests {

    @Autowired
    private AuthorReadOnlyRepository authors;

    @Test
    void findAll() {
        Assertions.assertThat(authors.findAll()).hasSize(1);
    }

    @Test
    void findById() {
    }

    @Test
    void findByName() {
    }

}