package com.naline.coopfull.service;

import com.naline.coopfull.domain.*; // for static metamodels
import com.naline.coopfull.domain.NumberSequence;
import com.naline.coopfull.repository.NumberSequenceRepository;
import com.naline.coopfull.service.criteria.NumberSequenceCriteria;
import com.naline.coopfull.service.dto.NumberSequenceDTO;
import com.naline.coopfull.service.mapper.NumberSequenceMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link NumberSequence} entities in the database.
 * The main input is a {@link NumberSequenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NumberSequenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NumberSequenceQueryService extends QueryService<NumberSequence> {

    private static final Logger LOG = LoggerFactory.getLogger(NumberSequenceQueryService.class);

    private final NumberSequenceRepository numberSequenceRepository;

    private final NumberSequenceMapper numberSequenceMapper;

    public NumberSequenceQueryService(NumberSequenceRepository numberSequenceRepository, NumberSequenceMapper numberSequenceMapper) {
        this.numberSequenceRepository = numberSequenceRepository;
        this.numberSequenceMapper = numberSequenceMapper;
    }

    /**
     * Return a {@link List} of {@link NumberSequenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NumberSequenceDTO> findByCriteria(NumberSequenceCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<NumberSequence> specification = createSpecification(criteria);
        return numberSequenceMapper.toDto(numberSequenceRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NumberSequenceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<NumberSequence> specification = createSpecification(criteria);
        return numberSequenceRepository.count(specification);
    }

    /**
     * Function to convert {@link NumberSequenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NumberSequence> createSpecification(NumberSequenceCriteria criteria) {
        Specification<NumberSequence> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(NumberSequence_.cooperative, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), NumberSequence_.id),
                    buildSpecification(criteria.getSequenceType(), NumberSequence_.sequenceType),
                    buildStringSpecification(criteria.getPrefix(), NumberSequence_.prefix),
                    buildRangeSpecification(criteria.getYear(), NumberSequence_.year),
                    buildRangeSpecification(criteria.getCurrentValue(), NumberSequence_.currentValue),
                    buildRangeSpecification(criteria.getPadding(), NumberSequence_.padding),
                    buildSpecification(criteria.getCooperativeId(), root ->
                        root.join(NumberSequence_.cooperative, JoinType.LEFT).get(Cooperative_.id)
                    )
                )
            );
        }
        return specification;
    }
}
