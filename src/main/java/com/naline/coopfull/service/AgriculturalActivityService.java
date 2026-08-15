package com.naline.coopfull.service;

import com.naline.coopfull.domain.AgriculturalActivity;
import com.naline.coopfull.repository.AgriculturalActivityRepository;
import com.naline.coopfull.service.dto.AgriculturalActivityDTO;
import com.naline.coopfull.service.mapper.AgriculturalActivityMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.AgriculturalActivity}.
 */
@Service
@Transactional
public class AgriculturalActivityService {

    private static final Logger LOG = LoggerFactory.getLogger(AgriculturalActivityService.class);

    private final AgriculturalActivityRepository agriculturalActivityRepository;

    private final AgriculturalActivityMapper agriculturalActivityMapper;

    public AgriculturalActivityService(
        AgriculturalActivityRepository agriculturalActivityRepository,
        AgriculturalActivityMapper agriculturalActivityMapper
    ) {
        this.agriculturalActivityRepository = agriculturalActivityRepository;
        this.agriculturalActivityMapper = agriculturalActivityMapper;
    }

    /**
     * Save a agriculturalActivity.
     *
     * @param agriculturalActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public AgriculturalActivityDTO save(AgriculturalActivityDTO agriculturalActivityDTO) {
        LOG.debug("Request to save AgriculturalActivity : {}", agriculturalActivityDTO);
        AgriculturalActivity agriculturalActivity = agriculturalActivityMapper.toEntity(agriculturalActivityDTO);
        agriculturalActivity = agriculturalActivityRepository.save(agriculturalActivity);
        return agriculturalActivityMapper.toDto(agriculturalActivity);
    }

    /**
     * Update a agriculturalActivity.
     *
     * @param agriculturalActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public AgriculturalActivityDTO update(AgriculturalActivityDTO agriculturalActivityDTO) {
        LOG.debug("Request to update AgriculturalActivity : {}", agriculturalActivityDTO);
        AgriculturalActivity agriculturalActivity = agriculturalActivityMapper.toEntity(agriculturalActivityDTO);
        agriculturalActivity = agriculturalActivityRepository.save(agriculturalActivity);
        return agriculturalActivityMapper.toDto(agriculturalActivity);
    }

    /**
     * Partially update a agriculturalActivity.
     *
     * @param agriculturalActivityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AgriculturalActivityDTO> partialUpdate(AgriculturalActivityDTO agriculturalActivityDTO) {
        LOG.debug("Request to partially update AgriculturalActivity : {}", agriculturalActivityDTO);

        return agriculturalActivityRepository
            .findById(agriculturalActivityDTO.getId())
            .map(existingAgriculturalActivity -> {
                agriculturalActivityMapper.partialUpdate(existingAgriculturalActivity, agriculturalActivityDTO);

                return existingAgriculturalActivity;
            })
            .map(agriculturalActivityRepository::save)
            .map(agriculturalActivityMapper::toDto);
    }

    /**
     * Get all the agriculturalActivities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AgriculturalActivityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return agriculturalActivityRepository.findAllWithEagerRelationships(pageable).map(agriculturalActivityMapper::toDto);
    }

    /**
     *  Get all the agriculturalActivities where EconomicActivity is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AgriculturalActivityDTO> findAllWhereEconomicActivityIsNull() {
        LOG.debug("Request to get all agriculturalActivities where EconomicActivity is null");
        return StreamSupport.stream(agriculturalActivityRepository.findAll().spliterator(), false)
            .filter(agriculturalActivity -> agriculturalActivity.getEconomicActivity() == null)
            .map(agriculturalActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one agriculturalActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgriculturalActivityDTO> findOne(Long id) {
        LOG.debug("Request to get AgriculturalActivity : {}", id);
        return agriculturalActivityRepository.findOneWithEagerRelationships(id).map(agriculturalActivityMapper::toDto);
    }

    /**
     * Delete the agriculturalActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AgriculturalActivity : {}", id);
        agriculturalActivityRepository.deleteById(id);
    }
}
