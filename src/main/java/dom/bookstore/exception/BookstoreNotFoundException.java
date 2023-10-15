package dom.bookstore.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookstoreNotFoundException extends IllegalArgumentException {

    @Getter
    private final long id;

    public BookstoreNotFoundException(String errorMessage, long id) {
        super(errorMessage);
        this.id = id;
    }
}
