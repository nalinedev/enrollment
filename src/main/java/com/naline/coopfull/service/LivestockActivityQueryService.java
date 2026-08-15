package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.LivestockActivity;
import com.naline.coopfull.repository.LivestockActivityRepository;
import com.naline.coopfull.service.criteria.LivestockActivityCriteria;
import com.naline.coopfull.service.dto.LivestockActivityDTO;
import com.naline.coopfull.service.mapper.LivestockActivityMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LivestockActivity} entities in the database.
 * The main input is a {@link LivestockActivityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LivestockActivityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LivestockActivityQueryService extends QueryService<LivestockActivity> {

    private static final Logger LOG = LoggerFactory.getLogger(LivestockActivityQueryService.class);

    private final LivestockActivityRepository livestockActivityRepository;

    private final LivestockActivityMapper livestockActivityMapper;

    public LivestockActivityQueryService(
        LivestockActivityRepository livestockActivityRepository,
        LivestockActivityMapper livestockActivityMapper
    ) {
        this.livestockActivityRepository = livestockActivityRepository;
        this.livestockActivityMapper = livestockActivityMapper;
    }

    /**
     * Return a {@link List} of {@link LivestockActivityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LivestockActivityDTO> findByCriteria(LivestockActivityCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<LivestockActivity> specification = createSpecification(criteria);
        return livestockActivityMapper.toDto(livestockActivityRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LivestockActivityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<LivestockActivity> specification = createSpecification(criteria);
        return livestockActivityRepository.count(specification);
    }

    /**
     * Function to convert {@link LivestockActivityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LivestockActivity> createSpecification(LivestockActivityCriteria criteria) {
        Specification<LivestockActivity> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(LivestockActivity_.location, JoinType.LEFT);
                root.fetch(LivestockActivity_.livestockType, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), LivestockActivity_.id),
                    buildStringSpecification(criteria.getName(), LivestockActivity_.name),
                    buildSpecification(criteria.getProductionMode(), LivestockActivity_.productionMode),
                    buildSpecification(criteria.getOwnershipType(), LivestockActivity_.ownershipType),
                    buildSpecification(criteria.getProductionType(), LivestockActivity_.productionType),
                    buildRangeSpecification(criteria.getStartDate(), LivestockActivity_.startDate),
                    buildRangeSpecification(criteria.getTotalArea(), LivestockActivity_.totalArea),
                    buildStringSpecification(criteria.getAreaUnit(), LivestockActivity_.areaUnit),
                    buildSpecification(criteria.getStatus(), LivestockActivity_.status),
                    buildRangeSpecification(criteria.getNumberOfAnimals(), LivestockActivity_.numberOfAnimals),
                    buildRangeSpecification(criteria.getAnnualRevenue(), LivestockActivity_.annualRevenue),
                    buildRangeSpecification(criteria.getMonthlyRevenue(), LivestockActivity_.monthlyRevenue),
                    buildRangeSpecification(criteria.getEmployees(), LivestockActivity_.employees),
                    buildSpecification(criteria.getVeterinaryServiceAvailable(), LivestockActivity_.veterinaryServiceAvailable),
                    buildStringSpecification(criteria.getFeedSource(), LivestockActivity_.feedSource),
                    buildStringSpecification(criteria.getWaterSource(), LivestockActivity_.waterSource),
                    buildStringSpecification(criteria.getCertification(), LivestockActivity_.certification),
                    buildSpecification(criteria.getLocationId(), root ->
                        root.join(LivestockActivity_.location, JoinType.LEFT).get(Location_.id)
                    ),
                    buildSpecification(criteria.getLivestockTypeId(), root ->
                        root.join(LivestockActivity_.livestockType, JoinType.LEFT).get(LivestockType_.id)
                    ),
                    buildSpecification(criteria.getEconomicActivityId(), root ->
                        root.join(LivestockActivity_.economicActivity, JoinType.LEFT).get(EconomicActivity_.id)
                    ),
                    buildSpecification(criteria.getProductionsId(), root ->
                        root.join(LivestockActivity_.productionses, JoinType.LEFT).get(LivestockProduction_.id)
                    )
                )
            );
        }
        return specification;
    }
}
