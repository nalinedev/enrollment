package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.AquacultureProduction;
import com.naline.coopfull.repository.AquacultureProductionRepository;
import com.naline.coopfull.service.criteria.AquacultureProductionCriteria;
import com.naline.coopfull.service.dto.AquacultureProductionDTO;
import com.naline.coopfull.service.mapper.AquacultureProductionMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AquacultureProduction} entities in the database.
 * The main input is a {@link AquacultureProductionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AquacultureProductionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AquacultureProductionQueryService extends QueryService<AquacultureProduction> {

    private static final Logger LOG = LoggerFactory.getLogger(AquacultureProductionQueryService.class);

    private final AquacultureProductionRepository aquacultureProductionRepository;

    private final AquacultureProductionMapper aquacultureProductionMapper;

    public AquacultureProductionQueryService(
        AquacultureProductionRepository aquacultureProductionRepository,
        AquacultureProductionMapper aquacultureProductionMapper
    ) {
        this.aquacultureProductionRepository = aquacultureProductionRepository;
        this.aquacultureProductionMapper = aquacultureProductionMapper;
    }

    /**
     * Return a {@link List} of {@link AquacultureProductionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AquacultureProductionDTO> findByCriteria(AquacultureProductionCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<AquacultureProduction> specification = createSpecification(criteria);
        return aquacultureProductionMapper.toDto(aquacultureProductionRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AquacultureProductionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AquacultureProduction> specification = createSpecification(criteria);
        return aquacultureProductionRepository.count(specification);
    }

    /**
     * Function to convert {@link AquacultureProductionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AquacultureProduction> createSpecification(AquacultureProductionCriteria criteria) {
        Specification<AquacultureProduction> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(AquacultureProduction_.aquacultureActivity, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), AquacultureProduction_.id),
                    buildRangeSpecification(criteria.getProductionDate(), AquacultureProduction_.productionDate),
                    buildRangeSpecification(criteria.getNumberOfAnimals(), AquacultureProduction_.numberOfAnimals),
                    buildRangeSpecification(criteria.getStockingDensity(), AquacultureProduction_.stockingDensity),
                    buildRangeSpecification(criteria.getProductionQuantity(), AquacultureProduction_.productionQuantity),
                    buildStringSpecification(criteria.getProductionUnit(), AquacultureProduction_.productionUnit),
                    buildRangeSpecification(criteria.getAverageWeightGrams(), AquacultureProduction_.averageWeightGrams),
                    buildRangeSpecification(criteria.getMortalityCount(), AquacultureProduction_.mortalityCount),
                    buildRangeSpecification(criteria.getStockingCount(), AquacultureProduction_.stockingCount),
                    buildRangeSpecification(criteria.getHarvestedCount(), AquacultureProduction_.harvestedCount),
                    buildRangeSpecification(criteria.getExpectedProduction(), AquacultureProduction_.expectedProduction),
                    buildRangeSpecification(criteria.getExpectedHarvestDate(), AquacultureProduction_.expectedHarvestDate),
                    buildRangeSpecification(criteria.getActualHarvestDate(), AquacultureProduction_.actualHarvestDate),
                    buildSpecification(criteria.getStatus(), AquacultureProduction_.status),
                    buildSpecification(criteria.getAquacultureActivityId(), root ->
                        root.join(AquacultureProduction_.aquacultureActivity, JoinType.LEFT).get(AquacultureActivity_.id)
                    )
                )
            );
        }
        return specification;
    }
}
