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
import org.junit.jupiter.api.DisplayName;

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
    @DisplayName("Given users exist in the database When I find all Then it should return all users")
    void findAll_ShouldReturnAllUsers() {
        List<User> users = repository.findAll();
        assertThat(users).hasSize(2)
                .containsExactlyInAnyOrderElementsOf(USERS);
    }

    @Test
    @DisplayName("Given a user exists When I find by ID Then it should return the user")
    void findById_ShouldReturnUser_WhenIdExists() {
        Optional<User> user = repository.findById(1L);
        assertThat(user).isPresent().contains(USERS.get(0));
    }

    @Test
    @DisplayName("Given a user does not exist When I find by ID Then it should return empty")
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        Optional<User> user = repository.findById(999L);
        assertThat(user).isEmpty();
    }
}
