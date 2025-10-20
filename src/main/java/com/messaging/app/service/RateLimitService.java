package com.messaging.app.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimitService {
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> lastRequestTimes = new ConcurrentHashMap<>();
    
    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final long MINUTE_IN_MILLIS = 60 * 1000;

    public boolean isAllowed(String clientIp) {
        long currentTime = System.currentTimeMillis();
        Long lastRequestTime = lastRequestTimes.get(clientIp);
        
        // Reset counter if more than a minute has passed
        if (lastRequestTime == null || currentTime - lastRequestTime > MINUTE_IN_MILLIS) {
            requestCounts.put(clientIp, new AtomicInteger(0));
            lastRequestTimes.put(clientIp, currentTime);
            return true;
        }
        
        // Check if under rate limit
        AtomicInteger count = requestCounts.get(clientIp);
        if (count != null && count.incrementAndGet() <= MAX_REQUESTS_PER_MINUTE) {
            return true;
        }
        
        return false;
    }

    public int getRemainingRequests(String clientIp) {
        AtomicInteger count = requestCounts.get(clientIp);
        if (count == null) {
            return MAX_REQUESTS_PER_MINUTE;
        }
        return Math.max(0, MAX_REQUESTS_PER_MINUTE - count.get());
    }
}