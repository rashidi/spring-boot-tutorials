package zin.rashidi.boot.messaging.rabbitmq.order;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.amqp.autoconfigure.RabbitListenerRetrySettingsCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

/**
 * @author Rashidi Zin
 */
@Configuration(proxyBeanMethods = false)
class OrderQueueConfiguration {

    static final String ORDER_EXCHANGE = "orders";
    static final String ORDER_QUEUE = "orders";
    static final String ORDER_ROUTING_KEY = "orders";

    static final String DEAD_LETTER_EXCHANGE = "orders.dlx";
    static final String DEAD_LETTER_QUEUE = "orders.dlq";
    static final String DEAD_LETTER_ROUTING_KEY = "orders.dlq";

    @Bean
    DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    @Bean
    Queue orderQueue() {
        return QueueBuilder.durable(ORDER_QUEUE)
                .deadLetterExchange(DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ORDER_ROUTING_KEY);
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    @Bean
    Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DEAD_LETTER_ROUTING_KEY);
    }

    @Bean
    MessageConverter messageConverter(JsonMapper mapper) {
        // Only types from this package may be deserialized based on the __TypeId__ header
        return new JacksonJsonMessageConverter(mapper, Order.class.getPackageName());
    }

    @Bean
    RabbitListenerRetrySettingsCustomizer retrySettingsCustomizer() {
        // Permanent failures are rejected immediately instead of being retried
        return settings -> settings.setExceptionExcludes(List.of(AmqpRejectAndDontRequeueException.class));
    }

}
