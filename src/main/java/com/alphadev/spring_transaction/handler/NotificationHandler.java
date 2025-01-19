package com.alphadev.spring_transaction.handler;

import com.alphadev.spring_transaction.entity.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationHandler {

    @Transactional(propagation = Propagation.NEVER)
    public void sendOrderConfirmationNotification(Order order) {
        //send an email notification to customer
        System.out.println(order.getId()+" Order placed successfully");
    }
}
