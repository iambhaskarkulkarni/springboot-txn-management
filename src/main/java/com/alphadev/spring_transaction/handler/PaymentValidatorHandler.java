package com.alphadev.spring_transaction.handler;

import com.alphadev.spring_transaction.entity.AuditLog;
import com.alphadev.spring_transaction.entity.Order;
import com.alphadev.spring_transaction.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentValidatorHandler {

    private final AuditLogRepository auditLogRepository;

    public PaymentValidatorHandler(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(propagation = Propagation.NESTED)
    public void validatePayment(Order order) {
        //Assume payment processing happens here
        boolean paymentSuccessful = false;

        //If payment is unsuccessful, we log payment failure in the mandatory transaction
        if(!paymentSuccessful) {
            AuditLog paymentFailureLog = new AuditLog();
            paymentFailureLog.setOrderId(Long.valueOf(order.getId()));
            paymentFailureLog.setAction("Payment failed for order");
            paymentFailureLog.setTimestamp(LocalDateTime.now());

            if(order.getTotalPrice()>1000) {
                throw new RuntimeException("Error in payment validator");
            }
            //Save the payment failure log
            auditLogRepository.save(paymentFailureLog);
        }
    }
}
