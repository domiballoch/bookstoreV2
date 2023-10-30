package dom.bookstore.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class BookstoreExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { BookstoreNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(BookstoreNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex, ex.getId());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = { DataAccessException.class })
    protected ResponseEntity<Object> handleInternal(DataAccessException ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = { BookstoreValidationException.class })
    protected ResponseEntity<Object> handleValidation(BookstoreValidationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(value = { BookstoreBasketException.class })
    protected ResponseEntity<Object> handleNoContent(BookstoreBasketException ex) {
        ApiError apiError = new ApiError(HttpStatus.NO_CONTENT, ex);
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    // @Validate For Validating Path Variables, Request Body and Request Params
    @ExceptionHandler(ConstraintViolationException.class)
    protected void constraintViolationException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    // error handle for @Valid
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all fields errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }
}
