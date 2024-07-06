package dom.bookstore.utils;

import dom.bookstore.domain.BasketItem;
import dom.bookstore.domain.Book;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@UtilityClass
public class BookStoreUtils {

    /**
     * Helper method for controller HTTP response - NOT FOUND
     *
     * @param t
     * @param r
     * @return
     * @param <T>
     */
    public static <T> ResponseEntity<T> noResultsFound(T t, T r) {
        log.info("No results found: {}", r);
        return new ResponseEntity<>(t, HttpStatus.NOT_FOUND);
    }

    /**
     * Calculates total of item where quantity is > 1
     *
     * @param book
     * @param quantity
     * @return
     */
    public static BigDecimal calculateTotalPrice(Book book, int quantity) {
        return book.getPrice().multiply(new BigDecimal(quantity)
                .setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros());
    }

    /**
     * Calculates total of basket
     *
     * @param basketItems
     * @return
     */
    public static BigDecimal calculateBasket(List<BasketItem> basketItems) { //move this to order service
        return basketItems.stream()
                .map(BasketItem::getBook)
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
