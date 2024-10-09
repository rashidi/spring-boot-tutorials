package zin.rashidi.data.event.user;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Rashidi Zin
 */
@Component
class UserValidation {

    private final UserRepository repository;

    UserValidation(UserRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void usernameIsUnique(UserBeforeSaveEvent event) {
        var usernameExisted = repository.existsByUsername(event.getSource().getUsername());

        if (usernameExisted) {
            throw new IllegalArgumentException("Username is already taken");
        }

    }

}
