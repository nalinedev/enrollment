package com.naline.coopfull.service;

import com.naline.coopfull.domain.LivestockActivity;
import com.naline.coopfull.repository.LivestockActivityRepository;
import com.naline.coopfull.service.dto.LivestockActivityDTO;
import com.naline.coopfull.service.mapper.LivestockActivityMapper;
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
 * Service Implementation for managing {@link com.naline.coopfull.domain.LivestockActivity}.
 */
@Service
@Transactional
public class LivestockActivityService {

    private static final Logger LOG = LoggerFactory.getLogger(LivestockActivityService.class);

    private final LivestockActivityRepository livestockActivityRepository;

    private final LivestockActivityMapper livestockActivityMapper;

    public LivestockActivityService(
        LivestockActivityRepository livestockActivityRepository,
        LivestockActivityMapper livestockActivityMapper
    ) {
        this.livestockActivityRepository = livestockActivityRepository;
        this.livestockActivityMapper = livestockActivityMapper;
    }

    /**
     * Save a livestockActivity.
     *
     * @param livestockActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public LivestockActivityDTO save(LivestockActivityDTO livestockActivityDTO) {
        LOG.debug("Request to save LivestockActivity : {}", livestockActivityDTO);
        LivestockActivity livestockActivity = livestockActivityMapper.toEntity(livestockActivityDTO);
        livestockActivity = livestockActivityRepository.save(livestockActivity);
        return livestockActivityMapper.toDto(livestockActivity);
    }

    /**
     * Update a livestockActivity.
     *
     * @param livestockActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public LivestockActivityDTO update(LivestockActivityDTO livestockActivityDTO) {
        LOG.debug("Request to update LivestockActivity : {}", livestockActivityDTO);
        LivestockActivity livestockActivity = livestockActivityMapper.toEntity(livestockActivityDTO);
        livestockActivity = livestockActivityRepository.save(livestockActivity);
        return livestockActivityMapper.toDto(livestockActivity);
    }

    /**
     * Partially update a livestockActivity.
     *
     * @param livestockActivityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LivestockActivityDTO> partialUpdate(LivestockActivityDTO livestockActivityDTO) {
        LOG.debug("Request to partially update LivestockActivity : {}", livestockActivityDTO);

        return livestockActivityRepository
            .findById(livestockActivityDTO.getId())
            .map(existingLivestockActivity -> {
                livestockActivityMapper.partialUpdate(existingLivestockActivity, livestockActivityDTO);

                return existingLivestockActivity;
            })
            .map(livestockActivityRepository::save)
            .map(livestockActivityMapper::toDto);
    }

    /**
     * Get all the livestockActivities with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LivestockActivityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return livestockActivityRepository.findAllWithEagerRelationships(pageable).map(livestockActivityMapper::toDto);
    }

    /**
     *  Get all the livestockActivities where EconomicActivity is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LivestockActivityDTO> findAllWhereEconomicActivityIsNull() {
        LOG.debug("Request to get all livestockActivities where EconomicActivity is null");
        return StreamSupport.stream(livestockActivityRepository.findAll().spliterator(), false)
            .filter(livestockActivity -> livestockActivity.getEconomicActivity() == null)
            .map(livestockActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one livestockActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LivestockActivityDTO> findOne(Long id) {
        LOG.debug("Request to get LivestockActivity : {}", id);
        return livestockActivityRepository.findOneWithEagerRelationships(id).map(livestockActivityMapper::toDto);
    }

    /**
     * Delete the livestockActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LivestockActivity : {}", id);
        livestockActivityRepository.deleteById(id);
    }
}
