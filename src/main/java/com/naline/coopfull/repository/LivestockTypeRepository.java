package com.naline.coopfull.repository;

import com.naline.coopfull.domain.LivestockType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LivestockType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LivestockTypeRepository extends JpaRepository<LivestockType, Long>, JpaSpecificationExecutor<LivestockType> {}
