package caci.technicaltest.bricks.controller;

import caci.technicaltest.bricks.dto.OrderDetails;
import caci.technicaltest.bricks.exception.InvalidOrderNumberException;
import caci.technicaltest.bricks.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;


    @Test
    public void createOrder() throws Exception {
        when(orderService.createOrder(100)).thenReturn("mockReference");
        mockMvc.perform(post("/bricks/order/100")).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().string("mockReference"));
    }

    @Test
    public void getOrder() throws Exception {
        var orderDetails = new OrderDetails("reference", 100);
        when(orderService.getOrder("reference")).thenReturn(Optional.of(orderDetails));
        mockMvc.perform(get("/bricks/order/reference")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("{\"orderReference\":\"reference\",\"quantity\":100}"));

        when(orderService.getOrder("invalid")).thenReturn(Optional.empty());
        mockMvc.perform(get("/bricks/order/invalid")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(""));

    }

    @Test
    public void updateOrder() throws Exception {
        var orderDetails = new OrderDetails("reference", 100);
        when(orderService.updateOrder(orderDetails)).thenReturn(Optional.of(orderDetails.orderReference()));
        var mapper = new ObjectMapper();

        var request = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(orderDetails);

        mockMvc.perform(put("/bricks/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("reference"));

        when(orderService.updateOrder(orderDetails)).thenReturn(Optional.empty());
        mockMvc.perform(put("/bricks/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));

    }

    @Test
    public void getAllOrders() throws Exception {
        var orderDetails = List.of(
                new OrderDetails("reference", 100),
                new OrderDetails("reference2", 150));

        when(orderService.getAllOrders()).thenReturn(orderDetails);
        mockMvc.perform(get("/bricks/order/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("[{\"orderReference\":\"reference\",\"quantity\":100},{\"orderReference\":\"reference2\",\"quantity\":150}]"));

        when(orderService.getAllOrders()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/bricks/order/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("[]"));

    }

    @Test
    public void fulfillOrder_valid() throws Exception {
        mockMvc.perform(post("/bricks/order/reference/fulfill"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void fulfillOrder_invalid() throws Exception {
        doThrow(new InvalidOrderNumberException("Order not found")).when(orderService).fulfillOrder("reference");


        mockMvc.perform(post("/bricks/order/reference/fulfill"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}