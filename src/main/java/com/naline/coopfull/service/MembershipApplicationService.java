package com.naline.coopfull.service;

import com.naline.coopfull.domain.MembershipApplication;
import com.naline.coopfull.repository.MembershipApplicationRepository;
import com.naline.coopfull.service.dto.MembershipApplicationDTO;
import com.naline.coopfull.service.mapper.MembershipApplicationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.naline.coopfull.domain.MembershipApplication}.
 */
@Service
@Transactional
public class MembershipApplicationService {

    private static final Logger LOG = LoggerFactory.getLogger(MembershipApplicationService.class);

    private final MembershipApplicationRepository membershipApplicationRepository;

    private final MembershipApplicationMapper membershipApplicationMapper;

    public MembershipApplicationService(
        MembershipApplicationRepository membershipApplicationRepository,
        MembershipApplicationMapper membershipApplicationMapper
    ) {
        this.membershipApplicationRepository = membershipApplicationRepository;
        this.membershipApplicationMapper = membershipApplicationMapper;
    }

    /**
     * Save a membershipApplication.
     *
     * @param membershipApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public MembershipApplicationDTO save(MembershipApplicationDTO membershipApplicationDTO) {
        LOG.debug("Request to save MembershipApplication : {}", membershipApplicationDTO);
        MembershipApplication membershipApplication = membershipApplicationMapper.toEntity(membershipApplicationDTO);
        membershipApplication = membershipApplicationRepository.save(membershipApplication);
        return membershipApplicationMapper.toDto(membershipApplication);
    }

    /**
     * Update a membershipApplication.
     *
     * @param membershipApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public MembershipApplicationDTO update(MembershipApplicationDTO membershipApplicationDTO) {
        LOG.debug("Request to update MembershipApplication : {}", membershipApplicationDTO);
        MembershipApplication membershipApplication = membershipApplicationMapper.toEntity(membershipApplicationDTO);
        membershipApplication = membershipApplicationRepository.save(membershipApplication);
        return membershipApplicationMapper.toDto(membershipApplication);
    }

    /**
     * Partially update a membershipApplication.
     *
     * @param membershipApplicationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MembershipApplicationDTO> partialUpdate(MembershipApplicationDTO membershipApplicationDTO) {
        LOG.debug("Request to partially update MembershipApplication : {}", membershipApplicationDTO);

        return membershipApplicationRepository
            .findById(membershipApplicationDTO.getId())
            .map(existingMembershipApplication -> {
                membershipApplicationMapper.partialUpdate(existingMembershipApplication, membershipApplicationDTO);

                return existingMembershipApplication;
            })
            .map(membershipApplicationRepository::save)
            .map(membershipApplicationMapper::toDto);
    }

    /**
     * Get all the membershipApplications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MembershipApplicationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return membershipApplicationRepository.findAllWithEagerRelationships(pageable).map(membershipApplicationMapper::toDto);
    }

    /**
     * Get one membershipApplication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MembershipApplicationDTO> findOne(Long id) {
        LOG.debug("Request to get MembershipApplication : {}", id);
        return membershipApplicationRepository.findOneWithEagerRelationships(id).map(membershipApplicationMapper::toDto);
    }

    /**
     * Delete the membershipApplication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MembershipApplication : {}", id);
        membershipApplicationRepository.deleteById(id);
    }
}
