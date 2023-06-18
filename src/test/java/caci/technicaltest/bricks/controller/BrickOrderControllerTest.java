package caci.technicaltest.bricks.controller;

import caci.technicaltest.bricks.dto.OrderDetails;
import caci.technicaltest.bricks.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class BrickOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;


    @Test
    public void createOrder() throws Exception {
        when(orderService.createOrder(100)).thenReturn("mockReference");
        this.mockMvc.perform(post("/bricks/order/100")).andDo(print()).andExpect(status().isCreated())
                .andExpect(content().string("mockReference"));
    }

    @Test
    public void getOrder() throws Exception {
        var orderDetails = new OrderDetails("reference", 100);
        when(orderService.getOrder("reference")).thenReturn(Optional.of(orderDetails));
        this.mockMvc.perform(get("/bricks/order/reference")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("{\"orderReference\":\"reference\",\"quantity\":100}"));

        when(orderService.getOrder("invalid")).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/bricks/order/invalid")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(""));

    }
}