package com.simple.restaurant.serverapp.service;

import com.google.common.collect.ImmutableMap;
import com.simple.restaurant.serverapp.dao.ItemDao;
import com.simple.restaurant.serverapp.dao.RestaurantDao;
import com.simple.restaurant.serverapp.dto.CreateDelOrderDto;
import com.simple.restaurant.serverapp.dto.OrderDto;
import com.simple.restaurant.serverapp.dto.OrderListDto;
import com.simple.restaurant.serverapp.exception.NoItemsException;
import com.simple.restaurant.serverapp.exception.OrderCreationException;
import com.simple.restaurant.serverapp.model.OrderModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private RestaurantDao dao;

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    public CompletableFuture<OrderListDto> getAllItems(Integer tableNum) {
        var daoResult = dao.getAllOrdersByTableNum(tableNum).orElseGet(() ->
                new ArrayList<>(List.of(ImmutableMap.of())));

        if (daoResult.isEmpty() || daoResult.get(0).isEmpty()) {
            log.info("Data not found when trying to retrieve all orders of table " +  tableNum);
            return CompletableFuture.completedFuture(new OrderListDto(0, Collections.emptyList()));
        }

        var orderList = daoResult.stream().map(row -> {
                    OrderDto orderDto = new OrderDto();
                    orderDto.setOrderId((Integer) row.get("order_id"));
                    orderDto.setTableNum((Integer) row.get("table_num"));
                    orderDto.setItemId((String) row.get("item_id"));
                    orderDto.setItemName((String) row.get("item_name"));
                    orderDto.setCookTime((Integer)row.get("cook_time"));
                    return orderDto;
        }).collect(Collectors.toList());
        return CompletableFuture.completedFuture(new OrderListDto(daoResult.size(), orderList));
    }

    public CompletableFuture<OrderDto> getItem(Integer orderId) {
        var daoResult = dao.getOrderByTableNumberAndItemId(orderId).orElseGet(ImmutableMap::of);
        if (daoResult.isEmpty()) {
            log.info("Data not found when trying to retrieve " + orderId + " orderId");
            return CompletableFuture.completedFuture(new OrderDto());
        }

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId((Integer) daoResult.get("order_id"));
        orderDto.setTableNum((Integer) daoResult.get("table_num"));
        orderDto.setItemId((String) daoResult.get("item_id"));
        orderDto.setItemName((String) daoResult.get("item_name"));
        orderDto.setCookTime((Integer)daoResult.get("cook_time"));
        return CompletableFuture.completedFuture(orderDto);
    }

    public CompletableFuture<CreateDelOrderDto> createOrder(Integer tableNum, String itemId) {
        validateTable(tableNum);
        validateItem(itemId);
        OrderModel model = new OrderModel();
        Random random = new Random();
        model.setTableNum(tableNum);
        model.setItemId(itemId);
        // random number between 5 to 15 (inclusive)
        model.setCookTime(random.nextInt(11) + 5);

        // returns 1 if data was inserted successfully
        Integer result = dao.createOrder(model);
        CreateDelOrderDto createDelOrderDto = new CreateDelOrderDto();
        if (Objects.equals(result, 1)) {
            log.info("order created successfully");
            createDelOrderDto.setSuccess(Boolean.TRUE);
        } else {
            log.info("the order could not be created");
            createDelOrderDto.setSuccess(Boolean.FALSE);
        }

        return CompletableFuture.completedFuture(createDelOrderDto);
    }

    public CompletableFuture<CreateDelOrderDto> deleteOrder(Integer orderId) {
        Integer result = dao.deleteOrderByOrderId(orderId);
        CreateDelOrderDto createDelOrderDto = new CreateDelOrderDto();
        // returns 1 if data was deleted successfully
        if (Objects.equals(result, 1)) {
            log.info("order removed successfully");
            createDelOrderDto.setSuccess(Boolean.TRUE);
        } else {
            log.info("the order could not be removed");
            createDelOrderDto.setSuccess(Boolean.FALSE);
        }
        return CompletableFuture.completedFuture(createDelOrderDto);
    }

    private void validateTable(Integer tableNum) {
        if (tableNum < 1) throw new OrderCreationException("Cannot create an order for table " + tableNum);
    }

    public List<ItemDao> getAvailableItems() {
        var daoResult = dao.getAllItems().orElseGet(() ->
                new ArrayList<>(List.of(ImmutableMap.of())));
        if (daoResult.isEmpty() || daoResult.get(0).isEmpty()) {
            log.info("No items found, verify DB data");
            throw new NoItemsException("No items found");
        }
        return daoResult.stream().map(row -> {
            ItemDao itemDao = new ItemDao();
            itemDao.setItemId(row.get("item_id").toString());
            itemDao.setItemName(row.get("item_name").toString());
            return itemDao;
        }).collect(Collectors.toList());
    }

    private void validateItem(String itemId) {
        var daoResult = dao.itemExists(itemId).orElseGet(HashMap::new);
        if (daoResult.isEmpty() || !itemId.equals(daoResult.get("item_id"))) {
            log.info(itemId + " not found in DB");
            throw new OrderCreationException("The item " + itemId + " is not available to order");
        }
    }
}
