package com.naline.coopfull.service;

import com.naline.coopfull.domain.AgriculturalProduction;
import com.naline.coopfull.repository.AgriculturalProductionRepository;
import com.naline.coopfull.service.dto.AgriculturalProductionDTO;
import com.naline.coopfull.service.mapper.AgriculturalProductionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.AgriculturalProduction}.
 */
@Service
@Transactional
public class AgriculturalProductionService {

    private static final Logger LOG = LoggerFactory.getLogger(AgriculturalProductionService.class);

    private final AgriculturalProductionRepository agriculturalProductionRepository;

    private final AgriculturalProductionMapper agriculturalProductionMapper;

    public AgriculturalProductionService(
        AgriculturalProductionRepository agriculturalProductionRepository,
        AgriculturalProductionMapper agriculturalProductionMapper
    ) {
        this.agriculturalProductionRepository = agriculturalProductionRepository;
        this.agriculturalProductionMapper = agriculturalProductionMapper;
    }

    /**
     * Save a agriculturalProduction.
     *
     * @param agriculturalProductionDTO the entity to save.
     * @return the persisted entity.
     */
    public AgriculturalProductionDTO save(AgriculturalProductionDTO agriculturalProductionDTO) {
        LOG.debug("Request to save AgriculturalProduction : {}", agriculturalProductionDTO);
        AgriculturalProduction agriculturalProduction = agriculturalProductionMapper.toEntity(agriculturalProductionDTO);
        agriculturalProduction = agriculturalProductionRepository.save(agriculturalProduction);
        return agriculturalProductionMapper.toDto(agriculturalProduction);
    }

    /**
     * Update a agriculturalProduction.
     *
     * @param agriculturalProductionDTO the entity to save.
     * @return the persisted entity.
     */
    public AgriculturalProductionDTO update(AgriculturalProductionDTO agriculturalProductionDTO) {
        LOG.debug("Request to update AgriculturalProduction : {}", agriculturalProductionDTO);
        AgriculturalProduction agriculturalProduction = agriculturalProductionMapper.toEntity(agriculturalProductionDTO);
        agriculturalProduction = agriculturalProductionRepository.save(agriculturalProduction);
        return agriculturalProductionMapper.toDto(agriculturalProduction);
    }

    /**
     * Partially update a agriculturalProduction.
     *
     * @param agriculturalProductionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AgriculturalProductionDTO> partialUpdate(AgriculturalProductionDTO agriculturalProductionDTO) {
        LOG.debug("Request to partially update AgriculturalProduction : {}", agriculturalProductionDTO);

        return agriculturalProductionRepository
            .findById(agriculturalProductionDTO.getId())
            .map(existingAgriculturalProduction -> {
                agriculturalProductionMapper.partialUpdate(existingAgriculturalProduction, agriculturalProductionDTO);

                return existingAgriculturalProduction;
            })
            .map(agriculturalProductionRepository::save)
            .map(agriculturalProductionMapper::toDto);
    }

    /**
     * Get all the agriculturalProductions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AgriculturalProductionDTO> findAllWithEagerRelationships(Pageable pageable) {
        return agriculturalProductionRepository.findAllWithEagerRelationships(pageable).map(agriculturalProductionMapper::toDto);
    }

    /**
     * Get one agriculturalProduction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgriculturalProductionDTO> findOne(Long id) {
        LOG.debug("Request to get AgriculturalProduction : {}", id);
        return agriculturalProductionRepository.findOneWithEagerRelationships(id).map(agriculturalProductionMapper::toDto);
    }

    /**
     * Delete the agriculturalProduction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AgriculturalProduction : {}", id);
        agriculturalProductionRepository.deleteById(id);
    }
}
