package dom.bookstore.controller;

import dom.bookstore.domain.Book;
import dom.bookstore.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exceptions handled by controller advice
 */
@RestController
@RequestMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(value = "/addNewBook")
    public ResponseEntity<Book> addNewBookToBookstore(@RequestBody Book book) {
        Book addedBook = adminService.addNewBookToBookstore(book);
        return new ResponseEntity<>(addedBook, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteBook/{isbn}")
    public ResponseEntity<Long> deleteBookFromBookstore(@PathVariable long isbn) {
        adminService.deleteBookFromBookstore(isbn);
        return new ResponseEntity<>(isbn, HttpStatus.OK);
    }

    //Using PUT for idempotency - resending the whole Entity
    @PutMapping(value = "/updateBook/{isbn}")
    public ResponseEntity<Book> updateBookInBookstore(@RequestBody Book book, @PathVariable long isbn) {
        Book updatedBook = adminService.updateBookInBookstore(book, isbn);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }
}
