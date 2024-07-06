package dom.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dom.bookstore.dao.BasketItemRepository;
import dom.bookstore.dao.BookRepository;
import dom.bookstore.domain.BasketItem;
import dom.bookstore.service.BasketService;
import lombok.SneakyThrows;
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

import java.util.Arrays;
import java.util.List;

import static dom.bookstore.utils.TestDataUtils.BOOK_1;
import static org.assertj.core.api.Assertions.assertThat;
import static dom.bookstore.utils.TestUtils.getResponseFrom;
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

@Sql(scripts = {"classpath:test_data/repositoryTestData.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BasketItemRepository basketItemRepository;

    @Autowired
    private BookRepository bookRepository;


    @SneakyThrows
    @Test
    public void getBasket() {
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/getBasket")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final List<BasketItem> result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.size()).isEqualTo(5);
    }

    @SneakyThrows
    @Test
    public void addNewBookToBasket() {
        bookRepository.save(BOOK_1);

        final ResultActions resultActions =
                mockMvc.perform(post("/rest/addBookToBasket/{isbn}/{quantity}", 1006, 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final BasketItem result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.getBook().getIsbn()).isEqualTo(1006);
        assertThat(basketItemRepository.findAll().size()).isEqualTo(6);
    }

    @SneakyThrows
    @Test
    public void removeBookFromBasket() {
        final ResultActions resultActions =
                mockMvc.perform(delete("/rest/removeBookFromBasket/{isbn}", 1001)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        assertThat(basketItemRepository.findAll().size()).isEqualTo(4);
    }
}
