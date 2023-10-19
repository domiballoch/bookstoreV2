package dom.bookstore.service;

import dom.bookstore.domain.BasketItem;
import dom.bookstore.domain.OrderDetails;
import dom.bookstore.domain.OrderItem;
import dom.bookstore.domain.Users;

import java.util.List;

public interface OrderService {

    void checkout();

    OrderDetails submitOrder(Users user);
}
