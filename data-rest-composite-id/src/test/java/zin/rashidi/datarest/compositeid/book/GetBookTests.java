package zin.rashidi.datarest.compositeid.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.testcontainers.junit.jupiter.Testcontainers;
import zin.rashidi.datarest.compositeid.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureMockMvc
class GetBookTests {

    @Autowired
    private MockMvcTester mvc;

    @Test
    @Sql(statements = {
            "INSERT INTO author (id, first_name, last_name) VALUES (200, 'Rudyard', 'Kipling')",
            "INSERT INTO book (prefix, registration_group, registrant, publication, check_digit, author_id, title) VALUES (978, 1, 509, 82782, 9, 200, 'The Jungle Book')"
    })
    @DisplayName("Given a book is available When I request by its ISBN Then its information should be returned")
    void get() {
        mvc
            .get().uri("/books/9781509827829")
            .assertThat().bodyJson()
                .hasPathSatisfying("$.title", title -> assertThat(title).asString().isEqualTo("The Jungle Book"))
                .hasPathSatisfying("$._links.author.href", authorUri -> assertThat(authorUri).asString().isEqualTo("http://localhost/books/9781509827829/author"))
                .hasPathSatisfying("$._links.self.href", uri -> assertThat(uri).asString().isEqualTo("http://localhost/books/9781509827829"));
    }

}
