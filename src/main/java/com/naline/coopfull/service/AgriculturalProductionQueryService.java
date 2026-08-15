package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.AgriculturalProduction;
import com.naline.coopfull.repository.AgriculturalProductionRepository;
import com.naline.coopfull.service.criteria.AgriculturalProductionCriteria;
import com.naline.coopfull.service.dto.AgriculturalProductionDTO;
import com.naline.coopfull.service.mapper.AgriculturalProductionMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AgriculturalProduction} entities in the database.
 * The main input is a {@link AgriculturalProductionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AgriculturalProductionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgriculturalProductionQueryService extends QueryService<AgriculturalProduction> {

    private static final Logger LOG = LoggerFactory.getLogger(AgriculturalProductionQueryService.class);

    private final AgriculturalProductionRepository agriculturalProductionRepository;

    private final AgriculturalProductionMapper agriculturalProductionMapper;

    public AgriculturalProductionQueryService(
        AgriculturalProductionRepository agriculturalProductionRepository,
        AgriculturalProductionMapper agriculturalProductionMapper
    ) {
        this.agriculturalProductionRepository = agriculturalProductionRepository;
        this.agriculturalProductionMapper = agriculturalProductionMapper;
    }

    /**
     * Return a {@link List} of {@link AgriculturalProductionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AgriculturalProductionDTO> findByCriteria(AgriculturalProductionCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<AgriculturalProduction> specification = createSpecification(criteria);
        return agriculturalProductionMapper.toDto(agriculturalProductionRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgriculturalProductionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AgriculturalProduction> specification = createSpecification(criteria);
        return agriculturalProductionRepository.count(specification);
    }

    /**
     * Function to convert {@link AgriculturalProductionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AgriculturalProduction> createSpecification(AgriculturalProductionCriteria criteria) {
        Specification<AgriculturalProduction> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(AgriculturalProduction_.agriculturalActivity, JoinType.LEFT);
                root.fetch(AgriculturalProduction_.crop, JoinType.LEFT);
                root.fetch(AgriculturalProduction_.cropVariety, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), AgriculturalProduction_.id),
                    buildRangeSpecification(criteria.getArea(), AgriculturalProduction_.area),
                    buildStringSpecification(criteria.getAreaUnit(), AgriculturalProduction_.areaUnit),
                    buildRangeSpecification(criteria.getPlantingDate(), AgriculturalProduction_.plantingDate),
                    buildRangeSpecification(criteria.getHarvestStartDate(), AgriculturalProduction_.harvestStartDate),
                    buildRangeSpecification(criteria.getHarvestEndDate(), AgriculturalProduction_.harvestEndDate),
                    buildRangeSpecification(criteria.getProductionQuantity(), AgriculturalProduction_.productionQuantity),
                    buildStringSpecification(criteria.getProductionUnit(), AgriculturalProduction_.productionUnit),
                    buildRangeSpecification(criteria.getExpectedAnnualProduction(), AgriculturalProduction_.expectedAnnualProduction),
                    buildRangeSpecification(criteria.getNumberOfPlants(), AgriculturalProduction_.numberOfPlants),
                    buildRangeSpecification(criteria.getPlantingDensity(), AgriculturalProduction_.plantingDensity),
                    buildRangeSpecification(criteria.getProductionYear(), AgriculturalProduction_.productionYear),
                    buildSpecification(criteria.getStatus(), AgriculturalProduction_.status),
                    buildSpecification(criteria.getAgriculturalActivityId(), root ->
                        root.join(AgriculturalProduction_.agriculturalActivity, JoinType.LEFT).get(AgriculturalActivity_.id)
                    ),
                    buildSpecification(criteria.getCropId(), root -> root.join(AgriculturalProduction_.crop, JoinType.LEFT).get(Crop_.id)),
                    buildSpecification(criteria.getCropVarietyId(), root ->
                        root.join(AgriculturalProduction_.cropVariety, JoinType.LEFT).get(CropVariety_.id)
                    )
                )
            );
        }
        return specification;
    }
}
