package zin.rashidi.boot.test.restassured.user;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * @author Rashidi Zin
 */
class UserCreationTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) {
        var mongo = testContext.getApplicationContext().getBean(MongoOperations.class);

        mongo.save(new User("Zaid Zin", "zaid.zin"));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
