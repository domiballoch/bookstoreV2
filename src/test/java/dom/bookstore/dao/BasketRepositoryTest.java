package dom.bookstore.dao;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Uses HSQLDB in-memory db
 */
@Sql(scripts = {"classpath:test_data/repositoryTestData.sql"})
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BasketRepositoryTest {

    //getBasket
    //deletebasket
    //saveBookToBasket

    //private BasketItemRepository basketItemRepository;


}
