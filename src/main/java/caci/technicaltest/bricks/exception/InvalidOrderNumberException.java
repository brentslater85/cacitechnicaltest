package caci.technicaltest.bricks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidOrderNumberException extends RuntimeException {
    public InvalidOrderNumberException(String message) {
        super(message);
    }

    public InvalidOrderNumberException(String message, Throwable cause) {
        super(message, cause);
    }

}
