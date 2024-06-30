package dom.bookstore.dao;

import dom.bookstore.domain.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;
import java.util.Optional;

/**
 * Uses HSQLDB in-memory db
 */
@Sql(scripts = {"classpath:test_data/repositoryTestData.sql"})
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRespositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAllUsers() {
        List<Users> users = userRepository.findAll();

        assertThat(users.size()).isEqualTo(5);
    }

    @Test
    public void findUserById() {
        Optional<Users> user = userRepository.findById(2001L);

        assertThat(user.get().getUserId()).isEqualTo(2001);
        assertThat(user.get().getFirstName()).isEqualTo("John");
        assertThat(user.get().getLastName()).isEqualTo("Smith");
        assertThat(user.get().getAddressLine1()).isEqualTo("10 Something Road");
        assertThat(user.get().getAddressLine2()).isEqualTo("London");
        assertThat(user.get().getPostCode()).isEqualTo("SW1");
    }

    @Test
    public void saveOneUser() {
        Users user = Users.builder()
                .firstName("FirstName6")
                .lastName("LastName6")
                .addressLine1("AddressLine1")
                .addressLine2("AddressLine2")
                .postCode("SW1").build();

        userRepository.save(user);
        List<Users> userList = userRepository.findAll();

        assertThat(userRepository.count()).isEqualTo(6);
        assertThat(userList.size()).isEqualTo(6);
        assertThat(userList.get(5).getUserId()).isEqualTo(2006L);
        assertThat(userList.get(5).getFirstName()).isEqualTo("FirstName6");
        assertThat(userList.get(5).getLastName()).isEqualTo("LastName6");
        assertThat(userList.get(5).getAddressLine1()).isEqualTo("AddressLine1");
        assertThat(userList.get(5).getAddressLine2()).isEqualTo("AddressLine2");
        assertThat(userList.get(5).getPostCode()).isEqualTo("SW1");
    }

    @Test
    public void deleteOneUser() {
        userRepository.deleteById(2005L);
        List<Users> userList = userRepository.findAll();

        assertThat(userList.size()).isEqualTo(4);
        assertThat(userRepository.findById(2005L)).isEqualTo(Optional.empty());
    }

    @Test
    public void findUserByUserDetailsIgnoreCase() {
        Optional<Users> user = userRepository.findUserByUserDetailsIgnoreCase(
                "John",
                "Smith",
                "10 Something Road",
                "London",
                "SW1");

        assertThat(user).isPresent();
    }

    @Test
    public void updateUser() {
    }
}
