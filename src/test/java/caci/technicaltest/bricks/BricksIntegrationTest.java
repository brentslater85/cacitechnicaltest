package caci.technicaltest.bricks;

import caci.technicaltest.bricks.repository.OrderRepository;
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


    @Test
    public void createOrder_201Created() throws Exception {
        mockMvc.perform(post("/bricks/order/100"))
            .andDo(print()).andExpect(status().isCreated())
            .andReturn();
    }

    @Test
    public void getOrder_200_valid_request() throws Exception {

        var orderRef = mockMvc.perform(post("/bricks/order/100"))
                .andDo(print()).andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        this.mockMvc.perform(get("/bricks/order/"+orderRef)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("{\"orderReference\":\"" + orderRef + "\",\"quantity\":100}"));

    }

    @Test
    public void getOrder_200_invalid_request() throws Exception {

        var uuid = UUID.randomUUID(); // use any UUID. highly unlikely to be in the DB which is what we want
        this.mockMvc.perform(get("/" + uuid)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(""));

    }

}
