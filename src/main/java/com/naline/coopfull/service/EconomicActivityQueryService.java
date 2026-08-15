package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.EconomicActivity;
import com.naline.coopfull.repository.EconomicActivityRepository;
import com.naline.coopfull.service.criteria.EconomicActivityCriteria;
import com.naline.coopfull.service.dto.EconomicActivityDTO;
import com.naline.coopfull.service.mapper.EconomicActivityMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EconomicActivity} entities in the database.
 * The main input is a {@link EconomicActivityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EconomicActivityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EconomicActivityQueryService extends QueryService<EconomicActivity> {

    private static final Logger LOG = LoggerFactory.getLogger(EconomicActivityQueryService.class);

    private final EconomicActivityRepository economicActivityRepository;

    private final EconomicActivityMapper economicActivityMapper;

    public EconomicActivityQueryService(
        EconomicActivityRepository economicActivityRepository,
        EconomicActivityMapper economicActivityMapper
    ) {
        this.economicActivityRepository = economicActivityRepository;
        this.economicActivityMapper = economicActivityMapper;
    }

    /**
     * Return a {@link List} of {@link EconomicActivityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EconomicActivityDTO> findByCriteria(EconomicActivityCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<EconomicActivity> specification = createSpecification(criteria);
        return economicActivityMapper.toDto(economicActivityRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EconomicActivityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EconomicActivity> specification = createSpecification(criteria);
        return economicActivityRepository.count(specification);
    }

    /**
     * Function to convert {@link EconomicActivityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EconomicActivity> createSpecification(EconomicActivityCriteria criteria) {
        Specification<EconomicActivity> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(EconomicActivity_.agriculturalActivity, JoinType.LEFT);
                root.fetch(EconomicActivity_.livestockActivity, JoinType.LEFT);
                root.fetch(EconomicActivity_.aquacultureActivity, JoinType.LEFT);
                root.fetch(EconomicActivity_.member, JoinType.LEFT);
                root.fetch(EconomicActivity_.activityType, JoinType.LEFT);
                root.fetch(EconomicActivity_.location, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), EconomicActivity_.id),
                    buildStringSpecification(criteria.getName(), EconomicActivity_.name),
                    buildSpecification(criteria.getMainActivity(), EconomicActivity_.mainActivity),
                    buildRangeSpecification(criteria.getStartDate(), EconomicActivity_.startDate),
                    buildRangeSpecification(criteria.getEndDate(), EconomicActivity_.endDate),
                    buildRangeSpecification(criteria.getAnnualRevenue(), EconomicActivity_.annualRevenue),
                    buildRangeSpecification(criteria.getMonthlyRevenue(), EconomicActivity_.monthlyRevenue),
                    buildRangeSpecification(criteria.getNumberOfEmployees(), EconomicActivity_.numberOfEmployees),
                    buildSpecification(criteria.getStatus(), EconomicActivity_.status),
                    buildSpecification(criteria.getAgriculturalActivityId(), root ->
                        root.join(EconomicActivity_.agriculturalActivity, JoinType.LEFT).get(AgriculturalActivity_.id)
                    ),
                    buildSpecification(criteria.getLivestockActivityId(), root ->
                        root.join(EconomicActivity_.livestockActivity, JoinType.LEFT).get(LivestockActivity_.id)
                    ),
                    buildSpecification(criteria.getAquacultureActivityId(), root ->
                        root.join(EconomicActivity_.aquacultureActivity, JoinType.LEFT).get(AquacultureActivity_.id)
                    ),
                    buildSpecification(criteria.getMemberId(), root -> root.join(EconomicActivity_.member, JoinType.LEFT).get(Member_.id)),
                    buildSpecification(criteria.getActivityTypeId(), root ->
                        root.join(EconomicActivity_.activityType, JoinType.LEFT).get(EconomicActivityType_.id)
                    ),
                    buildSpecification(criteria.getLocationId(), root ->
                        root.join(EconomicActivity_.location, JoinType.LEFT).get(Location_.id)
                    )
                )
            );
        }
        return specification;
    }
}
