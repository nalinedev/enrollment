package com.naline.coopfull.repository;

import com.naline.coopfull.domain.LivestockActivity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LivestockActivity entity.
 */
@Repository
public interface LivestockActivityRepository extends JpaRepository<LivestockActivity, Long>, JpaSpecificationExecutor<LivestockActivity> {
    default Optional<LivestockActivity> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LivestockActivity> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LivestockActivity> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select livestockActivity from LivestockActivity livestockActivity left join fetch livestockActivity.location left join fetch livestockActivity.livestockType",
        countQuery = "select count(livestockActivity) from LivestockActivity livestockActivity"
    )
    Page<LivestockActivity> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select livestockActivity from LivestockActivity livestockActivity left join fetch livestockActivity.location left join fetch livestockActivity.livestockType"
    )
    List<LivestockActivity> findAllWithToOneRelationships();

    @Query(
        "select livestockActivity from LivestockActivity livestockActivity left join fetch livestockActivity.location left join fetch livestockActivity.livestockType where livestockActivity.id =:id"
    )
    Optional<LivestockActivity> findOneWithToOneRelationships(@Param("id") Long id);
}
