package com.alphadev.spring_transaction.controller;

import com.alphadev.spring_transaction.entity.Order;
import com.alphadev.spring_transaction.service.OrderProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderProcessingController {

    private final OrderProcessingService orderProcessingService;

    public OrderProcessingController(OrderProcessingService orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderProcessingService.placeAnOrder(order));
    }
}
