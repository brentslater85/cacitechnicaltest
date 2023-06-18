package caci.technicaltest.bricks.service;

import caci.technicaltest.bricks.dto.OrderDetails;
import caci.technicaltest.bricks.entities.BrickOrder;
import caci.technicaltest.bricks.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
        var order = new BrickOrder(requiredQty);
        order = orderRepository.save(order);
        return order.getOrderReference().toString();
    }

    /**
     * Get the order details
     * @param orderReference the order reference to get details for
     * @return OrderDetails object containing the reference and quantity
     */
    public Optional<OrderDetails> getOrder(String orderReference) {
        var orderList = orderRepository.findByOrderReference(UUID.fromString(orderReference));
        return !orderList.isEmpty() ?
                Optional.of(toOrderDetails(orderList.get(0))) :
                Optional.empty();
    }


    private OrderDetails toOrderDetails(BrickOrder brickOrder) {
        return new OrderDetails(brickOrder.getOrderReference().toString(), brickOrder.getQuantity());
    }
}
