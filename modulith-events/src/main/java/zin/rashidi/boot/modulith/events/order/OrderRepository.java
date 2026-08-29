package zin.rashidi.boot.modulith.events.order;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

/**
 * @author Rashidi Zin
 */
interface OrderRepository extends CrudRepository<Order, UUID> {
}