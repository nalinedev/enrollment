package com.naline.coopfull.repository;

import com.naline.coopfull.domain.AgriculturalActivity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AgriculturalActivity entity.
 */
@Repository
public interface AgriculturalActivityRepository
    extends JpaRepository<AgriculturalActivity, Long>, JpaSpecificationExecutor<AgriculturalActivity>
{
    default Optional<AgriculturalActivity> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AgriculturalActivity> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AgriculturalActivity> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select agriculturalActivity from AgriculturalActivity agriculturalActivity left join fetch agriculturalActivity.location",
        countQuery = "select count(agriculturalActivity) from AgriculturalActivity agriculturalActivity"
    )
    Page<AgriculturalActivity> findAllWithToOneRelationships(Pageable pageable);

    @Query("select agriculturalActivity from AgriculturalActivity agriculturalActivity left join fetch agriculturalActivity.location")
    List<AgriculturalActivity> findAllWithToOneRelationships();

    @Query(
        "select agriculturalActivity from AgriculturalActivity agriculturalActivity left join fetch agriculturalActivity.location where agriculturalActivity.id =:id"
    )
    Optional<AgriculturalActivity> findOneWithToOneRelationships(@Param("id") Long id);
}
