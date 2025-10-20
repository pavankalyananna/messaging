package com.messaging.app.controller;

import com.messaging.app.dto.ApiResponse;
import com.messaging.app.repository.ContactRequestRepository;
import com.messaging.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private ContactRequestRepository contactRequestRepository;

    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
    public ApiResponse getDashboardStats() {
        try {
            System.out.println("=== GETTING DASHBOARD STATS ===");
            
            Map<String, Object> stats = new HashMap<>();
            
            long totalRequests = contactRequestRepository.count();
            long pendingRequests = contactRequestRepository.countByStatus(com.messaging.app.entity.RequestStatus.PENDING);
            long completedRequests = contactRequestRepository.countByStatus(com.messaging.app.entity.RequestStatus.COMPLETED);
            long callRequests = contactRequestRepository.countByStatus(com.messaging.app.entity.RequestStatus.PENDING);
            
            System.out.println("Total: " + totalRequests + ", Pending: " + pendingRequests + ", Completed: " + completedRequests);
            
            stats.put("totalRequests", totalRequests);
            stats.put("pendingRequests", pendingRequests);
            stats.put("completedRequests", completedRequests);
            stats.put("adminStatus", adminService.getAdminStatus());
            stats.put("callRequests", callRequests);
            
            return new ApiResponse(true, "Dashboard stats retrieved", stats);
            
        } catch (Exception e) {
            System.err.println("ERROR getting dashboard stats: " + e.getMessage());
            e.printStackTrace();
            return new ApiResponse(false, "Failed to get dashboard stats: " + e.getMessage());
        }
    }
}