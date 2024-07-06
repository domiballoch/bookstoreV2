package dom.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dom.bookstore.dao.BookRepository;
import dom.bookstore.domain.Book;
import dom.bookstore.domain.Category;
import dom.bookstore.exception.BookstoreNotFoundException;
import dom.bookstore.service.BookService;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.BOOK_NOT_FOUND;
import static dom.bookstore.utils.TestUtils.getResponseFrom;
import static dom.bookstore.utils.TestDataUtils.BOOKLIST;
import static dom.bookstore.utils.TestDataUtils.BOOK_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = {"classpath:test_data/repositoryTestData.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @SneakyThrows
    @Test
    public void findAllBooks() {
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findAllBooks")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final List<Book> result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.size()).isEqualTo(5);
    }

    @SneakyThrows
    @Test
    public void findBookByIsbn() {
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findBook/{isbn}", 1001)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final Book result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.getCategory()).isEqualTo(Category.FICTION);
        assertThat(result.getTitle()).isEqualTo("Tall Tales");
        assertThat(result.getAuthor()).isEqualTo("Mr Fredrikson");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(4.99));
        assertThat(result.getStock()).isEqualTo(25);

    }

    @SneakyThrows
    @Test
    public void findBookBySearchTermOneResult() {
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/search/{search}", "rose")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final List<Book> results = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getTitle()).isEqualTo("An English Rose");
    }

    @SneakyThrows
    @Test
    public void findBookBySearchTermTwoResults() {
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/search/{search}", "tales")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final List<Book> results = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.contains("Tall Tales"));
        assertThat(results.contains("Short Tales"));
    }

    @SneakyThrows
    @Test
    public void findBookByCategoryOneResult() {
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/category/{category}", "ROMANCE")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final List<Book> results = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getCategory()).isEqualTo(Category.ROMANCE);
    }

    @SneakyThrows
    @Test
    public void findBookByCategoryTwoResults() {
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/category/{category}", "FICTION")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());
        final List<Book> results = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.stream().anyMatch(b -> "Tall Tales".equals(b.getTitle())));
        assertThat(results.stream().anyMatch(b -> "Short Tales".equals(b.getTitle())));
    }

    @SneakyThrows
    @Test
    public void getBookstock() {
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/getBookstock/{isbn}", 1001)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());
        final int result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result).isEqualTo(25);
    }

    @SneakyThrows
    @Test
    public void findAllBooks_shouldThrow_NoContent() {
        bookRepository.deleteAll();

        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findAllBooks")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isNoContent());

        assertThat(bookRepository.findAll().size()).isEqualTo(0);
    }

    @SneakyThrows
    @Test
    public void findBook_shouldThrow_BookStoreNotFoundException() {
        mockMvc.perform(get("/rest/findBook/{isbn}", 99999)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BookstoreNotFoundException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), BOOK_NOT_FOUND));
    }

    @SneakyThrows
    @Test
    public void findBookBySearchTerm_shouldThrow_BookStoreNotFoundException() {
        mockMvc.perform(get("/rest/search/{search}", "random")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BookstoreNotFoundException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), BOOK_NOT_FOUND));
    }

    @SneakyThrows
    @Test
    public void findBookByCategory_shouldThrow_BookStoreNotFoundException() {
        mockMvc.perform(get("/rest/category/{category}", Category.NONE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BookstoreNotFoundException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), BOOK_NOT_FOUND));
    }

    @SneakyThrows
    @Test
    public void getBookstock_shouldThrow_BookStoreNotFoundException() {
        mockMvc.perform(get("/rest/getBookstock/{isbn}", 99999)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BookstoreNotFoundException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), BOOK_NOT_FOUND));
    }
}
