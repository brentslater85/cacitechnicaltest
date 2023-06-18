package caci.technicaltest.bricks.controller;

import caci.technicaltest.bricks.service.OrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/bricks/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /*
    As a Rest Client I want to submit new orders for bricks So I can start customers’ orders

Given
    A customer wants to buy any number of bricks
When
    A create Order request for a number of bricks is submitted
Then
    an Order reference is returned
And the Order reference is unique to the submission
     */

    @PostMapping("/{requiredQuantity}")
    @ResponseBody
    public String createOrder(@PathVariable int requiredQuantity) {
        return orderService.createOrder(requiredQuantity);
    }


}
