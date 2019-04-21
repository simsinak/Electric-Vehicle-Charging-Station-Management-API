package fi.devolon.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StationNotFoundException extends RuntimeException{
    public StationNotFoundException(long id){
            super("Station with ID "+id+" Not Found");
        }
}
