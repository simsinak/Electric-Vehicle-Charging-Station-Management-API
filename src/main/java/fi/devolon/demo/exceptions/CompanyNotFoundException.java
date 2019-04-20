package fi.devolon.demo.exceptions;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(long id){
        super("Company with ID "+id+" Not Found");
    }
}
