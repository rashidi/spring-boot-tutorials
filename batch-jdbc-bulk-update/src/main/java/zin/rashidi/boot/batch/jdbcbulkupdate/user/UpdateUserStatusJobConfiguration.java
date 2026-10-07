package zin.rashidi.boot.batch.jdbcbulkupdate.user;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcPagingItemReader;
import org.springframework.batch.infrastructure.item.database.Order;
import org.springframework.batch.infrastructure.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Rashidi Zin
 */
@Configuration
class UpdateUserStatusJobConfiguration {

    @Bean
    public Job job(JobRepository jobs, Step step) {
        return new JobBuilder("updateUserStatusJob", jobs).start(step).build();
    }

    @Bean
    public Step step(ItemReader<User> reader, ItemProcessor<User, UserUpdate> processor, ItemWriter<UserUpdate> writer, JobRepository jobs, PlatformTransactionManager transactionManager) {
        return new StepBuilder("updateUserStatusStep", jobs)
                .<User, UserUpdate>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public JdbcPagingItemReader<User> reader(DataSource dataSource) throws Exception {
        return new JdbcPagingItemReaderBuilder<User>()
                .name("updateUserStatusReader")
                .dataSource(dataSource)
                .selectClause("SELECT *")
                .fromClause("FROM users")
                .sortKeys(Map.of("username", Order.ASCENDING))
                .rowMapper((rs, _) -> new User(rs.getLong("id"), rs.getString("username"), User.Status.valueOf(rs.getString("status"))))
                .build();
    }

    @Bean
    public ItemProcessor<User, UserUpdate> processor() {

        return new ItemProcessor<User, UserUpdate>() {
            @Override
            public @Nullable UserUpdate process(User item) throws Exception {
                var nextStatus = User.Status.values()[item.status().ordinal() + 1];
                return new UserUpdate(item.id(), nextStatus);
            }
        };

    }

    @Bean
    public ItemWriter<UserUpdate> writer(JdbcTemplate jdbc) {
        return chunk -> {
            var users = chunk.getItems();

            jdbc.batchUpdate("UPDATE users SET status = ? WHERE id = ?", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    var user = users.get(i);

                    ps.setString(1, user.status.name());
                    ps.setLong(2, user.id);
                }

                @Override
                public int getBatchSize() {
                    return users.size();
                }
            });
        };
    }

    static class UserUpdate {

        private final Long id;
        private final User.Status status;

        UserUpdate(Long id, User.Status status) {
            this.id = id;
            this.status = status;
        }

    }

}
