package caci.technicaltest.bricks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrderDispatchedException extends RuntimeException {
    public OrderDispatchedException(String message) {
        super(message);
    }

    public OrderDispatchedException(String message, Throwable cause) {
        super(message, cause);
    }

}
