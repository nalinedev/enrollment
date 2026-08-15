package com.naline.coopfull.service;

import com.naline.coopfull.domain.Crop;
import com.naline.coopfull.repository.CropRepository;
import com.naline.coopfull.service.dto.CropDTO;
import com.naline.coopfull.service.mapper.CropMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.Crop}.
 */
@Service
@Transactional
public class CropService {

    private static final Logger LOG = LoggerFactory.getLogger(CropService.class);

    private final CropRepository cropRepository;

    private final CropMapper cropMapper;

    public CropService(CropRepository cropRepository, CropMapper cropMapper) {
        this.cropRepository = cropRepository;
        this.cropMapper = cropMapper;
    }

    /**
     * Save a crop.
     *
     * @param cropDTO the entity to save.
     * @return the persisted entity.
     */
    public CropDTO save(CropDTO cropDTO) {
        LOG.debug("Request to save Crop : {}", cropDTO);
        Crop crop = cropMapper.toEntity(cropDTO);
        crop = cropRepository.save(crop);
        return cropMapper.toDto(crop);
    }

    /**
     * Update a crop.
     *
     * @param cropDTO the entity to save.
     * @return the persisted entity.
     */
    public CropDTO update(CropDTO cropDTO) {
        LOG.debug("Request to update Crop : {}", cropDTO);
        Crop crop = cropMapper.toEntity(cropDTO);
        crop = cropRepository.save(crop);
        return cropMapper.toDto(crop);
    }

    /**
     * Partially update a crop.
     *
     * @param cropDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CropDTO> partialUpdate(CropDTO cropDTO) {
        LOG.debug("Request to partially update Crop : {}", cropDTO);

        return cropRepository
            .findById(cropDTO.getId())
            .map(existingCrop -> {
                cropMapper.partialUpdate(existingCrop, cropDTO);

                return existingCrop;
            })
            .map(cropRepository::save)
            .map(cropMapper::toDto);
    }

    /**
     * Get one crop by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CropDTO> findOne(Long id) {
        LOG.debug("Request to get Crop : {}", id);
        return cropRepository.findById(id).map(cropMapper::toDto);
    }

    /**
     * Delete the crop by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Crop : {}", id);
        cropRepository.deleteById(id);
    }
}
