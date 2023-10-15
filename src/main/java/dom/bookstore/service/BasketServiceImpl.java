package dom.bookstore.service;

import dom.bookstore.dao.BasketItemRepository;
import dom.bookstore.dao.BasketRepository;
import dom.bookstore.dao.BookRepository;
import dom.bookstore.dao.UserRepository;
import dom.bookstore.domain.Basket;
import dom.bookstore.domain.BasketItem;
import dom.bookstore.domain.Book;
import dom.bookstore.domain.Users;
import dom.bookstore.exception.BookstoreStockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.NOT_ENOUGH_STOCK;
import static dom.bookstore.utils.BookStoreConstants.OUT_OF_STOCK;

@Slf4j
@Service
public class BasketServiceImpl implements BasketService {

      private BasketRepository basketRepository;
      private BasketItemRepository basketItemRepository;
      private BookRepository bookRepository;
      private UserRepository userRepository;

      public BasketServiceImpl(BasketRepository basketRepository, BasketItemRepository basketItemRepository,
                               BookRepository bookRepository, UserRepository userRepository) {
          this.basketRepository = basketRepository;
          this.basketItemRepository = basketItemRepository;
          this.bookRepository = bookRepository;
          this.userRepository = userRepository;
    }

    /**
     * Get all books in basket
     *
     * @return List<BasketItem>
     */
    public List<BasketItem> getBasket() {
        List<BasketItem> basketItems = basketItemRepository.findAll();
        log.info("Total price of basket is {} ", calculateBasket(basketItems));
        return basketItems;
    }

    /**
     * Adds Book to Basket if in stock
     * If not in stock, throw not found exception
     *
     * Repository persists are part of a transaction, all or nothing
     * If one doesn't complete then everything is rolled back to prevent mis-aligned data between entities
     *
     * //cache evict
     *
     * @param isbn
     * @return List<BasketItem>
     */
    @Transactional
    @Override
    public BasketItem addBookToBasket(long isbn, int quantity, Users user) {  //add user login/logout and add user to session
        //Check book is in stock
        inStock(isbn, quantity);

        //Fetch book if exists - check made via inStock()
        Optional<Book> book = bookRepository.findById(isbn);

        //temp user path variable until login/session set up
        Optional<Users> foundUser = userRepository.findById(user.getUserId());
        //userRepository.save(foundUser.get());

        //Reduce stock of added book based on quantity added if book is still in stock
        Book updatededBook = reduceStock(book.get(), quantity);

        BasketItem basketItem = BasketItem.builder()
                .book(updatededBook)
                .quantity(quantity)
                .totalPrice(calculateTotalPrice(updatededBook, quantity))
                .build();
        Basket basket = Basket.builder()
                .basketItems(Arrays.asList(basketItem))
                .users(foundUser.get())
                .build();

        //Save basket - cascade to basket item (parent/child)
        try {
            basketRepository.save(basket);
        } catch (Exception e) {
            log.error("Error saving entity", e.getMessage());
        }
        log.info("Book added to basket: {}", updatededBook);

        return basketItem;
    }

    /**
     * Checks book repository for stock based on quantity required
     *
     * @param - isbn
     * @return - Returns a boolean if Book exists or not (greater then zero)
     */
    //@Cacheable("instock)")
    @Override
    public boolean inStock(long isbn, int quantity) {
        final Optional<Book> book = bookRepository.findById(isbn);
        if(!book.isPresent()) {
            log.info("Book is out of stock with isbn:{}", isbn);
            throw new BookstoreStockException(OUT_OF_STOCK, isbn);
        } else if(book.get().getStock() < quantity) {
            log.info("Not enough books in stock for isbn:{}", isbn);
            log.info("Stock remaining is {}, Quantity required was {}", book.get().getStock(), quantity);
            throw new BookstoreStockException(NOT_ENOUGH_STOCK, isbn);
        }
        log.info("Book is in stock for isbn:{}", isbn);
        final int inStock = book.get().getStock();
        return inStock > 0;
    }

