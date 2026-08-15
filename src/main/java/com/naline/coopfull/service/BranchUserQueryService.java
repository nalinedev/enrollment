package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.BranchUser;
import com.naline.coopfull.repository.BranchUserRepository;
import com.naline.coopfull.service.criteria.BranchUserCriteria;
import com.naline.coopfull.service.dto.BranchUserDTO;
import com.naline.coopfull.service.mapper.BranchUserMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link BranchUser} entities in the database.
 * The main input is a {@link BranchUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BranchUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BranchUserQueryService extends QueryService<BranchUser> {

    private static final Logger LOG = LoggerFactory.getLogger(BranchUserQueryService.class);

    private final BranchUserRepository branchUserRepository;

    private final BranchUserMapper branchUserMapper;

    public BranchUserQueryService(BranchUserRepository branchUserRepository, BranchUserMapper branchUserMapper) {
        this.branchUserRepository = branchUserRepository;
        this.branchUserMapper = branchUserMapper;
    }

    /**
     * Return a {@link List} of {@link BranchUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BranchUserDTO> findByCriteria(BranchUserCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<BranchUser> specification = createSpecification(criteria);
        return branchUserMapper.toDto(branchUserRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BranchUserCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<BranchUser> specification = createSpecification(criteria);
        return branchUserRepository.count(specification);
    }

    /**
     * Function to convert {@link BranchUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BranchUser> createSpecification(BranchUserCriteria criteria) {
        Specification<BranchUser> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(BranchUser_.appUser, JoinType.LEFT);
                root.fetch(BranchUser_.branch, JoinType.LEFT);
                root.fetch(BranchUser_.role, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), BranchUser_.id),
                    buildRangeSpecification(criteria.getStartDate(), BranchUser_.startDate),
                    buildRangeSpecification(criteria.getEndDate(), BranchUser_.endDate),
                    buildSpecification(criteria.getActive(), BranchUser_.active),
                    buildSpecification(criteria.getAppUserId(), root -> root.join(BranchUser_.appUser, JoinType.LEFT).get(AppUser_.id)),
                    buildSpecification(criteria.getBranchId(), root ->
                        root.join(BranchUser_.branch, JoinType.LEFT).get(CooperativeBranch_.id)
                    ),
                    buildSpecification(criteria.getRoleId(), root -> root.join(BranchUser_.role, JoinType.LEFT).get(CooperativeRole_.id))
                )
            );
        }
        return specification;
    }
}
