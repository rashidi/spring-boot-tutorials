package zin.rashidi.boot.batch.rest.user;

import java.net.MalformedURLException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.data.MongoItemWriter;
import org.springframework.batch.infrastructure.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.batch.infrastructure.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Rashidi Zin
 */
@Configuration
class UserJobConfiguration {

    private static final JsonMapper OBJECT_MAPPER = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .build();

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MongoOperations mongo;
    private final Resource usersResource;

    UserJobConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager, MongoOperations mongo,
                         @Value("${batch.users.resource:https://jsonplaceholder.typicode.com/users}") Resource usersResource) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.mongo = mongo;
        this.usersResource = usersResource;
    }

    @Bean
    public Job userJob() throws MalformedURLException {
        return new JobBuilder("userJob", jobRepository).start(step()).build();
    }

    private Step step() throws MalformedURLException {
        return new StepBuilder("userStep", jobRepository)
                .<User, User>chunk(10)
                .transactionManager(transactionManager)
                .reader(reader())
                .writer(writer())
                .build();
    }

    private JsonItemReader<User> reader() {
        JacksonJsonObjectReader<User> jsonObjectReader = new JacksonJsonObjectReader<>(User.class);

        jsonObjectReader.setMapper(OBJECT_MAPPER);

        return new JsonItemReaderBuilder<User>()
                .name("userReader")
                .jsonObjectReader(jsonObjectReader)
                .resource(usersResource)
                .build();
    }

    private MongoItemWriter<User> writer() {
        return new MongoItemWriterBuilder<User>()
                .template(mongo)
                .build();
    }

}
