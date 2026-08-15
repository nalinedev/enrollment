package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.AgriculturalActivity;
import com.naline.coopfull.repository.AgriculturalActivityRepository;
import com.naline.coopfull.service.criteria.AgriculturalActivityCriteria;
import com.naline.coopfull.service.dto.AgriculturalActivityDTO;
import com.naline.coopfull.service.mapper.AgriculturalActivityMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AgriculturalActivity} entities in the database.
 * The main input is a {@link AgriculturalActivityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AgriculturalActivityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgriculturalActivityQueryService extends QueryService<AgriculturalActivity> {

    private static final Logger LOG = LoggerFactory.getLogger(AgriculturalActivityQueryService.class);

    private final AgriculturalActivityRepository agriculturalActivityRepository;

    private final AgriculturalActivityMapper agriculturalActivityMapper;

    public AgriculturalActivityQueryService(
        AgriculturalActivityRepository agriculturalActivityRepository,
        AgriculturalActivityMapper agriculturalActivityMapper
    ) {
        this.agriculturalActivityRepository = agriculturalActivityRepository;
        this.agriculturalActivityMapper = agriculturalActivityMapper;
    }

    /**
     * Return a {@link List} of {@link AgriculturalActivityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AgriculturalActivityDTO> findByCriteria(AgriculturalActivityCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<AgriculturalActivity> specification = createSpecification(criteria);
        return agriculturalActivityMapper.toDto(agriculturalActivityRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgriculturalActivityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AgriculturalActivity> specification = createSpecification(criteria);
        return agriculturalActivityRepository.count(specification);
    }

    /**
     * Function to convert {@link AgriculturalActivityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AgriculturalActivity> createSpecification(AgriculturalActivityCriteria criteria) {
        Specification<AgriculturalActivity> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(AgriculturalActivity_.location, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), AgriculturalActivity_.id),
                    buildRangeSpecification(criteria.getTotalArea(), AgriculturalActivity_.totalArea),
                    buildStringSpecification(criteria.getAreaUnit(), AgriculturalActivity_.areaUnit),
                    buildSpecification(criteria.getExploitationMode(), AgriculturalActivity_.exploitationMode),
                    buildSpecification(criteria.getOwnershipType(), AgriculturalActivity_.ownershipType),
                    buildRangeSpecification(criteria.getStartDate(), AgriculturalActivity_.startDate),
                    buildSpecification(criteria.getIrrigationAvailable(), AgriculturalActivity_.irrigationAvailable),
                    buildSpecification(criteria.getOrganicProduction(), AgriculturalActivity_.organicProduction),
                    buildStringSpecification(criteria.getCertification(), AgriculturalActivity_.certification),
                    buildSpecification(criteria.getLocationId(), root ->
                        root.join(AgriculturalActivity_.location, JoinType.LEFT).get(Location_.id)
                    ),
                    buildSpecification(criteria.getEconomicActivityId(), root ->
                        root.join(AgriculturalActivity_.economicActivity, JoinType.LEFT).get(EconomicActivity_.id)
                    ),
                    buildSpecification(criteria.getProductionsId(), root ->
                        root.join(AgriculturalActivity_.productionses, JoinType.LEFT).get(AgriculturalProduction_.id)
                    )
                )
            );
        }
        return specification;
    }
}
