package dom.bookstore.service;

import dom.bookstore.domain.BasketItem;
import dom.bookstore.domain.Book;

import java.math.BigDecimal;
import java.util.List;

public interface BasketService {

    List<BasketItem> getBasket();

    BasketItem addBookToBasket(long isbn, int quantity);

    boolean inStock(long isbn, int quantity);

    Book reduceStock(Book book, int quantity);

    Book replenishStock(Book book, int quantity);

    List<BasketItem> removeBookFromBasket(long isbn);

    void clearBasket(List<BasketItem> basketItems);

    void clearBasketAfterOrder(List<BasketItem> basketItems);

}
