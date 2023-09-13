package zin.rashidi.boot.batch.user;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;

import zin.rashidi.boot.batch.TestBatchRestRepositoryApplication;

/**
 * @author Rashidi Zin
 */
@Import(TestBatchRestRepositoryApplication.class)
@SpringBootTest(properties = "spring.batch.jdbc.initialize-schema=always")
class UserBatchJobTests {

    @Autowired
    private MongoOperations mongo;

    @Test
    @DisplayName("Given there are 10 users returned from REST Service When the job is executed Then all users should be saved to MongoDB")
    void launch() {

        await().atMost(ofSeconds(30)).untilAsserted(() -> {
            var persistedUsers = mongo.findAll(User.class);

            assertThat(persistedUsers).hasSize(10);
        });

    }

}
