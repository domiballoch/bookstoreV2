package dom.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dom.bookstore.domain.OrderDetails;
import dom.bookstore.domain.Users;
import dom.bookstore.exception.BookstoreNotFoundException;
import dom.bookstore.service.OrderService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.ORDER_NOT_FOUND;
import static dom.bookstore.utils.ControllerTestHelper.getResponseFrom;
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

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @SneakyThrows
    @Test
    public void findAllOrders() {
        when(orderService.findAllOrders()).thenReturn(ORDERLIST);
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findAllOrders")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final List<OrderDetails> result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result).isEqualTo((ORDERLIST));
        verify(orderService, times(1)).findAllOrders();
    }

    @SneakyThrows
    @Test
    public void findOrderById() {
        when(orderService.findOrderById(1)).thenReturn(Optional.of(ORDER_DETAILS_1));
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findOrder/{oderDetailsId}", 1)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.orderDetailsId").value(1));

        final OrderDetails result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.getOrderDetailsId()).isEqualTo(1);
        assertThat(result).isEqualTo(ORDER_DETAILS_1);
        verify(orderService, times(1)).findOrderById(any(Long.class));
    }


    @SneakyThrows
    @Test
    public void submitOrder() {
        when(orderService.submitOrder(any(Users.class))).thenReturn(ORDER_DETAILS_1);
        final ResultActions resultActions =
                mockMvc.perform(post("/rest/submitOrder")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(ORDER_DETAILS_1))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final OrderDetails result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result).isEqualTo((ORDER_DETAILS_1));
        verify(orderService, times(1)).submitOrder(any(Users.class));

    }

//    @Disabled
//    @SneakyThrows
//    @Test
//    public void shouldThrow_BookStoreNotFoundException() {
//        mockMvc.perform(get("/rest/findOrder/{orderDetailsId}", 99999)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isNotFound())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BookstoreNotFoundException))
//                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), ORDER_NOT_FOUND));
//    }
}
