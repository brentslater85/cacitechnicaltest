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

    public UUID getOrderReference() {
        return orderReference;
    }
}
