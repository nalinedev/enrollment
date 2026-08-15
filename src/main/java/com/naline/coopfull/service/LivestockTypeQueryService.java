package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.LivestockType;
import com.naline.coopfull.repository.LivestockTypeRepository;
import com.naline.coopfull.service.criteria.LivestockTypeCriteria;
import com.naline.coopfull.service.dto.LivestockTypeDTO;
import com.naline.coopfull.service.mapper.LivestockTypeMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LivestockType} entities in the database.
 * The main input is a {@link LivestockTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LivestockTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LivestockTypeQueryService extends QueryService<LivestockType> {

    private static final Logger LOG = LoggerFactory.getLogger(LivestockTypeQueryService.class);

    private final LivestockTypeRepository livestockTypeRepository;

    private final LivestockTypeMapper livestockTypeMapper;

    public LivestockTypeQueryService(LivestockTypeRepository livestockTypeRepository, LivestockTypeMapper livestockTypeMapper) {
        this.livestockTypeRepository = livestockTypeRepository;
        this.livestockTypeMapper = livestockTypeMapper;
    }

    /**
     * Return a {@link List} of {@link LivestockTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LivestockTypeDTO> findByCriteria(LivestockTypeCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<LivestockType> specification = createSpecification(criteria);
        return livestockTypeMapper.toDto(livestockTypeRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LivestockTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<LivestockType> specification = createSpecification(criteria);
        return livestockTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link LivestockTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LivestockType> createSpecification(LivestockTypeCriteria criteria) {
        Specification<LivestockType> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), LivestockType_.id),
                    buildStringSpecification(criteria.getCode(), LivestockType_.code),
                    buildStringSpecification(criteria.getName(), LivestockType_.name),
                    buildStringSpecification(criteria.getScientificName(), LivestockType_.scientificName),
                    buildStringSpecification(criteria.getCategory(), LivestockType_.category),
                    buildSpecification(criteria.getActive(), LivestockType_.active)
                )
            );
        }
        return specification;
    }
}
