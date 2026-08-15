package com.naline.coopfull.repository;

import com.naline.coopfull.domain.AquacultureActivity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AquacultureActivity entity.
 */
@Repository
public interface AquacultureActivityRepository
    extends JpaRepository<AquacultureActivity, Long>, JpaSpecificationExecutor<AquacultureActivity>
{
    default Optional<AquacultureActivity> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AquacultureActivity> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AquacultureActivity> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select aquacultureActivity from AquacultureActivity aquacultureActivity left join fetch aquacultureActivity.location left join fetch aquacultureActivity.aquaticSpecies",
        countQuery = "select count(aquacultureActivity) from AquacultureActivity aquacultureActivity"
    )
    Page<AquacultureActivity> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select aquacultureActivity from AquacultureActivity aquacultureActivity left join fetch aquacultureActivity.location left join fetch aquacultureActivity.aquaticSpecies"
    )
    List<AquacultureActivity> findAllWithToOneRelationships();

    @Query(
        "select aquacultureActivity from AquacultureActivity aquacultureActivity left join fetch aquacultureActivity.location left join fetch aquacultureActivity.aquaticSpecies where aquacultureActivity.id =:id"
    )
    Optional<AquacultureActivity> findOneWithToOneRelationships(@Param("id") Long id);
}
