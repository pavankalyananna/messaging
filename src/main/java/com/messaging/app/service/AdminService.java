package com.messaging.app.service;

import com.messaging.app.entity.AdminUser;
import com.messaging.app.entity.AdminStatus;
import com.messaging.app.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    public void setAdminOnline(String sessionId) {
        Optional<AdminUser> adminOpt = adminUserRepository.findById("admin");
        AdminUser admin = adminOpt.orElseGet(() -> {
            AdminUser newAdmin = new AdminUser();
            newAdmin.setId("admin");
            return newAdmin;
        });
        admin.setStatus(AdminStatus.ONLINE);
        admin.setLastOnline(LocalDateTime.now());
        admin.setCurrentSessionId(sessionId);
        adminUserRepository.save(admin);
    }

    public void setAdminOffline() {
        adminUserRepository.findById("admin").ifPresent(admin -> {
            admin.setStatus(AdminStatus.OFFLINE);
            admin.setLastOnline(LocalDateTime.now());
            adminUserRepository.save(admin);
        });
    }

    public void setAdminBusy() {
        adminUserRepository.findById("admin").ifPresent(admin -> {
            admin.setStatus(AdminStatus.BUSY);
            admin.setLastOnline(LocalDateTime.now());
            adminUserRepository.save(admin);
        });
    }

    public boolean isAdminOnline() {
        return adminUserRepository.findById("admin")
                .map(admin -> admin.getStatus() == AdminStatus.ONLINE)
                .orElse(false);
    }

    public AdminUser getAdminStatus() {
        return adminUserRepository.findById("admin").orElse(new AdminUser());
    }
}