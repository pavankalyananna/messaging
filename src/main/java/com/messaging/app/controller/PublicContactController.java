package com.messaging.app.controller;

import com.messaging.app.dto.ApiResponse;
import com.messaging.app.entity.ContactRequest;
import com.messaging.app.entity.RequestType;
import com.messaging.app.service.AdminService;
import com.messaging.app.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PublicContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public String contactPage(Model model) {
        boolean isOnline = adminService.isAdminOnline();
        System.out.println("Loading contact page - Admin online: " + isOnline);
        model.addAttribute("isAdminOnline", isOnline);
        return "contact";
    }

    @GetMapping("/contact")
    public String contactPageAlt(Model model) {
        return contactPage(model);
    }

    @PostMapping("/api/contact/call")
    @ResponseBody
    public ResponseEntity<ApiResponse> requestCall(@RequestParam String name) {
        try {
            System.out.println("=== CALL REQUEST RECEIVED ===");
            System.out.println("Name: " + name);
            
            if (name == null || name.trim().isEmpty()) {
                System.out.println("Name validation failed");
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Name is required"));
            }

            ContactRequest callRequest = contactService.createContactRequest(name, RequestType.CALL, null);
            System.out.println("Call request created successfully: " + callRequest.getId());
            
            return ResponseEntity.ok(new ApiResponse(true, "Call request sent", callRequest.getId()));
            
        } catch (Exception e) {
            System.err.println("ERROR in call request: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to send call request: " + e.getMessage()));
        }
    }

    @PostMapping("/api/contact/message")
    @ResponseBody
    public ResponseEntity<ApiResponse> sendMessage(@RequestParam String name, @RequestParam String message) {
        try {
            System.out.println("=== MESSAGE REQUEST RECEIVED ===");
            System.out.println("Name: " + name + ", Message: " + message);
            
            if (name == null || name.trim().isEmpty()) {
                System.out.println("Name validation failed");
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Name is required"));
            }

            if (message == null || message.trim().isEmpty()) {
                System.out.println("Message validation failed");
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Message is required"));
            }

            ContactRequest messageRequest = contactService.createContactRequest(name, RequestType.MESSAGE, message);
            System.out.println("Message request created successfully: " + messageRequest.getId());
            
            return ResponseEntity.ok(new ApiResponse(true, "Message sent", messageRequest.getId()));
            
        } catch (Exception e) {
            System.err.println("ERROR in message request: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to send message: " + e.getMessage()));
        }
    }

    @GetMapping("/api/contact/status")
    @ResponseBody
    public ResponseEntity<ApiResponse> checkAdminStatus() {
        try {
            boolean isOnline = adminService.isAdminOnline();
            System.out.println("Admin status check - Online: " + isOnline);
            return ResponseEntity.ok(new ApiResponse(true, "Status checked", isOnline));
        } catch (Exception e) {
            System.err.println("ERROR checking admin status: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ApiResponse(false, "Failed to check status: " + e.getMessage()));
        }
    }

    // Debug endpoints
    @RestController
    @RequestMapping("/api/debug")
    public static class DebugController {
        
        @Autowired
        private ContactService contactService;
        
        @Autowired
        private AdminService adminService;

        @GetMapping("/test-message")
        public String testMessage() {
            try {
                System.out.println("=== TEST MESSAGE ENDPOINT CALLED ===");
                ContactRequest request = contactService.createContactRequest("TestUser", RequestType.MESSAGE, "This is a test message");
                return "Test message sent - ID: " + request.getId();
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @GetMapping("/test-call")
        public String testCall() {
            try {
                System.out.println("=== TEST CALL ENDPOINT CALLED ===");
                ContactRequest request = contactService.createContactRequest("TestUser", RequestType.CALL, null);
                return "Test call sent - ID: " + request.getId();
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }

        @GetMapping("/admin-status")
        public String adminStatus() {
            boolean isOnline = adminService.isAdminOnline();
            return "Admin online: " + isOnline;
        }

        @GetMapping("/pending-requests")
        public String pendingRequests() {
            try {
                var requests = contactService.getPendingRequests();
                return "Pending requests: " + requests.size();
            } catch (Exception e) {
                return "Error: " + e.getMessage();
            }
        }
    }
}