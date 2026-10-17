package zin.rashidi.boot.jdbc.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Testcontainers
@Import(UserRepository.class)
class UserRepositoryTests {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private UserRepository repository;

    private static final List<User> USERS = List.of(
            new User(1L, "Rashidi Zin", "rashidi.zin", "rashidi@zin.my"),
            new User(2L, "Zaid Zin", "zaid.zin", "zaid@zin.my")
    );

    @BeforeEach
    void setUp() {
        USERS.forEach(repository::save);
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        List<User> users = repository.findAll();
        assertThat(users).hasSize(2)
                .containsExactlyInAnyOrderElementsOf(USERS);
    }

    @Test
    void findById_ShouldReturnUser_WhenIdExists() {
        Optional<User> user = repository.findById(1L);
        assertThat(user).isPresent().contains(USERS.get(0));
    }

    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        Optional<User> user = repository.findById(999L);
        assertThat(user).isEmpty();
    }
}
