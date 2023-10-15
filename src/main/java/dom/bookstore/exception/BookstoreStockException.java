package dom.bookstore.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class BookstoreStockException extends IllegalArgumentException {

    @Getter
    private final long id;

    public BookstoreStockException(String errorMessage, long id) {
        super(errorMessage);
        this.id = id;
    }
}
