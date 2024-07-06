package dom.bookstore.controller;

import dom.bookstore.domain.Book;
import dom.bookstore.domain.Category;
import dom.bookstore.exception.BookstoreNotFoundException;
import dom.bookstore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.BOOK_NOT_FOUND;

/**
 * Exceptions handled by controller advice
 */
@RestController
@RequestMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

        private BookService bookService;

        public BookController(BookService bookService) {
            this.bookService = bookService;
        }

        @GetMapping(value = "/findAllBooks")
        public ResponseEntity<List<Book>> findAllBooks() {
            List<Book> bookList = bookService.findAllBooks();
            if(bookList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bookList, HttpStatus.OK);
        }

        @GetMapping(value = "/findBook/{isbn}")
        public ResponseEntity<Optional<Book>> findBookByIsbn(@PathVariable long isbn) {
            Optional<Book> book = Optional.ofNullable(bookService.findBookByIsbn(isbn)
                    .orElseThrow(() -> new BookstoreNotFoundException(BOOK_NOT_FOUND, isbn)));
            return new ResponseEntity<>(book, HttpStatus.OK);
        }

        @GetMapping(value = "/search/{search}")
        public ResponseEntity<List<Book>> findBooksBySearchTerm(@PathVariable String search) {
            List<Book> results = bookService.findBookBySearchTermIgnoreCase(search);
            if(results.isEmpty()) {
                throw new BookstoreNotFoundException(BOOK_NOT_FOUND , search);
            }
            return new ResponseEntity<>(results, HttpStatus.OK);
        }

        @GetMapping(value = "/category/{category}")
        public ResponseEntity<List<Book>> findBooksByCategory(@PathVariable Category category) {
            List<Book> results = bookService.findBooksByCategory(category);
            if(results.isEmpty()) {
                throw new BookstoreNotFoundException(BOOK_NOT_FOUND , category);
            }
            return new ResponseEntity<>(results, HttpStatus.OK);
        }

        @GetMapping(value = "/getBookstock/{isbn}")
        public ResponseEntity<Integer> getBookstock(@PathVariable long isbn) {
            Integer stock = bookService.getBookStock(isbn);
            if(Objects.isNull(stock)) {
                throw new BookstoreNotFoundException(BOOK_NOT_FOUND , isbn);
            }
            return new ResponseEntity(stock, HttpStatus.OK);
        }

}
