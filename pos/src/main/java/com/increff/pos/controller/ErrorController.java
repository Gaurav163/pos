package com.increff.pos.controller;

import com.increff.pos.model.ApiException;
import com.increff.pos.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handle(ApiException e) {
        ErrorMessage data = new ErrorMessage();
        data.setMessage(e.getMessage());
        return data;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handle(HttpMessageNotReadableException e) {
        ErrorMessage data = new ErrorMessage();
        data.setMessage("Invalid data");
        return data;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handle(Throwable e) {
        e.printStackTrace();
        System.out.println(e.getClass());
        ErrorMessage data = new ErrorMessage();
        data.setMessage("An unknown error has occurred - " + e.getMessage());
        return data;
    }
}
