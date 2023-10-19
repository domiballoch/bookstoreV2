package dom.bookstore.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiError {

    private Object input;
    private HttpStatus status;
    private String message;
    private String debugMessage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    ApiError(HttpStatus status, Throwable ex) {
        this(status);
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(HttpStatus status, String message) {
        this(status);
        this.message = message;
    }

    ApiError(HttpStatus status, String message, Throwable ex) {
        this(status, message);
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(HttpStatus status, Throwable ex, long input) {
        this(status, ex);
        this.input = input;
    }

    ApiError(HttpStatus status, String message, Throwable ex, long input) {
        this(status, message, ex);
        this.input = input;
    }
}
