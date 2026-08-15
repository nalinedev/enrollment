package com.naline.coopfull.service;

import com.naline.coopfull.domain.AquaticSpecies;
import com.naline.coopfull.repository.AquaticSpeciesRepository;
import com.naline.coopfull.service.dto.AquaticSpeciesDTO;
import com.naline.coopfull.service.mapper.AquaticSpeciesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.AquaticSpecies}.
 */
@Service
@Transactional
public class AquaticSpeciesService {

    private static final Logger LOG = LoggerFactory.getLogger(AquaticSpeciesService.class);

    private final AquaticSpeciesRepository aquaticSpeciesRepository;

    private final AquaticSpeciesMapper aquaticSpeciesMapper;

    public AquaticSpeciesService(AquaticSpeciesRepository aquaticSpeciesRepository, AquaticSpeciesMapper aquaticSpeciesMapper) {
        this.aquaticSpeciesRepository = aquaticSpeciesRepository;
        this.aquaticSpeciesMapper = aquaticSpeciesMapper;
    }

    /**
     * Save a aquaticSpecies.
     *
     * @param aquaticSpeciesDTO the entity to save.
     * @return the persisted entity.
     */
    public AquaticSpeciesDTO save(AquaticSpeciesDTO aquaticSpeciesDTO) {
        LOG.debug("Request to save AquaticSpecies : {}", aquaticSpeciesDTO);
        AquaticSpecies aquaticSpecies = aquaticSpeciesMapper.toEntity(aquaticSpeciesDTO);
        aquaticSpecies = aquaticSpeciesRepository.save(aquaticSpecies);
        return aquaticSpeciesMapper.toDto(aquaticSpecies);
    }

    /**
     * Update a aquaticSpecies.
     *
     * @param aquaticSpeciesDTO the entity to save.
     * @return the persisted entity.
     */
    public AquaticSpeciesDTO update(AquaticSpeciesDTO aquaticSpeciesDTO) {
        LOG.debug("Request to update AquaticSpecies : {}", aquaticSpeciesDTO);
        AquaticSpecies aquaticSpecies = aquaticSpeciesMapper.toEntity(aquaticSpeciesDTO);
        aquaticSpecies = aquaticSpeciesRepository.save(aquaticSpecies);
        return aquaticSpeciesMapper.toDto(aquaticSpecies);
    }

    /**
     * Partially update a aquaticSpecies.
     *
     * @param aquaticSpeciesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AquaticSpeciesDTO> partialUpdate(AquaticSpeciesDTO aquaticSpeciesDTO) {
        LOG.debug("Request to partially update AquaticSpecies : {}", aquaticSpeciesDTO);

        return aquaticSpeciesRepository
            .findById(aquaticSpeciesDTO.getId())
            .map(existingAquaticSpecies -> {
                aquaticSpeciesMapper.partialUpdate(existingAquaticSpecies, aquaticSpeciesDTO);

                return existingAquaticSpecies;
            })
            .map(aquaticSpeciesRepository::save)
            .map(aquaticSpeciesMapper::toDto);
    }

    /**
     * Get one aquaticSpecies by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AquaticSpeciesDTO> findOne(Long id) {
        LOG.debug("Request to get AquaticSpecies : {}", id);
        return aquaticSpeciesRepository.findById(id).map(aquaticSpeciesMapper::toDto);
    }

    /**
     * Delete the aquaticSpecies by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AquaticSpecies : {}", id);
        aquaticSpeciesRepository.deleteById(id);
    }
}
