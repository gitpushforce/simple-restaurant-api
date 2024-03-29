package com.simple.restaurant.serverapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDto implements Serializable {
    private String message;
    private String details;
}
