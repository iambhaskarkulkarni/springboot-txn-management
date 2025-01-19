package com.alphadev.spring_transaction.service;

import com.alphadev.spring_transaction.entity.Order;
import com.alphadev.spring_transaction.entity.Product;
import com.alphadev.spring_transaction.handler.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderProcessingService {

    private final OrderHandler orderHandler;
    private final InventoryHandler inventoryHandler;
    private final AuditLogHandler auditLogHandler;
    private final PaymentValidatorHandler paymentValidatorHandler;
    private final NotificationHandler notificationHandler;
    private final ProductRecommendationHandler productRecommendationHandler;

    public OrderProcessingService(
            OrderHandler orderHandler,
            InventoryHandler inventoryHandler,
            AuditLogHandler auditLogHandler,
            PaymentValidatorHandler paymentValidatorHandler,
            NotificationHandler notificationHandler, ProductRecommendationHandler productRecommendationHandler
    ) {
        this.orderHandler = orderHandler;
        this.inventoryHandler = inventoryHandler;
        this.auditLogHandler = auditLogHandler;
        this.paymentValidatorHandler = paymentValidatorHandler;
        this.notificationHandler = notificationHandler;
        this.productRecommendationHandler = productRecommendationHandler;
    }

    //REQUIRED : join an existing transaction or create a new one if not exist
    //REQUIRES_NEW : Always create a new transaction, suspending if any existing transaction
    //MANDATORY : Require an existing transaction, if nothing found it will throw exception
    //NEVER: Ensure the method will run without transaction, throw an exception if found any
    //NOT_SUPPORTED: Execute method without transaction, suspending any active transaction
    //SUPPORTS: Supports if there is an active transaction, if not executes without transaction
    // NESTED : Execute within a nested transaction, allowing nested transaction
    // to rollback independently if there is any exception without impacting outer transaction

    @Transactional(propagation = Propagation.REQUIRED)
    public Order placeAnOrder(Order order) {
        //get product inventory
        Product product = inventoryHandler.getProduct(order.getProductId());
        //validate stock availability < (5)
        validateStockAvailability(order, product);
        //update total price in order entity
        order.setTotalPrice(order.getQuantity()*product.getPrice());
        Order saveOrder = null;
        try {
            //save order
            saveOrder = orderHandler.saveOrder(order);
            //update stock in inventory
            updateInventoryStock(order, product);
            //log audit details with new transaction
            auditLogHandler.logAuditDetails(order, "Order Placement Successful");
        }catch (Exception ex) {
            //log audit details with new transaction
            auditLogHandler.logAuditDetails(order, "Order Placement Failed");
        }

//        notificationHandler.sendOrderConfirmationNotification(order);

        paymentValidatorHandler.validatePayment(order);

//        productRecommendationHandler.getRecommendations();

        getCustomerDetails(order);

        return saveOrder;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void getCustomerDetails(Order order){
        Product product = inventoryHandler.getProduct(order.getProductId());
        System.out.println("Customer Details Fetched !!!!"+product.toString());
    }


    // Call this method after placeAnOrder is successfully completed
    public void processOrder(Order order) {
        // Step 1: Place the order
        Order savedOrder = placeAnOrder(order);

        // Step 2: Send notification (non-transactional)
        notificationHandler.sendOrderConfirmationNotification(order);
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
