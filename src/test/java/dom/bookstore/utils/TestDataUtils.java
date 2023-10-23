package dom.bookstore.utils;

import dom.bookstore.domain.BasketItem;
import dom.bookstore.domain.Book;
import dom.bookstore.domain.Category;
import dom.bookstore.domain.OrderDetails;
import dom.bookstore.domain.OrderItem;
import dom.bookstore.domain.Users;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class TestDataUtils {

    //----- Book data -----//

    public static final Book BOOK_1 = Book.builder()
            .isbn(1L)
            .category(Category.COOKING)
            .title("title1")
            .author("author1")
            .price(BigDecimal.valueOf(4.99))
            .stock(10)
            .build();

    public static final Book BOOK_2 = Book.builder()
            .isbn(2L)
            .category(Category.HORROR)
            .title("title2")
            .author("author2")
            .price(BigDecimal.valueOf(24.99))
            .stock(10)
            .build();

    public static final Book BOOK_3 = Book.builder()
            .isbn(3L)
            .category(Category.ROMANCE)
            .title("title3")
            .author("author3")
            .price(BigDecimal.valueOf(11.99))
            .stock(10)
            .build();

    public static final List<Book> BOOKLIST =  Arrays.asList(BOOK_1, BOOK_2, BOOK_3);


    //----- Basket data -----//

    public static final BasketItem BASKET_1 = BasketItem.builder()
            .fkBasketId(1L)
            .book(BOOK_1)
            .quantity(1)
            .totalPrice(BookStoreUtils.calculateTotalPrice(BOOK_1, 1))
            .build();

    public static final BasketItem BASKET_2 = BasketItem.builder()
            .fkBasketId(2L)
            .book(BOOK_2)
            .quantity(2)
            .totalPrice(BookStoreUtils.calculateTotalPrice(BOOK_2, 2))
            .build();

    public static final BasketItem BASKET_3 = BasketItem.builder()
            .fkBasketId(3L)
            .book(BOOK_3)
            .quantity(3)
            .totalPrice(BookStoreUtils.calculateTotalPrice(BOOK_3, 3))
            .build();


    //----- User data -----//

    public static final Users USER_1 = Users.builder()
            .userId(1L)
            .firstName("Bob")
            .lastName("Jones")
            .addressLine1("99 Orange Grove")
            .addressLine2("London")
            .postCode("SW4").build();

    public static final Users USER_2 = Users.builder()
            .userId(2L)
            .firstName("Harry")
            .lastName("Peters")
            .addressLine1("77 Pine Forest")
            .addressLine2("London")
            .postCode("SW5").build();

    public static final Users USER_3 = Users.builder()
            .userId(3L)
            .firstName("Mia")
            .lastName("Cho")
            .addressLine1("50 Flora Grange")
            .addressLine2("London")
            .postCode("SW6").build();

    public static final List<Users> USERLIST = Arrays.asList(USER_1, USER_2, USER_3);


    //----- OrderItem data -----//

    public static final OrderItem ORDER_ITEM_1 = OrderItem.builder()
            .orderItemId(1L)
            .book(BOOK_1)
            .quantity(1)
            .totalPrice(BookStoreUtils.calculateTotalPrice(BOOK_1, 1))
            .build();

    public static final OrderItem ORDER_ITEM_2 = OrderItem.builder()
            .orderItemId(2L)
            .book(BOOK_2)
            .quantity(2)
            .totalPrice(BookStoreUtils.calculateTotalPrice(BOOK_2, 2))
            .build();

    public static final OrderItem ORDER_ITEM_3 = OrderItem.builder()
            .orderItemId(3L)
            .book(BOOK_3)
            .quantity(3)
            .totalPrice(BookStoreUtils.calculateTotalPrice(BOOK_3, 3))
            .build();


    //---- OrderDetails data ----//

    public static final OrderDetails ORDER_DETAILS_1 = OrderDetails.builder()
            .orderDetailsId(1L)
            .users(USER_1)
            .orderItems(List.of(ORDER_ITEM_1))
            .totalOrderPrice(BookStoreUtils.calculateBasket(Arrays.asList(BASKET_1)))
            .orderDate(LocalDateTime.now().minusDays(3).truncatedTo(ChronoUnit.SECONDS)).build();


    public static final OrderDetails ORDER_DETAILS_2 = OrderDetails.builder()
            .orderDetailsId(2L)
            .users(USER_2)
            .orderItems(Arrays.asList(ORDER_ITEM_2))
            .totalOrderPrice(BookStoreUtils.calculateBasket(Arrays.asList(BASKET_2)))
            .orderDate(LocalDateTime.now().minusDays(2).truncatedTo(ChronoUnit.SECONDS)).build();


    public static final OrderDetails ORDER_DETAILS_3 = OrderDetails.builder()
            .orderDetailsId(3L)
            .users(USER_3)
            .orderItems(Arrays.asList(ORDER_ITEM_3))
            .totalOrderPrice(BookStoreUtils.calculateBasket(Arrays.asList(BASKET_3)))
            .orderDate(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS)).build();

    public static final List<OrderDetails> ORDERLIST = Arrays.asList(ORDER_DETAILS_1, ORDER_DETAILS_2, ORDER_DETAILS_3);

}
