package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.MemberDocument;
import com.naline.coopfull.repository.MemberDocumentRepository;
import com.naline.coopfull.service.criteria.MemberDocumentCriteria;
import com.naline.coopfull.service.dto.MemberDocumentDTO;
import com.naline.coopfull.service.mapper.MemberDocumentMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MemberDocument} entities in the database.
 * The main input is a {@link MemberDocumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MemberDocumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MemberDocumentQueryService extends QueryService<MemberDocument> {

    private static final Logger LOG = LoggerFactory.getLogger(MemberDocumentQueryService.class);

    private final MemberDocumentRepository memberDocumentRepository;

    private final MemberDocumentMapper memberDocumentMapper;

    public MemberDocumentQueryService(MemberDocumentRepository memberDocumentRepository, MemberDocumentMapper memberDocumentMapper) {
        this.memberDocumentRepository = memberDocumentRepository;
        this.memberDocumentMapper = memberDocumentMapper;
    }

    /**
     * Return a {@link List} of {@link MemberDocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MemberDocumentDTO> findByCriteria(MemberDocumentCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<MemberDocument> specification = createSpecification(criteria);
        return memberDocumentMapper.toDto(memberDocumentRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MemberDocumentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MemberDocument> specification = createSpecification(criteria);
        return memberDocumentRepository.count(specification);
    }

    /**
     * Function to convert {@link MemberDocumentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MemberDocument> createSpecification(MemberDocumentCriteria criteria) {
        Specification<MemberDocument> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(MemberDocument_.member, JoinType.LEFT);
                root.fetch(MemberDocument_.uploadedBy, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), MemberDocument_.id),
                    buildSpecification(criteria.getDocumentType(), MemberDocument_.documentType),
                    buildStringSpecification(criteria.getOriginalFileName(), MemberDocument_.originalFileName),
                    buildStringSpecification(criteria.getStoredFileName(), MemberDocument_.storedFileName),
                    buildStringSpecification(criteria.getContentType(), MemberDocument_.contentType),
                    buildRangeSpecification(criteria.getFileSize(), MemberDocument_.fileSize),
                    buildStringSpecification(criteria.getStoragePath(), MemberDocument_.storagePath),
                    buildStringSpecification(criteria.getChecksum(), MemberDocument_.checksum),
                    buildSpecification(criteria.getVerificationStatus(), MemberDocument_.verificationStatus),
                    buildRangeSpecification(criteria.getUploadedAt(), MemberDocument_.uploadedAt),
                    buildRangeSpecification(criteria.getVerifiedAt(), MemberDocument_.verifiedAt),
                    buildSpecification(criteria.getMemberId(), root -> root.join(MemberDocument_.member, JoinType.LEFT).get(Member_.id)),
                    buildSpecification(criteria.getUploadedById(), root ->
                        root.join(MemberDocument_.uploadedBy, JoinType.LEFT).get(AppUser_.id)
                    )
                )
            );
        }
        return specification;
    }
}
