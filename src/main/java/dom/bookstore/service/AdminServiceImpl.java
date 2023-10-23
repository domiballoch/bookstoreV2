package dom.bookstore.service;

import dom.bookstore.dao.BookRepository;
import dom.bookstore.domain.Book;
import dom.bookstore.exception.BookstoreNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.BOOK_NOT_FOUND;

@Transactional
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private BookRepository bookRepository;

    public AdminServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Adds new book to bookstore
     *
     * @param book
     * @return
     */
    @Override
    public Book addNewBookToBookstore(Book book) { //add check for duplication
        log.info("Adding new book to bookstore");
        final Book newBook = Book.builder()
                .category(book.getCategory())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .stock(book.getStock())
                .build();
        bookRepository.save(newBook);

        log.info("New book added to bookstore: {}", newBook);
        return newBook;
    }

    /**
     * Deletes Book from Bookstore
     *
     * @param isbn
     */
    @Override
    public void deleteBookFromBookstore(long isbn) {
        log.info("Deleting book from bookstore by isbn: {}", isbn);
        try {
            bookRepository.deleteById(isbn);
        } catch(BookstoreNotFoundException e) {
            log.error(BOOK_NOT_FOUND , isbn);
        }
        log.info("Deleted book from bookstore by isbn: {}", isbn);
    }

    /**
     * Updates book in bookstore
     *
     * @param book
     * @return
     */
    @Override
    public Book updateBookInBookstore(Book book, long isbn) {
        Optional<Book> foundBook = Optional.ofNullable(bookRepository.findById(isbn)
                .orElseThrow(() -> new BookstoreNotFoundException(BOOK_NOT_FOUND, isbn)));
        log.info("Updating book: {}", foundBook);
        bookRepository.delete(foundBook.get());
        book.setIsbn(isbn);
        bookRepository.save(book);
        log.info("Saving book: {}", book);
        return book;
    }
}
