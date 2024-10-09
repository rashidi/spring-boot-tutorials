package zin.rashidi.data.event.user;

import org.springframework.context.ApplicationEvent;

/**
 * @author Rashidi Zin
 */
class UserBeforeSaveEvent extends ApplicationEvent {

    public UserBeforeSaveEvent(User source) {
        super(source);
    }

    @Override
    public User getSource() {
        return (User) super.getSource();
    }

}
