package com.alphadev.spring_transaction.handler;

import com.alphadev.spring_transaction.entity.AuditLog;
import com.alphadev.spring_transaction.entity.Order;
import com.alphadev.spring_transaction.repository.AuditLogRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditLogHandler {

    private AuditLogRepository auditLogRepository;

    public void logAuditDetails(Order order, String action) {
        AuditLog auditLog = new AuditLog();
        auditLog.setOrderId(Long.valueOf(order.getId()));
        auditLog.setAction(action);
        auditLog.setTimestamp(LocalDateTime.now());

        //save the audit log
        auditLogRepository.save(auditLog);

    }
}
