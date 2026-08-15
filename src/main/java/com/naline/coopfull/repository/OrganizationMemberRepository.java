package com.naline.coopfull.repository;

import com.naline.coopfull.domain.OrganizationMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrganizationMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationMemberRepository
    extends JpaRepository<OrganizationMember, Long>, JpaSpecificationExecutor<OrganizationMember> {}
