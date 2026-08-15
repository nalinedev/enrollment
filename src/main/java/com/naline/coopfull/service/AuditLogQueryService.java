package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.AuditLog;
import com.naline.coopfull.repository.AuditLogRepository;
import com.naline.coopfull.service.criteria.AuditLogCriteria;
import com.naline.coopfull.service.dto.AuditLogDTO;
import com.naline.coopfull.service.mapper.AuditLogMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AuditLog} entities in the database.
 * The main input is a {@link AuditLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AuditLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AuditLogQueryService extends QueryService<AuditLog> {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogQueryService.class);

    private final AuditLogRepository auditLogRepository;

    private final AuditLogMapper auditLogMapper;

    public AuditLogQueryService(AuditLogRepository auditLogRepository, AuditLogMapper auditLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
    }

    /**
     * Return a {@link List} of {@link AuditLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AuditLogDTO> findByCriteria(AuditLogCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<AuditLog> specification = createSpecification(criteria);
        return auditLogMapper.toDto(auditLogRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AuditLogCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AuditLog> specification = createSpecification(criteria);
        return auditLogRepository.count(specification);
    }

    /**
     * Function to convert {@link AuditLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AuditLog> createSpecification(AuditLogCriteria criteria) {
        Specification<AuditLog> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(AuditLog_.appUser, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), AuditLog_.id),
                    buildSpecification(criteria.getAction(), AuditLog_.action),
                    buildStringSpecification(criteria.getEntityName(), AuditLog_.entityName),
                    buildStringSpecification(criteria.getEntityId(), AuditLog_.entityId),
                    buildStringSpecification(criteria.getUsername(), AuditLog_.username),
                    buildRangeSpecification(criteria.getCooperativeId(), AuditLog_.cooperativeId),
                    buildRangeSpecification(criteria.getBranchId(), AuditLog_.branchId),
                    buildRangeSpecification(criteria.getTimestamp(), AuditLog_.timestamp),
                    buildStringSpecification(criteria.getIpAddress(), AuditLog_.ipAddress),
                    buildStringSpecification(criteria.getUserAgent(), AuditLog_.userAgent),
                    buildSpecification(criteria.getAppUserId(), root -> root.join(AuditLog_.appUser, JoinType.LEFT).get(AppUser_.id))
                )
            );
        }
        return specification;
    }
}
