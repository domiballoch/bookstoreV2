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

    /**
     * Returns all orders
     *
     * @return
     */
    @Override
    public List<OrderDetails> findAllOrders() {
        log.info("Finding all orders");
        return orderDetailsRepository.findAll();
    }

    /**
     * Finds order by orderDetailsId
     *
     * @param orderDetailsId
     * @return
     */
    @Override
    public Optional<OrderDetails> findOrderById(final long orderDetailsId) {
        log.info("Finding order by orderDetailsId: {}", orderDetailsId);
        return orderDetailsRepository.findById(orderDetailsId);
    }

    /**
     * Checkout can be implemented by moving all the code apart from the save order details
     * and returning the order details in the checkout response.
     * Remove orderItem from checkout and replenish stock can be taken from basket service
     * For ease of use implementation just contains basket and order
     */
    @Override
    public OrderDetails checkout() {return null;}

    @Transactional
    @Override
    public OrderDetails submitOrder(Users user) {
        OrderItem orderItem;
        OrderDetails orderDetails;
        List<OrderItem> orderItems = new ArrayList<>();

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
                //add each orderItem to orderDetails to return in response
                orderItems.add(orderItem);
            }

            //set orderDetails with orderItems - bi-directional
            orderDetails.setOrderItems(orderItems);

            //Save orderDetails - cascade to OrderItems (parent/child)
            try {
                orderDetailsRepository.save(orderDetails);
            } catch (Exception e) {
                log.error(ERROR_SAVING_ENTITY, e.getMessage());
            }
            log.info("Order complete: {}", orderDetails);
        }
        basketService.clearBasketAfterOrder(basketItems);

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
