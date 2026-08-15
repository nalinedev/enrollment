package com.naline.coopfull.repository;

import com.naline.coopfull.domain.EconomicActivityType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EconomicActivityType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EconomicActivityTypeRepository
    extends JpaRepository<EconomicActivityType, Long>, JpaSpecificationExecutor<EconomicActivityType> {}
