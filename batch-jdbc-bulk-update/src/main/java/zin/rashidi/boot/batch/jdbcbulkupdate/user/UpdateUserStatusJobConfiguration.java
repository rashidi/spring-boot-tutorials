package zin.rashidi.boot.batch.jdbcbulkupdate.user;

import org.springframework.batch.infrastructure.item.database.JdbcPagingItemReader;
import org.springframework.batch.infrastructure.item.database.Order;
import org.springframework.batch.infrastructure.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Rashidi Zin
 */
@Configuration
class UpdateUserStatusJobConfiguration {

    @Bean
    public JdbcPagingItemReader<User> reader(DataSource dataSource) throws Exception {
        return new JdbcPagingItemReaderBuilder<User>()
                .name("updateUserStatusReader")
                .dataSource(dataSource)
                .selectClause("SELECT id, status")
                .fromClause("FROM users")
                .whereClause("status != 'ACTIVE'")
                .sortKeys(Map.of("username", Order.ASCENDING))
                .rowMapper((rs, _) -> new User(rs.getLong("id"), rs.getString("username"), rs.getObject("status", User.Status.class)))
                .build();
    }

}
