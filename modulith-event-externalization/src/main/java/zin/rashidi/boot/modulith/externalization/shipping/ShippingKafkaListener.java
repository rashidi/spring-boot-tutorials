package zin.rashidi.boot.modulith.externalization.shipping;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import zin.rashidi.boot.modulith.externalization.order.OrderCompletedEvent;

/**
 * @author Rashidi Zin
 */
@Component
class ShippingKafkaListener {

    private final ShippingTracker tracker;

    ShippingKafkaListener(ShippingTracker tracker) {
        this.tracker = tracker;
    }

    @KafkaListener(topics = "orders.completed", groupId = "shipping-group")
    void onOrderCompleted(OrderCompletedEvent event) {
        tracker.record(event);
    }

}