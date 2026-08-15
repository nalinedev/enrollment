package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.CropVariety;
import com.naline.coopfull.repository.CropVarietyRepository;
import com.naline.coopfull.service.criteria.CropVarietyCriteria;
import com.naline.coopfull.service.dto.CropVarietyDTO;
import com.naline.coopfull.service.mapper.CropVarietyMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CropVariety} entities in the database.
 * The main input is a {@link CropVarietyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CropVarietyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CropVarietyQueryService extends QueryService<CropVariety> {

    private static final Logger LOG = LoggerFactory.getLogger(CropVarietyQueryService.class);

    private final CropVarietyRepository cropVarietyRepository;

    private final CropVarietyMapper cropVarietyMapper;

    public CropVarietyQueryService(CropVarietyRepository cropVarietyRepository, CropVarietyMapper cropVarietyMapper) {
        this.cropVarietyRepository = cropVarietyRepository;
        this.cropVarietyMapper = cropVarietyMapper;
    }

    /**
     * Return a {@link List} of {@link CropVarietyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CropVarietyDTO> findByCriteria(CropVarietyCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<CropVariety> specification = createSpecification(criteria);
        return cropVarietyMapper.toDto(cropVarietyRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CropVarietyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CropVariety> specification = createSpecification(criteria);
        return cropVarietyRepository.count(specification);
    }

    /**
     * Function to convert {@link CropVarietyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CropVariety> createSpecification(CropVarietyCriteria criteria) {
        Specification<CropVariety> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CropVariety_.crop, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CropVariety_.id),
                    buildStringSpecification(criteria.getCode(), CropVariety_.code),
                    buildStringSpecification(criteria.getName(), CropVariety_.name),
                    buildStringSpecification(criteria.getOrigin(), CropVariety_.origin),
                    buildRangeSpecification(criteria.getMaturityDays(), CropVariety_.maturityDays),
                    buildRangeSpecification(criteria.getYieldPotential(), CropVariety_.yieldPotential),
                    buildStringSpecification(criteria.getDiseaseResistance(), CropVariety_.diseaseResistance),
                    buildSpecification(criteria.getActive(), CropVariety_.active),
                    buildSpecification(criteria.getCropId(), root -> root.join(CropVariety_.crop, JoinType.LEFT).get(Crop_.id))
                )
            );
        }
        return specification;
    }
}
