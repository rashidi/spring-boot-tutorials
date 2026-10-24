package zin.rashidi.boot.data.jpa.keyset.pagination;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Testcontainers
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
        Window<Article> firstPage = repository.findTop2ByAuthorOrderByViewsDesc("Rashidi Zin", ScrollPosition.keyset());

        assertThat(firstPage).isNotNull();
        assertThat(firstPage.isEmpty()).isFalse();
        assertThat(firstPage.toList()).hasSize(2);
        assertThat(firstPage.toList().get(0).getTitle()).isEqualTo("Java 25 Features");
        assertThat(firstPage.toList().get(1).getTitle()).isEqualTo("Spring Boot Keyset Pagination");

        ScrollPosition position = firstPage.positionAt(1);

        Window<Article> secondPage = repository.findTop2ByAuthorOrderByViewsDesc("Rashidi Zin", position);

        assertThat(secondPage).isNotNull();
        assertThat(secondPage.isEmpty()).isFalse();
        assertThat(secondPage.toList()).hasSize(2);
        assertThat(secondPage.toList().get(0).getTitle()).isEqualTo("Testcontainers with Spring Boot");
        assertThat(secondPage.toList().get(1).getTitle()).isEqualTo("Mastering Spring Data JPA");
    }
}
