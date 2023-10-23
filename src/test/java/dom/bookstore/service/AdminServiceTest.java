package dom.bookstore.service;

import dom.bookstore.dao.BookRepository;
import dom.bookstore.domain.Book;
import dom.bookstore.utils.TestDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static dom.bookstore.utils.TestDataUtils.BOOK_1;
import static dom.bookstore.utils.TestDataUtils.BOOK_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private AdminServiceImpl adminServiceImpl;

    @Test
    public void shouldAddOneNewBookToBookStore(){
        when(bookRepository.save(any(Book.class))).thenReturn(BOOK_1);
        Book savedBook = adminServiceImpl.addNewBookToBookstore(BOOK_1);

        savedBook.setIsbn(1L);
        assertThat(savedBook).isEqualTo(BOOK_1);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void shouldDeleteOneBookFromBookStore(){
        adminServiceImpl.deleteBookFromBookstore(1L);
        verify(bookRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    public void shouldUpdateOneBookFromBookStore(){
        Book originalBook = (BOOK_1);
        Book newBookDetails = (BOOK_2);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(originalBook));

        newBookDetails.setIsbn(1L);
        Book updatedBook = adminServiceImpl.updateBookInBookstore(newBookDetails, 1);

        assertThat(updatedBook).isEqualTo(newBookDetails);
        verify(bookRepository, times(1)).save(any(Book.class));
    }
}
