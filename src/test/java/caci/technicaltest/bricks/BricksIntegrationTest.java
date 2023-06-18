package caci.technicaltest.bricks;

import caci.technicaltest.bricks.dto.OrderDetails;
import caci.technicaltest.bricks.repository.OrderRepository;
import caci.technicaltest.bricks.state.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BricksIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    private static final ObjectMapper mapper = new ObjectMapper();


    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    public void createOrder_201Created() throws Exception {
        createOrder(100);
    }

    @Test
    public void getOrder_200_valid_request() throws Exception {
        var orderRef = createOrder(100);
        checkOrder(orderRef, 100);
    }

    @Test
    public void getOrder_200_invalid_request() throws Exception {

        var uuid = UUID.randomUUID(); // use any UUID. Database is empty anyway
        mockMvc.perform(get("/bricks/order/" + uuid)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(""));

    }

    @Test
    public void getAllOrders_noOrders() throws Exception {
        mockMvc.perform(get("/bricks/order/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void getAllOrders() throws Exception {
        var ref1 = createOrder(100);
        var ref2 = createOrder(3456);
        var ref3 = createOrder(7);

        mockMvc.perform(get("/bricks/order/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("[{\"orderReference\":\"" + ref1 +
                        "\",\"quantity\":100},{\"orderReference\":\"" + ref2 +
                        "\",\"quantity\":3456},{\"orderReference\":\"" + ref3 +
                        "\",\"quantity\":7}]"));
    }

    @Test
    public void updateOrder_200_valid_request() throws Exception {

        var orderRef = createOrder(100);
        var orderDetails = new OrderDetails(orderRef, 250);

        var request = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(orderDetails);

        mockMvc.perform(put("/bricks/order/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(orderRef));

        checkOrder(orderRef, 250);
    }

    @Test
    public void fulfillOrder_200() throws Exception {
        var orderRef = createOrder(100);

        mockMvc.perform(post("/bricks/order/" + orderRef + "/fulfill"))
                .andDo(print())
                .andExpect(status().isOk());

        // we should make a change so that getOrder returns the status too, haven't as this would break earlier stories
        // it is an extension piece.
        // this means we have to check that this has worked a dirty way

        var orders = orderRepository.findByOrderReference(UUID.fromString(orderRef));
        if(orders.isEmpty()) {
            fail();
        }
        var order = orders.get(0);
        assertEquals(OrderStatus.DISPATCHED, order.getOrderStatus());
    }

    @Test
    public void fulfillOrder_400() throws Exception {
        mockMvc.perform(post("/bricks/order/" + UUID.randomUUID() + "/fulfill"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private String createOrder(int quantity) throws Exception {
        return mockMvc.perform(post("/bricks/order/" + quantity))
                .andDo(print()).andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private void checkOrder(String orderRef, int quantity) throws Exception {
        mockMvc.perform(get("/bricks/order/" + orderRef))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "{\"orderReference\":\"" + orderRef +
                                "\",\"quantity\":" + quantity + "}"));

    }

}
