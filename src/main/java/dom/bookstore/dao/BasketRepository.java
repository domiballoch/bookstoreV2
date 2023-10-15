package dom.bookstore.dao;

import dom.bookstore.domain.Basket;
import dom.bookstore.domain.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {}
