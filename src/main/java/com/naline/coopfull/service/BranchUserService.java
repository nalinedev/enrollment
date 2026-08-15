package com.naline.coopfull.service;

import com.naline.coopfull.domain.BranchUser;
import com.naline.coopfull.repository.BranchUserRepository;
import com.naline.coopfull.service.dto.BranchUserDTO;
import com.naline.coopfull.service.mapper.BranchUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.BranchUser}.
 */
@Service
@Transactional
public class BranchUserService {

    private static final Logger LOG = LoggerFactory.getLogger(BranchUserService.class);

    private final BranchUserRepository branchUserRepository;

    private final BranchUserMapper branchUserMapper;

    public BranchUserService(BranchUserRepository branchUserRepository, BranchUserMapper branchUserMapper) {
        this.branchUserRepository = branchUserRepository;
        this.branchUserMapper = branchUserMapper;
    }

    /**
     * Save a branchUser.
     *
     * @param branchUserDTO the entity to save.
     * @return the persisted entity.
     */
    public BranchUserDTO save(BranchUserDTO branchUserDTO) {
        LOG.debug("Request to save BranchUser : {}", branchUserDTO);
        BranchUser branchUser = branchUserMapper.toEntity(branchUserDTO);
        branchUser = branchUserRepository.save(branchUser);
        return branchUserMapper.toDto(branchUser);
    }

    /**
     * Update a branchUser.
     *
     * @param branchUserDTO the entity to save.
     * @return the persisted entity.
     */
    public BranchUserDTO update(BranchUserDTO branchUserDTO) {
        LOG.debug("Request to update BranchUser : {}", branchUserDTO);
        BranchUser branchUser = branchUserMapper.toEntity(branchUserDTO);
        branchUser = branchUserRepository.save(branchUser);
        return branchUserMapper.toDto(branchUser);
    }

    /**
     * Partially update a branchUser.
     *
     * @param branchUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BranchUserDTO> partialUpdate(BranchUserDTO branchUserDTO) {
        LOG.debug("Request to partially update BranchUser : {}", branchUserDTO);

        return branchUserRepository
            .findById(branchUserDTO.getId())
            .map(existingBranchUser -> {
                branchUserMapper.partialUpdate(existingBranchUser, branchUserDTO);

                return existingBranchUser;
            })
            .map(branchUserRepository::save)
            .map(branchUserMapper::toDto);
    }

    /**
     * Get all the branchUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BranchUserDTO> findAllWithEagerRelationships(Pageable pageable) {
        return branchUserRepository.findAllWithEagerRelationships(pageable).map(branchUserMapper::toDto);
    }

    /**
     * Get one branchUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BranchUserDTO> findOne(Long id) {
        LOG.debug("Request to get BranchUser : {}", id);
        return branchUserRepository.findOneWithEagerRelationships(id).map(branchUserMapper::toDto);
    }

    /**
     * Delete the branchUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BranchUser : {}", id);
        branchUserRepository.deleteById(id);
    }
}
