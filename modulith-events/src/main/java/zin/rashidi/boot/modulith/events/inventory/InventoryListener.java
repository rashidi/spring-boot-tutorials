package zin.rashidi.boot.modulith.events.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import zin.rashidi.boot.modulith.events.common.OrderPlacedEvent;

/**
 * @author Rashidi Zin
 */
@Component
class InventoryListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryListener.class);

    @ApplicationModuleListener
    void on(OrderPlacedEvent event) {
        log.info("Reserved inventory for order: {}", event.orderId());
    }

}