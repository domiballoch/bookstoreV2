package dom.bookstore.controller;

import dom.bookstore.domain.Users;
import dom.bookstore.exception.BookstoreNotFoundException;
import dom.bookstore.service.UserService;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.USER_NOT_FOUND;

/**
 * Exceptions handled by controller advice
 */
@RestController
@RequestMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;
    //private UserMapper userMapper; //dto here

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/findAllUsers")
    public ResponseEntity<List<Users>> findAllUsers() {
        List<Users> userList = userService.findAllUsers();
        if(userList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping(value = "/findUser/{userId}")
    public ResponseEntity<Optional<Users>> findUserById(@PathVariable long userId) {
        Optional<Users> user = Optional.ofNullable(userService.findUserById(userId)
                .orElseThrow(() -> new BookstoreNotFoundException(USER_NOT_FOUND, userId)));
        //final Optional<UserDTO> userDTO = user.map(userMapper::toDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/addNewUser")
    public ResponseEntity<Users> addNewUser(@Valid @RequestBody Users user) {
        Users addedUser = userService.addNewUser(user);
        return new ResponseEntity<>(addedUser, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteUser/{userId}")
    public ResponseEntity<Long> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    //Using PUT for idempotency - resending the whole Entity
    @PutMapping(value = "/updateUser/{userId}")
    public ResponseEntity<Users> updateUser(@Valid @RequestBody Users user, @PathVariable long userId) {
        Users updatedUser = userService.updateUser(userId, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}
