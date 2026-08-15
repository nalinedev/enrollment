package com.naline.coopfull.repository;

import com.naline.coopfull.domain.ProfessionalProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProfessionalProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessionalProfileRepository
    extends JpaRepository<ProfessionalProfile, Long>, JpaSpecificationExecutor<ProfessionalProfile> {}
