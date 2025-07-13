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

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureMockMvc
class CreateBookTests {

    @Autowired
    private MockMvcTester mvc;

    @Test
    @DisplayName("When a Book is created with an ISBN Then its Location should consists of the ISBN")
    @Sql(statements = "INSERT INTO author (id, first_name, last_name) VALUES (100, 'Rudyard', 'Kipling')")
    void create() {
        mvc
            .post().uri("/books")
                .content("""
                {
                  "isbn": "9781402745777",
                  "title": "The Jungle Book",
                  "author": "http://localhost/authors/100"
                }
                """)
            .assertThat().headers()
                .extracting(LOCATION).asString().isEqualTo("http://localhost/books/9781402745777");
    }

}
