package com.naline.coopfull.repository;

import com.naline.coopfull.domain.CooperativeRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CooperativeRole entity.
 *
 * When extending this class, extend CooperativeRoleRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CooperativeRoleRepository
    extends CooperativeRoleRepositoryWithBagRelationships, JpaRepository<CooperativeRole, Long>, JpaSpecificationExecutor<CooperativeRole>
{
    default Optional<CooperativeRole> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<CooperativeRole> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<CooperativeRole> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
