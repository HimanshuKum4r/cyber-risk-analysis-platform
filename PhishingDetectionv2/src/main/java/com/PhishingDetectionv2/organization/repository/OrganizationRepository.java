package com.PhishingDetectionv2.organization.repository;

import com.PhishingDetectionv2.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    Optional<Organization> findByName(String name);

    boolean existsByName(String name);

}
