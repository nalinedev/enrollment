package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.LivestockProduction;
import com.naline.coopfull.repository.LivestockProductionRepository;
import com.naline.coopfull.service.criteria.LivestockProductionCriteria;
import com.naline.coopfull.service.dto.LivestockProductionDTO;
import com.naline.coopfull.service.mapper.LivestockProductionMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LivestockProduction} entities in the database.
 * The main input is a {@link LivestockProductionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LivestockProductionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LivestockProductionQueryService extends QueryService<LivestockProduction> {

    private static final Logger LOG = LoggerFactory.getLogger(LivestockProductionQueryService.class);

    private final LivestockProductionRepository livestockProductionRepository;

    private final LivestockProductionMapper livestockProductionMapper;

    public LivestockProductionQueryService(
        LivestockProductionRepository livestockProductionRepository,
        LivestockProductionMapper livestockProductionMapper
    ) {
        this.livestockProductionRepository = livestockProductionRepository;
        this.livestockProductionMapper = livestockProductionMapper;
    }

    /**
     * Return a {@link List} of {@link LivestockProductionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LivestockProductionDTO> findByCriteria(LivestockProductionCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<LivestockProduction> specification = createSpecification(criteria);
        return livestockProductionMapper.toDto(livestockProductionRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LivestockProductionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<LivestockProduction> specification = createSpecification(criteria);
        return livestockProductionRepository.count(specification);
    }

    /**
     * Function to convert {@link LivestockProductionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LivestockProduction> createSpecification(LivestockProductionCriteria criteria) {
        Specification<LivestockProduction> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(LivestockProduction_.livestockActivity, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), LivestockProduction_.id),
                    buildRangeSpecification(criteria.getProductionDate(), LivestockProduction_.productionDate),
                    buildSpecification(criteria.getAnimalSex(), LivestockProduction_.animalSex),
                    buildRangeSpecification(criteria.getNumberOfAnimals(), LivestockProduction_.numberOfAnimals),
                    buildRangeSpecification(criteria.getAverageAgeMonths(), LivestockProduction_.averageAgeMonths),
                    buildRangeSpecification(criteria.getAverageWeightKg(), LivestockProduction_.averageWeightKg),
                    buildRangeSpecification(criteria.getProductionQuantity(), LivestockProduction_.productionQuantity),
                    buildStringSpecification(criteria.getProductionUnit(), LivestockProduction_.productionUnit),
                    buildRangeSpecification(criteria.getMortalityCount(), LivestockProduction_.mortalityCount),
                    buildRangeSpecification(criteria.getBirthCount(), LivestockProduction_.birthCount),
                    buildRangeSpecification(criteria.getSoldCount(), LivestockProduction_.soldCount),
                    buildSpecification(criteria.getStatus(), LivestockProduction_.status),
                    buildSpecification(criteria.getLivestockActivityId(), root ->
                        root.join(LivestockProduction_.livestockActivity, JoinType.LEFT).get(LivestockActivity_.id)
                    )
                )
            );
        }
        return specification;
    }
}
