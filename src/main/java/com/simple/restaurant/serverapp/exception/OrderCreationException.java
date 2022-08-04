package com.simple.restaurant.serverapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrderCreationException extends RuntimeException {
    public OrderCreationException(String mesage) {
        super(mesage);
    }
}