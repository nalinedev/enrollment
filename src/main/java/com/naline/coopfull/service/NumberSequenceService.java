package com.naline.coopfull.service;

import com.naline.coopfull.domain.NumberSequence;
import com.naline.coopfull.repository.NumberSequenceRepository;
import com.naline.coopfull.service.dto.NumberSequenceDTO;
import com.naline.coopfull.service.mapper.NumberSequenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.NumberSequence}.
 */
@Service
@Transactional
public class NumberSequenceService {

    private static final Logger LOG = LoggerFactory.getLogger(NumberSequenceService.class);

    private final NumberSequenceRepository numberSequenceRepository;

    private final NumberSequenceMapper numberSequenceMapper;

    public NumberSequenceService(NumberSequenceRepository numberSequenceRepository, NumberSequenceMapper numberSequenceMapper) {
        this.numberSequenceRepository = numberSequenceRepository;
        this.numberSequenceMapper = numberSequenceMapper;
    }

    /**
     * Save a numberSequence.
     *
     * @param numberSequenceDTO the entity to save.
     * @return the persisted entity.
     */
    public NumberSequenceDTO save(NumberSequenceDTO numberSequenceDTO) {
        LOG.debug("Request to save NumberSequence : {}", numberSequenceDTO);
        NumberSequence numberSequence = numberSequenceMapper.toEntity(numberSequenceDTO);
        numberSequence = numberSequenceRepository.save(numberSequence);
        return numberSequenceMapper.toDto(numberSequence);
    }

    /**
     * Update a numberSequence.
     *
     * @param numberSequenceDTO the entity to save.
     * @return the persisted entity.
     */
    public NumberSequenceDTO update(NumberSequenceDTO numberSequenceDTO) {
        LOG.debug("Request to update NumberSequence : {}", numberSequenceDTO);
        NumberSequence numberSequence = numberSequenceMapper.toEntity(numberSequenceDTO);
        numberSequence = numberSequenceRepository.save(numberSequence);
        return numberSequenceMapper.toDto(numberSequence);
    }

    /**
     * Partially update a numberSequence.
     *
     * @param numberSequenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NumberSequenceDTO> partialUpdate(NumberSequenceDTO numberSequenceDTO) {
        LOG.debug("Request to partially update NumberSequence : {}", numberSequenceDTO);

        return numberSequenceRepository
            .findById(numberSequenceDTO.getId())
            .map(existingNumberSequence -> {
                numberSequenceMapper.partialUpdate(existingNumberSequence, numberSequenceDTO);

                return existingNumberSequence;
            })
            .map(numberSequenceRepository::save)
            .map(numberSequenceMapper::toDto);
    }

    /**
     * Get all the numberSequences with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NumberSequenceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return numberSequenceRepository.findAllWithEagerRelationships(pageable).map(numberSequenceMapper::toDto);
    }

    /**
     * Get one numberSequence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NumberSequenceDTO> findOne(Long id) {
        LOG.debug("Request to get NumberSequence : {}", id);
        return numberSequenceRepository.findOneWithEagerRelationships(id).map(numberSequenceMapper::toDto);
    }

    /**
     * Delete the numberSequence by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NumberSequence : {}", id);
        numberSequenceRepository.deleteById(id);
    }
}
