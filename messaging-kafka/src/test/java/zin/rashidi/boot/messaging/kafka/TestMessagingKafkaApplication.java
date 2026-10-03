package zin.rashidi.boot.messaging.kafka;

import org.springframework.boot.SpringApplication;

/**
 * @author Rashidi Zin
 */
class TestMessagingKafkaApplication {

    public static void main(String[] args) {
        SpringApplication.from(MessagingKafkaApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
