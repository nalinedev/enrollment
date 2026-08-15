package com.naline.coopfull.service;

import com.naline.coopfull.domain.MemberDocument;
import com.naline.coopfull.repository.MemberDocumentRepository;
import com.naline.coopfull.service.dto.MemberDocumentDTO;
import com.naline.coopfull.service.mapper.MemberDocumentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.MemberDocument}.
 */
@Service
@Transactional
public class MemberDocumentService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberDocumentService.class);

    private final MemberDocumentRepository memberDocumentRepository;

    private final MemberDocumentMapper memberDocumentMapper;

    public MemberDocumentService(MemberDocumentRepository memberDocumentRepository, MemberDocumentMapper memberDocumentMapper) {
        this.memberDocumentRepository = memberDocumentRepository;
        this.memberDocumentMapper = memberDocumentMapper;
    }

    /**
     * Save a memberDocument.
     *
     * @param memberDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public MemberDocumentDTO save(MemberDocumentDTO memberDocumentDTO) {
        LOG.debug("Request to save MemberDocument : {}", memberDocumentDTO);
        MemberDocument memberDocument = memberDocumentMapper.toEntity(memberDocumentDTO);
        memberDocument = memberDocumentRepository.save(memberDocument);
        return memberDocumentMapper.toDto(memberDocument);
    }

    /**
     * Update a memberDocument.
     *
     * @param memberDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    public MemberDocumentDTO update(MemberDocumentDTO memberDocumentDTO) {
        LOG.debug("Request to update MemberDocument : {}", memberDocumentDTO);
        MemberDocument memberDocument = memberDocumentMapper.toEntity(memberDocumentDTO);
        memberDocument = memberDocumentRepository.save(memberDocument);
        return memberDocumentMapper.toDto(memberDocument);
    }

    /**
     * Partially update a memberDocument.
     *
     * @param memberDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MemberDocumentDTO> partialUpdate(MemberDocumentDTO memberDocumentDTO) {
        LOG.debug("Request to partially update MemberDocument : {}", memberDocumentDTO);

        return memberDocumentRepository
            .findById(memberDocumentDTO.getId())
            .map(existingMemberDocument -> {
                memberDocumentMapper.partialUpdate(existingMemberDocument, memberDocumentDTO);

                return existingMemberDocument;
            })
            .map(memberDocumentRepository::save)
            .map(memberDocumentMapper::toDto);
    }

    /**
     * Get all the memberDocuments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MemberDocumentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return memberDocumentRepository.findAllWithEagerRelationships(pageable).map(memberDocumentMapper::toDto);
    }

    /**
     * Get one memberDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MemberDocumentDTO> findOne(Long id) {
        LOG.debug("Request to get MemberDocument : {}", id);
        return memberDocumentRepository.findOneWithEagerRelationships(id).map(memberDocumentMapper::toDto);
    }

    /**
     * Delete the memberDocument by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MemberDocument : {}", id);
        memberDocumentRepository.deleteById(id);
    }
}
