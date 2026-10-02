package zin.rashidi.boot.messaging.kafka.user;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Rashidi Zin
 */
@Component
class UserListener {

    private final List<User> receivedUsers = Collections.synchronizedList(new ArrayList<>());

    @KafkaListener(topics = "user-events", groupId = "user-group")
    void listen(User user) {
        receivedUsers.add(user);
    }

    public List<User> getReceivedUsers() {
        return List.copyOf(receivedUsers);
    }
}
