package zin.rashidi.boot.batch.user;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Rashidi Zin
 */
@Configuration
class UserJobConfiguration {

    private final UserRepository repository;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final MongoOperations mongo;

    UserJobConfiguration(UserRepository repository, JobRepository jobRepository, PlatformTransactionManager transactionManager, MongoOperations mongo) {
        this.repository = repository;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.mongo = mongo;
    }

    @Bean
    public Job userJob() {
        return new JobBuilder("userJob", jobRepository).start(step()).build();
    }

    private Step step() {
        return new StepBuilder("userStep", jobRepository)
                .<User, User>chunk(10, transactionManager)
                .reader(reader())
                .writer(writer())
                .build();
    }

    private ItemReader<User> reader() {
        return new ItemReader<>() {

            private final AtomicInteger counter = new AtomicInteger();
            private List<User> users = new ArrayList<>();


            @Override
            public User read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
                return getUser();
            }

            private User getUser() {
                users = users.isEmpty() ? repository.findAll() : users;
                return counter.get() < users.size() ? users.get(counter.getAndIncrement()) : null;
            }

        };
    }

    private MongoItemWriter<User> writer() {
        return new MongoItemWriterBuilder<User>()
                .template(mongo)
                .build();
    }

}
