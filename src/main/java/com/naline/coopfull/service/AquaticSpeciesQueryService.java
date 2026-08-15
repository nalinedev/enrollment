package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.AquaticSpecies;
import com.naline.coopfull.repository.AquaticSpeciesRepository;
import com.naline.coopfull.service.criteria.AquaticSpeciesCriteria;
import com.naline.coopfull.service.dto.AquaticSpeciesDTO;
import com.naline.coopfull.service.mapper.AquaticSpeciesMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AquaticSpecies} entities in the database.
 * The main input is a {@link AquaticSpeciesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AquaticSpeciesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AquaticSpeciesQueryService extends QueryService<AquaticSpecies> {

    private static final Logger LOG = LoggerFactory.getLogger(AquaticSpeciesQueryService.class);

    private final AquaticSpeciesRepository aquaticSpeciesRepository;

    private final AquaticSpeciesMapper aquaticSpeciesMapper;

    public AquaticSpeciesQueryService(AquaticSpeciesRepository aquaticSpeciesRepository, AquaticSpeciesMapper aquaticSpeciesMapper) {
        this.aquaticSpeciesRepository = aquaticSpeciesRepository;
        this.aquaticSpeciesMapper = aquaticSpeciesMapper;
    }

    /**
     * Return a {@link List} of {@link AquaticSpeciesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AquaticSpeciesDTO> findByCriteria(AquaticSpeciesCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<AquaticSpecies> specification = createSpecification(criteria);
        return aquaticSpeciesMapper.toDto(aquaticSpeciesRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AquaticSpeciesCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AquaticSpecies> specification = createSpecification(criteria);
        return aquaticSpeciesRepository.count(specification);
    }

    /**
     * Function to convert {@link AquaticSpeciesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AquaticSpecies> createSpecification(AquaticSpeciesCriteria criteria) {
        Specification<AquaticSpecies> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), AquaticSpecies_.id),
                    buildStringSpecification(criteria.getCode(), AquaticSpecies_.code),
                    buildStringSpecification(criteria.getName(), AquaticSpecies_.name),
                    buildStringSpecification(criteria.getScientificName(), AquaticSpecies_.scientificName),
                    buildStringSpecification(criteria.getCategory(), AquaticSpecies_.category),
                    buildSpecification(criteria.getFreshwater(), AquaticSpecies_.freshwater),
                    buildSpecification(criteria.getSaltwater(), AquaticSpecies_.saltwater),
                    buildSpecification(criteria.getActive(), AquaticSpecies_.active)
                )
            );
        }
        return specification;
    }
}
