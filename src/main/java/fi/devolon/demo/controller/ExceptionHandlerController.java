package fi.devolon.demo.controller;

import fi.devolon.demo.exceptions.CompanyNotFoundException;
import fi.devolon.demo.exceptions.MissingValueException;
import fi.devolon.demo.exceptions.ResourceNotFoundException;
import fi.devolon.demo.exceptions.StationNotFoundException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getErrorResponse(status,headers,ex.getMessage());

    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return getErrorResponse(HttpStatus.BAD_REQUEST,headers,"Message Format Is Not Correct For Parser, Check Value Types");
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getErrorResponse(HttpStatus.BAD_REQUEST,headers,"Message Format Is Not Correct For Parser, Check Value Types");
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getErrorResponse(status,headers,"Message Format Is Not Correct, Check Value Types");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getField()+" "+x.getDefaultMessage())
                .collect(Collectors.toList());
        return getErrorResponse(status,headers,errors.toString());

    }

    @ExceptionHandler({CompanyNotFoundException.class,StationNotFoundException.class, ResourceNotFoundException.class})
    public final  ResponseEntity<Object> handleNotFound(RuntimeException ex){
        return getErrorResponse(HttpStatus.NOT_FOUND,ex.getMessage());

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final  ResponseEntity<Object> handleNotFound(ConstraintViolationException ex){
        List<String> errors =ex.getConstraintViolations()
                .stream()
                .map(x->x.getPropertyPath()+" "+x.getMessage())
                .collect(Collectors.toList());

        return getErrorResponse(HttpStatus.BAD_REQUEST,errors.toString());

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final  ResponseEntity<Object> handleNotFound(DataIntegrityViolationException ex){
        return getErrorResponse(HttpStatus.CONFLICT,"Values Are Duplicated");
    }

    @ExceptionHandler({MissingValueException.class,NumberFormatException.class,IllegalArgumentException.class})
    public final  ResponseEntity<Object> missingValueHandler(RuntimeException ex) {
        return getErrorResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
    }
    private ResponseEntity<Object> getErrorResponse(HttpStatus status, String message){
        Map<String, Object> body = getErrorBody(status,message);
        return new ResponseEntity<>(body, status);
    }
    private ResponseEntity<Object> getErrorResponse(HttpStatus status,HttpHeaders headers, String message){
        Map<String, Object> body = getErrorBody(status,message);
        return new ResponseEntity<>(body, headers,status);
    }
    private Map<String , Object> getErrorBody(HttpStatus status,String message){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", status);
        body.put("message", message);
        return body;
    }

}
