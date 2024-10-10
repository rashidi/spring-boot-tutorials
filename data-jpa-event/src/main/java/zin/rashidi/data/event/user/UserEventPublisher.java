package zin.rashidi.data.event.user;

import jakarta.persistence.PrePersist;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author Rashidi Zin
 */
@Component
class UserEventPublisher {

    private final ApplicationEventPublisher publisher;

    UserEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PrePersist
    public void beforeSave(User user) {
        publisher.publishEvent(new UserBeforeSaveEvent(user));
    }

}
