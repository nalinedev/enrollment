package com.naline.coopfull.repository;

import com.naline.coopfull.domain.LivestockProduction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LivestockProduction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LivestockProductionRepository
    extends JpaRepository<LivestockProduction, Long>, JpaSpecificationExecutor<LivestockProduction> {}
