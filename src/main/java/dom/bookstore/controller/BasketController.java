package dom.bookstore.controller;

import dom.bookstore.domain.BasketItem;
import dom.bookstore.exception.BookstoreBasketException;
import dom.bookstore.exception.BookstoreNotFoundException;
import dom.bookstore.service.BasketService;
import dom.bookstore.utils.BookStoreUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static dom.bookstore.utils.BookStoreConstants.BASKET_IS_EMPTY;
import static dom.bookstore.utils.BookStoreConstants.ORDER_NOT_FOUND;

/**
 * Exceptions handled by controller advice
 */
@RestController
@RequestMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
public class BasketController {

    private BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping(value = "/getBasket")
    public ResponseEntity<List<BasketItem>> getBasket() {
        List<BasketItem> basketItems = basketService.getBasket();
        if(basketItems.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(basketItems, HttpStatus.OK);
    }

    @PostMapping(value = "/addBookToBasket/{isbn}/{quantity}")
    public ResponseEntity<BasketItem> addBookToBasket(@PathVariable long isbn, @PathVariable int quantity) {
        BasketItem basketItem = basketService.addBookToBasket(isbn, quantity);
        return new ResponseEntity<>(basketItem, HttpStatus.OK);
    }

    @DeleteMapping(value = "/removeBookFromBasket/{isbn}")
    public ResponseEntity<List<BasketItem>> removeBookFromBasket(@PathVariable long isbn) {
        List<BasketItem> basketItems = basketService.removeBookFromBasket(isbn); //handle if book not exist - basket empty
        return new ResponseEntity<>(basketItems, HttpStatus.OK);
    }
}
