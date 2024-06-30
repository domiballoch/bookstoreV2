package dom.bookstore.dao;

import dom.bookstore.domain.Book;
import dom.bookstore.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Using native SQL queries but best practice in prod code is to use JPQL
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByCategory(Category category);

    @Query(value = "SELECT * FROM book b WHERE b.title LIKE CONCAT('%', :search, '%')", nativeQuery = true)
    List<Book> findBookBySearchTermIgnoreCase(@Param("search") String search);

    @Query(value = "SELECT stock FROM book b WHERE b.isbn = :isbn", nativeQuery = true)
    Integer getBookStock(@Param("isbn") long isbn);

    @Transactional
    @Modifying
    @Query(value = "UPDATE book b SET b.stock = :stock WHERE b.isbn = :isbn", nativeQuery = true)
    void updateBookStock(@Param("stock") int stock, @Param("isbn") long isbn);
}
