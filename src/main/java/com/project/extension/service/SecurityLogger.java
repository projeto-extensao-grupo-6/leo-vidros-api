package com.project.extension.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SecurityLogger {
    
    public void logLoginAttempt(String email, String ip, boolean success) {
        if (success) {
            log.info("Login successful for user: {}, IP: {}", email, ip);
        } else {
            log.warn("Login failed for user: {}, IP: {}", email, ip);
        }
    }
    
    public void logUnauthorizedAccess(String email, String resource, String ip) {
        log.warn("Unauthorized access attempt - User: {}, Resource: {}, IP: {}", 
                email, resource, ip);
    }
    
    public void logDataAccess(String user, String entity, Integer id) {
        log.info("Data access - User: {}, Entity: {}, ID: {}", user, entity, id);
    }
    
    public void logDataModification(String user, String entity, String operation) {
        log.info("Data modification - User: {}, Entity: {}, Operation: {}", 
                user, entity, operation);
    }
    
    public void logSecurityEvent(String event, String details) {
        log.warn("Security event: {} - Details: {}", event, details);
    }
}