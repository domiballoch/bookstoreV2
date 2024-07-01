package dom.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dom.bookstore.dao.UserRepository;
import dom.bookstore.domain.Users;
import dom.bookstore.exception.BookstoreNotFoundException;
import dom.bookstore.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.USER_NOT_FOUND;
import static dom.bookstore.utils.ControllerTestHelper.getResponseFrom;
import static dom.bookstore.utils.TestDataUtils.USERLIST;
import static dom.bookstore.utils.TestDataUtils.USER_1;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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
import static org.hamcrest.Matchers.*;

@Sql(scripts = {"classpath:test_data/repositoryTestData.sql"})
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;


    @SneakyThrows
    @Test
    public void findAllUsers() {
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findAllUsers")
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.*", hasSize(5)));

        final List<Users> result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.size()).isEqualTo(5);
    }

    @SneakyThrows
    @Test
    public void findUserById() {
        final ResultActions resultActions =
                mockMvc.perform(get("/rest/findUser/{userId}", 2001)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final Users result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getAddressLine1()).isEqualTo("10 Something Road");
        assertThat(result.getAddressLine2()).isEqualTo("London");
        assertThat(result.getPostCode()).isEqualTo("SW1");
    }

    @SneakyThrows
    @Test
    public void addNewUser() {
        final ResultActions resultActions =
                mockMvc.perform(post("/rest/addNewUser")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(USER_1))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final Users result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.getFirstName()).isEqualTo("Bob");
        assertThat(result.getLastName()).isEqualTo("Jones");
        assertThat(result.getAddressLine1()).isEqualTo("99 Orange Grove");
        assertThat(result.getAddressLine2()).isEqualTo("London");
        assertThat(result.getPostCode()).isEqualTo("SW4");
        assertThat(userRepository.findAll().size()).isEqualTo(6);
    }

    @SneakyThrows
    @Test
    public void deleteUser() {
        final ResultActions resultActions =
                mockMvc.perform(delete("/rest/deleteUser/{userId}", 2001)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        assertThat(userRepository.findById(2001L)).isEmpty();
        assertThat(userRepository.findAll().size()).isEqualTo(4);
    }

    @SneakyThrows
    @Test
    public void updateUser() {
        Optional<Users> user1 = userRepository.findById(2001L);
        assertThat(user1.get().getFirstName()).isEqualTo("John");
        assertThat(user1.get().getLastName()).isEqualTo("Smith");
        assertThat(user1.get().getAddressLine1()).isEqualTo("10 Something Road");
        assertThat(user1.get().getAddressLine2()).isEqualTo("London");
        assertThat(user1.get().getPostCode()).isEqualTo("SW1");

        final ResultActions resultActions =
                mockMvc.perform(put("/rest/updateUser/{userId}", 2001)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(USER_1))
                                .accept(MediaType.APPLICATION_JSON_VALUE))
                        .andDo(print())
                        .andExpect(status().isOk());

        final Users result = getResponseFrom(resultActions, objectMapper, new TypeReference<>() {});
        assertThat(result.getFirstName()).isEqualTo("Bob");
        assertThat(result.getLastName()).isEqualTo("Jones");
        assertThat(result.getAddressLine1()).isEqualTo("99 Orange Grove");
        assertThat(result.getAddressLine2()).isEqualTo("London");
        assertThat(result.getPostCode()).isEqualTo("SW4");
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
