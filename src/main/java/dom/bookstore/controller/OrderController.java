package dom.bookstore.controller;

import dom.bookstore.domain.OrderDetails;
import dom.bookstore.domain.Users;
import dom.bookstore.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/rest", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping(value = "/submitOrder")
    public ResponseEntity<OrderDetails> submitOrder(@RequestBody Users user) {
        final OrderDetails orderDetails = orderService.submitOrder(user);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }
}
