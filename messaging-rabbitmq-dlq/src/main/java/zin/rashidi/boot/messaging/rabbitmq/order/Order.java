package zin.rashidi.boot.messaging.rabbitmq.order;

/**
 * @author Rashidi Zin
 */
public record Order(Long id, String product, int quantity) {
}
