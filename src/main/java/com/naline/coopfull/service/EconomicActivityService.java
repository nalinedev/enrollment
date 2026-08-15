package com.naline.coopfull.service;

import com.naline.coopfull.domain.EconomicActivity;
import com.naline.coopfull.repository.EconomicActivityRepository;
import com.naline.coopfull.service.dto.EconomicActivityDTO;
import com.naline.coopfull.service.mapper.EconomicActivityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.EconomicActivity}.
 */
@Service
@Transactional
public class EconomicActivityService {

    private static final Logger LOG = LoggerFactory.getLogger(EconomicActivityService.class);

    private final EconomicActivityRepository economicActivityRepository;

    private final EconomicActivityMapper economicActivityMapper;

    public EconomicActivityService(EconomicActivityRepository economicActivityRepository, EconomicActivityMapper economicActivityMapper) {
        this.economicActivityRepository = economicActivityRepository;
        this.economicActivityMapper = economicActivityMapper;
    }

    /**
     * Save a economicActivity.
     *
     * @param economicActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public EconomicActivityDTO save(EconomicActivityDTO economicActivityDTO) {
        LOG.debug("Request to save EconomicActivity : {}", economicActivityDTO);
        EconomicActivity economicActivity = economicActivityMapper.toEntity(economicActivityDTO);
        economicActivity = economicActivityRepository.save(economicActivity);
        return economicActivityMapper.toDto(economicActivity);
    }

    /**
     * Update a economicActivity.
     *
     * @param economicActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public EconomicActivityDTO update(EconomicActivityDTO economicActivityDTO) {
        LOG.debug("Request to update EconomicActivity : {}", economicActivityDTO);
        EconomicActivity economicActivity = economicActivityMapper.toEntity(economicActivityDTO);
        economicActivity = economicActivityRepository.save(economicActivity);
        return economicActivityMapper.toDto(economicActivity);
    }

    /**
     * Partially update a economicActivity.
     *
     * @param economicActivityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EconomicActivityDTO> partialUpdate(EconomicActivityDTO economicActivityDTO) {
        LOG.debug("Request to partially update EconomicActivity : {}", economicActivityDTO);

        return economicActivityRepository
            .findById(economicActivityDTO.getId())
            .map(existingEconomicActivity -> {
                economicActivityMapper.partialUpdate(existingEconomicActivity, economicActivityDTO);

                return existingEconomicActivity;
            })
            .map(economicActivityRepository::save)
            .map(economicActivityMapper::toDto);
    }

    /**
     * Get all the economicActivities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EconomicActivityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return economicActivityRepository.findAllWithEagerRelationships(pageable).map(economicActivityMapper::toDto);
    }

    /**
     * Get one economicActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EconomicActivityDTO> findOne(Long id) {
        LOG.debug("Request to get EconomicActivity : {}", id);
        return economicActivityRepository.findOneWithEagerRelationships(id).map(economicActivityMapper::toDto);
    }

    /**
     * Delete the economicActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EconomicActivity : {}", id);
        economicActivityRepository.deleteById(id);
    }
}
