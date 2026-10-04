package zin.rashidi.boot.messaging.rabbitmq.dlq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RabbitMQConfiguration {

    static final String USER_EXCHANGE = "user-exchange";
    static final String USER_QUEUE = "user-queue";
    static final String USER_ROUTING_KEY = "user-routing-key";

    static final String USER_DLQ = "user-dlq";
    static final String USER_DLQ_ROUTING_KEY = "user-dlq-routing-key";

    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    DirectExchange userExchange() {
        return new DirectExchange(USER_EXCHANGE);
    }

    @Bean
    Queue userQueue() {
        return QueueBuilder.durable(USER_QUEUE)
                .withArgument("x-dead-letter-exchange", USER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", USER_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    Queue userDlq() {
        return QueueBuilder.durable(USER_DLQ).build();
    }

    @Bean
    Binding userBinding(Queue userQueue, DirectExchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with(USER_ROUTING_KEY);
    }

    @Bean
    Binding userDlqBinding(Queue userDlq, DirectExchange userExchange) {
        return BindingBuilder.bind(userDlq).to(userExchange).with(USER_DLQ_ROUTING_KEY);
    }
}
