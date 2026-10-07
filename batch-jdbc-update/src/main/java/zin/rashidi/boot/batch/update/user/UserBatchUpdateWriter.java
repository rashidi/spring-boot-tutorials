package zin.rashidi.boot.batch.update.user;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Rashidi Zin
 */
@Component
class UserBatchUpdateWriter implements ItemWriter<UserUpdate> {

    private final JdbcTemplate jdbcTemplate;

    UserBatchUpdateWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(Chunk<? extends UserUpdate> chunk) {
        List<? extends UserUpdate> items = chunk.getItems();

        jdbcTemplate.batchUpdate("UPDATE users SET status = ? WHERE id = ?", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                UserUpdate item = items.get(i);
                ps.setString(1, item.status());
                ps.setLong(2, item.id());
            }

            @Override
            public int getBatchSize() {
                return items.size();
            }
        });
    }

}
