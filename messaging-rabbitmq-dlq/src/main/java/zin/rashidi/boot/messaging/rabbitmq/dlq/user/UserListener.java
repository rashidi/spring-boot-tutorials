package zin.rashidi.boot.messaging.rabbitmq.dlq.user;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class UserListener {

    private final List<User> receivedUsers = Collections.synchronizedList(new ArrayList<>());

    @RabbitListener(queues = "user-queue")
    public void listen(User user) {
        if ("rashidi.zin".equals(user.username())) {
            throw new RuntimeException("Simulating an error to push to DLQ");
        }
        receivedUsers.add(user);
    }

    public List<User> getReceivedUsers() {
        return List.copyOf(receivedUsers);
    }
}
