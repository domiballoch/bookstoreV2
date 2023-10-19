package dom.bookstore.service;

import dom.bookstore.dao.BasketItemRepository;
import dom.bookstore.dao.OrderDetailsRepository;
import dom.bookstore.dao.UserRepository;
import dom.bookstore.domain.BasketItem;
import dom.bookstore.domain.OrderDetails;
import dom.bookstore.domain.OrderItem;
import dom.bookstore.domain.Users;
import dom.bookstore.exception.BookstoreBasketException;
import dom.bookstore.exception.BookstoreValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.BASKET_IS_EMPTY;
import static dom.bookstore.utils.BookStoreConstants.ERROR_SAVING_ENTITY;
import static dom.bookstore.utils.BookStoreConstants.INCORRECT_DETAILS;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private BasketItemRepository basketItemRepository;
    private UserRepository userRepository;
    private OrderDetailsRepository orderDetailsRepository;
    private BasketService basketService;


    public OrderServiceImpl(BasketItemRepository basketItemRepository, UserRepository userRepository,
                             OrderDetailsRepository orderDetailsRepository, BasketService basketService) {
        this.basketItemRepository = basketItemRepository;
        this.userRepository = userRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.basketService = basketService;
    }

    @Transactional
    @Override
    public OrderDetails submitOrder(Users user) {
        OrderItem orderItem = null;
        OrderDetails orderDetails;

        //get basket to add to order
        List<BasketItem> basketItems = new ArrayList<>();
        try {
            basketItems = basketItemRepository.findAll();
        } catch(BookstoreBasketException e) {
            log.error(BASKET_IS_EMPTY, e.getMessage());
        }

        //get user from repository if exists - if not then save new user details
        final Optional<Users> foundUser = userRepository.findById(user.getUserId());
        if(!foundUser.isPresent()) {
            userRepository.save(user);
            log.info("User not found so saving details {}", user);
        } if(validateUser(Optional.of(user)) == false) {
            log.info("User details from search do not match stored records");
            throw new BookstoreValidationException(INCORRECT_DETAILS);
        } else {
            log.info("User found, saving order details {}", user);

            //set order details
            orderDetails = OrderDetails.builder()
                    .orderDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    .users(user)
                    .totalOrderPrice(basketService.calculateBasket(basketItemRepository.findAll()))
                    .build();

            //set order items from basket
            for (BasketItem basketItem : basketItems) {
                orderItem = OrderItem.builder()
                        .orderDetails(orderDetails) //set orderItem with orderDetails - bi-directional
                        .book(basketItem.getBook())
                        .quantity(basketItem.getQuantity())
                        .totalPrice(basketItem.getTotalPrice()).build();
            }

            //set orderDetails with orderItems - bi-directional
            orderDetails.setOrderItems(Arrays.asList(orderItem));

            //Save orderDetails - cascade to OrderItems (parent/child)
            try {
                orderDetailsRepository.save(orderDetails);
            } catch (Exception e) {
                log.error(ERROR_SAVING_ENTITY, e.getMessage());
            }
            log.info("Order complete: {}", orderDetails);
        }
        basketService.clearBasket(basketItems);

        return orderDetails;
    }

    /**
     * Validate user request matches user in repository
     *
     * @param user
     * @return
     */
    private boolean validateUser(final Optional<Users> user) {
        final Optional<Users> foundUser = userRepository.findById(user.get().getUserId());
        return user.get().equals(foundUser.get()) ? true : false;
    }
}
