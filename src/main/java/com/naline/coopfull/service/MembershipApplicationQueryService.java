package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.MembershipApplication;
import com.naline.coopfull.repository.MembershipApplicationRepository;
import com.naline.coopfull.service.criteria.MembershipApplicationCriteria;
import com.naline.coopfull.service.dto.MembershipApplicationDTO;
import com.naline.coopfull.service.mapper.MembershipApplicationMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link MembershipApplication} entities in the database.
 * The main input is a {@link MembershipApplicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MembershipApplicationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MembershipApplicationQueryService extends QueryService<MembershipApplication> {

    private static final Logger LOG = LoggerFactory.getLogger(MembershipApplicationQueryService.class);

    private final MembershipApplicationRepository membershipApplicationRepository;

    private final MembershipApplicationMapper membershipApplicationMapper;

    public MembershipApplicationQueryService(
        MembershipApplicationRepository membershipApplicationRepository,
        MembershipApplicationMapper membershipApplicationMapper
    ) {
        this.membershipApplicationRepository = membershipApplicationRepository;
        this.membershipApplicationMapper = membershipApplicationMapper;
    }

    /**
     * Return a {@link List} of {@link MembershipApplicationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MembershipApplicationDTO> findByCriteria(MembershipApplicationCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<MembershipApplication> specification = createSpecification(criteria);
        return membershipApplicationMapper.toDto(membershipApplicationRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MembershipApplicationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<MembershipApplication> specification = createSpecification(criteria);
        return membershipApplicationRepository.count(specification);
    }

    /**
     * Function to convert {@link MembershipApplicationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MembershipApplication> createSpecification(MembershipApplicationCriteria criteria) {
        Specification<MembershipApplication> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(MembershipApplication_.member, JoinType.LEFT);
                root.fetch(MembershipApplication_.cooperative, JoinType.LEFT);
                root.fetch(MembershipApplication_.branch, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), MembershipApplication_.id),
                    buildStringSpecification(criteria.getApplicationNumber(), MembershipApplication_.applicationNumber),
                    buildSpecification(criteria.getStatus(), MembershipApplication_.status),
                    buildRangeSpecification(criteria.getApplicationDate(), MembershipApplication_.applicationDate),
                    buildRangeSpecification(criteria.getSubmittedAt(), MembershipApplication_.submittedAt),
                    buildRangeSpecification(criteria.getReviewedAt(), MembershipApplication_.reviewedAt),
                    buildRangeSpecification(criteria.getApprovedAt(), MembershipApplication_.approvedAt),
                    buildRangeSpecification(criteria.getRejectedAt(), MembershipApplication_.rejectedAt),
                    buildSpecification(criteria.getConfirmation(), MembershipApplication_.confirmation),
                    buildSpecification(criteria.getMemberId(), root ->
                        root.join(MembershipApplication_.member, JoinType.LEFT).get(Member_.id)
                    ),
                    buildSpecification(criteria.getCooperativeId(), root ->
                        root.join(MembershipApplication_.cooperative, JoinType.LEFT).get(Cooperative_.id)
                    ),
                    buildSpecification(criteria.getBranchId(), root ->
                        root.join(MembershipApplication_.branch, JoinType.LEFT).get(CooperativeBranch_.id)
                    )
                )
            );
        }
        return specification;
    }
}
