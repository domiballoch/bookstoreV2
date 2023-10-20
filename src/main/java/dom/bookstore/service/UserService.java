package dom.bookstore.service;

import dom.bookstore.domain.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<Users> findUserById(long userId);

    List<Users> findAllUsers();

    Users addNewUser(Users user);

    void deleteUser(long userId);

    Users updateUser(Users user, long userId);
}
