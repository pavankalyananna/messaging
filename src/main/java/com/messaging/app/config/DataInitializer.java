package com.messaging.app.config;

import com.messaging.app.entity.AdminUser;
import com.messaging.app.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize admin user if not exists
        if (adminUserRepository.findById("admin").isEmpty()) {
            AdminUser admin = new AdminUser();
            admin.setId("admin");
            adminUserRepository.save(admin);
            System.out.println("Admin user initialized");
        }
    }
}