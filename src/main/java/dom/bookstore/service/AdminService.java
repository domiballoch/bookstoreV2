package dom.bookstore.service;

import dom.bookstore.domain.Book;

public interface AdminService {

    Book addNewBookToBookstore(Book book);

    void deleteBookFromBookstore(long isbn);

    Book updateBookInBookstore(Book book, long isbn);

}
