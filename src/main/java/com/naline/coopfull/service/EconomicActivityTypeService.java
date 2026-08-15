package com.naline.coopfull.service;

import com.naline.coopfull.domain.EconomicActivityType;
import com.naline.coopfull.repository.EconomicActivityTypeRepository;
import com.naline.coopfull.service.dto.EconomicActivityTypeDTO;
import com.naline.coopfull.service.mapper.EconomicActivityTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.EconomicActivityType}.
 */
@Service
@Transactional
public class EconomicActivityTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(EconomicActivityTypeService.class);

    private final EconomicActivityTypeRepository economicActivityTypeRepository;

    private final EconomicActivityTypeMapper economicActivityTypeMapper;

    public EconomicActivityTypeService(
        EconomicActivityTypeRepository economicActivityTypeRepository,
        EconomicActivityTypeMapper economicActivityTypeMapper
    ) {
        this.economicActivityTypeRepository = economicActivityTypeRepository;
        this.economicActivityTypeMapper = economicActivityTypeMapper;
    }

    /**
     * Save a economicActivityType.
     *
     * @param economicActivityTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public EconomicActivityTypeDTO save(EconomicActivityTypeDTO economicActivityTypeDTO) {
        LOG.debug("Request to save EconomicActivityType : {}", economicActivityTypeDTO);
        EconomicActivityType economicActivityType = economicActivityTypeMapper.toEntity(economicActivityTypeDTO);
        economicActivityType = economicActivityTypeRepository.save(economicActivityType);
        return economicActivityTypeMapper.toDto(economicActivityType);
    }

    /**
     * Update a economicActivityType.
     *
     * @param economicActivityTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public EconomicActivityTypeDTO update(EconomicActivityTypeDTO economicActivityTypeDTO) {
        LOG.debug("Request to update EconomicActivityType : {}", economicActivityTypeDTO);
        EconomicActivityType economicActivityType = economicActivityTypeMapper.toEntity(economicActivityTypeDTO);
        economicActivityType = economicActivityTypeRepository.save(economicActivityType);
        return economicActivityTypeMapper.toDto(economicActivityType);
    }

    /**
     * Partially update a economicActivityType.
     *
     * @param economicActivityTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EconomicActivityTypeDTO> partialUpdate(EconomicActivityTypeDTO economicActivityTypeDTO) {
        LOG.debug("Request to partially update EconomicActivityType : {}", economicActivityTypeDTO);

        return economicActivityTypeRepository
            .findById(economicActivityTypeDTO.getId())
            .map(existingEconomicActivityType -> {
                economicActivityTypeMapper.partialUpdate(existingEconomicActivityType, economicActivityTypeDTO);

                return existingEconomicActivityType;
            })
            .map(economicActivityTypeRepository::save)
            .map(economicActivityTypeMapper::toDto);
    }

    /**
     * Get one economicActivityType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EconomicActivityTypeDTO> findOne(Long id) {
        LOG.debug("Request to get EconomicActivityType : {}", id);
        return economicActivityTypeRepository.findById(id).map(economicActivityTypeMapper::toDto);
    }

    /**
     * Delete the economicActivityType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EconomicActivityType : {}", id);
        economicActivityTypeRepository.deleteById(id);
    }
}
