package dom.bookstore.service;

import dom.bookstore.domain.BasketItem;
import dom.bookstore.domain.Book;
import dom.bookstore.domain.Users;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BasketService {

    List<BasketItem> getBasket();

    BasketItem addBookToBasket(long isbn, int quantity, Users user);

    boolean inStock(long isbn, int quantity);

    BigDecimal calculateTotalPrice(Book book, int quantity);

    BigDecimal calculateBasket(List<BasketItem> basketItems);

    Book reduceStock(Book book, int quantity);

    Book replenishStock(Book book, int quantity);

    List<BasketItem> removeBookFromBasket(long isbn);

    //void clearBasketAfterOrder();

    //void clearBasket();
}
