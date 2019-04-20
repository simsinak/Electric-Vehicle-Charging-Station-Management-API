package fi.devolon.demo.exceptions;

public class StationNotFoundException extends RuntimeException{
    public StationNotFoundException(long id){
            super("Sttion with ID "+id+" Not Found");
        }
}
