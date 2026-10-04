package zin.rashidi.boot.messaging.rabbitmq.dlq.user;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class UserDlqListener {

    private final List<User> receivedDlqUsers = Collections.synchronizedList(new ArrayList<>());

    @RabbitListener(queues = "user-dlq")
    public void listen(User user) {
        receivedDlqUsers.add(user);
    }

    public List<User> getReceivedDlqUsers() {
        return List.copyOf(receivedDlqUsers);
    }
}
