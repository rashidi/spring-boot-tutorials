package zin.rashidi.boot.test.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * @author Rashidi Zin
 */
class UserDeletionTestExecutionListener extends AbstractTestExecutionListener {

    private static Logger log = LoggerFactory.getLogger(UserDeletionTestExecutionListener.class);

    @Override
    public void afterTestClass(TestContext testContext) {
        var mongo = testContext.getApplicationContext().getBean(MongoOperations.class);

        mongo.dropCollection(User.class);

        log.info("user collection dropped");
    }

}
