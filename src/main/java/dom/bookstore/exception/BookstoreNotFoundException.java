package dom.bookstore.exception;

import dom.bookstore.domain.Category;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookstoreNotFoundException extends IllegalArgumentException {

    @Getter
    private long id;

    @Getter
    private String search;

    @Getter
    private Category category;

    public BookstoreNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public BookstoreNotFoundException(String errorMessage, long id) {
        super(errorMessage);
        this.id = id;
    }

    public BookstoreNotFoundException(String errorMessage, String search) {
        super(errorMessage);
        this.search = search;
    }

    public BookstoreNotFoundException(String errorMessage, Category category) {
        super(errorMessage);
        this.category = category;
    }
}
