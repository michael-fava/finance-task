package com.mfava.akcetask.advice;

import com.mfava.akcetask.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.mfava.akcetask.controller")
public class ControllerAdvice {


    @ExceptionHandler(AccountCreateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> accountCreateException(AccountCreateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> accountNotFoundException(AccountNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccountRemoveException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> accountRemoveException(AccountRemoveException ex) {
        return new ResponseEntity<>("Account could not be removed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ClientCreationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> clientCreationException(ClientCreationException ex) {
        return new ResponseEntity<>("Client could not be created", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> clientNotFoundException(ClientNotFoundException ex) {
        return new ResponseEntity<>("Client Not Found "+ex.getClientId(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> insufficientFundsException(InsufficientFundsException ex) {
        return new ResponseEntity<>("Insufficient Funds", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> genericException(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>("Please speak to your System Administrator", HttpStatus.INTERNAL_SERVER_ERROR);
    }





}
