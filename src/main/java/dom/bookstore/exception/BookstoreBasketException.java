package dom.bookstore.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class BookstoreBasketException extends DataAccessException {

    public BookstoreBasketException(String errorMessage) {
        super(errorMessage);
    }
}
