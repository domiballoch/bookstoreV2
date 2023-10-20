package dom.bookstore.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@UtilityClass
public class BookStoreUtils {

    public static <T> ResponseEntity<T> noResultsFound(T t, T r) {
        log.info("No results found: {}", r);
        return new ResponseEntity<T>(t, HttpStatus.NOT_FOUND);
    }
}
