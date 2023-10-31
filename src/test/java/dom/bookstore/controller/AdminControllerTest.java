package dom.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dom.bookstore.domain.Book;
import dom.bookstore.service.AdminService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static dom.bookstore.utils.ControllerTestHelper.getResponseFrom;
import static dom.bookstore.utils.TestDataUtils.BOOK_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;


    @SneakyThrows
    @Test
    public void addNewBookToBookstore() {
        when(adminService.addNewBookToBookstore(any(Book.class))).thenReturn(BOOK_1);
        final ResultActions resultActions =
                mockMvc.perform(post("/rest/addNewBook")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(BOOK_1))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isCreated());

        final Book result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result).isEqualTo(BOOK_1);
        verify(adminService, times(1)).addNewBookToBookstore(any(Book.class));
    }

    @SneakyThrows
    @Test
    public void deleteBookFromBookstore() {
        final ResultActions resultActions =
                mockMvc.perform(delete("/rest/deleteBook/{isbn}", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        verify(adminService, times(1)).deleteBookFromBookstore(any(Long.class));
    }

    @SneakyThrows
    @Test
    public void updateBookInBookstore() {
        when(adminService.updateBookInBookstore(any(Book.class), any(Long.class))).thenReturn(BOOK_1);
        final ResultActions resultActions =
                mockMvc.perform(put("/rest/updateBook/{isbn}", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(BOOK_1))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final Book result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result).isEqualTo(BOOK_1);
        verify(adminService, times(1)).updateBookInBookstore(any(Book.class), any(Long.class));
    }

    @SneakyThrows
    @Test
    public void addNewBook_shouldThrow_MethodArgumentNotValidException_BadRequest_whenTitleIsEmpty() {
        String bookInJson = "{\"category\": \"KIDS\", " +
                "\"title\": \"\", " +
                "\"author\": \"Mr Dom\", " +
                "\"price\": \"100\", " +
                "\"stock\": \"1\"}";

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookInJson)
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

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookInJson)
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

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookInJson)
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

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookInJson)
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

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookInJson)
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

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookInJson)
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

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookInJson)
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

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookInJson)
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

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookInJson)
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

        mockMvc.perform(post("/rest/addNewBook")
                        .content(bookInJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).contains("Stock must only contain numbers with a maximum of 9999"));
    }
}
