package zin.rashidi.boot.test.user;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static zin.rashidi.boot.test.user.User.Status.INACTIVE;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * @author Rashidi Zin
 */
class UserStatusUpdateTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) {
        var mongo = testContext.getApplicationContext().getBean(MongoOperations.class);
        var findByUsername = mongo.findOne(query(where("username").is("rashidi.zin")), User.class);

        mongo.save(findByUsername.status(INACTIVE));
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
