package zin.rashidi.boot.messaging.rabbitmq.dlq.user;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserResource {

    private final RabbitTemplate rabbitTemplate;

    UserResource(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void publish(@RequestBody User user) {
        rabbitTemplate.convertAndSend("user-exchange", "user-routing-key", user);
    }
}
