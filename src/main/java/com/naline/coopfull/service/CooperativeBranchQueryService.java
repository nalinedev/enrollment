package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.CooperativeBranch;
import com.naline.coopfull.repository.CooperativeBranchRepository;
import com.naline.coopfull.service.criteria.CooperativeBranchCriteria;
import com.naline.coopfull.service.dto.CooperativeBranchDTO;
import com.naline.coopfull.service.mapper.CooperativeBranchMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CooperativeBranch} entities in the database.
 * The main input is a {@link CooperativeBranchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CooperativeBranchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CooperativeBranchQueryService extends QueryService<CooperativeBranch> {

    private static final Logger LOG = LoggerFactory.getLogger(CooperativeBranchQueryService.class);

    private final CooperativeBranchRepository cooperativeBranchRepository;

    private final CooperativeBranchMapper cooperativeBranchMapper;

    public CooperativeBranchQueryService(
        CooperativeBranchRepository cooperativeBranchRepository,
        CooperativeBranchMapper cooperativeBranchMapper
    ) {
        this.cooperativeBranchRepository = cooperativeBranchRepository;
        this.cooperativeBranchMapper = cooperativeBranchMapper;
    }

    /**
     * Return a {@link List} of {@link CooperativeBranchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CooperativeBranchDTO> findByCriteria(CooperativeBranchCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<CooperativeBranch> specification = createSpecification(criteria);
        return cooperativeBranchMapper.toDto(cooperativeBranchRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CooperativeBranchCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CooperativeBranch> specification = createSpecification(criteria);
        return cooperativeBranchRepository.count(specification);
    }

    /**
     * Function to convert {@link CooperativeBranchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CooperativeBranch> createSpecification(CooperativeBranchCriteria criteria) {
        Specification<CooperativeBranch> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CooperativeBranch_.cooperative, JoinType.LEFT);
                root.fetch(CooperativeBranch_.location, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CooperativeBranch_.id),
                    buildStringSpecification(criteria.getCode(), CooperativeBranch_.code),
                    buildStringSpecification(criteria.getName(), CooperativeBranch_.name),
                    buildStringSpecification(criteria.getDescription(), CooperativeBranch_.description),
                    buildStringSpecification(criteria.getPhone(), CooperativeBranch_.phone),
                    buildStringSpecification(criteria.getEmail(), CooperativeBranch_.email),
                    buildSpecification(criteria.getStatus(), CooperativeBranch_.status),
                    buildRangeSpecification(criteria.getOpeningDate(), CooperativeBranch_.openingDate),
                    buildRangeSpecification(criteria.getClosingDate(), CooperativeBranch_.closingDate),
                    buildSpecification(criteria.getCooperativeId(), root ->
                        root.join(CooperativeBranch_.cooperative, JoinType.LEFT).get(Cooperative_.id)
                    ),
                    buildSpecification(criteria.getLocationId(), root ->
                        root.join(CooperativeBranch_.location, JoinType.LEFT).get(Location_.id)
                    )
                )
            );
        }
        return specification;
    }
}
