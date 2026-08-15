package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.FamilyMember;
import com.naline.coopfull.repository.FamilyMemberRepository;
import com.naline.coopfull.service.criteria.FamilyMemberCriteria;
import com.naline.coopfull.service.dto.FamilyMemberDTO;
import com.naline.coopfull.service.mapper.FamilyMemberMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FamilyMember} entities in the database.
 * The main input is a {@link FamilyMemberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FamilyMemberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FamilyMemberQueryService extends QueryService<FamilyMember> {

    private static final Logger LOG = LoggerFactory.getLogger(FamilyMemberQueryService.class);

    private final FamilyMemberRepository familyMemberRepository;

    private final FamilyMemberMapper familyMemberMapper;

    public FamilyMemberQueryService(FamilyMemberRepository familyMemberRepository, FamilyMemberMapper familyMemberMapper) {
        this.familyMemberRepository = familyMemberRepository;
        this.familyMemberMapper = familyMemberMapper;
    }

    /**
     * Return a {@link List} of {@link FamilyMemberDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FamilyMemberDTO> findByCriteria(FamilyMemberCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<FamilyMember> specification = createSpecification(criteria);
        return familyMemberMapper.toDto(familyMemberRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FamilyMemberCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<FamilyMember> specification = createSpecification(criteria);
        return familyMemberRepository.count(specification);
    }

    /**
     * Function to convert {@link FamilyMemberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FamilyMember> createSpecification(FamilyMemberCriteria criteria) {
        Specification<FamilyMember> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(FamilyMember_.member, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), FamilyMember_.id),
                    buildStringSpecification(criteria.getFirstName(), FamilyMember_.firstName),
                    buildStringSpecification(criteria.getMiddleName(), FamilyMember_.middleName),
                    buildStringSpecification(criteria.getLastName(), FamilyMember_.lastName),
                    buildStringSpecification(criteria.getRelationship(), FamilyMember_.relationship),
                    buildStringSpecification(criteria.getGender(), FamilyMember_.gender),
                    buildRangeSpecification(criteria.getBirthDate(), FamilyMember_.birthDate),
                    buildStringSpecification(criteria.getBirthPlace(), FamilyMember_.birthPlace),
                    buildStringSpecification(criteria.getNationality(), FamilyMember_.nationality),
                    buildStringSpecification(criteria.getPhoneNumber(), FamilyMember_.phoneNumber),
                    buildStringSpecification(criteria.getOccupation(), FamilyMember_.occupation),
                    buildSpecification(criteria.getDependent(), FamilyMember_.dependent),
                    buildSpecification(criteria.getMemberId(), root -> root.join(FamilyMember_.member, JoinType.LEFT).get(Member_.id))
                )
            );
        }
        return specification;
    }
}
