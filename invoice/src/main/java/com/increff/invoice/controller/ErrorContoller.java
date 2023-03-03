package com.increff.invoice.controller;

import com.increff.invoice.model.ApiException;
import com.increff.invoice.model.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorContoller {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handle(ApiException e) {
        ErrorMessage data = new ErrorMessage();
        data.setMessage(e.getMessage());
        return data;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handle(HttpMessageNotReadableException e) {
        ErrorMessage data = new ErrorMessage();
        data.setMessage("Wrong data provided");
        return data;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handle(Throwable e) {
        e.printStackTrace();
        log.error(e.getMessage());
        ErrorMessage data = new ErrorMessage();
        data.setMessage("An unknown error has occurred - " + e.getMessage());
        return data;
    }
}
