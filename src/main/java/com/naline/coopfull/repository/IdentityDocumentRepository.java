package com.naline.coopfull.repository;

import com.naline.coopfull.domain.IdentityDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IdentityDocument entity.
 */
@Repository
public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, Long>, JpaSpecificationExecutor<IdentityDocument> {
    default Optional<IdentityDocument> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<IdentityDocument> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<IdentityDocument> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select identityDocument from IdentityDocument identityDocument left join fetch identityDocument.member",
        countQuery = "select count(identityDocument) from IdentityDocument identityDocument"
    )
    Page<IdentityDocument> findAllWithToOneRelationships(Pageable pageable);

    @Query("select identityDocument from IdentityDocument identityDocument left join fetch identityDocument.member")
    List<IdentityDocument> findAllWithToOneRelationships();

    @Query(
        "select identityDocument from IdentityDocument identityDocument left join fetch identityDocument.member where identityDocument.id =:id"
    )
    Optional<IdentityDocument> findOneWithToOneRelationships(@Param("id") Long id);
}
