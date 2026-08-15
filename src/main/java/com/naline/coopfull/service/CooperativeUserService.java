package com.naline.coopfull.service;

import com.naline.coopfull.domain.CooperativeUser;
import com.naline.coopfull.repository.CooperativeUserRepository;
import com.naline.coopfull.service.dto.CooperativeUserDTO;
import com.naline.coopfull.service.mapper.CooperativeUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.CooperativeUser}.
 */
@Service
@Transactional
public class CooperativeUserService {

    private static final Logger LOG = LoggerFactory.getLogger(CooperativeUserService.class);

    private final CooperativeUserRepository cooperativeUserRepository;

    private final CooperativeUserMapper cooperativeUserMapper;

    public CooperativeUserService(CooperativeUserRepository cooperativeUserRepository, CooperativeUserMapper cooperativeUserMapper) {
        this.cooperativeUserRepository = cooperativeUserRepository;
        this.cooperativeUserMapper = cooperativeUserMapper;
    }

    /**
     * Save a cooperativeUser.
     *
     * @param cooperativeUserDTO the entity to save.
     * @return the persisted entity.
     */
    public CooperativeUserDTO save(CooperativeUserDTO cooperativeUserDTO) {
        LOG.debug("Request to save CooperativeUser : {}", cooperativeUserDTO);
        CooperativeUser cooperativeUser = cooperativeUserMapper.toEntity(cooperativeUserDTO);
        cooperativeUser = cooperativeUserRepository.save(cooperativeUser);
        return cooperativeUserMapper.toDto(cooperativeUser);
    }

    /**
     * Update a cooperativeUser.
     *
     * @param cooperativeUserDTO the entity to save.
     * @return the persisted entity.
     */
    public CooperativeUserDTO update(CooperativeUserDTO cooperativeUserDTO) {
        LOG.debug("Request to update CooperativeUser : {}", cooperativeUserDTO);
        CooperativeUser cooperativeUser = cooperativeUserMapper.toEntity(cooperativeUserDTO);
        cooperativeUser = cooperativeUserRepository.save(cooperativeUser);
        return cooperativeUserMapper.toDto(cooperativeUser);
    }

    /**
     * Partially update a cooperativeUser.
     *
     * @param cooperativeUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CooperativeUserDTO> partialUpdate(CooperativeUserDTO cooperativeUserDTO) {
        LOG.debug("Request to partially update CooperativeUser : {}", cooperativeUserDTO);

        return cooperativeUserRepository
            .findById(cooperativeUserDTO.getId())
            .map(existingCooperativeUser -> {
                cooperativeUserMapper.partialUpdate(existingCooperativeUser, cooperativeUserDTO);

                return existingCooperativeUser;
            })
            .map(cooperativeUserRepository::save)
            .map(cooperativeUserMapper::toDto);
    }

    /**
     * Get all the cooperativeUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CooperativeUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cooperativeUserRepository.findAllWithEagerRelationships(pageable).map(cooperativeUserMapper::toDto);
    }

    /**
     * Get one cooperativeUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CooperativeUserDTO> findOne(Long id) {
        LOG.debug("Request to get CooperativeUser : {}", id);
        return cooperativeUserRepository.findOneWithEagerRelationships(id).map(cooperativeUserMapper::toDto);
    }

    /**
     * Delete the cooperativeUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CooperativeUser : {}", id);
        cooperativeUserRepository.deleteById(id);
    }
}
