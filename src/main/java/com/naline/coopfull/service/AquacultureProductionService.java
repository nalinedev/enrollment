package com.naline.coopfull.service;

import com.naline.coopfull.domain.AquacultureProduction;
import com.naline.coopfull.repository.AquacultureProductionRepository;
import com.naline.coopfull.service.dto.AquacultureProductionDTO;
import com.naline.coopfull.service.mapper.AquacultureProductionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.AquacultureProduction}.
 */
@Service
@Transactional
public class AquacultureProductionService {

    private static final Logger LOG = LoggerFactory.getLogger(AquacultureProductionService.class);

    private final AquacultureProductionRepository aquacultureProductionRepository;

    private final AquacultureProductionMapper aquacultureProductionMapper;

    public AquacultureProductionService(
        AquacultureProductionRepository aquacultureProductionRepository,
        AquacultureProductionMapper aquacultureProductionMapper
    ) {
        this.aquacultureProductionRepository = aquacultureProductionRepository;
        this.aquacultureProductionMapper = aquacultureProductionMapper;
    }

    /**
     * Save a aquacultureProduction.
     *
     * @param aquacultureProductionDTO the entity to save.
     * @return the persisted entity.
     */
    public AquacultureProductionDTO save(AquacultureProductionDTO aquacultureProductionDTO) {
        LOG.debug("Request to save AquacultureProduction : {}", aquacultureProductionDTO);
        AquacultureProduction aquacultureProduction = aquacultureProductionMapper.toEntity(aquacultureProductionDTO);
        aquacultureProduction = aquacultureProductionRepository.save(aquacultureProduction);
        return aquacultureProductionMapper.toDto(aquacultureProduction);
    }

    /**
     * Update a aquacultureProduction.
     *
     * @param aquacultureProductionDTO the entity to save.
     * @return the persisted entity.
     */
    public AquacultureProductionDTO update(AquacultureProductionDTO aquacultureProductionDTO) {
        LOG.debug("Request to update AquacultureProduction : {}", aquacultureProductionDTO);
        AquacultureProduction aquacultureProduction = aquacultureProductionMapper.toEntity(aquacultureProductionDTO);
        aquacultureProduction = aquacultureProductionRepository.save(aquacultureProduction);
        return aquacultureProductionMapper.toDto(aquacultureProduction);
    }

    /**
     * Partially update a aquacultureProduction.
     *
     * @param aquacultureProductionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AquacultureProductionDTO> partialUpdate(AquacultureProductionDTO aquacultureProductionDTO) {
        LOG.debug("Request to partially update AquacultureProduction : {}", aquacultureProductionDTO);

        return aquacultureProductionRepository
            .findById(aquacultureProductionDTO.getId())
            .map(existingAquacultureProduction -> {
                aquacultureProductionMapper.partialUpdate(existingAquacultureProduction, aquacultureProductionDTO);

                return existingAquacultureProduction;
            })
            .map(aquacultureProductionRepository::save)
            .map(aquacultureProductionMapper::toDto);
    }

    /**
     * Get one aquacultureProduction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AquacultureProductionDTO> findOne(Long id) {
        LOG.debug("Request to get AquacultureProduction : {}", id);
        return aquacultureProductionRepository.findById(id).map(aquacultureProductionMapper::toDto);
    }

    /**
     * Delete the aquacultureProduction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AquacultureProduction : {}", id);
        aquacultureProductionRepository.deleteById(id);
    }
}
