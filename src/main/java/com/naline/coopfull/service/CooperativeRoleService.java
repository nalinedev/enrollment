package com.naline.coopfull.service;

import com.naline.coopfull.domain.CooperativeRole;
import com.naline.coopfull.repository.CooperativeRoleRepository;
import com.naline.coopfull.service.dto.CooperativeRoleDTO;
import com.naline.coopfull.service.mapper.CooperativeRoleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.CooperativeRole}.
 */
@Service
@Transactional
public class CooperativeRoleService {

    private static final Logger LOG = LoggerFactory.getLogger(CooperativeRoleService.class);

    private final CooperativeRoleRepository cooperativeRoleRepository;

    private final CooperativeRoleMapper cooperativeRoleMapper;

    public CooperativeRoleService(CooperativeRoleRepository cooperativeRoleRepository, CooperativeRoleMapper cooperativeRoleMapper) {
        this.cooperativeRoleRepository = cooperativeRoleRepository;
        this.cooperativeRoleMapper = cooperativeRoleMapper;
    }

    /**
     * Save a cooperativeRole.
     *
     * @param cooperativeRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public CooperativeRoleDTO save(CooperativeRoleDTO cooperativeRoleDTO) {
        LOG.debug("Request to save CooperativeRole : {}", cooperativeRoleDTO);
        CooperativeRole cooperativeRole = cooperativeRoleMapper.toEntity(cooperativeRoleDTO);
        cooperativeRole = cooperativeRoleRepository.save(cooperativeRole);
        return cooperativeRoleMapper.toDto(cooperativeRole);
    }

    /**
     * Update a cooperativeRole.
     *
     * @param cooperativeRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public CooperativeRoleDTO update(CooperativeRoleDTO cooperativeRoleDTO) {
        LOG.debug("Request to update CooperativeRole : {}", cooperativeRoleDTO);
        CooperativeRole cooperativeRole = cooperativeRoleMapper.toEntity(cooperativeRoleDTO);
        cooperativeRole = cooperativeRoleRepository.save(cooperativeRole);
        return cooperativeRoleMapper.toDto(cooperativeRole);
    }

    /**
     * Partially update a cooperativeRole.
     *
     * @param cooperativeRoleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CooperativeRoleDTO> partialUpdate(CooperativeRoleDTO cooperativeRoleDTO) {
        LOG.debug("Request to partially update CooperativeRole : {}", cooperativeRoleDTO);

        return cooperativeRoleRepository
            .findById(cooperativeRoleDTO.getId())
            .map(existingCooperativeRole -> {
                cooperativeRoleMapper.partialUpdate(existingCooperativeRole, cooperativeRoleDTO);

                return existingCooperativeRole;
            })
            .map(cooperativeRoleRepository::save)
            .map(cooperativeRoleMapper::toDto);
    }

    /**
     * Get all the cooperativeRoles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CooperativeRoleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cooperativeRoleRepository.findAllWithEagerRelationships(pageable).map(cooperativeRoleMapper::toDto);
    }

    /**
     * Get one cooperativeRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CooperativeRoleDTO> findOne(Long id) {
        LOG.debug("Request to get CooperativeRole : {}", id);
        return cooperativeRoleRepository.findOneWithEagerRelationships(id).map(cooperativeRoleMapper::toDto);
    }

    /**
     * Delete the cooperativeRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CooperativeRole : {}", id);
        cooperativeRoleRepository.deleteById(id);
    }
}
