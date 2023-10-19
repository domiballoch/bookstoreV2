package dom.bookstore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookstoreValidationException extends IllegalArgumentException {

    public BookstoreValidationException(String errorMessage) {
        super(errorMessage);
    }
}
