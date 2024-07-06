package dom.bookstore.service;

import dom.bookstore.dao.BookRepository;
import dom.bookstore.domain.Book;
import dom.bookstore.domain.Category;
import dom.bookstore.exception.BookstoreNotFoundException;
import dom.bookstore.exception.BookstoreStockException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.BOOK_NOT_FOUND;
import static dom.bookstore.utils.BookStoreConstants.NOT_ENOUGH_STOCK;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Finds all books from Bookstore
     * If empty then should return Collections.emptyList() by default
     *
     * @return - List<Book>
     */
    @Override
    public List<Book> findAllBooks() {
        log.info("Finding all books");
        return bookRepository.findAll();
    }

    /**
     * Finds Book from Bookstore by isbn
     *
     * @param isbn
     * @return
     */
    //@Cacheable("book")
    @Override
    public Optional<Book> findBookByIsbn(long isbn){
        log.info("Finding book by isbn: {}", isbn);
        return bookRepository.findById(isbn);
    }

    /**
     * Counts stock from Bookstore
     *
     * @param - isbn
     * @return
     */
    //@Cacheable("bookstock")
    @Override
    public Integer getBookStock(long isbn) {
        log.info("Getting book stock by isbn: {}", isbn);
        Integer bookStock = bookRepository.getBookStock(isbn);
        log.info("Current stock of item: {}", bookStock);
        return bookStock;
    }

    /**
     * Checks book repository for stock count
     *
     * @param - isbn
     * @return -
     */
    //@Cacheable("checkstock)")
    @Override
    public boolean checkStock(long isbn) {
        Optional<Book> book = bookRepository.findById(isbn);
        if(!book.isPresent()) {
            log.info("Book is not found with isbn:{}", isbn);
            throw new BookstoreNotFoundException(BOOK_NOT_FOUND, isbn);
        } else if(book.get().getStock() == 0) {
            log.info("Book is out of stock for isbn:{}", isbn);
            throw new BookstoreStockException(NOT_ENOUGH_STOCK, isbn);
        }
        log.info("Book is in stock for isbn:{} Stock remaining is {}", isbn, book.get().getStock());
        int inStock = book.get().getStock();
        return inStock > 0;
    }

    /**
     * Finds Books by Category
     *
     * @param category
     * @return
     */
    //@Cacheable("book")
    @Override
    public List<Book> findBooksByCategory(Category category) {
        log.info("Finding book by category: {}", category);
        return bookRepository.findByCategory(category);
    }

    /**
     * Finds Books by search term using fuzzy search logic - first 3 chars
     *
     * @param search
     * @return
     */
    @Override
    public List<Book> findBookBySearchTermIgnoreCase(String search) {
        log.info("Finding book by search term: {}", search);
        return bookRepository.findBookBySearchTermIgnoreCase(search);
    }

}
