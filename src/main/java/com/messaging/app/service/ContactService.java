package com.messaging.app.service;

import com.messaging.app.entity.ContactRequest;
import com.messaging.app.entity.RequestStatus;
import com.messaging.app.entity.RequestType;
import com.messaging.app.repository.ContactRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    @Autowired
    private ContactRequestRepository contactRequestRepository;
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private WebSocketService webSocketService;

    public ContactRequest createContactRequest(String visitorName, RequestType type, String message) {
        try {
            System.out.println("=== CREATING CONTACT REQUEST ===");
            System.out.println("Name: " + visitorName + ", Type: " + type + ", Message: " + message);
            
            ContactRequest request = new ContactRequest();
            request.setVisitorName(visitorName);
            request.setType(type);
            request.setMessageContent(message);
            
            boolean isAdminOnline = adminService.isAdminOnline();
            System.out.println("Admin online status: " + isAdminOnline);
            
            if (isAdminOnline) {
                request.setStatus(RequestStatus.PENDING);
                System.out.println("Setting status to PENDING");
            } else {
                request.setStatus(RequestStatus.OFFLINE);
                System.out.println("Setting status to OFFLINE");
            }
            
            ContactRequest savedRequest = contactRequestRepository.save(request);
            System.out.println("Request saved with ID: " + savedRequest.getId());
            
            // Notify admin if online
            if (isAdminOnline) {
                if (type == RequestType.CALL) {
                    System.out.println("Sending call notification to admin");
                    webSocketService.notifyCallRequestToAdmin(visitorName);
                } else {
                    System.out.println("Sending message notification to admin");
                    webSocketService.notifyNewRequestToAdmin(visitorName, message);
                }
            } else {
                System.out.println("Admin is offline - no notification sent");
            }
            
            return savedRequest;
            
        } catch (Exception e) {
            System.err.println("ERROR creating contact request: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<ContactRequest> getRequest(String requestId) {
        return contactRequestRepository.findById(requestId);
    }

    public List<ContactRequest> getPendingRequests() {
        List<ContactRequest> pending = contactRequestRepository.findByStatusOrderByCreatedAtDesc(RequestStatus.PENDING);
        System.out.println("Found " + pending.size() + " pending requests");
        return pending;
    }

    public List<ContactRequest> getAllRequests() {
        List<ContactRequest> all = contactRequestRepository.findAllByOrderByCreatedAtDesc();
        System.out.println("Found " + all.size() + " total requests");
        return all;
    }

    public ContactRequest acceptRequest(String requestId) {
        try {
            System.out.println("=== ACCEPTING REQUEST ===");
            System.out.println("Request ID: " + requestId);
            
            return contactRequestRepository.findById(requestId).map(request -> {
                request.setStatus(RequestStatus.ACCEPTED);
                request.setRespondedAt(LocalDateTime.now());
                ContactRequest updated = contactRequestRepository.save(request);
                
                System.out.println("Request accepted: " + updated.getId());
                
                // Notify the visitor that their request was accepted
                if (updated.getType() == RequestType.CALL) {
                    System.out.println("Sending call accepted notification");
                    webSocketService.notifyCallAccepted(updated.getVisitorName(), requestId);
                }
                
                return updated;
            }).orElse(null);
            
        } catch (Exception e) {
            System.err.println("ERROR accepting request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public ContactRequest rejectRequest(String requestId, String reason) {
        try {
            System.out.println("=== REJECTING REQUEST ===");
            System.out.println("Request ID: " + requestId + ", Reason: " + reason);
            
            return contactRequestRepository.findById(requestId).map(request -> {
                request.setStatus(RequestStatus.REJECTED);
                request.setRespondedAt(LocalDateTime.now());
                request.setAdminResponse(reason);
                ContactRequest updated = contactRequestRepository.save(request);
                
                System.out.println("Request rejected: " + updated.getId());
                
                // Notify the visitor
                if (updated.getType() == RequestType.CALL) {
                    System.out.println("Sending call rejected notification");
                    webSocketService.notifyCallRejected(updated.getVisitorName(), requestId, reason);
                }
                
                return updated;
            }).orElse(null);
            
        } catch (Exception e) {
            System.err.println("ERROR rejecting request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void completeRequest(String requestId) {
        try {
            contactRequestRepository.findById(requestId).ifPresent(request -> {
                request.setStatus(RequestStatus.COMPLETED);
                contactRequestRepository.save(request);
                System.out.println("Request completed: " + requestId);
            });
        } catch (Exception e) {
            System.err.println("ERROR completing request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public long getPendingCount() {
        long count = contactRequestRepository.countByStatus(RequestStatus.PENDING);
        System.out.println("Pending count: " + count);
        return count;
    }
}