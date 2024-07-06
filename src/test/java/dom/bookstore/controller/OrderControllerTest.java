package dom.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dom.bookstore.dao.UserRepository;
import dom.bookstore.domain.OrderDetails;
import dom.bookstore.domain.Users;
import dom.bookstore.exception.BookstoreNotFoundException;
import dom.bookstore.service.OrderService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.ORDER_NOT_FOUND;
import static dom.bookstore.utils.TestDataUtils.USER_1;
import static dom.bookstore.utils.TestUtils.getResponseFrom;
import static dom.bookstore.utils.TestDataUtils.ORDERLIST;
import static dom.bookstore.utils.TestDataUtils.ORDER_DETAILS_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {"classpath:test_data/repositoryTestData.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;


    @SneakyThrows
    @Test
    public void findAllOrders() {
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findAllOrders")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final List<OrderDetails> result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.size()).isEqualTo(5);
    }

    @SneakyThrows
    @Test
    public void findOrderById() {
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findOrder/{oderDetailsId}", 6001)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final OrderDetails result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.getOrderItems().size()).isEqualTo(1);
        assertThat(result.getTotalOrderPrice()).isEqualTo(BigDecimal.valueOf(4.99));
        assertThat(result.getOrderDate()).isEqualTo(LocalDateTime.of(2022,03,01, 9,0,0));
    }


    @Disabled
    @SneakyThrows
    @Test
    public void submitOrder() {
        Optional<Users> user = userRepository.findById(2001L);
        final ResultActions resultActions =
                mockMvc.perform(post("/rest/submitOrder")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(user))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final OrderDetails result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.getOrderItems().size()).isEqualTo(5);
        assertThat(result.getUsers().getUserId()).isEqualTo(2001L);
        assertThat(result.getTotalOrderPrice()).isEqualTo(BigDecimal.valueOf(64.95));
    }

    //@Disabled
    @SneakyThrows
    @Test
    public void shouldThrow_BookStoreNotFoundException() {
        mockMvc.perform(get("/rest/findOrder/{orderDetailsId}", 99999)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BookstoreNotFoundException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), ORDER_NOT_FOUND));
    }

    //other exceptions in order service?
}
