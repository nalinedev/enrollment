package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.IdentityDocument;
import com.naline.coopfull.repository.IdentityDocumentRepository;
import com.naline.coopfull.service.criteria.IdentityDocumentCriteria;
import com.naline.coopfull.service.dto.IdentityDocumentDTO;
import com.naline.coopfull.service.mapper.IdentityDocumentMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IdentityDocument} entities in the database.
 * The main input is a {@link IdentityDocumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IdentityDocumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IdentityDocumentQueryService extends QueryService<IdentityDocument> {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityDocumentQueryService.class);

    private final IdentityDocumentRepository identityDocumentRepository;

    private final IdentityDocumentMapper identityDocumentMapper;

    public IdentityDocumentQueryService(
        IdentityDocumentRepository identityDocumentRepository,
        IdentityDocumentMapper identityDocumentMapper
    ) {
        this.identityDocumentRepository = identityDocumentRepository;
        this.identityDocumentMapper = identityDocumentMapper;
    }

    /**
     * Return a {@link List} of {@link IdentityDocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IdentityDocumentDTO> findByCriteria(IdentityDocumentCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<IdentityDocument> specification = createSpecification(criteria);
        return identityDocumentMapper.toDto(identityDocumentRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IdentityDocumentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IdentityDocument> specification = createSpecification(criteria);
        return identityDocumentRepository.count(specification);
    }

    /**
     * Function to convert {@link IdentityDocumentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IdentityDocument> createSpecification(IdentityDocumentCriteria criteria) {
        Specification<IdentityDocument> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(IdentityDocument_.member, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), IdentityDocument_.id),
                    buildStringSpecification(criteria.getDocumentType(), IdentityDocument_.documentType),
                    buildStringSpecification(criteria.getDocumentNumber(), IdentityDocument_.documentNumber),
                    buildRangeSpecification(criteria.getIssueDate(), IdentityDocument_.issueDate),
                    buildRangeSpecification(criteria.getExpiryDate(), IdentityDocument_.expiryDate),
                    buildStringSpecification(criteria.getIssuingAuthority(), IdentityDocument_.issuingAuthority),
                    buildStringSpecification(criteria.getIssuingCountry(), IdentityDocument_.issuingCountry),
                    buildSpecification(criteria.getStatus(), IdentityDocument_.status),
                    buildSpecification(criteria.getVerified(), IdentityDocument_.verified),
                    buildRangeSpecification(criteria.getVerificationDate(), IdentityDocument_.verificationDate),
                    buildStringSpecification(criteria.getVerificationComment(), IdentityDocument_.verificationComment),
                    buildSpecification(criteria.getMemberId(), root -> root.join(IdentityDocument_.member, JoinType.LEFT).get(Member_.id))
                )
            );
        }
        return specification;
    }
}
