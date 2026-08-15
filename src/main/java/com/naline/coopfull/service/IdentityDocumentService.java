package com.naline.coopfull.service;

import com.naline.coopfull.domain.IdentityDocument;
import com.naline.coopfull.repository.IdentityDocumentRepository;
import com.naline.coopfull.service.dto.IdentityDocumentDTO;
import com.naline.coopfull.service.mapper.IdentityDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.IdentityDocument}.
 */
@Service
@Transactional
public class IdentityDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityDocumentService.class);

    private final IdentityDocumentRepository identityDocumentRepository;

    private final IdentityDocumentMapper identityDocumentMapper;

    public IdentityDocumentService(IdentityDocumentRepository identityDocumentRepository, IdentityDocumentMapper identityDocumentMapper) {
        this.identityDocumentRepository = identityDocumentRepository;
        this.identityDocumentMapper = identityDocumentMapper;
    }

    /**
     * Save a identityDocument.
     *
     * @param identityDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public IdentityDocumentDTO save(IdentityDocumentDTO identityDocumentDTO) {
        LOG.debug("Request to save IdentityDocument : {}", identityDocumentDTO);
        IdentityDocument identityDocument = identityDocumentMapper.toEntity(identityDocumentDTO);
        identityDocument = identityDocumentRepository.save(identityDocument);
        return identityDocumentMapper.toDto(identityDocument);
    }

    /**
     * Update a identityDocument.
     *
     * @param identityDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public IdentityDocumentDTO update(IdentityDocumentDTO identityDocumentDTO) {
        LOG.debug("Request to update IdentityDocument : {}", identityDocumentDTO);
        IdentityDocument identityDocument = identityDocumentMapper.toEntity(identityDocumentDTO);
        identityDocument = identityDocumentRepository.save(identityDocument);
        return identityDocumentMapper.toDto(identityDocument);
    }

    /**
     * Partially update a identityDocument.
     *
     * @param identityDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IdentityDocumentDTO> partialUpdate(IdentityDocumentDTO identityDocumentDTO) {
        LOG.debug("Request to partially update IdentityDocument : {}", identityDocumentDTO);

        return identityDocumentRepository
            .findById(identityDocumentDTO.getId())
            .map(existingIdentityDocument -> {
                identityDocumentMapper.partialUpdate(existingIdentityDocument, identityDocumentDTO);

                return existingIdentityDocument;
            })
            .map(identityDocumentRepository::save)
            .map(identityDocumentMapper::toDto);
    }

    /**
     * Get all the identityDocuments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<IdentityDocumentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return identityDocumentRepository.findAllWithEagerRelationships(pageable).map(identityDocumentMapper::toDto);
    }

    /**
     * Get one identityDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IdentityDocumentDTO> findOne(Long id) {
        LOG.debug("Request to get IdentityDocument : {}", id);
        return identityDocumentRepository.findOneWithEagerRelationships(id).map(identityDocumentMapper::toDto);
    }

    /**
     * Delete the identityDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IdentityDocument : {}", id);
        identityDocumentRepository.deleteById(id);
    }
}
