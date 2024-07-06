package dom.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dom.bookstore.dao.BookRepository;
import dom.bookstore.domain.Book;
import dom.bookstore.domain.Category;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.util.Optional;

import static dom.bookstore.utils.TestUtils.getResponseFrom;
import static dom.bookstore.utils.TestDataUtils.BOOK_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static dom.bookstore.utils.TestUtils.getJsonRequestFromClasspathAsString;

@Sql(scripts = {"classpath:test_data/repositoryTestData.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;


    @SneakyThrows
    @Test
    public void addNewBookToBookstore() {
        final ResultActions resultActions =
                mockMvc.perform(post("/rest/addNewBook")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(BOOK_1))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isCreated());

        final Book result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.getCategory()).isEqualTo(Category.COOKING);
        assertThat(result.getTitle()).isEqualTo("title1");
        assertThat(result.getAuthor()).isEqualTo("author1");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(4.99));
        assertThat(result.getStock()).isEqualTo(10);
        assertThat(bookRepository.findAll().size()).isEqualTo(6);
    }

    @SneakyThrows
    @Test
    public void deleteBookFromBookstore() {
        mockMvc.perform(delete("/rest/deleteBook/{isbn}", 1001)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        assertThat(bookRepository.findById(1001L)).isEmpty();
        assertThat(bookRepository.findAll().size()).isEqualTo(4);
    }

    @SneakyThrows
    @Test
    public void updateBookInBookstore() {
        Optional<Book> book1 = bookRepository.findById(1001L);
        assertThat(book1.get().getCategory()).isEqualTo(Category.FICTION);
        assertThat(book1.get().getTitle()).isEqualTo("Tall Tales");
        assertThat(book1.get().getAuthor()).isEqualTo("Mr Fredrikson");
        assertThat(book1.get().getPrice()).isEqualTo(BigDecimal.valueOf(4.99));
        assertThat(book1.get().getStock()).isEqualTo(25);

        final ResultActions resultActions =
                mockMvc.perform(put("/rest/updateBook/{isbn}", 1001)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(BOOK_1))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final Book result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.getCategory()).isEqualTo(Category.COOKING);
        assertThat(result.getTitle()).isEqualTo("title1");
        assertThat(result.getAuthor()).isEqualTo("author1");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(4.99));
        assertThat(result.getStock()).isEqualTo(10);
    }

    @SneakyThrows
    @Test
    public void addNewBook_shouldThrow_MethodArgumentNotValidException_BadRequest_whenTitleIsEmpty() {
        //Two different ways of testing a request
        String bookInJson = "{\"category\": \"KIDS\", " +
                "\"title\": \"\", " +
                "\"author\": \"Mr Dom\", " +
                "\"price\": \"100\", " +
                "\"stock\": \"1\"}";

        String bookRequest = getJsonRequestFromClasspathAsString("/requests/book_request_title_empty.json", Book.class);

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Title must not be empty"))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Title length must be greater than zero"));
    }

    @SneakyThrows
    @Test
    public void addNewBook_shouldThrow_MethodArgumentNotValidException_BadRequest_whenTitleIsTooLong() {
        String bookInJson = "{\"category\": \"KIDS\", " +
                "\"title\": \"A Looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooog Title\", " +
                "\"author\": \"Mr Dom\", " +
                "\"price\": \"100\", " +
                "\"stock\": \"1\"}";

        String bookRequest = getJsonRequestFromClasspathAsString("/requests/book_request_title_too_long.json", Book.class);

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Title length must be less than one hundred chars"));
    }

    @SneakyThrows
    @Test
    public void addNewBook_shouldThrow_MethodArgumentNotValidException_BadRequest_whenAuthorIsEmpty() {
        String bookInJson = "{\"category\": \"KIDS\", " +
                "\"title\": \"Title\", " +
                "\"author\": \"\", " +
                "\"price\": \"100\", " +
                "\"stock\": \"1\"}";

        String bookRequest = getJsonRequestFromClasspathAsString("/requests/book_request_author_empty.json", Book.class);

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Author must not be empty"))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Author length must be greater than zero"));
    }

    @SneakyThrows
    @Test
    public void addNewBook_shouldThrow_MethodArgumentNotValidException_BadRequest_whenAuthorIsTooLong() {
        String bookInJson = "{\"category\": \"KIDS\", " +
                "\"title\": \"Title\", " +
                "\"author\": \"Mr Doooooooooooooooooooooooooooooooooooooooooooooom\", " +
                "\"price\": \"100\", " +
                "\"stock\": \"1\"}";

        String bookRequest = getJsonRequestFromClasspathAsString("/requests/book_request_author_too_long.json", Book.class);

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Author length must be less than fifty chars"));
    }

    @SneakyThrows
    @Test
    public void addNewBook_shouldThrow_MethodArgumentNotValidException_BadRequest_whenPriceIsEmpty() {
        String bookInJson = "{\"category\": \"KIDS\", " +
                "\"title\": \"Title\", " +
                "\"author\": \"Mr Dom\", " +
                "\"price\": \"\", " +
                "\"stock\": \"1\"}";

        String bookRequest = getJsonRequestFromClasspathAsString("/requests/book_request_price_empty.json", Book.class);

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Price cannot be null"));
    }

    @SneakyThrows
    @Test
    public void addNewBook_shouldThrow_MethodArgumentNotValidException_BadRequest_whenPriceIsZero() {
        String bookInJson = "{\"category\": \"KIDS\", " +
                "\"title\": \"Title\", " +
                "\"author\": \"Mr Dom\", " +
                "\"price\": \"0\", " +
                "\"stock\": \"1\"}";

        String bookRequest = getJsonRequestFromClasspathAsString("/requests/book_request_price_zero.json", Book.class);

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Price must be greater than zero"));

    }

    @SneakyThrows
    @Test
    public void addNewBook_shouldThrow_MethodArgumentNotValidException_BadRequest_whenPriceIsTooHigh() {
        String bookInJson = "{\"category\": \"KIDS\", " +
                "\"title\": \"Title\", " +
                "\"author\": \"Mr Dom\", " +
                "\"price\": \"1001.00\", " +
                "\"stock\": \"1\"}";

        String bookRequest = getJsonRequestFromClasspathAsString("/requests/book_request_price_too_high.json", Book.class);

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Price must have a max of 3 integers and 2 fractions"))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Price must be less than 1,000.00"));

    }

    @SneakyThrows
    @Test
    public void addNewBook_shouldThrow_MethodArgumentNotValidException_BadRequest_whenStockIsEmpty() {
        String bookInJson = "{\"category\": \"KIDS\", " +
                "\"title\": \"Title\", " +
                "\"author\": \"Mr Dom\", " +
                "\"price\": \"10.99\", " +
                "\"stock\": \"\"}";

        String bookRequest = getJsonRequestFromClasspathAsString("/requests/book_request_stock_empty.json", Book.class);

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Stock cannot be null"));
    }

    @SneakyThrows
    @Test
    public void addNewBook_shouldThrow_MethodArgumentNotValidException_BadRequest_whenStockIsNotANumber() {
        String bookInJson = "{\"category\": \"KIDS\", " +
                "\"title\": \"Title\", " +
                "\"author\": \"Mr Dom\", " +
                "\"price\": \"10.99\", " +
                "\"stock\": \"-1\"}";

        String bookRequest = getJsonRequestFromClasspathAsString("/requests/book_request_stock_invalid_char.json", Book.class);

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Stock must only contain numbers with a minimum of zero"));
    }

    @SneakyThrows
    @Test
    public void addNewBook_shouldThrow_MethodArgumentNotValidException_BadRequest_whenStockIsTooHigh() {
        String bookInJson = "{\"category\": \"KIDS\", " +
                "\"title\": \"Title\", " +
                "\"author\": \"Mr Dom\", " +
                "\"price\": \"10.99\", " +
                "\"stock\": \"10000\"}";

        String bookRequest = getJsonRequestFromClasspathAsString("/requests/book_request_stock_too_high.json", Book.class);

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Stock must only contain numbers with a maximum of 9999"));
    }
}
