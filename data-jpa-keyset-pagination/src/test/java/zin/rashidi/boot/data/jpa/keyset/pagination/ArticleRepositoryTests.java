package zin.rashidi.boot.data.jpa.keyset.pagination;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Testcontainers
class ArticleRepositoryTests {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    private static final List<Article> ARTICLES = List.of(
            new Article(UUID.randomUUID(), "Spring Boot Keyset Pagination", "Rashidi Zin", 1000),
            new Article(UUID.randomUUID(), "Mastering Spring Data JPA", "Rashidi Zin", 800),
            new Article(UUID.randomUUID(), "Java 25 Features", "Rashidi Zin", 1200),
            new Article(UUID.randomUUID(), "Testcontainers with Spring Boot", "Rashidi Zin", 900),
            new Article(UUID.randomUUID(), "Introduction to Docker", "Jane Doe", 500)
    );

    @Autowired
    private ArticleRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        ARTICLES.forEach(repository::save);
    }

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
