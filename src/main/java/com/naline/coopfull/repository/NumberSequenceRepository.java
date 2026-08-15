package com.naline.coopfull.repository;

import com.naline.coopfull.domain.NumberSequence;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the NumberSequence entity.
 */
@Repository
public interface NumberSequenceRepository extends JpaRepository<NumberSequence, Long>, JpaSpecificationExecutor<NumberSequence> {
    default Optional<NumberSequence> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<NumberSequence> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<NumberSequence> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select numberSequence from NumberSequence numberSequence left join fetch numberSequence.cooperative",
        countQuery = "select count(numberSequence) from NumberSequence numberSequence"
    )
    Page<NumberSequence> findAllWithToOneRelationships(Pageable pageable);

    @Query("select numberSequence from NumberSequence numberSequence left join fetch numberSequence.cooperative")
    List<NumberSequence> findAllWithToOneRelationships();

    @Query(
        "select numberSequence from NumberSequence numberSequence left join fetch numberSequence.cooperative where numberSequence.id =:id"
    )
    Optional<NumberSequence> findOneWithToOneRelationships(@Param("id") Long id);
}
