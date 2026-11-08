package zin.rashidi.boot.modulith.externalization.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

/**
 * @author Rashidi Zin
 */
@Table("orders")
class Order {

    @Id
    private Long id;

    private final String customerEmail;
    private final BigDecimal totalAmount;
    private OrderStatus status;

    Order(String customerEmail, BigDecimal totalAmount, OrderStatus status) {
        this.customerEmail = customerEmail;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    Long getId() {
        return id;
    }

    String getCustomerEmail() {
        return customerEmail;
    }

    BigDecimal getTotalAmount() {
        return totalAmount;
    }

    OrderStatus getStatus() {
        return status;
    }

    void setStatus(OrderStatus status) {
        this.status = status;
    }

    enum OrderStatus {
        CREATED, COMPLETED
    }

}