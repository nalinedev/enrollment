package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.CooperativeUser;
import com.naline.coopfull.repository.CooperativeUserRepository;
import com.naline.coopfull.service.criteria.CooperativeUserCriteria;
import com.naline.coopfull.service.dto.CooperativeUserDTO;
import com.naline.coopfull.service.mapper.CooperativeUserMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link CooperativeUser} entities in the database.
 * The main input is a {@link CooperativeUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CooperativeUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CooperativeUserQueryService extends QueryService<CooperativeUser> {

    private static final Logger LOG = LoggerFactory.getLogger(CooperativeUserQueryService.class);

    private final CooperativeUserRepository cooperativeUserRepository;

    private final CooperativeUserMapper cooperativeUserMapper;

    public CooperativeUserQueryService(CooperativeUserRepository cooperativeUserRepository, CooperativeUserMapper cooperativeUserMapper) {
        this.cooperativeUserRepository = cooperativeUserRepository;
        this.cooperativeUserMapper = cooperativeUserMapper;
    }

    /**
     * Return a {@link List} of {@link CooperativeUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CooperativeUserDTO> findByCriteria(CooperativeUserCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<CooperativeUser> specification = createSpecification(criteria);
        return cooperativeUserMapper.toDto(cooperativeUserRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CooperativeUserCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<CooperativeUser> specification = createSpecification(criteria);
        return cooperativeUserRepository.count(specification);
    }

    /**
     * Function to convert {@link CooperativeUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CooperativeUser> createSpecification(CooperativeUserCriteria criteria) {
        Specification<CooperativeUser> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(CooperativeUser_.appUser, JoinType.LEFT);
                root.fetch(CooperativeUser_.cooperative, JoinType.LEFT);
                root.fetch(CooperativeUser_.role, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), CooperativeUser_.id),
                    buildRangeSpecification(criteria.getStartDate(), CooperativeUser_.startDate),
                    buildRangeSpecification(criteria.getEndDate(), CooperativeUser_.endDate),
                    buildSpecification(criteria.getActive(), CooperativeUser_.active),
                    buildSpecification(criteria.getAppUserId(), root ->
                        root.join(CooperativeUser_.appUser, JoinType.LEFT).get(AppUser_.id)
                    ),
                    buildSpecification(criteria.getCooperativeId(), root ->
                        root.join(CooperativeUser_.cooperative, JoinType.LEFT).get(Cooperative_.id)
                    ),
                    buildSpecification(criteria.getRoleId(), root ->
                        root.join(CooperativeUser_.role, JoinType.LEFT).get(CooperativeRole_.id)
                    )
                )
            );
        }
        return specification;
    }
}
