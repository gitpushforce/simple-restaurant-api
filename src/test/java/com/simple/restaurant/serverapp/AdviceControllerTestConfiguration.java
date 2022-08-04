package com.simple.restaurant.serverapp;

import com.simple.restaurant.serverapp.exception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;

@TestConfiguration
public class AdviceControllerTestConfiguration {
    @Bean
    public GlobalExceptionHandler globalExceptionHandlerTest() {
        return new GlobalExceptionHandler();
    }
}
