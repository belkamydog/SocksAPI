package com.api.socks.controller;

import com.api.socks.exception.InvalidCsvException;
import com.api.socks.exception.LackOfSocksException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
@ResponseBody
public class HandlerControllerExceptions extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> badRequest(Exception exception) {
       return new ResponseEntity<>("Bad request", HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> notFoundException(Exception exception) {
        return new ResponseEntity<>("This id not found", HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(LackOfSocksException.class)
    public ResponseEntity<String> lackSocksException(Exception exception) {
        return new ResponseEntity<>("lack socks", HttpStatusCode.valueOf(406));
    }

    @ExceptionHandler(InvalidCsvException.class)
    public ResponseEntity<String> invalidCsvException(Exception exception) {
        return new ResponseEntity<>("invalid csv Exception", HttpStatusCode.valueOf(415));
    }
}