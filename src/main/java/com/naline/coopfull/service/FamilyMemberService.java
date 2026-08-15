package com.naline.coopfull.service;

import com.naline.coopfull.domain.FamilyMember;
import com.naline.coopfull.repository.FamilyMemberRepository;
import com.naline.coopfull.service.dto.FamilyMemberDTO;
import com.naline.coopfull.service.mapper.FamilyMemberMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.FamilyMember}.
 */
@Service
@Transactional
public class FamilyMemberService {

    private static final Logger LOG = LoggerFactory.getLogger(FamilyMemberService.class);

    private final FamilyMemberRepository familyMemberRepository;

    private final FamilyMemberMapper familyMemberMapper;

    public FamilyMemberService(FamilyMemberRepository familyMemberRepository, FamilyMemberMapper familyMemberMapper) {
        this.familyMemberRepository = familyMemberRepository;
        this.familyMemberMapper = familyMemberMapper;
    }

    /**
     * Save a familyMember.
     *
     * @param familyMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public FamilyMemberDTO save(FamilyMemberDTO familyMemberDTO) {
        LOG.debug("Request to save FamilyMember : {}", familyMemberDTO);
        FamilyMember familyMember = familyMemberMapper.toEntity(familyMemberDTO);
        familyMember = familyMemberRepository.save(familyMember);
        return familyMemberMapper.toDto(familyMember);
    }

    /**
     * Update a familyMember.
     *
     * @param familyMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public FamilyMemberDTO update(FamilyMemberDTO familyMemberDTO) {
        LOG.debug("Request to update FamilyMember : {}", familyMemberDTO);
        FamilyMember familyMember = familyMemberMapper.toEntity(familyMemberDTO);
        familyMember = familyMemberRepository.save(familyMember);
        return familyMemberMapper.toDto(familyMember);
    }

    /**
     * Partially update a familyMember.
     *
     * @param familyMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FamilyMemberDTO> partialUpdate(FamilyMemberDTO familyMemberDTO) {
        LOG.debug("Request to partially update FamilyMember : {}", familyMemberDTO);

        return familyMemberRepository
            .findById(familyMemberDTO.getId())
            .map(existingFamilyMember -> {
                familyMemberMapper.partialUpdate(existingFamilyMember, familyMemberDTO);

                return existingFamilyMember;
            })
            .map(familyMemberRepository::save)
            .map(familyMemberMapper::toDto);
    }

    /**
     * Get all the familyMembers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<FamilyMemberDTO> findAllWithEagerRelationships(Pageable pageable) {
        return familyMemberRepository.findAllWithEagerRelationships(pageable).map(familyMemberMapper::toDto);
    }

    /**
     * Get one familyMember by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FamilyMemberDTO> findOne(Long id) {
        LOG.debug("Request to get FamilyMember : {}", id);
        return familyMemberRepository.findOneWithEagerRelationships(id).map(familyMemberMapper::toDto);
    }

    /**
     * Delete the familyMember by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FamilyMember : {}", id);
        familyMemberRepository.deleteById(id);
    }
}
