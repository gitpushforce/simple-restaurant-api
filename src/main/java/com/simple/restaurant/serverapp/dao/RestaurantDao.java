package com.simple.restaurant.serverapp.dao;

import com.simple.restaurant.serverapp.model.OrderModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RestaurantDao {

    Optional<List<Map<String, Object>>> getAllOrdersByTableNum(Integer tableNum);
    Optional<Map<String, Object>> getOrderByTableNumberAndItemId(Integer orderId);
    Integer createOrder(OrderModel order);
    Integer deleteOrderByOrderId(Integer orderId);
    Optional<List<Map<String, Object>>> getAllItems();
    Optional<Map<String, Object>> itemExists(String itemId);
}
