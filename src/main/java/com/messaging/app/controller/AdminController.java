package com.messaging.app.controller;

import com.messaging.app.dto.ApiResponse;
import com.messaging.app.entity.ContactRequest;
import com.messaging.app.service.ContactService;
import com.messaging.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private AdminService adminService;

    @GetMapping
    public String adminPanel(Model model) {
        System.out.println("=== LOADING ADMIN PANEL ===");
        
        List<ContactRequest> allRequests = contactService.getAllRequests();
        List<ContactRequest> pendingRequests = contactService.getPendingRequests();
        long pendingCount = contactService.getPendingCount();
        
        System.out.println("Total requests: " + allRequests.size());
        System.out.println("Pending requests: " + pendingRequests.size());
        System.out.println("Pending count: " + pendingCount);
        
        model.addAttribute("allRequests", allRequests);
        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("adminStatus", adminService.getAdminStatus());
        
        return "admin-panel";
    }

    @GetMapping("/requests")
    @ResponseBody
    public ResponseEntity<ApiResponse> getAllRequests() {
        try {
            System.out.println("=== GETTING ALL REQUESTS ===");
            List<ContactRequest> requests = contactService.getAllRequests();
            return ResponseEntity.ok(new ApiResponse(true, "Requests retrieved", requests));
        } catch (Exception e) {
            System.err.println("ERROR getting requests: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to get requests: " + e.getMessage()));
        }
    }

    @PostMapping("/request/{requestId}/accept")
    @ResponseBody
    public ResponseEntity<ApiResponse> acceptRequest(@PathVariable String requestId) {
        try {
            System.out.println("=== ACCEPTING REQUEST VIA API ===");
            System.out.println("Request ID: " + requestId);
            
            ContactRequest request = contactService.acceptRequest(requestId);
            if (request != null) {
                System.out.println("Request accepted successfully");
                return ResponseEntity.ok(new ApiResponse(true, "Request accepted", request));
            } else {
                System.out.println("Request not found");
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Request not found"));
            }
        } catch (Exception e) {
            System.err.println("ERROR accepting request: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to accept request: " + e.getMessage()));
        }
    }

    @PostMapping("/request/{requestId}/reject")
    @ResponseBody
    public ResponseEntity<ApiResponse> rejectRequest(@PathVariable String requestId, 
                                                   @RequestParam(required = false) String reason) {
        try {
            System.out.println("=== REJECTING REQUEST VIA API ===");
            System.out.println("Request ID: " + requestId + ", Reason: " + reason);
            
            ContactRequest request = contactService.rejectRequest(requestId, reason);
            if (request != null) {
                System.out.println("Request rejected successfully");
                return ResponseEntity.ok(new ApiResponse(true, "Request rejected", request));
            } else {
                System.out.println("Request not found");
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Request not found"));
            }
        } catch (Exception e) {
            System.err.println("ERROR rejecting request: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to reject request: " + e.getMessage()));
        }
    }

    @PostMapping("/request/{requestId}/complete")
    @ResponseBody
    public ResponseEntity<ApiResponse> completeRequest(@PathVariable String requestId) {
        try {
            System.out.println("=== COMPLETING REQUEST ===");
            contactService.completeRequest(requestId);
            return ResponseEntity.ok(new ApiResponse(true, "Request completed"));
        } catch (Exception e) {
            System.err.println("ERROR completing request: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to complete request: " + e.getMessage()));
        }
    }

    @PostMapping("/status/online")
    @ResponseBody
    public ResponseEntity<ApiResponse> setOnline() {
        try {
            // This will be called via WebSocket, but we keep this for REST API if needed
            return ResponseEntity.ok(new ApiResponse(true, "Use WebSocket for status updates"));
        } catch (Exception e) {
            System.err.println("ERROR setting online status: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to update status: " + e.getMessage()));
        }
    }
}