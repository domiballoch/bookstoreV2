package dom.bookstore.service;

import dom.bookstore.domain.Book;
import dom.bookstore.domain.Category;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> findAllBooks();

    Optional<Book> findBookByIsbn(long isbn);

    Integer getBookStock(long isbn);

    boolean checkStock(long isbn);

    List<Book> findBooksByCategory(Category category);

    List<Book> findBookBySearchTermIgnoreCase(String search);

}
