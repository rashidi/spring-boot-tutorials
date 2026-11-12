package zin.rashidi.boot.messaging.rabbitmq;

import org.springframework.boot.SpringApplication;

/**
 * @author Rashidi Zin
 */
class TestMessagingRabbitmqDlqApplication {

    public static void main(String[] args) {
        SpringApplication.from(MessagingRabbitmqDlqApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
