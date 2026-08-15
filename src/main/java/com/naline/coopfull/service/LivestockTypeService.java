package com.naline.coopfull.service;

import com.naline.coopfull.domain.LivestockType;
import com.naline.coopfull.repository.LivestockTypeRepository;
import com.naline.coopfull.service.dto.LivestockTypeDTO;
import com.naline.coopfull.service.mapper.LivestockTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.LivestockType}.
 */
@Service
@Transactional
public class LivestockTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(LivestockTypeService.class);

    private final LivestockTypeRepository livestockTypeRepository;

    private final LivestockTypeMapper livestockTypeMapper;

    public LivestockTypeService(LivestockTypeRepository livestockTypeRepository, LivestockTypeMapper livestockTypeMapper) {
        this.livestockTypeRepository = livestockTypeRepository;
        this.livestockTypeMapper = livestockTypeMapper;
    }

    /**
     * Save a livestockType.
     *
     * @param livestockTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public LivestockTypeDTO save(LivestockTypeDTO livestockTypeDTO) {
        LOG.debug("Request to save LivestockType : {}", livestockTypeDTO);
        LivestockType livestockType = livestockTypeMapper.toEntity(livestockTypeDTO);
        livestockType = livestockTypeRepository.save(livestockType);
        return livestockTypeMapper.toDto(livestockType);
    }

    /**
     * Update a livestockType.
     *
     * @param livestockTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public LivestockTypeDTO update(LivestockTypeDTO livestockTypeDTO) {
        LOG.debug("Request to update LivestockType : {}", livestockTypeDTO);
        LivestockType livestockType = livestockTypeMapper.toEntity(livestockTypeDTO);
        livestockType = livestockTypeRepository.save(livestockType);
        return livestockTypeMapper.toDto(livestockType);
    }

    /**
     * Partially update a livestockType.
     *
     * @param livestockTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LivestockTypeDTO> partialUpdate(LivestockTypeDTO livestockTypeDTO) {
        LOG.debug("Request to partially update LivestockType : {}", livestockTypeDTO);

        return livestockTypeRepository
            .findById(livestockTypeDTO.getId())
            .map(existingLivestockType -> {
                livestockTypeMapper.partialUpdate(existingLivestockType, livestockTypeDTO);

                return existingLivestockType;
            })
            .map(livestockTypeRepository::save)
            .map(livestockTypeMapper::toDto);
    }

    /**
     * Get one livestockType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LivestockTypeDTO> findOne(Long id) {
        LOG.debug("Request to get LivestockType : {}", id);
        return livestockTypeRepository.findById(id).map(livestockTypeMapper::toDto);
    }

    /**
     * Delete the livestockType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LivestockType : {}", id);
        livestockTypeRepository.deleteById(id);
    }
}
