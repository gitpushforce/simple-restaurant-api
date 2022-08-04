package com.simple.restaurant.serverapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateDelOrderDto implements Serializable {
    Boolean success;
}
