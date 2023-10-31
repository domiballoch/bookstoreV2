package dom.bookstore.dao;

import dom.bookstore.domain.Book;
import dom.bookstore.domain.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Uses HSQLDB in-memory db
 */
@Sql(scripts = {"classpath:test_data/repositoryTestData.sql"})
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void findAllBooks() {
        List<Book> books = bookRepository.findAll();

        assertThat(books.size()).isEqualTo(5);
    }

    @Test
    public void findBookById() {
        Optional<Book> book = bookRepository.findById(1001L);

        assertThat(book.get().getIsbn()).isEqualTo(1001);
        assertThat(book.get().getTitle()).isEqualTo("Tall Tales");
        assertThat(book.get().getAuthor()).isEqualTo("Mr Fredrikson");
        assertThat(book.get().getCategory()).isEqualTo(Category.FICTION);
        assertThat(book.get().getPrice()).isEqualTo(BigDecimal.valueOf(4.99));
        assertThat(book.get().getStock()).isEqualTo(25);
    }

    @Test
    public void findBookBySearchTermIgnoreCase_OneResult() {
        List<Book> books = bookRepository.findBookBySearchTermIgnoreCase("English");

        assertThat(books.get(0).getTitle()).isEqualTo("An English Rose");
    }

    @Test
    public void findBookBySearchTermIgnoreCase_TwoResults() {
        List<Book> books = bookRepository.findBookBySearchTermIgnoreCase("Tales");

        assertThat(books.size()).isEqualTo(2);
        assertThat(books.get(0).getTitle()).isEqualTo("Tall Tales");
        assertThat(books.get(1).getTitle()).isEqualTo("Short Tales");
    }

    @Test
    public void findBookByCategory_OneResult() {
        List<Book> books = bookRepository.findByCategory(Category.HORROR);

        assertThat(books.get(0).getTitle()).isEqualTo("The Crab Man");
    }

    @Test
    public void findBookByCategory_TwoResults() {
        List<Book> books = bookRepository.findByCategory(Category.FICTION);

        assertThat(books.size()).isEqualTo(2);
        assertThat(books.get(0).getTitle()).isEqualTo("Tall Tales");
        assertThat(books.get(1).getTitle()).isEqualTo("Short Tales");
    }

    @Test
    public void getBookStock() {
        Integer stock = bookRepository.getBookStock(1003);

        assertThat(stock).isEqualTo(10);
    }

    @Test
    public void saveOneBook() {
        Book book = Book.builder()
                .title("title6")
                .author("author6")
                .category(Category.TECHNOLOGY)
                .price(BigDecimal.valueOf(4.99))
                .stock(10)
                .build();

        bookRepository.save(book);
        List<Book> bookList = bookRepository.findAll();

        assertThat(bookRepository.count()).isEqualTo(6);
        assertThat(bookList.size()).isEqualTo(6);
        assertThat(bookList.get(5).getIsbn()).isEqualTo(1006L);
        assertThat(bookList.get(5).getTitle()).isEqualTo("title6");
        assertThat(bookList.get(5).getAuthor()).isEqualTo("author6");
        assertThat(bookList.get(5).getCategory()).isEqualTo(Category.TECHNOLOGY);
        assertThat(bookList.get(5).getPrice()).isEqualTo(BigDecimal.valueOf(4.99));
        assertThat(bookList.get(5).getStock()).isEqualTo(10);
    }

    @Test
    public void deleteOneBook() {
        bookRepository.deleteById(1005L);
        List<Book> bookList = bookRepository.findAll();

        assertThat(bookList.size()).isEqualTo(4);
        assertThat(bookRepository.findById(1005L)).isEmpty();
    }

    @Test
    public void updateBookstock() {
        Optional<Book> book = bookRepository.findById(1001L);
        assertThat(book.get().getStock()).isEqualTo(25);

        bookRepository.updateBookStock(50,1001);

        Integer result = bookRepository.getBookStock(1001);
        assertThat(result).isEqualTo(50);
    }

}
