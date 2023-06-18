package caci.technicaltest.bricks.service;

import caci.technicaltest.bricks.entities.BrickOrder;
import caci.technicaltest.bricks.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrickOrderServiceTest {

    private static final String REFERENCE = "1-1-1-1-1";
    public static final int QTY = 100;
    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;

    private BrickOrder brickOrder;

    @BeforeEach
    public void setUp() {
        brickOrder = new BrickOrder(QTY);
    }

    @Test
    void createOrder() {
        when(orderRepository.save(any(BrickOrder.class))).thenReturn(brickOrder);
        assertEquals(brickOrder.getOrderReference().toString(), orderService.createOrder(QTY));
    }

    @Test
    void getOrder() {
        when(orderRepository.findByOrderReference(UUID.fromString(REFERENCE))).thenReturn(List.of(brickOrder));
        var response = orderService.getOrder(REFERENCE);
        assertTrue(response.isPresent());
        assertEquals(brickOrder.getOrderReference().toString(), response.get().orderReference());
        assertEquals(brickOrder.getQuantity(), response.get().quantity());

    }

    @Test
    void getOrder_noOrder() {
        when(orderRepository.findByOrderReference(UUID.fromString(REFERENCE))).thenReturn(Collections.emptyList());
        var response = orderService.getOrder(REFERENCE);
        assertTrue(response.isEmpty());
    }

    void getAllOrders() {
        var order1 = new BrickOrder(1000);
        var order2 = new BrickOrder(50);
        var order3 = new BrickOrder(200);
        when(orderRepository.findAll()).thenReturn(List.of(order1, order2, order3));

        var response = orderService.getAllOrders();

        assertEquals(3, response.size());

        assertEquals(1000, response.get(0).quantity());
        assertEquals(50, response.get(1).quantity());
        assertEquals(200, response.get(2).quantity());

        assertEquals(order1.getOrderReference().toString(), response.get(0).orderReference());
        assertEquals(order2.getOrderReference().toString(), response.get(1).orderReference());
        assertEquals(order3.getOrderReference().toString(), response.get(2).orderReference());

    }

    void getAllOrders_noOrder() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

    }
}