package com.project.extension.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAttemptService {
    
    private final Map<String, AtomicInteger> attemptsCache = new ConcurrentHashMap<>();
    private final SecurityLogger securityLogger;
    private static final int MAX_ATTEMPTS = 5;
    
    public void loginSucceeded(String email) {
        attemptsCache.remove(email);
        securityLogger.logSecurityEvent("LOGIN_SUCCESS", "Email: " + email);
    }
    
    public void loginFailed(String email) {
        int attempts = attemptsCache.computeIfAbsent(email, k -> new AtomicInteger(0))
                .incrementAndGet();
        
        securityLogger.logSecurityEvent("LOGIN_FAILED", 
                String.format("Email: %s, Tentativa: %d/%d", email, attempts, MAX_ATTEMPTS));
        
        if (attempts >= MAX_ATTEMPTS) {
            securityLogger.logSecurityEvent("ACCOUNT_BLOCKED", "Email: " + email);
        }
    }
    
    public boolean isBlocked(String email) {
        return attemptsCache.getOrDefault(email, new AtomicInteger(0)).get() >= MAX_ATTEMPTS;
    }
    
    public int getAttemptCount(String email) {
        return attemptsCache.getOrDefault(email, new AtomicInteger(0)).get();
    }
    
    public void clearAttempts(String email) {
        attemptsCache.remove(email);
    }
}