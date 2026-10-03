package zin.rashidi.boot.messaging.kafka.user;

import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rashidi Zin
 */
@RestController
class UserResource {

    private final KafkaTemplate<String, User> kafkaTemplate;

    UserResource(KafkaTemplate<String, User> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void publish(@RequestBody User user) {
        kafkaTemplate.send("user-events", String.valueOf(user.id()), user);
    }

}
