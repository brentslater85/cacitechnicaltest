package caci.technicaltest.bricks;

import caci.technicaltest.bricks.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        var url = "/bricks/order/" + orderRef;

        mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("{\"orderReference\":\"" + orderRef + "\",\"quantity\":100}"));

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
                .andExpect(content().string("[{\"orderReference\":\"" + ref1 + "\",\"quantity\":100},{\"orderReference\":\"" + ref2 + "\",\"quantity\":3456},{\"orderReference\":\"" + ref3 + "\",\"quantity\":7}]"));
    }

    private String createOrder(int quantity) throws Exception {
        return mockMvc.perform(post("/bricks/order/" + quantity))
                .andDo(print()).andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

}
