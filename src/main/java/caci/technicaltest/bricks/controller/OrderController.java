package caci.technicaltest.bricks.controller;

import caci.technicaltest.bricks.dto.OrderDetails;
import caci.technicaltest.bricks.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/bricks/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/{requiredQuantity}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public String createOrder(@PathVariable int requiredQuantity) {
        return orderService.createOrder(requiredQuantity);
    }

/*
* As a Rest Client I want to retrieve orders So I can display simple customers’ orders

Given
    A customer has submitted an order for some bricks
When
    A Get Order request is submitted with a valid Order reference
Then
    the order details are returned
and the order details contains the Order reference and the number of bricks ordered

When
    A Get Order request is submitted with an invalid Order reference
Then
    no order details are returned
* */
    @GetMapping("/{orderReference}")
    @ResponseBody
    public OrderDetails getOrder(@PathVariable String orderReference) {
        return orderService.getOrder(orderReference).orElse(null);
    }

}
