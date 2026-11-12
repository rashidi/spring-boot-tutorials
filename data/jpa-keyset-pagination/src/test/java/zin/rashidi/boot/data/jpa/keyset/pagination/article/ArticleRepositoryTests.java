package zin.rashidi.boot.data.jpa.keyset.pagination.article;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@Testcontainers
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Sql(
        statements = {
                "INSERT INTO articles (id, title, author, views) VALUES (gen_random_uuid(), 'Spring Boot Keyset Pagination', 'Rashidi Zin', 1000)",
                "INSERT INTO articles (id, title, author, views) VALUES (gen_random_uuid(), 'Mastering Spring Data JPA', 'Rashidi Zin', 800)",
                "INSERT INTO articles (id, title, author, views) VALUES (gen_random_uuid(), 'Java 25 Features', 'Rashidi Zin', 1200)",
                "INSERT INTO articles (id, title, author, views) VALUES (gen_random_uuid(), 'Testcontainers with Spring Boot', 'Rashidi Zin', 900)",
                "INSERT INTO articles (id, title, author, views) VALUES (gen_random_uuid(), 'Introduction to Docker', 'Jane Doe', 500)"
        },
        executionPhase = BEFORE_TEST_CLASS
)
class ArticleRepositoryTests {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer postgres = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));

    @Autowired
    private ArticleRepository repository;

    @Test
    @DisplayName("Given articles exist When fetching by author using keyset pagination Then should return ordered window")
    void fetchUsingKeysetPagination() {
        var firstPage = repository.findTop2ByAuthorOrderByViewsDesc("Rashidi Zin", ScrollPosition.keyset());

        assertThat(firstPage.toList()).hasSize(2)
                .extracting("title").containsOnly("Java 25 Features", "Spring Boot Keyset Pagination");

        var position = firstPage.positionAt(1);

        var secondPage = repository.findTop2ByAuthorOrderByViewsDesc("Rashidi Zin", position);

        assertThat(secondPage.toList()).hasSize(2)
                .extracting("title").containsOnly("Testcontainers with Spring Boot", "Mastering Spring Data JPA");
    }

}