    /**
     * Calculates total of item where quantity is > 1
     *
     * @param book
     * @param quantity
     * @return
     */
    @Override
    public BigDecimal calculateTotalPrice(Book book, int quantity) {
        return book.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Calculates total of basket
     *
     * @param basketItems
     * @return totalPrice
     */
    @Override
    public BigDecimal calculateBasket(List<BasketItem> basketItems) { //move this to order service
        return basketItems.stream()
                .map(BasketItem::getBook)
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add); //quantity should multiply
    }

    /**
     * Reduces stock of book when added to basket
     *
     * @param book
     * @param quantity
     * @return
     */
    @Transactional
    @Override
    public Book reduceStock(Book book, int quantity) {
        //stock already checked  - should not go lower than zero
        int originalStock = book.getStock();
        int updatedStock = book.getStock() - quantity;
        //fix for javax constraint violation on book entity when updating existing data in row
        Book updatededBook = book.toBuilder().stock(updatedStock).build();
        bookRepository.updateBookStock(updatedStock, book.getIsbn());
        log.info("Stock reduced from {} to {}", originalStock, updatedStock);

        return updatededBook;
    }

    /**
     * Replenishes stock of book when book removed from basket
     *
     * @param book
     * @param quantity
     * @return
     */
    @Transactional
    @Override
    public Book replenishStock(Book book, int quantity) {
        int currentStock = book.getStock();
        int updatedStock = book.getStock() + quantity;
        //fix for javax constraint violation on book entity when updating existing data in row
        Book updatededBook = book.toBuilder().stock(updatedStock).build();
        //int currentStock = updatededBook.getStock();
        bookRepository.updateBookStock(updatedStock, book.getIsbn());
        log.info("Stock replenished from {} to {}", currentStock, updatedStock);

        return updatededBook;
    }

    /**
     * Removes Book(Basket Item) from Basket
     * First adds removed BasketItem(s) to separate list the compares against current BasketItem(s)
     * If Basket contains any then they get removed - avoiding ConcurrentModificationException of Stream
     *
     * Deleting BasketItem cascades entity deletion from child to parent to remove basket entry
     * Updates stock back to previous level by summing quantities
     *
     * @param isbn
     * @return List<BasketItem>
     */
    @Override
    public List<BasketItem> removeBookFromBasket(long isbn) {
        log.info("Removing book from basket: {}", isbn);

        List<BasketItem> removedBasketItems = new ArrayList<>();
        List<BasketItem> basketItems = basketItemRepository.findAll();

        basketItems.forEach(item -> {
                    if(item.getBook().getIsbn().equals(isbn)) {
                        removedBasketItems.add(item);
                    }
                });

        //deletes all books and quantities with isbn
        if(CollectionUtils.containsAny(removedBasketItems, basketItems)) {
            basketItemRepository.deleteAll((removedBasketItems));
            log.info("Removed book(s) from basket: {}", removedBasketItems);

            Book book = null;
            int sumOfQuantities = 0;

            for(BasketItem item : removedBasketItems) {
                book = item.getBook();
                sumOfQuantities += item.getQuantity();
            }
            replenishStock(book, sumOfQuantities);
        }

        return removedBasketItems;
    }

    /**
     * Clears basket after order
     */
//    public void clearBasketAfterOrder() {
//        basketRepository.delete(basket);
//        //basketOld.getBooks().clear();
//        log.info("Basket cleared");
//    }

    /**
     * Clears basket and resets stock
     */
//    @Override
//    public void clearBasket() {
//        basket.getBasketItems().forEach(basketItem -> basketItem.getBook().setStock(
//                basketItem.getBook().getStock()+1));
//        basketRepository.delete(basket);
//        //basketOld.getBooks().forEach(book -> book.setStock(book.getStock()+1));
//        //basketOld.getBooks().clear();
//        log.info("Basket cleared and stock reset");
//    }

}
