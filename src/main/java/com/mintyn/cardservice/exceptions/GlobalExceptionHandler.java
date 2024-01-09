package com.mintyn.cardservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(EmailAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handlerForEmailAlreadyExists(final EmailAlreadyExists ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResponseMsg(ex.getMessage());
        //customized response code
        errorResponse.setResponseCode("99");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErrorResponse> handlerForDatabaseException(final DatabaseException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResponseMsg(ex.getMessage());
        //customized response code
        errorResponse.setResponseCode("55");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PasswordNotMatchingException.class)
    public ResponseEntity<ErrorResponse> handlerForPasswordNotMatchingException(final PasswordNotMatchingException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResponseMsg(ex.getMessage());
        //customized response code
        errorResponse.setResponseCode("99");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse response = new ErrorResponse();
        response.setResponseCode("99");
        response.setResponseMsg(e.getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.badRequest().body(response);
    }

}
