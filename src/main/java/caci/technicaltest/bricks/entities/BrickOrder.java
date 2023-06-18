package caci.technicaltest.bricks.entities;

import jakarta.persistence.*;

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

    protected BrickOrder() {}

    public BrickOrder(int quantity) {
        this.quantity = quantity;
        orderReference = UUID.randomUUID();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        var that = (BrickOrder) o;

        if (quantity != that.quantity) return false;
        if (!id.equals(that.id)) return false;
        return orderReference.equals(that.orderReference);
    }

    @Override
    public int hashCode() {
        var result = id.hashCode();
        result = 31 * result + quantity;
        result = 31 * result + orderReference.hashCode();
        return result;
    }
}
