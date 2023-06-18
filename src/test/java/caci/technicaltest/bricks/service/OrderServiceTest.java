package caci.technicaltest.bricks.service;

import caci.technicaltest.bricks.entities.Order;
import caci.technicaltest.bricks.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order(100);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
    }

    @Test
    void createOrder() {
        assertEquals(order.getOrderReference().toString(), orderService.createOrder(100));
    }
}