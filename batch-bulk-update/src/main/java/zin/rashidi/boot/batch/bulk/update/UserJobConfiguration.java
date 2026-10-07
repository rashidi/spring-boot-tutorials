package zin.rashidi.boot.batch.bulk.update;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.batch.infrastructure.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import javax.sql.DataSource;

/**
 * @author Rashidi Zin
 */
@Configuration
class UserJobConfiguration {

    private static final JsonMapper OBJECT_MAPPER = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .build();

    private JsonItemReader<UserFile> reader() {
        JacksonJsonObjectReader<UserFile> reader = new JacksonJsonObjectReader<>(UserFile.class);

        reader.setMapper(OBJECT_MAPPER);

        return new JsonItemReaderBuilder<UserFile>()
                .jsonObjectReader(reader)
                .name("userReader")
                .resource(new ClassPathResource("users.json"))
                .build();
    }

    private ItemProcessor<UserFile, User> processor() {
        return item -> new User(item.id(), item.name(), item.username());
    }

    private JdbcBatchItemWriter<User> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setString(1, item.name());
                    ps.setString(2, item.username());
                    ps.setLong(3, item.id());
                })
                .sql("UPDATE users SET name = ?, username = ? WHERE id = ?")
                .build();
    }

    private Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource) {
        return new StepBuilder("userStep", jobRepository)
                .<UserFile, User>chunk(10)
                .transactionManager(transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer(dataSource))
                .build();
    }

    @Bean
    public Job job(JobRepository repository, PlatformTransactionManager transactionManager, DataSource dataSource) {
        return new JobBuilder("userJob", repository)
                .start(step(repository, transactionManager, dataSource))
                .build();
    }
}
