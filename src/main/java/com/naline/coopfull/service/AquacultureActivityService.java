package com.naline.coopfull.service;

import com.naline.coopfull.domain.AquacultureActivity;
import com.naline.coopfull.repository.AquacultureActivityRepository;
import com.naline.coopfull.service.dto.AquacultureActivityDTO;
import com.naline.coopfull.service.mapper.AquacultureActivityMapper;
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
 * Service Implementation for managing {@link com.naline.coopfull.domain.AquacultureActivity}.
 */
@Service
@Transactional
public class AquacultureActivityService {

    private static final Logger LOG = LoggerFactory.getLogger(AquacultureActivityService.class);

    private final AquacultureActivityRepository aquacultureActivityRepository;

    private final AquacultureActivityMapper aquacultureActivityMapper;

    public AquacultureActivityService(
        AquacultureActivityRepository aquacultureActivityRepository,
        AquacultureActivityMapper aquacultureActivityMapper
    ) {
        this.aquacultureActivityRepository = aquacultureActivityRepository;
        this.aquacultureActivityMapper = aquacultureActivityMapper;
    }

    /**
     * Save a aquacultureActivity.
     *
     * @param aquacultureActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public AquacultureActivityDTO save(AquacultureActivityDTO aquacultureActivityDTO) {
        LOG.debug("Request to save AquacultureActivity : {}", aquacultureActivityDTO);
        AquacultureActivity aquacultureActivity = aquacultureActivityMapper.toEntity(aquacultureActivityDTO);
        aquacultureActivity = aquacultureActivityRepository.save(aquacultureActivity);
        return aquacultureActivityMapper.toDto(aquacultureActivity);
    }

    /**
     * Update a aquacultureActivity.
     *
     * @param aquacultureActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public AquacultureActivityDTO update(AquacultureActivityDTO aquacultureActivityDTO) {
        LOG.debug("Request to update AquacultureActivity : {}", aquacultureActivityDTO);
        AquacultureActivity aquacultureActivity = aquacultureActivityMapper.toEntity(aquacultureActivityDTO);
        aquacultureActivity = aquacultureActivityRepository.save(aquacultureActivity);
        return aquacultureActivityMapper.toDto(aquacultureActivity);
    }

    /**
     * Partially update a aquacultureActivity.
     *
     * @param aquacultureActivityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AquacultureActivityDTO> partialUpdate(AquacultureActivityDTO aquacultureActivityDTO) {
        LOG.debug("Request to partially update AquacultureActivity : {}", aquacultureActivityDTO);

        return aquacultureActivityRepository
            .findById(aquacultureActivityDTO.getId())
            .map(existingAquacultureActivity -> {
                aquacultureActivityMapper.partialUpdate(existingAquacultureActivity, aquacultureActivityDTO);

                return existingAquacultureActivity;
            })
            .map(aquacultureActivityRepository::save)
            .map(aquacultureActivityMapper::toDto);
    }

    /**
     * Get all the aquacultureActivities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AquacultureActivityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return aquacultureActivityRepository.findAllWithEagerRelationships(pageable).map(aquacultureActivityMapper::toDto);
    }

    /**
     *  Get all the aquacultureActivities where EconomicActivity is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AquacultureActivityDTO> findAllWhereEconomicActivityIsNull() {
        LOG.debug("Request to get all aquacultureActivities where EconomicActivity is null");
        return StreamSupport.stream(aquacultureActivityRepository.findAll().spliterator(), false)
            .filter(aquacultureActivity -> aquacultureActivity.getEconomicActivity() == null)
            .map(aquacultureActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one aquacultureActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AquacultureActivityDTO> findOne(Long id) {
        LOG.debug("Request to get AquacultureActivity : {}", id);
        return aquacultureActivityRepository.findOneWithEagerRelationships(id).map(aquacultureActivityMapper::toDto);
    }

    /**
     * Delete the aquacultureActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AquacultureActivity : {}", id);
        aquacultureActivityRepository.deleteById(id);
    }
}
