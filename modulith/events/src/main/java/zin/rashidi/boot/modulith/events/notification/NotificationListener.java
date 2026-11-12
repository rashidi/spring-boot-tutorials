package zin.rashidi.boot.modulith.events.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import zin.rashidi.boot.modulith.events.common.OrderPlacedEvent;

/**
 * @author Rashidi Zin
 */
@Component
class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @ApplicationModuleListener
    void on(OrderPlacedEvent event) {
        log.info("Sent order confirmation email to: {} for order: {}", event.customerEmail(), event.orderId());
    }

}