package com.increff.pos.controller;

import com.increff.pos.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorContoller {

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageDto handle(IllegalStateException e) {
        ErrorMessageDto data = new ErrorMessageDto();
        data.setMessage(e.getMessage());
        return data;
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageDto handle(Throwable e) {
        ErrorMessageDto data = new ErrorMessageDto();
        data.setMessage("An unknown error has occurred - " + e.getMessage());
        return data;
    }
}
