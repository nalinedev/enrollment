package com.naline.coopfull.repository;

import com.naline.coopfull.domain.Crop;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Crop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CropRepository extends JpaRepository<Crop, Long>, JpaSpecificationExecutor<Crop> {}
