package dom.bookstore.controller;

import dom.bookstore.domain.Book;
import dom.bookstore.domain.Category;
import dom.bookstore.exception.BookstoreNotFoundException;
import dom.bookstore.service.BookService;
import dom.bookstore.utils.BookStoreUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.BOOK_NOT_FOUND;

@Slf4j
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
                BookStoreUtils.noResultsFound(bookList, "All books");
            }
            return new ResponseEntity<>(bookList, HttpStatus.OK);
        }

        @SneakyThrows
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
                BookStoreUtils.noResultsFound(results, search);
            }
            return new ResponseEntity<>(results, HttpStatus.OK);
        }

        @GetMapping(value = "/category/{category}")
        public ResponseEntity<List<Book>> findBooksByCategory(@PathVariable Category category) {
            List<Book> results = bookService.findBooksByCategory(category);
            if(results.isEmpty()) {
                BookStoreUtils.noResultsFound(results, category);
            }
            return new ResponseEntity<>(results, HttpStatus.OK);
        }

        @GetMapping(value = "/getBookstock/{isbn}")
        public ResponseEntity<Integer> getBookstock(@PathVariable long isbn) {
            int stock = bookService.getBookStock(isbn);
            return new ResponseEntity(stock, HttpStatus.OK);
        }

}
