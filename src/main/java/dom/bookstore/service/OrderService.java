package dom.bookstore.service;

import dom.bookstore.domain.OrderDetails;
import dom.bookstore.domain.Users;

public interface OrderService {

    OrderDetails submitOrder(Users user);
}
