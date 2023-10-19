package dom.bookstore.controller;

import dom.bookstore.domain.Basket;
import dom.bookstore.domain.BasketItem;
import dom.bookstore.domain.Users;
import dom.bookstore.service.BasketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //final BigDecimal totalPrice = basketService.calculateBasket(basket);
        //return new ResponseEntity<>(new ArrayList<>(Arrays.asList(basketItems, totalPrice)), HttpStatus.OK);
        return new ResponseEntity<>(basketItems, HttpStatus.OK);
    }

    @PostMapping(value = "/addBookToBasket/{isbn}/{quantity}/{user}")
    public ResponseEntity<BasketItem> addBookToBasket(@PathVariable long isbn, @PathVariable int quantity) {
        BasketItem basketItem = basketService.addBookToBasket(isbn, quantity);
        return new ResponseEntity<>(basketItem, HttpStatus.OK);
    }

    @DeleteMapping(value = "/removeBookFromBasket/{isbn}")
    public ResponseEntity<List<BasketItem>> removeBookFromBasket(@PathVariable long isbn) {
        List<BasketItem> basketItems = basketService.removeBookFromBasket(isbn);
        return new ResponseEntity<>(basketItems, HttpStatus.OK);
    }
}
