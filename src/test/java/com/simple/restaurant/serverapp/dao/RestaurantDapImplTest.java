package com.simple.restaurant.serverapp.dao;

import com.simple.restaurant.serverapp.common.SQLCommon;
import com.simple.restaurant.serverapp.exception.DataBaseAccessException;
import com.simple.restaurant.serverapp.model.OrderModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantDapImplTest {

    @InjectMocks
    RestaurantDaoImpl target;

    @Mock
    SQLCommon sqlCommonMock;

    @Nested
    @DisplayName("/getAllOrdersByTableNum method tests")
    class getAllOrdersByTableNum {
        @DisplayName("getAllOrdersByTableNum: success")
        @Test
        void getAllOrdersByTableNum_success() throws Exception {
            // given:
            List<Map<String, Object>> expected = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("order_id", 2);
            expected.add(map);

            given(sqlCommonMock.getListSQL(Mockito.anyString(), any(Map.class))).willReturn(expected);

            // when:
            var res = target.getAllOrdersByTableNum(1);

            // then:
            assertEquals(res, Optional.of(expected));
        }

        @DisplayName("getAllOrdersByTableNum: exception")
        @Test
        void getAllOrdersByTableNum_exception() throws Exception {
            // given:
            String expectedMessage = "Data Access Error occurred while trying to retrieve data";
            given(sqlCommonMock.getListSQL(Mockito.anyString(), any(Map.class))).willThrow(new RuntimeException());

            // when:
            var exceptionResult = assertThrows(DataBaseAccessException.class, () -> {
                target.getAllOrdersByTableNum(1);
            });

            // then:
            String actualMessage = exceptionResult.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Nested
    @DisplayName("/getOrderByTableNumberAndItemId method tests")
    class getOrderByTableNumberAndItemId {
        @DisplayName("getOrderByTableNumberAndItemId: success")
        @Test
        void getOrderByTableNumberAndItemId_success() throws Exception {
            // given:
            Map<String, Object> expected = new HashMap<>();
            expected.put("order_id", 2);

            given(sqlCommonMock.getObjectSQL(Mockito.anyString(), any(Map.class))).willReturn(expected);

            // when:
            var res = target.getOrderByTableNumberAndItemId(20);

            // then:
            assertEquals(res, Optional.of(expected));
        }

        @DisplayName("getOrderByTableNumberAndItemId: exception")
        @Test
        void getOrderByTableNumberAndItemId_exception() throws Exception {
            // given:
            String expectedMessage = "Data Access Error occurred while trying to retrieve data";
            given(sqlCommonMock.getObjectSQL(Mockito.anyString(), any(Map.class))).willThrow(new RuntimeException());

            // when:
            var exceptionResult = assertThrows(DataBaseAccessException.class, () -> {
                target.getOrderByTableNumberAndItemId(1);
            });

            // then:
            String actualMessage = exceptionResult.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Nested
    @DisplayName("/createOrder method tests")
    class createOrder {
        @DisplayName("createOrder: success")
        @Test
        void createOrder_success() throws Exception {
            // given:
            given(sqlCommonMock.insUpdDelRow(Mockito.anyString(), any(Map.class))).willReturn(1);

            // when:
            var res = target.createOrder(new OrderModel());

            // then:
            assertEquals(res, 1);
        }

        @DisplayName("createOrder: exception")
        @Test
        void createOrder() throws Exception {
            // given:
            String expectedMessage = "Data Access Error occurred while trying to create the order";
            given(sqlCommonMock.insUpdDelRow(Mockito.anyString(), any(Map.class))).willThrow(new RuntimeException());

            // when:
            var exceptionResult = assertThrows(DataBaseAccessException.class, () -> target.createOrder(new OrderModel()));

            // then:
            String actualMessage = exceptionResult.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Nested
    @DisplayName("/deleteOrderByOrderId method tests")
    class deleteOrderByOrderId {
        @DisplayName("deleteOrderByOrderId: success")
        @Test
        void deleteOrderByOrderId_success() throws Exception {
            // given:
            given(sqlCommonMock.insUpdDelRow(Mockito.anyString(), any(Map.class))).willReturn(1);

            // when:
            var res = target.deleteOrderByOrderId(22);

            // then:
            assertEquals(res, 1);
        }

        @DisplayName("deleteOrderByOrderId: exception")
        @Test
        void deleteOrderByOrderId() throws Exception {
            // given:
            String expectedMessage = "Data Access Error occurred while trying to remove the order";
            given(sqlCommonMock.insUpdDelRow(Mockito.anyString(), any(Map.class))).willThrow(new RuntimeException());

            // when:
            var exceptionResult = assertThrows(DataBaseAccessException.class, () -> target.deleteOrderByOrderId(22));

            // then:
            String actualMessage = exceptionResult.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Nested
    @DisplayName("/getAllItems method tests")
    class getAllItems {
        @DisplayName("getAllItems: success")
        @Test
        void getAllItems_success() throws Exception {
            // given:
            List<Map<String, Object>> expected = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("item_id", "0001");
            map.put("item_name", "item1");
            expected.add(map);

            given(sqlCommonMock.getListSQL(Mockito.anyString(), any(Map.class))).willReturn(expected);

            // when:
            var res = target.getAllItems();

            // then:
            assertEquals(res, Optional.of(expected));
        }

        @DisplayName("getAllItems: exception")
        @Test
        void getAllItems_exception() throws Exception {
            // given:
            String expectedMessage = "Data Access Error occurred while trying to get all Items";
            given(sqlCommonMock.getListSQL(Mockito.anyString(), any(Map.class))).willThrow(new RuntimeException());

            // when:
            var exceptionResult = assertThrows(DataBaseAccessException.class, () -> target.getAllItems());

            // then:
            String actualMessage = exceptionResult.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Nested
    @DisplayName("/itemExists method tests")
    class itemExists {
        @DisplayName("itemExists: success")
        @Test
        void itemExists_success() throws Exception {
            // given:
            Map<String, Object> expected = new HashMap<>();
            expected.put("item_id", "0001");

            given(sqlCommonMock.getObjectSQL(Mockito.anyString(), any(Map.class))).willReturn(expected);

            // when:
            var res = target.itemExists("0001");

            // then:
            assertEquals(Optional.of(expected), res);
        }

        @DisplayName("itemExists: exception")
        @Test
        void itemExists_exception() throws Exception {
            // given:
            String expectedMessage = "Data Access Error occurred while trying to check item existence";
            given(sqlCommonMock.getObjectSQL(Mockito.anyString(), any(Map.class))).willThrow(new RuntimeException());

            // when:
            var exceptionResult = assertThrows(DataBaseAccessException.class, () -> target.itemExists("0023"));

            // then:
            String actualMessage = exceptionResult.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }
}
