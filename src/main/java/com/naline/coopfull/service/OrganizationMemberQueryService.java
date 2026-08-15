package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.OrganizationMember;
import com.naline.coopfull.repository.OrganizationMemberRepository;
import com.naline.coopfull.service.criteria.OrganizationMemberCriteria;
import com.naline.coopfull.service.dto.OrganizationMemberDTO;
import com.naline.coopfull.service.mapper.OrganizationMemberMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link OrganizationMember} entities in the database.
 * The main input is a {@link OrganizationMemberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganizationMemberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganizationMemberQueryService extends QueryService<OrganizationMember> {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationMemberQueryService.class);

    private final OrganizationMemberRepository organizationMemberRepository;

    private final OrganizationMemberMapper organizationMemberMapper;

    public OrganizationMemberQueryService(
        OrganizationMemberRepository organizationMemberRepository,
        OrganizationMemberMapper organizationMemberMapper
    ) {
        this.organizationMemberRepository = organizationMemberRepository;
        this.organizationMemberMapper = organizationMemberMapper;
    }

    /**
     * Return a {@link List} of {@link OrganizationMemberDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganizationMemberDTO> findByCriteria(OrganizationMemberCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<OrganizationMember> specification = createSpecification(criteria);
        return organizationMemberMapper.toDto(organizationMemberRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrganizationMemberCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<OrganizationMember> specification = createSpecification(criteria);
        return organizationMemberRepository.count(specification);
    }

    /**
     * Function to convert {@link OrganizationMemberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrganizationMember> createSpecification(OrganizationMemberCriteria criteria) {
        Specification<OrganizationMember> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), OrganizationMember_.id),
                    buildStringSpecification(criteria.getLegalName(), OrganizationMember_.legalName),
                    buildStringSpecification(criteria.getTradeName(), OrganizationMember_.tradeName),
                    buildStringSpecification(criteria.getRegistrationNumber(), OrganizationMember_.registrationNumber),
                    buildStringSpecification(criteria.getTaxNumber(), OrganizationMember_.taxNumber),
                    buildStringSpecification(criteria.getLegalForm(), OrganizationMember_.legalForm),
                    buildRangeSpecification(criteria.getRegistrationDate(), OrganizationMember_.registrationDate),
                    buildStringSpecification(criteria.getEmail(), OrganizationMember_.email),
                    buildStringSpecification(criteria.getPhoneNumber(), OrganizationMember_.phoneNumber),
                    buildStringSpecification(criteria.getWebsite(), OrganizationMember_.website),
                    buildSpecification(criteria.getMemberId(), root -> root.join(OrganizationMember_.member, JoinType.LEFT).get(Member_.id))
                )
            );
        }
        return specification;
    }
}
