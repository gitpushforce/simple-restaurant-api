package com.simple.restaurant.serverapp.dao;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ItemDao implements Serializable {

    String itemId;
    String itemName;
}
