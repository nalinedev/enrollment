package com.naline.coopfull.repository;

import com.naline.coopfull.domain.IndividualMember;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IndividualMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndividualMemberRepository extends JpaRepository<IndividualMember, Long>, JpaSpecificationExecutor<IndividualMember> {}
