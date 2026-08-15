package com.naline.coopfull.repository;

import com.naline.coopfull.domain.EconomicActivity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EconomicActivity entity.
 */
@Repository
public interface EconomicActivityRepository extends JpaRepository<EconomicActivity, Long>, JpaSpecificationExecutor<EconomicActivity> {
    default Optional<EconomicActivity> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<EconomicActivity> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<EconomicActivity> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select economicActivity from EconomicActivity economicActivity left join fetch economicActivity.member left join fetch economicActivity.activityType left join fetch economicActivity.location",
        countQuery = "select count(economicActivity) from EconomicActivity economicActivity"
    )
    Page<EconomicActivity> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select economicActivity from EconomicActivity economicActivity left join fetch economicActivity.member left join fetch economicActivity.activityType left join fetch economicActivity.location"
    )
    List<EconomicActivity> findAllWithToOneRelationships();

    @Query(
        "select economicActivity from EconomicActivity economicActivity left join fetch economicActivity.member left join fetch economicActivity.activityType left join fetch economicActivity.location where economicActivity.id =:id"
    )
    Optional<EconomicActivity> findOneWithToOneRelationships(@Param("id") Long id);
}
