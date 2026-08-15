package com.naline.coopfull.repository;

import com.naline.coopfull.domain.MemberDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MemberDocument entity.
 */
@Repository
public interface MemberDocumentRepository extends JpaRepository<MemberDocument, Long>, JpaSpecificationExecutor<MemberDocument> {
    default Optional<MemberDocument> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MemberDocument> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MemberDocument> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select memberDocument from MemberDocument memberDocument left join fetch memberDocument.member",
        countQuery = "select count(memberDocument) from MemberDocument memberDocument"
    )
    Page<MemberDocument> findAllWithToOneRelationships(Pageable pageable);

    @Query("select memberDocument from MemberDocument memberDocument left join fetch memberDocument.member")
    List<MemberDocument> findAllWithToOneRelationships();

    @Query("select memberDocument from MemberDocument memberDocument left join fetch memberDocument.member where memberDocument.id =:id")
    Optional<MemberDocument> findOneWithToOneRelationships(@Param("id") Long id);
}
