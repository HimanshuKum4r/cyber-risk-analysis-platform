package com.PhishingDetectionv2.auth.repository;

import com.PhishingDetectionv2.auth.entity.Role;
import com.PhishingDetectionv2.auth.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public  interface   RolePermissionRepository extends JpaRepository<RolePermission, UUID> {
    List<RolePermission> findByRole(Role role);

}
