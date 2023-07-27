package zin.rashidi.boot.data.mongodb.tm.user;

import static zin.rashidi.boot.data.mongodb.tm.user.User.Status.ACTIVE;

import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author Rashidi Zin
 */
@Component
class UpdateUserStatus {

    @TransactionalEventListener
    public void onBeforeSave(BeforeSaveEvent<User> event) {
        var user = event.getSource();

        user.status(ACTIVE);
    }

}
