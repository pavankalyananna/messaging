package com.messaging.app.repository;

import com.messaging.app.entity.ContactRequest;
import com.messaging.app.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRequestRepository extends JpaRepository<ContactRequest, String> {
    List<ContactRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);
    List<ContactRequest> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT COUNT(c) FROM ContactRequest c WHERE c.status = :status")
    long countByStatus(RequestStatus status);
    
    List<ContactRequest> findByTypeOrderByCreatedAtDesc(String type);
}