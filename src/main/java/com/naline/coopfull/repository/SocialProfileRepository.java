package com.naline.coopfull.repository;

import com.naline.coopfull.domain.SocialProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SocialProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialProfileRepository extends JpaRepository<SocialProfile, Long>, JpaSpecificationExecutor<SocialProfile> {}
