package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.repository.LocationRepository;
import com.naline.coopfull.service.criteria.LocationCriteria;
import com.naline.coopfull.service.dto.LocationDTO;
import com.naline.coopfull.service.mapper.LocationMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Location} entities in the database.
 * The main input is a {@link LocationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LocationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LocationQueryService extends QueryService<Location> {

    private static final Logger LOG = LoggerFactory.getLogger(LocationQueryService.class);

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    public LocationQueryService(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    /**
     * Return a {@link List} of {@link LocationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LocationDTO> findByCriteria(LocationCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Location> specification = createSpecification(criteria);
        return locationMapper.toDto(locationRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LocationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Location> specification = createSpecification(criteria);
        return locationRepository.count(specification);
    }

    /**
     * Function to convert {@link LocationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Location> createSpecification(LocationCriteria criteria) {
        Specification<Location> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Location_.parent, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Location_.id),
                    buildStringSpecification(criteria.getCode(), Location_.code),
                    buildStringSpecification(criteria.getName(), Location_.name),
                    buildSpecification(criteria.getType(), Location_.type),
                    buildStringSpecification(criteria.getAddressLine1(), Location_.addressLine1),
                    buildStringSpecification(criteria.getAddressLine2(), Location_.addressLine2),
                    buildStringSpecification(criteria.getPostalCode(), Location_.postalCode),
                    buildRangeSpecification(criteria.getLatitude(), Location_.latitude),
                    buildRangeSpecification(criteria.getLongitude(), Location_.longitude),
                    buildStringSpecification(criteria.getDescription(), Location_.description),
                    buildSpecification(criteria.getParentId(), root -> root.join(Location_.parent, JoinType.LEFT).get(Location_.id))
                )
            );
        }
        return specification;
    }
}
