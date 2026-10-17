package zin.rashidi.boot.jdbc.client;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<User> findAll() {
        return jdbcClient.sql("SELECT id, name, username, email FROM users")
                .query(User.class)
                .list();
    }

    public Optional<User> findById(Long id) {
        return jdbcClient.sql("SELECT id, name, username, email FROM users WHERE id = :id")
                .param("id", id)
                .query(User.class)
                .optional();
    }

    public void save(User user) {
        jdbcClient.sql("INSERT INTO users (id, name, username, email) VALUES (:id, :name, :username, :email)")
                .param("id", user.id())
                .param("name", user.name())
                .param("username", user.username())
                .param("email", user.email())
                .update();
    }
}
