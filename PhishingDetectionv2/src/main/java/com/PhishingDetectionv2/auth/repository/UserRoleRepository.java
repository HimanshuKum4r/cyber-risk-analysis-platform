package com.PhishingDetectionv2.auth.repository;

import com.PhishingDetectionv2.auth.entity.User;
import com.PhishingDetectionv2.auth.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRoleRepository  extends JpaRepository<UserRole, UUID> {
    List<UserRole> findByUser(User user);
}
