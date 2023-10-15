package dom.bookstore.service;

import dom.bookstore.dao.BookRepository;
import dom.bookstore.domain.Book;
import dom.bookstore.exception.BookstoreNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.BOOK_NOT_FOUND;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;



}
