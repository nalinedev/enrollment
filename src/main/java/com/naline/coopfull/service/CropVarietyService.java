package com.naline.coopfull.service;

import com.naline.coopfull.domain.CropVariety;
import com.naline.coopfull.repository.CropVarietyRepository;
import com.naline.coopfull.service.dto.CropVarietyDTO;
import com.naline.coopfull.service.mapper.CropVarietyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.CropVariety}.
 */
@Service
@Transactional
public class CropVarietyService {

    private static final Logger LOG = LoggerFactory.getLogger(CropVarietyService.class);

    private final CropVarietyRepository cropVarietyRepository;

    private final CropVarietyMapper cropVarietyMapper;

    public CropVarietyService(CropVarietyRepository cropVarietyRepository, CropVarietyMapper cropVarietyMapper) {
        this.cropVarietyRepository = cropVarietyRepository;
        this.cropVarietyMapper = cropVarietyMapper;
    }

    /**
     * Save a cropVariety.
     *
     * @param cropVarietyDTO the entity to save.
     * @return the persisted entity.
     */
    public CropVarietyDTO save(CropVarietyDTO cropVarietyDTO) {
        LOG.debug("Request to save CropVariety : {}", cropVarietyDTO);
        CropVariety cropVariety = cropVarietyMapper.toEntity(cropVarietyDTO);
        cropVariety = cropVarietyRepository.save(cropVariety);
        return cropVarietyMapper.toDto(cropVariety);
    }

    /**
     * Update a cropVariety.
     *
     * @param cropVarietyDTO the entity to save.
     * @return the persisted entity.
     */
    public CropVarietyDTO update(CropVarietyDTO cropVarietyDTO) {
        LOG.debug("Request to update CropVariety : {}", cropVarietyDTO);
        CropVariety cropVariety = cropVarietyMapper.toEntity(cropVarietyDTO);
        cropVariety = cropVarietyRepository.save(cropVariety);
        return cropVarietyMapper.toDto(cropVariety);
    }

    /**
     * Partially update a cropVariety.
     *
     * @param cropVarietyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CropVarietyDTO> partialUpdate(CropVarietyDTO cropVarietyDTO) {
        LOG.debug("Request to partially update CropVariety : {}", cropVarietyDTO);

        return cropVarietyRepository
            .findById(cropVarietyDTO.getId())
            .map(existingCropVariety -> {
                cropVarietyMapper.partialUpdate(existingCropVariety, cropVarietyDTO);

                return existingCropVariety;
            })
            .map(cropVarietyRepository::save)
            .map(cropVarietyMapper::toDto);
    }

    /**
     * Get all the cropVarieties with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CropVarietyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cropVarietyRepository.findAllWithEagerRelationships(pageable).map(cropVarietyMapper::toDto);
    }

    /**
     * Get one cropVariety by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CropVarietyDTO> findOne(Long id) {
        LOG.debug("Request to get CropVariety : {}", id);
        return cropVarietyRepository.findOneWithEagerRelationships(id).map(cropVarietyMapper::toDto);
    }

    /**
     * Delete the cropVariety by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CropVariety : {}", id);
        cropVarietyRepository.deleteById(id);
    }
}
