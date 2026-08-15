package com.naline.coopfull.repository;

import com.naline.coopfull.domain.AquaticSpecies;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AquaticSpecies entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AquaticSpeciesRepository extends JpaRepository<AquaticSpecies, Long>, JpaSpecificationExecutor<AquaticSpecies> {}
