package com.simple.restaurant.serverapp.dao;

import com.simple.restaurant.serverapp.common.SQLCommon;
import com.simple.restaurant.serverapp.exception.DataBaseAccessException;
import com.simple.restaurant.serverapp.model.OrderModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RestaurantDaoImpl implements RestaurantDao {

    @Autowired
    private SQLCommon sqlCommon;

    private final Logger log = LoggerFactory.getLogger(RestaurantDaoImpl.class);

    @Override
    public Optional<List<Map<String, Object>>> getAllOrdersByTableNum(Integer tableNum)  {
        try {
            Map<String, Object> params =  new HashMap<>();
            params.put("table_num", tableNum);
            return Optional.ofNullable(
                    sqlCommon.getListSQL("select_all_orders", params));
        } catch(RuntimeException e) {
            log.error("Data Access Error occurred while trying to retrieve data");
            throw new DataBaseAccessException("Data Access Error occurred while trying to retrieve data");
        }
    }

    @Override
    public Optional<Map<String, Object>> getOrderByTableNumberAndItemId(Integer orderId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("order_id", orderId);

            return Optional.ofNullable(
                    sqlCommon.getObjectSQL("select_item", params));

        } catch(RuntimeException e) {
            log.error("Data Access Error occurred while trying to retrieve data");
            throw new DataBaseAccessException("Data Access Error occurred while trying to retrieve data");
        }
    }

    @Override
    public Integer createOrder(OrderModel order) {
        Map<String, Object> params = new HashMap<>();
        params.put("table_num", order.getTableNum());
        params.put("item_id", order.getItemId());
        params.put("cook_time", order.getCookTime());
        try {
            return sqlCommon.insUpdDelRow("insert_order", params);
        } catch (RuntimeException e) {
            log.error("Data Access Error occurred while trying to insert data");
            throw new DataBaseAccessException("Data Access Error occurred while trying to create the order");
        }
    }

    @Override
    public Integer deleteOrderByOrderId(Integer orderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("order_id", orderId);

        try {
            return sqlCommon.insUpdDelRow("delete_order", params);
        } catch (RuntimeException e) {
            log.error("Data Access Error occurred while trying to remove data");
            throw new DataBaseAccessException("Data Access Error occurred while trying to remove the order");
        }
    }

    @Override
    public Optional<List<Map<String, Object>>> getAllItems() {
        try {
            return Optional.ofNullable(sqlCommon.getListSQL("select_all_items", new HashMap<>()));
        } catch (RuntimeException e) {
            log.error("Data Access Error occurred while trying to get all Items");
            throw new DataBaseAccessException("Data Access Error occurred while trying to get all Items");
        }
    }

    @Override
    public Optional<Map<String, Object>> itemExists(String itemId) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("item_id", itemId);
            return Optional.ofNullable(sqlCommon.getObjectSQL("select_item_id", params));
        } catch (RuntimeException e) {
            log.error("Data Access Error occurred while trying to check item existence");
            throw new DataBaseAccessException("Data Access Error occurred while trying to check item existence");
        }
    }
}
