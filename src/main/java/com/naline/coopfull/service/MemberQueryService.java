package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.repository.MemberRepository;
import com.naline.coopfull.service.criteria.MemberCriteria;
import com.naline.coopfull.service.dto.MemberDTO;
import com.naline.coopfull.service.mapper.MemberMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Member} entities in the database.
 * The main input is a {@link MemberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MemberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MemberQueryService extends QueryService<Member> {

    private static final Logger LOG = LoggerFactory.getLogger(MemberQueryService.class);

    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;

    public MemberQueryService(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }

    /**
     * Return a {@link List} of {@link MemberDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MemberDTO> findByCriteria(MemberCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Member> specification = createSpecification(criteria);
        return memberMapper.toDto(memberRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MemberCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Member> specification = createSpecification(criteria);
        return memberRepository.count(specification);
    }

    /**
     * Function to convert {@link MemberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Member> createSpecification(MemberCriteria criteria) {
        Specification<Member> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Member_.individualMember, JoinType.LEFT);
                root.fetch(Member_.organizationMember, JoinType.LEFT);
                root.fetch(Member_.socialProfile, JoinType.LEFT);
                root.fetch(Member_.professionalProfile, JoinType.LEFT);
                root.fetch(Member_.cooperative, JoinType.LEFT);
                root.fetch(Member_.branch, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Member_.id),
                    buildStringSpecification(criteria.getMemberNumber(), Member_.memberNumber),
                    buildSpecification(criteria.getMemberType(), Member_.memberType),
                    buildSpecification(criteria.getStatus(), Member_.status),
                    buildRangeSpecification(criteria.getAdmissionDate(), Member_.admissionDate),
                    buildRangeSpecification(criteria.getExitDate(), Member_.exitDate),
                    buildStringSpecification(criteria.getExitReason(), Member_.exitReason),
                    buildRangeSpecification(criteria.getCreatedDate(), Member_.createdDate),
                    buildRangeSpecification(criteria.getLastModifiedDate(), Member_.lastModifiedDate),
                    buildSpecification(criteria.getIndividualMemberId(), root ->
                        root.join(Member_.individualMember, JoinType.LEFT).get(IndividualMember_.id)
                    ),
                    buildSpecification(criteria.getOrganizationMemberId(), root ->
                        root.join(Member_.organizationMember, JoinType.LEFT).get(OrganizationMember_.id)
                    ),
                    buildSpecification(criteria.getSocialProfileId(), root ->
                        root.join(Member_.socialProfile, JoinType.LEFT).get(SocialProfile_.id)
                    ),
                    buildSpecification(criteria.getProfessionalProfileId(), root ->
                        root.join(Member_.professionalProfile, JoinType.LEFT).get(ProfessionalProfile_.id)
                    ),
                    buildSpecification(criteria.getCooperativeId(), root ->
                        root.join(Member_.cooperative, JoinType.LEFT).get(Cooperative_.id)
                    ),
                    buildSpecification(criteria.getBranchId(), root -> root.join(Member_.branch, JoinType.LEFT).get(CooperativeBranch_.id))
                )
            );
        }
        return specification;
    }
}
