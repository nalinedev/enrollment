package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.IndividualMember;
import com.naline.coopfull.repository.IndividualMemberRepository;
import com.naline.coopfull.service.criteria.IndividualMemberCriteria;
import com.naline.coopfull.service.dto.IndividualMemberDTO;
import com.naline.coopfull.service.mapper.IndividualMemberMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link IndividualMember} entities in the database.
 * The main input is a {@link IndividualMemberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IndividualMemberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IndividualMemberQueryService extends QueryService<IndividualMember> {

    private static final Logger LOG = LoggerFactory.getLogger(IndividualMemberQueryService.class);

    private final IndividualMemberRepository individualMemberRepository;

    private final IndividualMemberMapper individualMemberMapper;

    public IndividualMemberQueryService(
        IndividualMemberRepository individualMemberRepository,
        IndividualMemberMapper individualMemberMapper
    ) {
        this.individualMemberRepository = individualMemberRepository;
        this.individualMemberMapper = individualMemberMapper;
    }

    /**
     * Return a {@link List} of {@link IndividualMemberDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IndividualMemberDTO> findByCriteria(IndividualMemberCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<IndividualMember> specification = createSpecification(criteria);
        return individualMemberMapper.toDto(individualMemberRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IndividualMemberCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<IndividualMember> specification = createSpecification(criteria);
        return individualMemberRepository.count(specification);
    }

    /**
     * Function to convert {@link IndividualMemberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IndividualMember> createSpecification(IndividualMemberCriteria criteria) {
        Specification<IndividualMember> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), IndividualMember_.id),
                    buildStringSpecification(criteria.getFirstName(), IndividualMember_.firstName),
                    buildStringSpecification(criteria.getMiddleName(), IndividualMember_.middleName),
                    buildStringSpecification(criteria.getLastName(), IndividualMember_.lastName),
                    buildStringSpecification(criteria.getMaidenName(), IndividualMember_.maidenName),
                    buildStringSpecification(criteria.getGender(), IndividualMember_.gender),
                    buildRangeSpecification(criteria.getBirthDate(), IndividualMember_.birthDate),
                    buildStringSpecification(criteria.getBirthPlace(), IndividualMember_.birthPlace),
                    buildStringSpecification(criteria.getNationality(), IndividualMember_.nationality),
                    buildStringSpecification(criteria.getEmail(), IndividualMember_.email),
                    buildStringSpecification(criteria.getPhoneNumber(), IndividualMember_.phoneNumber),
                    buildStringSpecification(criteria.getOccupation(), IndividualMember_.occupation),
                    buildSpecification(criteria.getMemberId(), root -> root.join(IndividualMember_.member, JoinType.LEFT).get(Member_.id))
                )
            );
        }
        return specification;
    }
}
