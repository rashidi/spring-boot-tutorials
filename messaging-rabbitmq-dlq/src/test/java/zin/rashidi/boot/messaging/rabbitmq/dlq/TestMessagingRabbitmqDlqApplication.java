package zin.rashidi.boot.messaging.rabbitmq.dlq;

import org.springframework.boot.SpringApplication;

public class TestMessagingRabbitmqDlqApplication {

	public static void main(String[] args) {
		SpringApplication.from(MessagingRabbitmqDlqApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
