package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.Crop;
import com.naline.coopfull.repository.CropRepository;
import com.naline.coopfull.service.criteria.CropCriteria;
import com.naline.coopfull.service.dto.CropDTO;
import com.naline.coopfull.service.mapper.CropMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Crop} entities in the database.
 * The main input is a {@link CropCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CropDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CropQueryService extends QueryService<Crop> {

    private static final Logger LOG = LoggerFactory.getLogger(CropQueryService.class);

    private final CropRepository cropRepository;

    private final CropMapper cropMapper;

    public CropQueryService(CropRepository cropRepository, CropMapper cropMapper) {
        this.cropRepository = cropRepository;
        this.cropMapper = cropMapper;
    }

    /**
     * Return a {@link List} of {@link CropDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CropDTO> findByCriteria(CropCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Crop> specification = createSpecification(criteria);
        return cropMapper.toDto(cropRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CropCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Crop> specification = createSpecification(criteria);
        return cropRepository.count(specification);
    }

    /**
     * Function to convert {@link CropCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Crop> createSpecification(CropCriteria criteria) {
        Specification<Crop> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Crop_.id),
                    buildStringSpecification(criteria.getCode(), Crop_.code),
                    buildStringSpecification(criteria.getName(), Crop_.name),
                    buildStringSpecification(criteria.getScientificName(), Crop_.scientificName),
                    buildStringSpecification(criteria.getCategory(), Crop_.category),
                    buildSpecification(criteria.getPerennial(), Crop_.perennial),
                    buildSpecification(criteria.getActive(), Crop_.active)
                )
            );
        }
        return specification;
    }
}
