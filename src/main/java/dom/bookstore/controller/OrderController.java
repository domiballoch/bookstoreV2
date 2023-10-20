package dom.bookstore.controller;

import dom.bookstore.domain.OrderDetails;
import dom.bookstore.domain.Users;
import dom.bookstore.exception.BookstoreNotFoundException;
import dom.bookstore.service.OrderService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static dom.bookstore.utils.BookStoreConstants.ORDER_NOT_FOUND;

@Slf4j
@RestController
@RequestMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/findAllOrders")
    public ResponseEntity<List<OrderDetails>> findAllOrders() {
        List<OrderDetails> orderDetails = orderService.findAllOrders();
        if(orderDetails.isEmpty()) {
            noResultsFound(orderDetails, "All orders");
        }
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping(value = "/findOrder/{orderDetailsId}")
    public ResponseEntity<Optional<OrderDetails>> findOrderById(@PathVariable long orderDetailsId) {
        Optional<OrderDetails> orderDetails = Optional.ofNullable(orderService.findOrderById(orderDetailsId))
                .orElseThrow(() -> new BookstoreNotFoundException(ORDER_NOT_FOUND, orderDetailsId));
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @PostMapping(value = "/submitOrder")
    public ResponseEntity<OrderDetails> submitOrder(@RequestBody Users user) {
        OrderDetails orderDetails = orderService.submitOrder(user);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    private <T>  ResponseEntity<T> noResultsFound(T t, T r) {
        log.info("No results found: {}", r);
        return new ResponseEntity<T>(t, HttpStatus.NOT_FOUND);
    }
}
