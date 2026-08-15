package com.naline.coopfull.repository;

import com.naline.coopfull.domain.CooperativeUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CooperativeUser entity.
 */
@Repository
public interface CooperativeUserRepository extends JpaRepository<CooperativeUser, Long>, JpaSpecificationExecutor<CooperativeUser> {
    default Optional<CooperativeUser> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CooperativeUser> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CooperativeUser> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cooperativeUser from CooperativeUser cooperativeUser left join fetch cooperativeUser.cooperative left join fetch cooperativeUser.role",
        countQuery = "select count(cooperativeUser) from CooperativeUser cooperativeUser"
    )
    Page<CooperativeUser> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select cooperativeUser from CooperativeUser cooperativeUser left join fetch cooperativeUser.cooperative left join fetch cooperativeUser.role"
    )
    List<CooperativeUser> findAllWithToOneRelationships();

    @Query(
        "select cooperativeUser from CooperativeUser cooperativeUser left join fetch cooperativeUser.cooperative left join fetch cooperativeUser.role where cooperativeUser.id =:id"
    )
    Optional<CooperativeUser> findOneWithToOneRelationships(@Param("id") Long id);
}
