package zin.rashidi.data.mongodb.tc.dataload.user;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * @author Rashidi Zin
 */
class UserTestExecutionListener extends AbstractTestExecutionListener {

    private User user;


    @Override
    public void beforeTestClass(TestContext testContext) {
        var mongo = testContext.getApplicationContext().getBean(MongoOperations.class);

        user = mongo.insert(new User(null, "rashidi.zin", "Rashidi Zin"));
    }

    @Override
    public void afterTestClass(TestContext testContext) {
        var mongo = testContext.getApplicationContext().getBean(MongoOperations.class);

        mongo.remove(user);
    }

}
