package com.naline.coopfull.service;

import com.naline.coopfull.domain.SocialProfile;
import com.naline.coopfull.repository.SocialProfileRepository;
import com.naline.coopfull.service.dto.SocialProfileDTO;
import com.naline.coopfull.service.mapper.SocialProfileMapper;
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
 * Service Implementation for managing {@link com.naline.coopfull.domain.SocialProfile}.
 */
@Service
@Transactional
public class SocialProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(SocialProfileService.class);

    private final SocialProfileRepository socialProfileRepository;

    private final SocialProfileMapper socialProfileMapper;

    public SocialProfileService(SocialProfileRepository socialProfileRepository, SocialProfileMapper socialProfileMapper) {
        this.socialProfileRepository = socialProfileRepository;
        this.socialProfileMapper = socialProfileMapper;
    }

    /**
     * Save a socialProfile.
     *
     * @param socialProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public SocialProfileDTO save(SocialProfileDTO socialProfileDTO) {
        LOG.debug("Request to save SocialProfile : {}", socialProfileDTO);
        SocialProfile socialProfile = socialProfileMapper.toEntity(socialProfileDTO);
        socialProfile = socialProfileRepository.save(socialProfile);
        return socialProfileMapper.toDto(socialProfile);
    }

    /**
     * Update a socialProfile.
     *
     * @param socialProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public SocialProfileDTO update(SocialProfileDTO socialProfileDTO) {
        LOG.debug("Request to update SocialProfile : {}", socialProfileDTO);
        SocialProfile socialProfile = socialProfileMapper.toEntity(socialProfileDTO);
        socialProfile = socialProfileRepository.save(socialProfile);
        return socialProfileMapper.toDto(socialProfile);
    }

    /**
     * Partially update a socialProfile.
     *
     * @param socialProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SocialProfileDTO> partialUpdate(SocialProfileDTO socialProfileDTO) {
        LOG.debug("Request to partially update SocialProfile : {}", socialProfileDTO);

        return socialProfileRepository
            .findById(socialProfileDTO.getId())
            .map(existingSocialProfile -> {
                socialProfileMapper.partialUpdate(existingSocialProfile, socialProfileDTO);

                return existingSocialProfile;
            })
            .map(socialProfileRepository::save)
            .map(socialProfileMapper::toDto);
    }

    /**
     *  Get all the socialProfiles where Member is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SocialProfileDTO> findAllWhereMemberIsNull() {
        LOG.debug("Request to get all socialProfiles where Member is null");
        return StreamSupport.stream(socialProfileRepository.findAll().spliterator(), false)
            .filter(socialProfile -> socialProfile.getMember() == null)
            .map(socialProfileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one socialProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SocialProfileDTO> findOne(Long id) {
        LOG.debug("Request to get SocialProfile : {}", id);
        return socialProfileRepository.findById(id).map(socialProfileMapper::toDto);
    }

    /**
     * Delete the socialProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SocialProfile : {}", id);
        socialProfileRepository.deleteById(id);
    }
}
