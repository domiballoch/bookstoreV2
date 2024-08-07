package dom.bookstore.service;

import dom.bookstore.dao.UserRepository;
import dom.bookstore.domain.Users;
import dom.bookstore.exception.BookstoreNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.USER_NOT_FOUND;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds all users
     *
     * @return
     */
    @Override
    public List<Users> findAllUsers() {
        log.info("Finding all users");
        return userRepository.findAll();
    }

    /**
     * Finds user by id
     *
     * @param userId
     * @return
     */
    @Override
    public Optional<Users> findUserById(long userId){
        log.info("Finding user by id: {}", userId);
        return userRepository.findById(userId);
    }

    /**
     * Adds new user
     *
     * @param user
     * @return
     */
    @Transactional
    @Override
    public Users addNewUser(Users user) { //check for duplicate
        log.info("Adding new user");
        userRepository.save(user);
        log.info("New user added: {}", user);
        return user;
    }

    /**
     * Deletes user by id
     *
     * @param userId
     */
    @Transactional
    @Override
    public void deleteUser(long userId) {
        log.info("Deleting user by id: {}", userId);
            userRepository.deleteById(userId);
        log.info("Deleted user by id: {}", userId);
    }

    /**
     * Updates user by id
     *
     * @param userId
     * @param user
     * @return
     */
    @Transactional
    @Override
    public Users updateUser(long userId, Users user) {
        Optional<Users> foundUser = Optional.ofNullable(userRepository.findById(userId)
                .orElseThrow(() -> new BookstoreNotFoundException(USER_NOT_FOUND, userId)));
        log.info("Updating user: {}", foundUser);
        userRepository.updateUser(userId, user.getFirstName(), user.getLastName(),
                user.getAddressLine1(), user.getAddressLine2(), user.getPostCode());
        log.info("Saving user: {}", user);
        return user;
    }
}
