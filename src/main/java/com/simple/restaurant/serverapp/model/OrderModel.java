package com.simple.restaurant.serverapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderModel {

    private Integer tableNum;
    private String itemId;
    private Integer cookTime;
}
