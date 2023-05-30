package com.example.controller;

import com.example.ConsumerController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ConsumerControllerTest {

    @InjectMocks
    private ConsumerController consumerController;

    @Test
    public void testHealth() {
        ResponseEntity<String> healthCheck = consumerController.health();
        assertEquals(HttpStatus.OK, healthCheck.getStatusCode());
    }

}