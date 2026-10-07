package zin.rashidi.boot.batch.update.user;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.batch.infrastructure.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author Rashidi Zin
 */
@Configuration
class UserJobConfiguration {

    private static final JsonMapper OBJECT_MAPPER = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .build();

    private JsonItemReader<UserUpdate> reader() {
        JacksonJsonObjectReader<UserUpdate> reader = new JacksonJsonObjectReader<>(UserUpdate.class);

        reader.setMapper(OBJECT_MAPPER);

        return new JsonItemReaderBuilder<UserUpdate>()
                .jsonObjectReader(reader)
                .name("userUpdateReader")
                .resource(new ClassPathResource("user-updates.json"))
                .build();
    }

    private Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, UserBatchUpdateWriter writer) {
        return new StepBuilder("userUpdateStep", jobRepository)
                .<UserUpdate, UserUpdate>chunk(10)
                .transactionManager(transactionManager)
                .reader(reader())
                .writer(writer)
                .build();
    }

    @Bean
    public Job job(JobRepository repository, PlatformTransactionManager transactionManager, UserBatchUpdateWriter writer) {
        return new JobBuilder("userUpdateJob", repository)
                .start(step(repository, transactionManager, writer))
                .build();
    }
}
