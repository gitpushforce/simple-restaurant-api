package com.simple.restaurant.serverapp.service;

import com.simple.restaurant.serverapp.dao.RestaurantDao;
import com.simple.restaurant.serverapp.dto.OrderDto;
import com.simple.restaurant.serverapp.dto.OrderListDto;
import com.simple.restaurant.serverapp.exception.NoItemsException;
import com.simple.restaurant.serverapp.exception.OrderCreationException;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceTest {

    @InjectMocks
    OrderService target;

    @Mock
    RestaurantDao daoMock;

    @Nested
    @DisplayName("/getAllItems method tests")
    class getAllItems {
        @DisplayName("getAllItems: if data exist")
        @Test
        void getAllItems_dataExists() throws Exception {
            // given:
            List<Map<String, Object>> mockedDB = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("order_id", 1);
            map.put("table_num", 3);
            map.put("item_id", "0004");
            map.put("item_name", "item4");
            map.put("cook_time", 7);
            mockedDB.add(map);

            List<OrderDto> expectedOrder = new ArrayList<>();
            OrderDto orderDto = new OrderDto();
            orderDto.setOrderId(1);
            orderDto.setTableNum(3);
            orderDto.setItemId("0004");
            orderDto.setItemName("item4");
            orderDto.setCookTime(7);
            expectedOrder.add(orderDto);

            var expected = new OrderListDto(expectedOrder.size(), expectedOrder);

            given(daoMock.getAllOrdersByTableNum(anyInt())).willReturn(Optional.of(mockedDB));

            // when:
            var res = target.getAllItems(1);

            // then:
            assertEquals(res.getCount(), expected.getCount());
            assertEquals(res.getOrder().get(0).getOrderId(), expected.getOrder().get(0).getOrderId());
            assertEquals(res.getOrder().get(0).getTableNum(), expected.getOrder().get(0).getTableNum());
            assertEquals(res.getOrder().get(0).getItemId(), expected.getOrder().get(0).getItemId());
            assertEquals(res.getOrder().get(0).getItemName(), expected.getOrder().get(0).getItemName());
            assertEquals(res.getOrder().get(0).getCookTime(), expected.getOrder().get(0).getCookTime());
        }

        @DisplayName("getAllItems: no data")
        @Test
        void getAllItems_noData() throws Exception {
            // given:
            List<Map<String, Object>> mockedDB = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();

            given(daoMock.getAllOrdersByTableNum(anyInt())).willReturn(Optional.of(mockedDB));

            // when:
            var res = target.getAllItems(1);

            // then:
            assertEquals(res.getCount(), 0);
            assertTrue(res.getOrder().isEmpty());
        }

        @DisplayName("getAllItems: Optional retrieved from dao was null")
        @Test
        void getAllItems_daoNull() throws Exception {
            // given:
            List<Map<String, Object>> mockedDB = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();

            given(daoMock.getAllOrdersByTableNum(anyInt())).willReturn(Optional.empty());

            // when:
            var res = target.getAllItems(1);

            // then:
            assertEquals(res.getCount(), 0);
            assertTrue(res.getOrder().isEmpty());
        }
    }

    @Nested
    @DisplayName("/getItem method tests")
    class getItem {
        @DisplayName("getItem: if data exist")
        @Test
        void getItem_dataExists() throws Exception {
            // given:
            Map<String, Object> mockedDB = new HashMap<>();
            mockedDB.put("order_id", 1);
            mockedDB.put("table_num", 3);
            mockedDB.put("item_id", "0004");
            mockedDB.put("item_name", "item4");
            mockedDB.put("cook_time", 7);

            OrderDto orderDto = new OrderDto();
            orderDto.setOrderId(1);
            orderDto.setTableNum(3);
            orderDto.setItemId("0004");
            orderDto.setItemName("item4");
            orderDto.setCookTime(7);

            given(daoMock.getOrderByTableNumberAndItemId(anyInt())).willReturn(Optional.of(mockedDB));

            // when:
            var res = target.getItem(1);

            // then:
            assertEquals(res.getOrderId(), 1);
            assertEquals(res.getTableNum(), 3);
            assertEquals(res.getItemId(), "0004");
            assertEquals(res.getItemName(), "item4");
            assertEquals(res.getCookTime(), 7);
        }

        @DisplayName("getItem: no data")
        @Test
        void getItem_noData() throws Exception {
            // given:
            Map<String, Object> mockedDB = new HashMap<>();

            given(daoMock.getOrderByTableNumberAndItemId(anyInt())).willReturn(Optional.of(mockedDB));

            // when:
            var res = target.getItem(1);

            // then:
            assertNull(res.getOrderId());
            assertNull(res.getTableNum());
            assertNull(res.getItemId());
            assertNull(res.getItemName());
            assertNull(res.getCookTime());
        }
    }

    @Nested
    @DisplayName("/createOrder method tests")
    class createOrder {
        @DisplayName("createOrder: insert successfully")
        @Test
        void createOrder_success() throws Exception {
            // given:
            Map<String, Object> map = new HashMap<>();
            map.put("item_id", "0004");

            given(daoMock.itemExists(Mockito.anyString())).willReturn(Optional.of(map));
            given(daoMock.createOrder(Mockito.any(OrderModel.class))).willReturn(1);

            // when:
            var res = target.createOrder(3, "0004");

            // then:
            assertEquals(res.getSuccess(), true);
        }

        @DisplayName("createOrder: insert fail")
        @Test
        void createOrder_fail() throws Exception {
            // given:
            Map<String, Object> map = new HashMap<>();
            map.put("item_id", "0004");

            given(daoMock.itemExists(Mockito.anyString())).willReturn(Optional.of(map));
            given(daoMock.createOrder(Mockito.any(OrderModel.class))).willReturn(0);

            // when:
            var res = target.createOrder(3, "0004");

            // then
            assertEquals(res.getSuccess(), false);
        }

        @DisplayName("createOrder: table number validation fail")
        @Test
        void createOrder_fail_tableNumValidation() throws Exception {
            // given:
            int tableNum = 0;
            String expectedMessage = "Cannot create an order for table " + tableNum;

            // when:
            var exceptionResult = assertThrows(OrderCreationException.class, () -> target.createOrder(tableNum, "0001"));

            // then
            String actualMessage = exceptionResult.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }

        @DisplayName("createOrder: item id validation fail")
        @Test
        void createOrder_fail_itemIdValidation() throws Exception {
            // given:
            String itemId = "0000";
            String expectedMessage = "The item " + itemId + " is not available to order";

            Map<String, Object> map = new HashMap<>();

            given(daoMock.itemExists(Mockito.anyString())).willReturn(Optional.of(map));

            // when:
            var exceptionResult = assertThrows(OrderCreationException.class, () -> target.createOrder(3, itemId));

            // then
            String actualMessage = exceptionResult.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Nested
    @DisplayName("/deleteOrder method tests")
    class deleteOrder {
        @DisplayName("deleteOrder: insert successfully")
        @Test
        void deleteOrder_success() throws Exception {
            // given:
            given(daoMock.deleteOrderByOrderId(anyInt())).willReturn(1);

            // when:
            var res = target.deleteOrder(2);

            // then:
            assertEquals(res.getSuccess(), true);
        }

        @DisplayName("deleteOrder: insert fail")
        @Test
        void deleteOrder_fail() throws Exception {
            // given:
            given(daoMock.deleteOrderByOrderId(anyInt())).willReturn(0);

            // when:
            var res = target.deleteOrder(2);

            // then:
            assertEquals(res.getSuccess(), false);
        }
    }

    @Nested
    @DisplayName("/getAvailableItems method tests")
    class getAvailableItems {
        @DisplayName("getAvailableItems: retrieve data successfully")
        @Test
        void getAvailableItems_success() throws Exception {
            // given:
            List<Map<String, Object>> mockDaoRes = new ArrayList<>();
            Map<String, Object> mockMap = new HashMap<>();
            mockMap.put("item_id", "0001");
            mockMap.put("item_name", "item1");
            mockDaoRes.add(mockMap);

            given(daoMock.getAllItems()).willReturn(Optional.of(mockDaoRes));

            // when:
            var res = target.getAvailableItems();

            // then:
            assertEquals(res.size(), 1);
            assertEquals(res.get(0).getItemId(), "0001");
        }

        @DisplayName("getAvailableItems: retrieve data is empty")
        @Test
        void getAvailableItems_fail() throws Exception {
            // given:
            String expectedMessage = "No items found";
            List<Map<String, Object>> mockDaoRes = new ArrayList<>();
            Map<String, Object> mockMap = new HashMap<>();
            mockDaoRes.add(mockMap);

            given(daoMock.getAllItems()).willReturn(Optional.of(mockDaoRes));

            // when:
            var exceptionResult = assertThrows(NoItemsException.class, () -> target.getAvailableItems());

            // then:
            String actualMessage = exceptionResult.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }

        @DisplayName("getAvailableItems: retrieved data was null")
        @Test
        void getAvailableItems_fail_optionalEmpty() throws Exception {
            // given:
            String expectedMessage = "No items found";

            given(daoMock.getAllItems()).willReturn(Optional.empty());

            // when:
            var exceptionResult = assertThrows(NoItemsException.class, () -> target.getAvailableItems());

            // then:
            String actualMessage = exceptionResult.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }
}
