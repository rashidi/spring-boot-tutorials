package zin.rashidi.boot.modulith.events.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author Rashidi Zin
 */
@Table("orders")
class Order {

    @Id
    private UUID id;

    private final String customerEmail;
    private final BigDecimal totalAmount;
    private OrderStatus status;

    Order(String customerEmail, BigDecimal totalAmount, OrderStatus status) {
        this.customerEmail = customerEmail;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    enum OrderStatus {
        CREATED, COMPLETED, CANCELLED
    }

}