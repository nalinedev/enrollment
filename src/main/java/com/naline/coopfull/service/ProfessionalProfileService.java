package com.naline.coopfull.service;

import com.naline.coopfull.domain.ProfessionalProfile;
import com.naline.coopfull.repository.ProfessionalProfileRepository;
import com.naline.coopfull.service.dto.ProfessionalProfileDTO;
import com.naline.coopfull.service.mapper.ProfessionalProfileMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.ProfessionalProfile}.
 */
@Service
@Transactional
public class ProfessionalProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalProfileService.class);

    private final ProfessionalProfileRepository professionalProfileRepository;

    private final ProfessionalProfileMapper professionalProfileMapper;

    public ProfessionalProfileService(
        ProfessionalProfileRepository professionalProfileRepository,
        ProfessionalProfileMapper professionalProfileMapper
    ) {
        this.professionalProfileRepository = professionalProfileRepository;
        this.professionalProfileMapper = professionalProfileMapper;
    }

    /**
     * Save a professionalProfile.
     *
     * @param professionalProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalProfileDTO save(ProfessionalProfileDTO professionalProfileDTO) {
        LOG.debug("Request to save ProfessionalProfile : {}", professionalProfileDTO);
        ProfessionalProfile professionalProfile = professionalProfileMapper.toEntity(professionalProfileDTO);
        professionalProfile = professionalProfileRepository.save(professionalProfile);
        return professionalProfileMapper.toDto(professionalProfile);
    }

    /**
     * Update a professionalProfile.
     *
     * @param professionalProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfessionalProfileDTO update(ProfessionalProfileDTO professionalProfileDTO) {
        LOG.debug("Request to update ProfessionalProfile : {}", professionalProfileDTO);
        ProfessionalProfile professionalProfile = professionalProfileMapper.toEntity(professionalProfileDTO);
        professionalProfile = professionalProfileRepository.save(professionalProfile);
        return professionalProfileMapper.toDto(professionalProfile);
    }

    /**
     * Partially update a professionalProfile.
     *
     * @param professionalProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfessionalProfileDTO> partialUpdate(ProfessionalProfileDTO professionalProfileDTO) {
        LOG.debug("Request to partially update ProfessionalProfile : {}", professionalProfileDTO);

        return professionalProfileRepository
            .findById(professionalProfileDTO.getId())
            .map(existingProfessionalProfile -> {
                professionalProfileMapper.partialUpdate(existingProfessionalProfile, professionalProfileDTO);

                return existingProfessionalProfile;
            })
            .map(professionalProfileRepository::save)
            .map(professionalProfileMapper::toDto);
    }

    /**
     *  Get all the professionalProfiles where Member is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProfessionalProfileDTO> findAllWhereMemberIsNull() {
        LOG.debug("Request to get all professionalProfiles where Member is null");
        return StreamSupport.stream(professionalProfileRepository.findAll().spliterator(), false)
            .filter(professionalProfile -> professionalProfile.getMember() == null)
            .map(professionalProfileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one professionalProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfessionalProfileDTO> findOne(Long id) {
        LOG.debug("Request to get ProfessionalProfile : {}", id);
        return professionalProfileRepository.findById(id).map(professionalProfileMapper::toDto);
    }

    /**
     * Delete the professionalProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProfessionalProfile : {}", id);
        professionalProfileRepository.deleteById(id);
    }
}
