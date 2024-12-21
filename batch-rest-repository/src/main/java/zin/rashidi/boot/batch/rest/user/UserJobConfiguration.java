package zin.rashidi.boot.batch.rest.user;

import java.net.MalformedURLException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Rashidi Zin
 */
@Configuration
class UserJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MongoOperations mongo;

    UserJobConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager, MongoOperations mongo) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.mongo = mongo;
    }

    @Bean
    public Job userJob() throws MalformedURLException {
        return new JobBuilder("userJob", jobRepository).start(step()).build();
    }

    private Step step() throws MalformedURLException {
        return new StepBuilder("userStep", jobRepository)
                .<User, User>chunk(10, transactionManager)
                .reader(reader())
                .writer(writer())
                .build();
    }

    private JsonItemReader<User> reader() throws MalformedURLException {
        JacksonJsonObjectReader<User> jsonObjectReader = new JacksonJsonObjectReader<>(User.class);

        jsonObjectReader.setMapper(new ObjectMapper());

        return new JsonItemReaderBuilder<User>()
                .name("userReader")
                .jsonObjectReader(jsonObjectReader)
                .resource(new UrlResource("https://jsonplaceholder.typicode.com/users"))
                .build();
    }

    private MongoItemWriter<User> writer() {
        return new MongoItemWriterBuilder<User>()
                .template(mongo)
                .build();
    }

}
