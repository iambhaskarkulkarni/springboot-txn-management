package com.alphadev.spring_transaction.service;

import com.alphadev.spring_transaction.entity.Order;
import com.alphadev.spring_transaction.entity.Product;
import com.alphadev.spring_transaction.handler.AuditLogHandler;
import com.alphadev.spring_transaction.handler.InventoryHandler;
import com.alphadev.spring_transaction.handler.OrderHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderProcessingService {

    private final OrderHandler orderHandler;
    private final InventoryHandler inventoryHandler;
    private final AuditLogHandler auditLogHandler;

    public OrderProcessingService(OrderHandler orderHandler, InventoryHandler inventoryHandler, AuditLogHandler auditLogHandler) {
        this.orderHandler = orderHandler;
        this.inventoryHandler = inventoryHandler;
        this.auditLogHandler = auditLogHandler;
    }


    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Order placeAnOrder(Order order) {
        //get product inventory
        Product product = inventoryHandler.getProduct(order.getProductId());
        //validate stock availability < (5)
        validateStockAvailability(order, product);
        //update total price in order entity
        order.setTotalPrice(order.getQuantity()*product.getPrice());
        //save order
        Order saveOrder = orderHandler.saveOrder(order);
        //update stock in inventory
        updateInventoryStock(order, product);
        return saveOrder;
    }

    private static void validateStockAvailability(Order order, Product product) {
        if(order.getQuantity() > product.getStockQuantity()) {
            throw new RuntimeException("Insufficient stock !");
        }
    }

    private void updateInventoryStock(Order order, Product product) {
        int availableStock = product.getStockQuantity() - order.getQuantity();
        product.setStockQuantity(availableStock);
        inventoryHandler.updateProductDetails(product);
    }
}
