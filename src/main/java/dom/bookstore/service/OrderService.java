package dom.bookstore.service;

import dom.bookstore.domain.OrderDetails;
import dom.bookstore.domain.Users;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<OrderDetails> findAllOrders();

    Optional<OrderDetails> findOrderById(long orderDetailsId);

    OrderDetails checkout();

    OrderDetails submitOrder(Users user) throws Exception;
}
