package caci.technicaltest.bricks.service;

import caci.technicaltest.bricks.entities.Order;
import caci.technicaltest.bricks.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    OrderRepository orderRepository;
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    /**
     * Creates a new order of required quantity and returns the order reference.
     * @param requiredQty required quantity of bricks in the order
     * @return Order Reference
     */
    public String createOrder(int requiredQty) {
        var order = new Order(requiredQty);
        order = orderRepository.save(order);
        return order.getOrderReference().toString();
    }
}
