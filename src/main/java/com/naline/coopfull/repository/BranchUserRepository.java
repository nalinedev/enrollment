package com.naline.coopfull.repository;

import com.naline.coopfull.domain.BranchUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BranchUser entity.
 */
@Repository
public interface BranchUserRepository extends JpaRepository<BranchUser, Long>, JpaSpecificationExecutor<BranchUser> {
    default Optional<BranchUser> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<BranchUser> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<BranchUser> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select branchUser from BranchUser branchUser left join fetch branchUser.branch left join fetch branchUser.role",
        countQuery = "select count(branchUser) from BranchUser branchUser"
    )
    Page<BranchUser> findAllWithToOneRelationships(Pageable pageable);

    @Query("select branchUser from BranchUser branchUser left join fetch branchUser.branch left join fetch branchUser.role")
    List<BranchUser> findAllWithToOneRelationships();

    @Query(
        "select branchUser from BranchUser branchUser left join fetch branchUser.branch left join fetch branchUser.role where branchUser.id =:id"
    )
    Optional<BranchUser> findOneWithToOneRelationships(@Param("id") Long id);
}
