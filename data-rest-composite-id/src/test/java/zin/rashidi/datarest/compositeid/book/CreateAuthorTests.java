package zin.rashidi.datarest.compositeid.book;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.lang3.math.NumberUtils;
import zin.rashidi.datarest.compositeid.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureMockMvc
class CreateAuthorTests {

    @Autowired
    private MockMvcTester mvc;

    @Test
    @DisplayName("When an Author is created Then its ID should be a number")
    void create() {
        mvc
            .post().uri("/authors")
                .contentType(APPLICATION_JSON)
                .content("""
                {
                  "name": {
                    "first": "Rudyard",
                    "last": "Kipling"
                  }
                }
                """)
            .assertThat().headers()
                .extracting(LOCATION).asString().satisfies(location -> assertThat(idFromLocation(location)).is(numeric()));
    }

    private Condition<String> numeric() {
        return new Condition<>(NumberUtils::isDigits, "is a number");
    }

    private String idFromLocation(String location) {
        return location.substring(location.lastIndexOf("/") + 1);
    }

}
