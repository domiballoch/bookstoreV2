package dom.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dom.bookstore.domain.Book;
import dom.bookstore.domain.Category;
import dom.bookstore.exception.BookstoreNotFoundException;
import dom.bookstore.service.BookService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.BOOK_NOT_FOUND;
import static dom.bookstore.utils.ControllerTestHelper.getResponseFrom;
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

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @SneakyThrows
    @Test
    public void findAllBooks() {
        when(bookService.findAllBooks()).thenReturn(BOOKLIST);
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findAllBooks")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final List<Book> result = getResponseFrom(resultActions, objectMapper, new TypeReference<List<Book>>() {});
        assertThat(result).isEqualTo((BOOKLIST));
        verify(bookService, times(1)).findAllBooks();
    }

    @SneakyThrows
    @Test
    public void findBookByIsbn() {
        when(bookService.findBookByIsbn(1)).thenReturn(Optional.of(BOOK_1));
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findBook/{isbn}", 1)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.isbn").value(1));

        final Book result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result).isEqualTo(BOOK_1);
        verify(bookService, times(1)).findBookByIsbn(any(Long.class));
    }

    @SneakyThrows
    @Test
    public void findBookBySearchTerm() {
        when(bookService.findBookBySearchTermIgnoreCase("ti")).thenReturn(List.of(BOOK_1));
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/search/{search}", "ti")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.[0].title").value("title1"));

        final List<Book> results = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(results.get(0).getTitle()).isEqualTo("title1");
        assertThat(results.get(0)).isEqualTo(BOOK_1);
        verify(bookService, times(1)).findBookBySearchTermIgnoreCase(any(String.class));
    }

    @SneakyThrows
    @Test
    public void findBookByCategory() {
        when(bookService.findBooksByCategory(Category.COOKING)).thenReturn(List.of(BOOK_1));
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/category/{category}", "COOKING")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.[0].category").value("COOKING"));

        final List<Book> results = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(results.get(0).getCategory()).isEqualTo(Category.COOKING);
        assertThat(results.get(0)).isEqualTo(BOOK_1);
        verify(bookService, times(1)).findBooksByCategory(any(Category.class));
    }

    @SneakyThrows
    @Test
    public void getBookstock() {
        when(bookService.getBookStock(1)).thenReturn(10);
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/getBookstock/{isbn}", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());
        final int result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result).isEqualTo(10);
        verify(bookService, times(1)).getBookStock(any(Long.class));
    }

    @SneakyThrows
    @Test
    public void shouldThrow_BookStoreNotFoundException() {
        mockMvc.perform(get("/rest/findBook/{isbn}", 99999)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BookstoreNotFoundException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), BOOK_NOT_FOUND));
    }

    @Disabled
    @SneakyThrows
    @Test
    public void findBookBySearchTermNoContent() {
        when(bookService.findBookBySearchTermIgnoreCase(any(String.class))).thenReturn(any(List.class));
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/search/{search}", "abcdefg")
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isNotFound());
        verify(bookService, times(1)).findBookBySearchTermIgnoreCase(any(String.class));
    }

    @Disabled
    @SneakyThrows
    @Test
    public void findBookByCategoryNoContent() {
        when(bookService.findBooksByCategory(any(Category.class))).thenReturn(Collections.emptyList());
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/category/{category}", Category.LITERATURE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isNotFound());
        verify(bookService, times(1)).findBooksByCategory(any(Category.class));
    }
}
