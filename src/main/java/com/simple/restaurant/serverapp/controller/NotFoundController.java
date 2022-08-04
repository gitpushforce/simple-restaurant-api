package com.simple.restaurant.serverapp.controller;

import com.simple.restaurant.serverapp.dto.ErrorDto;
import com.simple.restaurant.serverapp.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotFoundController {

    private final Logger log = LoggerFactory.getLogger(NotFoundController.class);

    @GetMapping(value="/**")
    public ErrorDto notFoundRequest() {
        log.error("Resource not found");
        throw new ResourceNotFoundException("Resource not found");
    }
}
