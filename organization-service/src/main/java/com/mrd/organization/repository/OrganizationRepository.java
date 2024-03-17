package com.mrd.organization.repository;

import com.mrd.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization,Long> {
    Organization findByOrganizationCode(String code);

}
