package zin.rashidi.boot.modulith.externalization.shipping;

import org.springframework.stereotype.Service;
import zin.rashidi.boot.modulith.externalization.order.OrderCompletedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Rashidi Zin
 */
@Service
public class ShippingTracker {

    private final List<OrderCompletedEvent> completedOrders = Collections.synchronizedList(new ArrayList<>());

    void record(OrderCompletedEvent event) {
        completedOrders.add(event);
    }

    public List<OrderCompletedEvent> getCompletedOrders() {
        return List.copyOf(completedOrders);
    }

    public void clear() {
        completedOrders.clear();
    }

}