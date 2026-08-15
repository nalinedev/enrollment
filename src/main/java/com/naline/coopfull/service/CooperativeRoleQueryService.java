package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.CooperativeRole;
import com.naline.coopfull.repository.CooperativeRoleRepository;
import com.naline.coopfull.service.criteria.CooperativeRoleCriteria;
import com.naline.coopfull.service.dto.CooperativeRoleDTO;
import com.naline.coopfull.service.mapper.CooperativeRoleMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CooperativeRole} entities in the database.
 * The main input is a {@link CooperativeRoleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CooperativeRoleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CooperativeRoleQueryService extends QueryService<CooperativeRole> {

    private static final Logger LOG = LoggerFactory.getLogger(CooperativeRoleQueryService.class);

    private final CooperativeRoleRepository cooperativeRoleRepository;

    private final CooperativeRoleMapper cooperativeRoleMapper;

    public CooperativeRoleQueryService(CooperativeRoleRepository cooperativeRoleRepository, CooperativeRoleMapper cooperativeRoleMapper) {
        this.cooperativeRoleRepository = cooperativeRoleRepository;
        this.cooperativeRoleMapper = cooperativeRoleMapper;
    }

    /**
     * Return a {@link List} of {@link CooperativeRoleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CooperativeRoleDTO> findByCriteria(CooperativeRoleCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<CooperativeRole> specification = createSpecification(criteria);
        return cooperativeRoleMapper.toDto(
            cooperativeRoleRepository.fetchBagRelationships(cooperativeRoleRepository.findAll(specification))
        );
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CooperativeRoleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CooperativeRole> specification = createSpecification(criteria);
        return cooperativeRoleRepository.count(specification);
    }

    /**
     * Function to convert {@link CooperativeRoleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CooperativeRole> createSpecification(CooperativeRoleCriteria criteria) {
        Specification<CooperativeRole> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CooperativeRole_.id),
                    buildStringSpecification(criteria.getCode(), CooperativeRole_.code),
                    buildStringSpecification(criteria.getName(), CooperativeRole_.name),
                    buildSpecification(criteria.getStatus(), CooperativeRole_.status),
                    buildSpecification(criteria.getPermissionsId(), root ->
                        root.join(CooperativeRole_.permissionses, JoinType.LEFT).get(Permission_.id)
                    )
                )
            );
        }
        return specification;
    }
}
