package caci.technicaltest.bricks.service;

import caci.technicaltest.bricks.dto.OrderDetails;
import caci.technicaltest.bricks.entities.BrickOrder;
import caci.technicaltest.bricks.exception.InvalidOrderNumberException;
import caci.technicaltest.bricks.exception.OrderDispatchedException;
import caci.technicaltest.bricks.repository.OrderRepository;
import caci.technicaltest.bricks.state.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
     * Returns the list of all orders
     * @return List of OrderDetail showing all orders.
     */
    public List<OrderDetails> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toOrderDetails)
                .collect(Collectors.toList());
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
    /**
     * Update the order quantity
     * @param orderDetails the new Order Details
     * @return OrderDetails object containing the reference and updated quantity
     * @throws caci.technicaltest.bricks.exception.OrderDispatchedException when order already dispatched
     */
    @Transactional
    public Optional<String> updateOrder(OrderDetails orderDetails) {
        var orderList = orderRepository.findByOrderReference(UUID.fromString(orderDetails.orderReference()));

        if (orderList.isEmpty()) {
            return Optional.empty();
        }
        var order = orderList.get(0);
        if (order.getOrderStatus() == OrderStatus.DISPATCHED) {
            throw new OrderDispatchedException("Order Reference: " +
                    orderDetails.orderReference() + " Unable to update. Status : Dispatched");
        }
        order.setQuantity(orderDetails.quantity());
        orderRepository.save(order);
        // I have deliberately not updated the reference, the spec wasn't clear if it needed to be
        // But I thought normally when you update an order in a real shop your order number doesn't change
        // it says unique to the submission but I felt like unique to the order.
        // I could have implemented state at this point and set the order to cancelled and created a new one
        // and returned that UUID.
        return Optional.of(order.getOrderReference().toString());
    }

    /**
     * Sets an order to status fulfilled if it exists and wasn't already.
     * @param orderReference the reference of the Order to fulfill
     */
    @Transactional
    public void fulfillOrder(String orderReference) {
        var orderList = orderRepository.findByOrderReference(UUID.fromString(orderReference));

        if(orderList.isEmpty()) {
            throw new InvalidOrderNumberException("Order Reference: " + orderReference + " Not Found");
        }
        var order = orderList.get(0);
        order.fulfill();
        orderRepository.save(order);
    }


    private OrderDetails toOrderDetails(BrickOrder brickOrder) {
        return new OrderDetails(brickOrder.getOrderReference().toString(), brickOrder.getQuantity());
    }
}
