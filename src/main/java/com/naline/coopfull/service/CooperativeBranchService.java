package com.naline.coopfull.service;

import com.naline.coopfull.domain.CooperativeBranch;
import com.naline.coopfull.repository.CooperativeBranchRepository;
import com.naline.coopfull.service.dto.CooperativeBranchDTO;
import com.naline.coopfull.service.mapper.CooperativeBranchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.CooperativeBranch}.
 */
@Service
@Transactional
public class CooperativeBranchService {

    private static final Logger LOG = LoggerFactory.getLogger(CooperativeBranchService.class);

    private final CooperativeBranchRepository cooperativeBranchRepository;

    private final CooperativeBranchMapper cooperativeBranchMapper;

    public CooperativeBranchService(
        CooperativeBranchRepository cooperativeBranchRepository,
        CooperativeBranchMapper cooperativeBranchMapper
    ) {
        this.cooperativeBranchRepository = cooperativeBranchRepository;
        this.cooperativeBranchMapper = cooperativeBranchMapper;
    }

    /**
     * Save a cooperativeBranch.
     *
     * @param cooperativeBranchDTO the entity to save.
     * @return the persisted entity.
     */
    public CooperativeBranchDTO save(CooperativeBranchDTO cooperativeBranchDTO) {
        LOG.debug("Request to save CooperativeBranch : {}", cooperativeBranchDTO);
        CooperativeBranch cooperativeBranch = cooperativeBranchMapper.toEntity(cooperativeBranchDTO);
        cooperativeBranch = cooperativeBranchRepository.save(cooperativeBranch);
        return cooperativeBranchMapper.toDto(cooperativeBranch);
    }

    /**
     * Update a cooperativeBranch.
     *
     * @param cooperativeBranchDTO the entity to save.
     * @return the persisted entity.
     */
    public CooperativeBranchDTO update(CooperativeBranchDTO cooperativeBranchDTO) {
        LOG.debug("Request to update CooperativeBranch : {}", cooperativeBranchDTO);
        CooperativeBranch cooperativeBranch = cooperativeBranchMapper.toEntity(cooperativeBranchDTO);
        cooperativeBranch = cooperativeBranchRepository.save(cooperativeBranch);
        return cooperativeBranchMapper.toDto(cooperativeBranch);
    }

    /**
     * Partially update a cooperativeBranch.
     *
     * @param cooperativeBranchDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CooperativeBranchDTO> partialUpdate(CooperativeBranchDTO cooperativeBranchDTO) {
        LOG.debug("Request to partially update CooperativeBranch : {}", cooperativeBranchDTO);

        return cooperativeBranchRepository
            .findById(cooperativeBranchDTO.getId())
            .map(existingCooperativeBranch -> {
                cooperativeBranchMapper.partialUpdate(existingCooperativeBranch, cooperativeBranchDTO);

                return existingCooperativeBranch;
            })
            .map(cooperativeBranchRepository::save)
            .map(cooperativeBranchMapper::toDto);
    }

    /**
     * Get all the cooperativeBranches with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CooperativeBranchDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cooperativeBranchRepository.findAllWithEagerRelationships(pageable).map(cooperativeBranchMapper::toDto);
    }

    /**
     * Get one cooperativeBranch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CooperativeBranchDTO> findOne(Long id) {
        LOG.debug("Request to get CooperativeBranch : {}", id);
        return cooperativeBranchRepository.findOneWithEagerRelationships(id).map(cooperativeBranchMapper::toDto);
    }

    /**
     * Delete the cooperativeBranch by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CooperativeBranch : {}", id);
        cooperativeBranchRepository.deleteById(id);
    }
}
