package com.PhishingDetectionv2.auth.config;

import com.PhishingDetectionv2.auth.entity.Permission;
import com.PhishingDetectionv2.auth.entity.Role;
import com.PhishingDetectionv2.auth.entity.RolePermission;
import com.PhishingDetectionv2.auth.repository.PermissionRepository;
import com.PhishingDetectionv2.auth.repository.RolePermissionRepository;
import com.PhishingDetectionv2.auth.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Transactional
public class SecurityDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    private static final Map<String, List<String>> ROLE_PERMISSIONS = Map.of(

            "OWNER", List.of("*"),

            "ADMIN", List.of(
                    "USER_CREATE",
                    "USER_READ",
                    "USER_UPDATE",
                    "ROLE_READ",
                    "ORGANIZATION_READ",
                    "ORGANIZATION_UPDATE",
                    "URL_SCAN",
                    "EMAIL_SCAN",
                    "REPORT_CREATE",
                    "REPORT_READ",
                    "API_KEY_CREATE",
                    "API_KEY_READ",
                    "API_KEY_REVOKE"
            ),

            "SECURITY_ANALYST", List.of(
                    "URL_SCAN",
                    "EMAIL_SCAN",
                    "REPORT_CREATE",
                    "REPORT_READ"
            ),

            "VIEWER", List.of(
                    "USER_READ",
                    "ROLE_READ",
                    "ORGANIZATION_READ",
                    "REPORT_READ",
                    "API_KEY_READ"
            )
    );

    @Override
    public void run(String... args) {

        seedRoles();

        seedPermissions();

        assignPermissionsToRoles();

    }

    private void seedRoles() {

        createRoleIfNotExists(
                "OWNER",
                "Owner of an individual workspace"
        );

        createRoleIfNotExists(
                "ADMIN",
                "Organization administrator"
        );

        createRoleIfNotExists(
                "SECURITY_ANALYST",
                "Can analyze phishing threats"
        );

        createRoleIfNotExists(
                "VIEWER",
                "Read only access"
        );

    }

    private void createRoleIfNotExists(
            String name,
            String description
    ) {

        if (roleRepository.existsByName(name)) {
            return;
        }

        Role role = Role.builder()
                .name(name)
                .description(description)
                .build();

        roleRepository.save(role);

    }


    private void seedPermissions() {

        // USER
        createPermissionIfNotExists("USER_CREATE", "USER", "CREATE", "Create users");
        createPermissionIfNotExists("USER_READ", "USER", "READ", "Read users");
        createPermissionIfNotExists("USER_UPDATE", "USER", "UPDATE", "Update users");
        createPermissionIfNotExists("USER_DELETE", "USER", "DELETE", "Delete users");

        // ROLE
        createPermissionIfNotExists("ROLE_CREATE", "ROLE", "CREATE", "Create roles");
        createPermissionIfNotExists("ROLE_READ", "ROLE", "READ", "Read roles");
        createPermissionIfNotExists("ROLE_UPDATE", "ROLE", "UPDATE", "Update roles");
        createPermissionIfNotExists("ROLE_DELETE", "ROLE", "DELETE", "Delete roles");

        // PERMISSION
        createPermissionIfNotExists("PERMISSION_READ", "PERMISSION", "READ", "Read permissions");
        createPermissionIfNotExists("PERMISSION_ASSIGN", "PERMISSION", "ASSIGN", "Assign permissions");

        // ORGANIZATION
        createPermissionIfNotExists("ORGANIZATION_CREATE", "ORGANIZATION", "CREATE", "Create organization");
        createPermissionIfNotExists("ORGANIZATION_READ", "ORGANIZATION", "READ", "Read organization");
        createPermissionIfNotExists("ORGANIZATION_UPDATE", "ORGANIZATION", "UPDATE", "Update organization");
        createPermissionIfNotExists("ORGANIZATION_DELETE", "ORGANIZATION", "DELETE", "Delete organization");

        // URL
        createPermissionIfNotExists("URL_SCAN", "URL", "SCAN", "Scan URLs");

        // EMAIL
        createPermissionIfNotExists("EMAIL_SCAN", "EMAIL", "SCAN", "Scan emails");

        // REPORT
        createPermissionIfNotExists("REPORT_CREATE", "REPORT", "CREATE", "Create reports");
        createPermissionIfNotExists("REPORT_READ", "REPORT", "READ", "Read reports");

        // API KEY
        createPermissionIfNotExists("API_KEY_CREATE", "API_KEY", "CREATE", "Create API Keys");
        createPermissionIfNotExists("API_KEY_READ", "API_KEY", "READ", "Read API Keys");
        createPermissionIfNotExists("API_KEY_REVOKE", "API_KEY", "REVOKE", "Revoke API Keys");

    }

    private void createPermissionIfNotExists(
            String name,
            String resource,
            String action,
            String description
    ) {

        if (permissionRepository.existsByName(name)) {
            return;
        }

        Permission permission = Permission.builder()
                .name(name)
                .resource(resource)
                .action(action)
                .description(description)
                .systemPermission(true)
                .build();

        permissionRepository.save(permission);

    }

    private void assignPermissionsToRoles() {

        ROLE_PERMISSIONS.forEach((roleName, permissions) -> {

            if (permissions.contains("*")) {

                permissionRepository.findAll()
                        .forEach(permission ->
                                assignPermission(roleName, permission.getName()));

                return;
            }

            permissions.forEach(permission ->
                    assignPermission(roleName, permission));

        });

    }

    private void assignPermission(
            String roleName,
            String permissionName
    ) {

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Role not found : " + roleName));

        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Permission not found : " + permissionName));

        if (rolePermissionRepository.existsByRoleAndPermission(role, permission)) {
            return;
        }

        RolePermission rolePermission = RolePermission.builder()
                .role(role)
                .permission(permission)
                .build();

        rolePermissionRepository.save(rolePermission);

    }

}