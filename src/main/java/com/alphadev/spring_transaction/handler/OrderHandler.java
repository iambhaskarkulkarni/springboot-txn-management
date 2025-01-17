package com.alphadev.spring_transaction.handler;

import com.alphadev.spring_transaction.entity.Order;
import com.alphadev.spring_transaction.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderHandler {

    private final OrderRepository orderRepository;

    public OrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}
