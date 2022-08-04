package com.simple.restaurant.serverapp.controller;

import com.simple.restaurant.serverapp.dao.ItemDao;
import com.simple.restaurant.serverapp.dto.CreateDelOrderDto;
import com.simple.restaurant.serverapp.dto.OrderDto;
import com.simple.restaurant.serverapp.dto.OrderListDto;
import com.simple.restaurant.serverapp.exception.DataBaseAccessException;
import com.simple.restaurant.serverapp.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrderControllerTest {
    MockMvc mockMvc;

    @InjectMocks
    private OrderController target;

    @Mock
    private OrderService orderServiceMock;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(target).build();
    }

    @Nested
    @DisplayName("/query resources tests")
    class query {

        @DisplayName("queryall: Response status 200 and Json response: success")
        @Test
        void queryall_success() throws Exception {
            // given:
            List<OrderDto> orderDtoList = new ArrayList<>();
            OrderDto orderDto = new OrderDto();
            orderDto.setOrderId(1);
            orderDto.setTableNum(3);
            orderDto.setItemId("0001");
            orderDto.setItemName("item1");
            orderDto.setCookTime(8);
            orderDtoList.add(orderDto);

            OrderListDto expected = new OrderListDto(1, orderDtoList);
            given(orderServiceMock.getAllItems(anyInt())).willReturn(expected);

            // when:
            mockMvc.perform(get("/v1/queryall?table=1"))
                    // then:
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count", is(1)))
                    .andExpect(jsonPath("$.order[0].orderId", is(1)))
                    .andExpect(jsonPath("$.order[0].tableNum", is(3)))
                    .andExpect(jsonPath("$.order[0].itemId", is("0001")))
                    .andExpect(jsonPath("$.order[0].itemName", is("item1")))
                    .andExpect(jsonPath("$.order[0].cookTime", is(8)));

        }

        @DisplayName("queryall: Response status 500 when DataAccessException is thrown")
        @Test
        void queryall_DatabaseAccessException() throws Exception {
            // given:
            given(orderServiceMock.getAllItems(anyInt())).willThrow(new DataBaseAccessException("Exception"));

            // when:
            mockMvc.perform(get("/v1/queryall?table=1"))
                    //then
                    .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        @DisplayName("queryall: Response status 400 and error Json, when there was a Bad Request")
        @Test
        void queryall_BadRequest() throws Exception {
            // given:
            String expected = "{\"message\":\"Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; nested exception is java.lang.NumberFormatException: For input string: \\\"a\\\"\",\"details\":\"uri=/v1/queryall\"}";
            String invalidJson = "Invalid";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(invalidJson, headers);

            // when:
            ResponseEntity<String> response = restTemplate.exchange("/v1/queryall?table=a", HttpMethod.GET, entity, String.class);

            // then:
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        }

        @DisplayName("queryitem: Response status 200 and Json response: success")
        @Test
        void queryitem_success() throws Exception {
            // given:
            OrderDto expected = new OrderDto();
            expected.setOrderId(1);
            expected.setTableNum(3);
            expected.setItemId("0001");
            expected.setItemName("item1");
            expected.setCookTime(8);

            given(orderServiceMock.getItem(anyInt())).willReturn(expected);

            // when:
            mockMvc.perform(get("/v1/queryitem?orderId=0001"))
                    // then:
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orderId", is(1)))
                    .andExpect(jsonPath("$.tableNum", is(3)))
                    .andExpect(jsonPath("$.itemId", is("0001")))
                    .andExpect(jsonPath("$.itemName", is("item1")))
                    .andExpect(jsonPath("$.cookTime", is(8)));
        }

        @DisplayName("queryitem: Response status 500 when DataAccessException is thrown")
        @Test
        void queryitem_DatabaseAccessException() throws Exception {
            // given:
            given(orderServiceMock.getItem(anyInt())).willThrow(new DataBaseAccessException("Exception"));

            // when:
            mockMvc.perform(get("/v1/queryitem?orderId=0001"))
                    // then:
                    .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        @DisplayName("queryitem: Response status 400 and error Json, when there was a Bad Request")
        @Test
        void queryitem_BadRequest() throws Exception {
            // given:
            String expected = "{\"message\":\"Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; nested exception is java.lang.NumberFormatException: For input string: \\\"a\\\"\",\"details\":\"uri=/v1/queryitem\"}";
            String invalidJson = "Invalid";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(invalidJson, headers);

            // when:
            ResponseEntity<String> response = restTemplate.exchange("/v1/queryitem?orderId=a", HttpMethod.GET, entity, String.class);

            // then:
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        }
    }

    @Nested
    @DisplayName("/add resource tests")
    class add {
        @DisplayName("add: Response status 200 and Json response: success")
        @Test
        void add_success() throws Exception {
            // given:
            CreateDelOrderDto createOrder = new CreateDelOrderDto();
            createOrder.setSuccess(true);

            given(orderServiceMock.createOrder(1, "0001")).willReturn(createOrder);

            // when:
            mockMvc.perform(put("/v1/add?table=1&item=0001"))
                    // then:
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)));
        }

        @DisplayName("add: Response status 500 when DataAccessException is thrown")
        @Test
        void add_DatabaseAccessException() throws Exception {
            // given:
            given(orderServiceMock.createOrder(anyInt(), anyString())).willThrow(new DataBaseAccessException("Exception"));

            // when:
            mockMvc.perform(put("/v1/add?table=1&item=0001"))
                    // then:
                    .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        @DisplayName("add: Response status 400 and error Json, when there was a Bad Request")
        @Test
        void add_BadRequest() throws Exception {
            // given:
            String expected = "{\"message\":\"Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; nested exception is java.lang.NumberFormatException: For input string: \\\"a\\\"\",\"details\":\"uri=/v1/add\"}";
            String invalidJson = "Invalid";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(invalidJson, headers);

            // when:
            ResponseEntity<String> response = restTemplate.exchange("/v1/add?table=a&item=0001", HttpMethod.PUT, entity, String.class);

            // then:
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        }
    }

    @Nested
    @DisplayName("/del resource tests")
    class delete {
        @DisplayName("del: Response status 200 and Json response: success")
        @Test
        void del_success() throws Exception {
            // given:
            CreateDelOrderDto createOrder = new CreateDelOrderDto();
            createOrder.setSuccess(true);

            given(orderServiceMock.deleteOrder(anyInt())).willReturn(createOrder);

            // when:
            mockMvc.perform(delete("/v1/del?orderId=2"))
                    // then:
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)));
        }

        @DisplayName("del: Response status 500 when DataAccessException is thrown")
        @Test
        void delDatabaseAccessException() throws Exception {
            // given:
            given(orderServiceMock.deleteOrder(anyInt())).willThrow(new DataBaseAccessException("Exception"));

            // when:
            mockMvc.perform(delete("/v1/del?orderId=2"))
                    // then:
                    .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        @DisplayName("del: Response status 400 and error Json, when there was a Bad Request")
        @Test
        void del_BadRequest() throws Exception {
            // given:
            String expected = "{\"message\":\"Failed to convert value of type 'java.lang.String' to required type 'java.lang.Integer'; nested exception is java.lang.NumberFormatException: For input string: \\\"a\\\"\",\"details\":\"uri=/v1/del\"}";
            String invalidJson = "Invalid";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(invalidJson, headers);

            // when:
            ResponseEntity<String> response = restTemplate.exchange("/v1/del?orderId=a", HttpMethod.DELETE, entity, String.class);

            // then:
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        }
    }

    @Nested
    @DisplayName("/items resource tests")
    class getAvailableItems {
        @DisplayName("items: Response status 200 and Json response: success")
        @Test
        void items_success() throws Exception {
            // given:
            List<ItemDao> listItems = new ArrayList<>();
            ItemDao itemDao = new ItemDao();
            itemDao.setItemId("0001");
            itemDao.setItemName("item1");
            listItems.add(itemDao);

            given(orderServiceMock.getAvailableItems()).willReturn(listItems);

            // when:
            mockMvc.perform(get("/v1/items"))
                    // then:
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].itemId", is("0001")))
                    .andExpect(jsonPath("$[0].itemName", is("item1")));
        }

        @DisplayName("items: Response status 404 and error Json, when request with parameters")
        @Test
        void items_BadRequest() throws Exception {
            // given:
            String expected = "{\"message\":\"Resource not found\",\"details\":\"uri=/v1/items&table=2\"}";
            String invalidJson = "Invalid";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(invalidJson, headers);

            // when:
            ResponseEntity<String> response = restTemplate.exchange("/v1/items&table=2", HttpMethod.GET, entity, String.class);

            // then:
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        }
    }


        @Nested
    @DisplayName("/non existing resources requests (under /v1) tests")
    class otherRequests {
        @DisplayName("others: Response status 404 and error Json, when a non existing resource was requested")
        @Test
        void others_NotFound() throws Exception {
            // given:
            String expected = "{\"message\":\"Resource not found\",\"details\":\"uri=/v1/abc\"}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // when:
            ResponseEntity<String> response = restTemplate.exchange("/v1/abc?orderId=12", HttpMethod.GET, entity, String.class);

            // then:
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        }
    }
}
