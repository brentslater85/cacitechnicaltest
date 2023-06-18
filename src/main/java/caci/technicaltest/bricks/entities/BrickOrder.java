package caci.technicaltest.bricks.entities;

import caci.technicaltest.bricks.state.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name="BRICK_ORDER")
public class BrickOrder {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="QUANTITY")
    private int quantity;

    @Column(name= "ORDER_REFERENCE", unique = true)
    private UUID orderReference;


    @Column(name = "ORDER_STATUS")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    protected BrickOrder() {}

    public BrickOrder(int quantity) {
        this.quantity = quantity;
        orderReference = UUID.randomUUID();
        orderStatus = OrderStatus.PLACED;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void fulfill() {
        if (orderStatus == OrderStatus.PLACED) {
            orderStatus = OrderStatus.DISPATCHED;
        }
    }

    public UUID getOrderReference() {
        return orderReference;
    }

    @Override
    public String toString() {
        return "BrickOrder{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", orderReference=" + orderReference +
                ", orderStatus=" + orderStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrickOrder that = (BrickOrder) o;

        if (quantity != that.quantity) return false;
        if (!orderReference.equals(that.orderReference)) return false;
        return orderStatus == that.orderStatus;
    }

    @Override
    public int hashCode() {
        int result = quantity;
        result = 31 * result + orderReference.hashCode();
        result = 31 * result + orderStatus.hashCode();
        return result;
    }
}
