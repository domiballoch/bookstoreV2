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

import static dom.bookstore.utils.ControllerTestHelper.getResponseFrom;
import static dom.bookstore.utils.TestDataUtils.BOOK_1;
import static org.assertj.core.api.Assertions.assertThat;
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
}
