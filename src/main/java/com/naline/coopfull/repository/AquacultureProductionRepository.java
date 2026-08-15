package com.naline.coopfull.repository;

import com.naline.coopfull.domain.AquacultureProduction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AquacultureProduction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AquacultureProductionRepository
    extends JpaRepository<AquacultureProduction, Long>, JpaSpecificationExecutor<AquacultureProduction> {}
