package dom.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dom.bookstore.domain.BasketItem;
import dom.bookstore.domain.Book;
import dom.bookstore.service.BasketService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static dom.bookstore.utils.TestDataUtils.BOOK_1;
import static org.assertj.core.api.Assertions.assertThat;
import static dom.bookstore.utils.ControllerTestHelper.getResponseFrom;
import static dom.bookstore.utils.TestDataUtils.BASKET_1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BasketController.class)
public class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BasketService basketService;


    @SneakyThrows
    @Test
    public void getBasket() {
        when(basketService.getBasket()).thenReturn(Arrays.asList(BASKET_1));
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/getBasket")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final List<BasketItem> result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result).isEqualTo(Arrays.asList(BASKET_1));
        verify(basketService, times(1)).getBasket();
    }

    @SneakyThrows
    @Test
    public void addNewBookToBasket() {
        when(basketService.addBookToBasket(any(Long.class), any(Integer.class))).thenReturn(BASKET_1);
        final ResultActions resultActions =
                mockMvc.perform(post("/rest/addBookToBasket/{isbn}/{quantity}", 1, 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(BASKET_1))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final BasketItem result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result).isEqualTo(BASKET_1);
        verify(basketService, times(1)).addBookToBasket(any(Long.class), any(Integer.class));
    }

    @SneakyThrows
    @Test
    public void removeBookFromBasket() {
        final ResultActions resultActions =
                mockMvc.perform(delete("/rest/removeBookFromBasket/{isbn}", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        verify(basketService, times(1)).removeBookFromBasket(any(Long.class));
    }
}
