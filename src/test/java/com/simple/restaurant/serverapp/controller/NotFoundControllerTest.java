package com.simple.restaurant.serverapp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class NotFoundControllerTest {
    MockMvc mockMvc;

    @InjectMocks
    private NotFoundController target;

    @Autowired
    private TestRestTemplate restTemplate;

    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(target).build();
    }

    @Nested
    @DisplayName("/non existing resources requests (outside /v1) tests")
    class otherRequests {
        @DisplayName("others: Response status 404 and error Json, when a non existing resource was requested (outside /v1)")
        @Test
        void notFoundRequest() throws Exception {
            // given:
            String expected = "{\"message\":\"Resource not found\",\"details\":\"uri=/aaa\"}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // when:
            ResponseEntity<String> response = restTemplate.exchange("/aaa", HttpMethod.GET, entity, String.class);

            // then:
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            JSONAssert.assertEquals(expected, response.getBody(), false);
        }
    }
}
