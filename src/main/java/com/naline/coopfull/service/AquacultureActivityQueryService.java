package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.AquacultureActivity;
import com.naline.coopfull.repository.AquacultureActivityRepository;
import com.naline.coopfull.service.criteria.AquacultureActivityCriteria;
import com.naline.coopfull.service.dto.AquacultureActivityDTO;
import com.naline.coopfull.service.mapper.AquacultureActivityMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AquacultureActivity} entities in the database.
 * The main input is a {@link AquacultureActivityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AquacultureActivityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AquacultureActivityQueryService extends QueryService<AquacultureActivity> {

    private static final Logger LOG = LoggerFactory.getLogger(AquacultureActivityQueryService.class);

    private final AquacultureActivityRepository aquacultureActivityRepository;

    private final AquacultureActivityMapper aquacultureActivityMapper;

    public AquacultureActivityQueryService(
        AquacultureActivityRepository aquacultureActivityRepository,
        AquacultureActivityMapper aquacultureActivityMapper
    ) {
        this.aquacultureActivityRepository = aquacultureActivityRepository;
        this.aquacultureActivityMapper = aquacultureActivityMapper;
    }

    /**
     * Return a {@link List} of {@link AquacultureActivityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AquacultureActivityDTO> findByCriteria(AquacultureActivityCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<AquacultureActivity> specification = createSpecification(criteria);
        return aquacultureActivityMapper.toDto(aquacultureActivityRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AquacultureActivityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AquacultureActivity> specification = createSpecification(criteria);
        return aquacultureActivityRepository.count(specification);
    }

    /**
     * Function to convert {@link AquacultureActivityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AquacultureActivity> createSpecification(AquacultureActivityCriteria criteria) {
        Specification<AquacultureActivity> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(AquacultureActivity_.location, JoinType.LEFT);
                root.fetch(AquacultureActivity_.aquaticSpecies, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), AquacultureActivity_.id),
                    buildStringSpecification(criteria.getName(), AquacultureActivity_.name),
                    buildSpecification(criteria.getProductionMode(), AquacultureActivity_.productionMode),
                    buildSpecification(criteria.getOwnershipType(), AquacultureActivity_.ownershipType),
                    buildSpecification(criteria.getProductionType(), AquacultureActivity_.productionType),
                    buildSpecification(criteria.getSystemType(), AquacultureActivity_.systemType),
                    buildRangeSpecification(criteria.getStartDate(), AquacultureActivity_.startDate),
                    buildRangeSpecification(criteria.getTotalArea(), AquacultureActivity_.totalArea),
                    buildStringSpecification(criteria.getAreaUnit(), AquacultureActivity_.areaUnit),
                    buildStringSpecification(criteria.getWaterSource(), AquacultureActivity_.waterSource),
                    buildRangeSpecification(criteria.getNumberOfProductionUnits(), AquacultureActivity_.numberOfProductionUnits),
                    buildStringSpecification(criteria.getProductionUnitDescription(), AquacultureActivity_.productionUnitDescription),
                    buildSpecification(criteria.getStatus(), AquacultureActivity_.status),
                    buildRangeSpecification(criteria.getAnnualRevenue(), AquacultureActivity_.annualRevenue),
                    buildRangeSpecification(criteria.getMonthlyRevenue(), AquacultureActivity_.monthlyRevenue),
                    buildRangeSpecification(criteria.getEmployees(), AquacultureActivity_.employees),
                    buildStringSpecification(criteria.getCertification(), AquacultureActivity_.certification),
                    buildSpecification(criteria.getLocationId(), root ->
                        root.join(AquacultureActivity_.location, JoinType.LEFT).get(Location_.id)
                    ),
                    buildSpecification(criteria.getAquaticSpeciesId(), root ->
                        root.join(AquacultureActivity_.aquaticSpecies, JoinType.LEFT).get(AquaticSpecies_.id)
                    ),
                    buildSpecification(criteria.getEconomicActivityId(), root ->
                        root.join(AquacultureActivity_.economicActivity, JoinType.LEFT).get(EconomicActivity_.id)
                    ),
                    buildSpecification(criteria.getProductionsId(), root ->
                        root.join(AquacultureActivity_.productionses, JoinType.LEFT).get(AquacultureProduction_.id)
                    )
                )
            );
        }
        return specification;
    }
}
