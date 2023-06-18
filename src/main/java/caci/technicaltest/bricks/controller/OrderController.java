package caci.technicaltest.bricks.controller;

import caci.technicaltest.bricks.dto.OrderDetails;
import caci.technicaltest.bricks.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bricks/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/{requiredQuantity}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String createOrder(@PathVariable int requiredQuantity) {
        return orderService.createOrder(requiredQuantity);
    }

    @GetMapping("/{orderReference}")
    public OrderDetails getOrder(@PathVariable String orderReference) {
        return orderService.getOrder(orderReference).orElse(null);
    }

    @GetMapping("/")
    public List<OrderDetails> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/")
    public String createOrder(@RequestBody OrderDetails orderDetails) {
        return orderService.updateOrder(orderDetails).orElse(null);
    }

    @PostMapping("/{orderReference}/fulfill")
    public void fulfillOrder(@PathVariable String orderReference) {
         orderService.fulfillOrder(orderReference);
    }
}
