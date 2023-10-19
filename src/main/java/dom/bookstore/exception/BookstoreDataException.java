package dom.bookstore.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BookstoreDataException extends DataAccessException {

    public BookstoreDataException(String errorMessage) {
        super(errorMessage);
    }
}
