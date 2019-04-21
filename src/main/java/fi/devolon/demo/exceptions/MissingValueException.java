package fi.devolon.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingValueException extends RuntimeException {
    public MissingValueException(String message){
        super(message);
    }
}
