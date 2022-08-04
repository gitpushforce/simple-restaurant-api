package com.simple.restaurant.serverapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderListDto implements Serializable {
    private int count;
    private List<OrderDto> order;
}
