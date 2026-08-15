package com.naline.coopfull.repository;

import com.naline.coopfull.domain.AgriculturalProduction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AgriculturalProduction entity.
 */
@Repository
public interface AgriculturalProductionRepository
    extends JpaRepository<AgriculturalProduction, Long>, JpaSpecificationExecutor<AgriculturalProduction>
{
    default Optional<AgriculturalProduction> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AgriculturalProduction> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AgriculturalProduction> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select agriculturalProduction from AgriculturalProduction agriculturalProduction left join fetch agriculturalProduction.crop left join fetch agriculturalProduction.cropVariety",
        countQuery = "select count(agriculturalProduction) from AgriculturalProduction agriculturalProduction"
    )
    Page<AgriculturalProduction> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select agriculturalProduction from AgriculturalProduction agriculturalProduction left join fetch agriculturalProduction.crop left join fetch agriculturalProduction.cropVariety"
    )
    List<AgriculturalProduction> findAllWithToOneRelationships();

    @Query(
        "select agriculturalProduction from AgriculturalProduction agriculturalProduction left join fetch agriculturalProduction.crop left join fetch agriculturalProduction.cropVariety where agriculturalProduction.id =:id"
    )
    Optional<AgriculturalProduction> findOneWithToOneRelationships(@Param("id") Long id);
}
