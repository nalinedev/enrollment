package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.SocialProfile;
import com.naline.coopfull.repository.SocialProfileRepository;
import com.naline.coopfull.service.criteria.SocialProfileCriteria;
import com.naline.coopfull.service.dto.SocialProfileDTO;
import com.naline.coopfull.service.mapper.SocialProfileMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SocialProfile} entities in the database.
 * The main input is a {@link SocialProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SocialProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SocialProfileQueryService extends QueryService<SocialProfile> {

    private static final Logger LOG = LoggerFactory.getLogger(SocialProfileQueryService.class);

    private final SocialProfileRepository socialProfileRepository;

    private final SocialProfileMapper socialProfileMapper;

    public SocialProfileQueryService(SocialProfileRepository socialProfileRepository, SocialProfileMapper socialProfileMapper) {
        this.socialProfileRepository = socialProfileRepository;
        this.socialProfileMapper = socialProfileMapper;
    }

    /**
     * Return a {@link List} of {@link SocialProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SocialProfileDTO> findByCriteria(SocialProfileCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<SocialProfile> specification = createSpecification(criteria);
        return socialProfileMapper.toDto(socialProfileRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SocialProfileCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SocialProfile> specification = createSpecification(criteria);
        return socialProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link SocialProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SocialProfile> createSpecification(SocialProfileCriteria criteria) {
        Specification<SocialProfile> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), SocialProfile_.id),
                    buildSpecification(criteria.getMaritalStatus(), SocialProfile_.maritalStatus),
                    buildRangeSpecification(criteria.getNumberOfChildren(), SocialProfile_.numberOfChildren),
                    buildRangeSpecification(criteria.getNumberOfDependents(), SocialProfile_.numberOfDependents),
                    buildStringSpecification(criteria.getEducationLevel(), SocialProfile_.educationLevel),
                    buildStringSpecification(criteria.getHousingStatus(), SocialProfile_.housingStatus),
                    buildRangeSpecification(criteria.getResidenceSince(), SocialProfile_.residenceSince),
                    buildSpecification(criteria.getDisabilityStatus(), SocialProfile_.disabilityStatus),
                    buildStringSpecification(criteria.getSocialCategory(), SocialProfile_.socialCategory),
                    buildSpecification(criteria.getMemberId(), root -> root.join(SocialProfile_.member, JoinType.LEFT).get(Member_.id))
                )
            );
        }
        return specification;
    }
}
