package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.repository.CooperativeRepository;
import com.naline.coopfull.service.criteria.CooperativeCriteria;
import com.naline.coopfull.service.dto.CooperativeDTO;
import com.naline.coopfull.service.mapper.CooperativeMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cooperative} entities in the database.
 * The main input is a {@link CooperativeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CooperativeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CooperativeQueryService extends QueryService<Cooperative> {

    private static final Logger LOG = LoggerFactory.getLogger(CooperativeQueryService.class);

    private final CooperativeRepository cooperativeRepository;

    private final CooperativeMapper cooperativeMapper;

    public CooperativeQueryService(CooperativeRepository cooperativeRepository, CooperativeMapper cooperativeMapper) {
        this.cooperativeRepository = cooperativeRepository;
        this.cooperativeMapper = cooperativeMapper;
    }

    /**
     * Return a {@link List} of {@link CooperativeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CooperativeDTO> findByCriteria(CooperativeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Cooperative> specification = createSpecification(criteria);
        return cooperativeMapper.toDto(cooperativeRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CooperativeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Cooperative> specification = createSpecification(criteria);
        return cooperativeRepository.count(specification);
    }

    /**
     * Function to convert {@link CooperativeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cooperative> createSpecification(CooperativeCriteria criteria) {
        Specification<Cooperative> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Cooperative_.id),
                    buildStringSpecification(criteria.getCode(), Cooperative_.code),
                    buildStringSpecification(criteria.getName(), Cooperative_.name),
                    buildStringSpecification(criteria.getLegalName(), Cooperative_.legalName),
                    buildStringSpecification(criteria.getRegistrationNumber(), Cooperative_.registrationNumber),
                    buildStringSpecification(criteria.getTaxNumber(), Cooperative_.taxNumber),
                    buildSpecification(criteria.getStatus(), Cooperative_.status),
                    buildRangeSpecification(criteria.getFoundedDate(), Cooperative_.foundedDate),
                    buildStringSpecification(criteria.getEmail(), Cooperative_.email),
                    buildStringSpecification(criteria.getPhone(), Cooperative_.phone),
                    buildStringSpecification(criteria.getWebsite(), Cooperative_.website),
                    buildRangeSpecification(criteria.getCreatedDate(), Cooperative_.createdDate),
                    buildRangeSpecification(criteria.getLastModifiedDate(), Cooperative_.lastModifiedDate)
                )
            );
        }
        return specification;
    }
}
