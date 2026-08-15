package com.naline.coopfull.repository;

import com.naline.coopfull.domain.CooperativeBranch;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CooperativeBranch entity.
 */
@Repository
public interface CooperativeBranchRepository extends JpaRepository<CooperativeBranch, Long>, JpaSpecificationExecutor<CooperativeBranch> {
    default Optional<CooperativeBranch> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CooperativeBranch> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CooperativeBranch> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cooperativeBranch from CooperativeBranch cooperativeBranch left join fetch cooperativeBranch.cooperative left join fetch cooperativeBranch.location",
        countQuery = "select count(cooperativeBranch) from CooperativeBranch cooperativeBranch"
    )
    Page<CooperativeBranch> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select cooperativeBranch from CooperativeBranch cooperativeBranch left join fetch cooperativeBranch.cooperative left join fetch cooperativeBranch.location"
    )
    List<CooperativeBranch> findAllWithToOneRelationships();

    @Query(
        "select cooperativeBranch from CooperativeBranch cooperativeBranch left join fetch cooperativeBranch.cooperative left join fetch cooperativeBranch.location where cooperativeBranch.id =:id"
    )
    Optional<CooperativeBranch> findOneWithToOneRelationships(@Param("id") Long id);
}
