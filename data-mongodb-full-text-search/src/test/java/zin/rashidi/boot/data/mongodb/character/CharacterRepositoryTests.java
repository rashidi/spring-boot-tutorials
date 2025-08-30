package zin.rashidi.boot.data.mongodb.character;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@DataMongoTest
class CharacterRepositoryTests {

    @Container
    @ServiceConnection
    private final static MongoDBContainer mongo = new MongoDBContainer("mongo:latest");

    @Autowired
    private MongoOperations operations;

    @Autowired
    private CharacterRepository repository;

    @Test
    @DisplayName("Generated query: Search for 'captain marvel' should return 'Captain Marvel' and 'Thanos'")
    void withGeneratedQuery() {
        // Simulate predefined index
        operations.indexOps(Character.class).createIndex(TextIndexDefinition.builder().onFields("name", "publisher").build());

        var characters = repository.findAllBy(new TextCriteria().matchingAny("captain", "marvel"), Sort.by("name"));

        assertThat(characters)
                .hasSize(2)
                .extracting("name")
                .containsOnly("Captain Marvel", "Thanos")
                .doesNotContain("Joker");
    }

    @Test
    @DisplayName("Custom implementation: Search for 'captain marvel' should return 'Captain Marvel' and 'Thanos'")
    void findByText() {
        var characters = repository.findByText("captain marvel", Sort.by("name"));

        assertThat(characters)
                .hasSize(2)
                .extracting("name")
                .containsOnly("Captain Marvel", "Thanos")
                .doesNotContain("Joker");
    }

    @BeforeEach
    void populate() {
        repository.saveAll(List.of(
                new Character("Captain Marvel", "Marvel"),
                new Character("Joker", "DC"),
                new Character("Thanos", "Marvel")
        ));
    }

    @AfterEach
    void deleteAll() {
        repository.deleteAll();
    }

}
