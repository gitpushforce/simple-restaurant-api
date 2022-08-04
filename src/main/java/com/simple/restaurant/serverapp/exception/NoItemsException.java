package com.simple.restaurant.serverapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class NoItemsException extends RuntimeException {
    public NoItemsException(String message) {
        super(message);
    }
}
