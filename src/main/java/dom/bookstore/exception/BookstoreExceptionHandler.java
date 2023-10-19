package dom.bookstore.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class BookstoreExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { BookstoreNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(BookstoreNotFoundException ex) {
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex, ex.getId());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = { DataAccessException.class })
    protected ResponseEntity<Object> handleInternal(DataAccessException ex) {
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = { BookstoreValidationException.class })
    protected ResponseEntity<Object> handleValidation(BookstoreValidationException ex) {
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = { BookstoreBasketException.class })
    protected ResponseEntity<Object> handleNoContent(BookstoreBasketException ex) {
        final ApiError apiError = new ApiError(HttpStatus.NO_CONTENT, ex);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
