package com.simple.restaurant.serverapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {

    private Integer orderId;
    private Integer tableNum;
    private String itemId;
    private String itemName;
    private Integer cookTime;
}
