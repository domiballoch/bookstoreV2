package dom.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dom.bookstore.domain.Users;
import dom.bookstore.exception.BookstoreNotFoundException;
import dom.bookstore.service.UserService;
import dom.bookstore.utils.TestDataUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.USER_NOT_FOUND;
import static dom.bookstore.utils.ControllerTestHelper.getResponseFrom;
import static dom.bookstore.utils.TestDataUtils.USERLIST;
import static dom.bookstore.utils.TestDataUtils.USER_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @SneakyThrows
    @Test
    public void findAllUsers() {
        when(userService.findAllUsers()).thenReturn(USERLIST);
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findAllUsers")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final List<Users> result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result).isEqualTo((USERLIST));
        verify(userService, times(1)).findAllUsers();
    }

    @SneakyThrows
    @Test
    public void findUserById() {
        when(userService.findUserById(1)).thenReturn(Optional.of(USER_1));
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findUser/{userId}", 1)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.userId").value(1));

        final Users result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.getUserId()).isEqualTo(1);
        assertThat(result).isEqualTo(USER_1);
        verify(userService, times(1)).findUserById(any(Long.class));
    }

    @SneakyThrows
    @Test
    public void addNewUser() {
        when(userService.addNewUser(any(Users.class))).thenReturn(USER_1);
        final ResultActions resultActions =
                mockMvc.perform(post("/rest/addNewUser")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(USER_1))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final Users result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result).isEqualTo((USER_1));
        verify(userService, times(1)).addNewUser(any(Users.class));
    }

    @SneakyThrows
    @Test
    public void deleteUser() {
        final ResultActions resultActions =
                mockMvc.perform(delete("/rest/deleteUser/{userId}", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(any(Long.class));
    }

    @SneakyThrows
    @Test
    public void updateUser() {
        when(userService.updateUser(any(Users.class), any(Long.class))).thenReturn(USER_1);
        final ResultActions resultActions =
                mockMvc.perform(put("/rest/updateUser/{userId}", 1)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(USER_1))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final Users result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result).isEqualTo((USER_1));
        verify(userService, times(1)).updateUser(any(Users.class), any(Long.class));
    }

    @SneakyThrows
    @Test
    public void shouldThrow_BookstoreNotFoundException() {
        mockMvc.perform(get("/rest/findUser/{userId}", 99999)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BookstoreNotFoundException))
                .andExpect(result -> assertEquals(result.getResolvedException().getMessage(), USER_NOT_FOUND));
    }
}
