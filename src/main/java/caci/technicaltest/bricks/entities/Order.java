package caci.technicaltest.bricks.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private int quantity;

    @Column(unique = true)
    private UUID orderReference;

    protected Order() {}

    public Order(int quantity) {
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
