package com.naline.coopfull.repository;

import com.naline.coopfull.domain.CropVariety;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CropVariety entity.
 */
@Repository
public interface CropVarietyRepository extends JpaRepository<CropVariety, Long>, JpaSpecificationExecutor<CropVariety> {
    default Optional<CropVariety> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CropVariety> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CropVariety> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cropVariety from CropVariety cropVariety left join fetch cropVariety.crop",
        countQuery = "select count(cropVariety) from CropVariety cropVariety"
    )
    Page<CropVariety> findAllWithToOneRelationships(Pageable pageable);

    @Query("select cropVariety from CropVariety cropVariety left join fetch cropVariety.crop")
    List<CropVariety> findAllWithToOneRelationships();

    @Query("select cropVariety from CropVariety cropVariety left join fetch cropVariety.crop where cropVariety.id =:id")
    Optional<CropVariety> findOneWithToOneRelationships(@Param("id") Long id);
}
