package com.simple.restaurant.serverapp.controller;

import com.simple.restaurant.serverapp.dao.ItemDao;
import com.simple.restaurant.serverapp.dto.CreateDelOrderDto;
import com.simple.restaurant.serverapp.dto.OrderDto;
import com.simple.restaurant.serverapp.dto.OrderListDto;
import com.simple.restaurant.serverapp.exception.ResourceNotFoundException;
import com.simple.restaurant.serverapp.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/v1")
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService service;

    // accepts : /v1/queryall?table={table} URIs
    @GetMapping(value="/queryall")
    @Async
    public CompletableFuture<OrderListDto> getAllItems(@RequestParam Integer table) {
        log.info("/queryall resource was called using table=" + table + " parameter ");
        return service.getAllItems(table);
    }

    // accepts : /v1/queryitem?orderId={orderId} URIs
    @GetMapping(value="/queryitem")
    @Async
    public CompletableFuture<OrderDto> getItem(@RequestParam Integer orderId) {
        log.info("/queryitem resource was called using orderId=" + orderId + " parameter");
        return service.getItem(orderId);
    }

    // accepts : /v1/add?table={table}&item={item} URIs
    @PutMapping(value="/add")
    @Async
    public CompletableFuture<CreateDelOrderDto> createOrder(@RequestParam Integer table, @RequestParam String item) {
        log.info("/add resource was called using table=" + table + " and item=" + item + " parameters");
        return service.createOrder(table, item);
    }

    // accepts : /v1/del?orderId={orderId}  URIs
    @DeleteMapping(value="/del")
    @Async
    public CompletableFuture<CreateDelOrderDto> deleteOrder(@RequestParam Integer orderId) {
        log.info("/del resource was called using orderId=" + orderId + " parameter");
        return service.deleteOrder(orderId);
    }

    // accepts : /v1/items  URIs
    @GetMapping(value="/items")
    public List<ItemDao> getAvailableItems() {
        log.info("/items resource was called");
        return service.getAvailableItems();
    }

    // other requests
    @GetMapping(value="/**")
    public OrderDto otherRequests() {
        log.error("Resource not found in /v1");
        throw new ResourceNotFoundException("Resource not found");
    }
}