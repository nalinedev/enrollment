package com.naline.coopfull.service;

import com.naline.coopfull.domain.LivestockProduction;
import com.naline.coopfull.repository.LivestockProductionRepository;
import com.naline.coopfull.service.dto.LivestockProductionDTO;
import com.naline.coopfull.service.mapper.LivestockProductionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.LivestockProduction}.
 */
@Service
@Transactional
public class LivestockProductionService {

    private static final Logger LOG = LoggerFactory.getLogger(LivestockProductionService.class);

    private final LivestockProductionRepository livestockProductionRepository;

    private final LivestockProductionMapper livestockProductionMapper;

    public LivestockProductionService(
        LivestockProductionRepository livestockProductionRepository,
        LivestockProductionMapper livestockProductionMapper
    ) {
        this.livestockProductionRepository = livestockProductionRepository;
        this.livestockProductionMapper = livestockProductionMapper;
    }

    /**
     * Save a livestockProduction.
     *
     * @param livestockProductionDTO the entity to save.
     * @return the persisted entity.
     */
    public LivestockProductionDTO save(LivestockProductionDTO livestockProductionDTO) {
        LOG.debug("Request to save LivestockProduction : {}", livestockProductionDTO);
        LivestockProduction livestockProduction = livestockProductionMapper.toEntity(livestockProductionDTO);
        livestockProduction = livestockProductionRepository.save(livestockProduction);
        return livestockProductionMapper.toDto(livestockProduction);
    }

    /**
     * Update a livestockProduction.
     *
     * @param livestockProductionDTO the entity to save.
     * @return the persisted entity.
     */
    public LivestockProductionDTO update(LivestockProductionDTO livestockProductionDTO) {
        LOG.debug("Request to update LivestockProduction : {}", livestockProductionDTO);
        LivestockProduction livestockProduction = livestockProductionMapper.toEntity(livestockProductionDTO);
        livestockProduction = livestockProductionRepository.save(livestockProduction);
        return livestockProductionMapper.toDto(livestockProduction);
    }

    /**
     * Partially update a livestockProduction.
     *
     * @param livestockProductionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LivestockProductionDTO> partialUpdate(LivestockProductionDTO livestockProductionDTO) {
        LOG.debug("Request to partially update LivestockProduction : {}", livestockProductionDTO);

        return livestockProductionRepository
            .findById(livestockProductionDTO.getId())
            .map(existingLivestockProduction -> {
                livestockProductionMapper.partialUpdate(existingLivestockProduction, livestockProductionDTO);

                return existingLivestockProduction;
            })
            .map(livestockProductionRepository::save)
            .map(livestockProductionMapper::toDto);
    }

    /**
     * Get one livestockProduction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LivestockProductionDTO> findOne(Long id) {
        LOG.debug("Request to get LivestockProduction : {}", id);
        return livestockProductionRepository.findById(id).map(livestockProductionMapper::toDto);
    }

    /**
     * Delete the livestockProduction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LivestockProduction : {}", id);
        livestockProductionRepository.deleteById(id);
    }
}
