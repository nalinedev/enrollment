package com.naline.coopfull.service;

import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.repository.CooperativeRepository;
import com.naline.coopfull.service.dto.CooperativeDTO;
import com.naline.coopfull.service.mapper.CooperativeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.Cooperative}.
 */
@Service
@Transactional
public class CooperativeService {

    private static final Logger LOG = LoggerFactory.getLogger(CooperativeService.class);

    private final CooperativeRepository cooperativeRepository;

    private final CooperativeMapper cooperativeMapper;

    public CooperativeService(CooperativeRepository cooperativeRepository, CooperativeMapper cooperativeMapper) {
        this.cooperativeRepository = cooperativeRepository;
        this.cooperativeMapper = cooperativeMapper;
    }

    /**
     * Save a cooperative.
     *
     * @param cooperativeDTO the entity to save.
     * @return the persisted entity.
     */
    public CooperativeDTO save(CooperativeDTO cooperativeDTO) {
        LOG.debug("Request to save Cooperative : {}", cooperativeDTO);
        Cooperative cooperative = cooperativeMapper.toEntity(cooperativeDTO);
        cooperative = cooperativeRepository.save(cooperative);
        return cooperativeMapper.toDto(cooperative);
    }

    /**
     * Update a cooperative.
     *
     * @param cooperativeDTO the entity to save.
     * @return the persisted entity.
     */
    public CooperativeDTO update(CooperativeDTO cooperativeDTO) {
        LOG.debug("Request to update Cooperative : {}", cooperativeDTO);
        Cooperative cooperative = cooperativeMapper.toEntity(cooperativeDTO);
        cooperative = cooperativeRepository.save(cooperative);
        return cooperativeMapper.toDto(cooperative);
    }

    /**
     * Partially update a cooperative.
     *
     * @param cooperativeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CooperativeDTO> partialUpdate(CooperativeDTO cooperativeDTO) {
        LOG.debug("Request to partially update Cooperative : {}", cooperativeDTO);

        return cooperativeRepository
            .findById(cooperativeDTO.getId())
            .map(existingCooperative -> {
                cooperativeMapper.partialUpdate(existingCooperative, cooperativeDTO);

                return existingCooperative;
            })
            .map(cooperativeRepository::save)
            .map(cooperativeMapper::toDto);
    }

    /**
     * Get one cooperative by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CooperativeDTO> findOne(Long id) {
        LOG.debug("Request to get Cooperative : {}", id);
        return cooperativeRepository.findById(id).map(cooperativeMapper::toDto);
    }

    /**
     * Delete the cooperative by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Cooperative : {}", id);
        cooperativeRepository.deleteById(id);
    }
}
