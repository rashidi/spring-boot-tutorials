package zin.rashidi.boot.batch.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author Rashidi Zin
 */
@Configuration
class UserJobConfiguration {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
        return item -> switch (item.username()) {
            case "Elwyn.Skiles" -> null;
            case "Maxime_Nienow" -> throw new UsernameNotAllowedException(item.username());
            default -> new User(item.id(), item.name(), item.username());
        };
    }

    private JdbcBatchItemWriter<User> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setLong(1, item.id());
                    ps.setString(2, item.name());
                    ps.setString(3, item.username());
                })
                .sql("INSERT INTO users (id, name, username) VALUES (?, ?, ?)")
                .build();
    }

    private Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource) {
        return new StepBuilder("userStep", jobRepository)
                .<UserFile, User>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer(dataSource))
                .faultTolerant()
                .skip(UsernameNotAllowedException.class)
                .skipLimit(1)
                .build();
    }

    @Bean
    public Job job(JobRepository repository, PlatformTransactionManager transactionManager, DataSource dataSource) {
        return new JobBuilder("userJob", repository)
                .start(step(repository, transactionManager, dataSource))
                .build();
    }

    static class UsernameNotAllowedException extends RuntimeException {

        public UsernameNotAllowedException(String username) {
            super("Username " + username + " is not allowed");
        }

    }

}
