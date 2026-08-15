package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.EconomicActivityType;
import com.naline.coopfull.repository.EconomicActivityTypeRepository;
import com.naline.coopfull.service.criteria.EconomicActivityTypeCriteria;
import com.naline.coopfull.service.dto.EconomicActivityTypeDTO;
import com.naline.coopfull.service.mapper.EconomicActivityTypeMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EconomicActivityType} entities in the database.
 * The main input is a {@link EconomicActivityTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EconomicActivityTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EconomicActivityTypeQueryService extends QueryService<EconomicActivityType> {

    private static final Logger LOG = LoggerFactory.getLogger(EconomicActivityTypeQueryService.class);

    private final EconomicActivityTypeRepository economicActivityTypeRepository;

    private final EconomicActivityTypeMapper economicActivityTypeMapper;

    public EconomicActivityTypeQueryService(
        EconomicActivityTypeRepository economicActivityTypeRepository,
        EconomicActivityTypeMapper economicActivityTypeMapper
    ) {
        this.economicActivityTypeRepository = economicActivityTypeRepository;
        this.economicActivityTypeMapper = economicActivityTypeMapper;
    }

    /**
     * Return a {@link List} of {@link EconomicActivityTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EconomicActivityTypeDTO> findByCriteria(EconomicActivityTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<EconomicActivityType> specification = createSpecification(criteria);
        return economicActivityTypeMapper.toDto(economicActivityTypeRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EconomicActivityTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EconomicActivityType> specification = createSpecification(criteria);
        return economicActivityTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link EconomicActivityTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EconomicActivityType> createSpecification(EconomicActivityTypeCriteria criteria) {
        Specification<EconomicActivityType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), EconomicActivityType_.id),
                    buildStringSpecification(criteria.getCode(), EconomicActivityType_.code),
                    buildStringSpecification(criteria.getName(), EconomicActivityType_.name),
                    buildStringSpecification(criteria.getSector(), EconomicActivityType_.sector),
                    buildSpecification(criteria.getActive(), EconomicActivityType_.active)
                )
            );
        }
        return specification;
    }
}
