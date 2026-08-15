package com.naline.coopfull.repository;

import com.naline.coopfull.domain.MembershipApplication;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MembershipApplication entity.
 */
@Repository
public interface MembershipApplicationRepository
    extends JpaRepository<MembershipApplication, Long>, JpaSpecificationExecutor<MembershipApplication>
{
    default Optional<MembershipApplication> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MembershipApplication> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MembershipApplication> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select membershipApplication from MembershipApplication membershipApplication left join fetch membershipApplication.member left join fetch membershipApplication.cooperative left join fetch membershipApplication.branch",
        countQuery = "select count(membershipApplication) from MembershipApplication membershipApplication"
    )
    Page<MembershipApplication> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select membershipApplication from MembershipApplication membershipApplication left join fetch membershipApplication.member left join fetch membershipApplication.cooperative left join fetch membershipApplication.branch"
    )
    List<MembershipApplication> findAllWithToOneRelationships();

    @Query(
        "select membershipApplication from MembershipApplication membershipApplication left join fetch membershipApplication.member left join fetch membershipApplication.cooperative left join fetch membershipApplication.branch where membershipApplication.id =:id"
    )
    Optional<MembershipApplication> findOneWithToOneRelationships(@Param("id") Long id);
}
