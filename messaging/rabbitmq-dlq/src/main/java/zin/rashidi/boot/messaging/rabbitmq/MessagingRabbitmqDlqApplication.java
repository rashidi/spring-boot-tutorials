package zin.rashidi.boot.messaging.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Rashidi Zin
 */
@SpringBootApplication
public class MessagingRabbitmqDlqApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessagingRabbitmqDlqApplication.class, args);
    }
}
