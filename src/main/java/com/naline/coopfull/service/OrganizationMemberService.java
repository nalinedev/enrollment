package com.naline.coopfull.service;

import com.naline.coopfull.domain.OrganizationMember;
import com.naline.coopfull.repository.OrganizationMemberRepository;
import com.naline.coopfull.service.dto.OrganizationMemberDTO;
import com.naline.coopfull.service.mapper.OrganizationMemberMapper;
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
 * Service Implementation for managing {@link com.naline.coopfull.domain.OrganizationMember}.
 */
@Service
@Transactional
public class OrganizationMemberService {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationMemberService.class);

    private final OrganizationMemberRepository organizationMemberRepository;

    private final OrganizationMemberMapper organizationMemberMapper;

    public OrganizationMemberService(
        OrganizationMemberRepository organizationMemberRepository,
        OrganizationMemberMapper organizationMemberMapper
    ) {
        this.organizationMemberRepository = organizationMemberRepository;
        this.organizationMemberMapper = organizationMemberMapper;
    }

    /**
     * Save a organizationMember.
     *
     * @param organizationMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public OrganizationMemberDTO save(OrganizationMemberDTO organizationMemberDTO) {
        LOG.debug("Request to save OrganizationMember : {}", organizationMemberDTO);
        OrganizationMember organizationMember = organizationMemberMapper.toEntity(organizationMemberDTO);
        organizationMember = organizationMemberRepository.save(organizationMember);
        return organizationMemberMapper.toDto(organizationMember);
    }

    /**
     * Update a organizationMember.
     *
     * @param organizationMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public OrganizationMemberDTO update(OrganizationMemberDTO organizationMemberDTO) {
        LOG.debug("Request to update OrganizationMember : {}", organizationMemberDTO);
        OrganizationMember organizationMember = organizationMemberMapper.toEntity(organizationMemberDTO);
        organizationMember = organizationMemberRepository.save(organizationMember);
        return organizationMemberMapper.toDto(organizationMember);
    }

    /**
     * Partially update a organizationMember.
     *
     * @param organizationMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrganizationMemberDTO> partialUpdate(OrganizationMemberDTO organizationMemberDTO) {
        LOG.debug("Request to partially update OrganizationMember : {}", organizationMemberDTO);

        return organizationMemberRepository
            .findById(organizationMemberDTO.getId())
            .map(existingOrganizationMember -> {
                organizationMemberMapper.partialUpdate(existingOrganizationMember, organizationMemberDTO);

                return existingOrganizationMember;
            })
            .map(organizationMemberRepository::save)
            .map(organizationMemberMapper::toDto);
    }

    /**
     *  Get all the organizationMembers where Member is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrganizationMemberDTO> findAllWhereMemberIsNull() {
        LOG.debug("Request to get all organizationMembers where Member is null");
        return StreamSupport.stream(organizationMemberRepository.findAll().spliterator(), false)
            .filter(organizationMember -> organizationMember.getMember() == null)
            .map(organizationMemberMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one organizationMember by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrganizationMemberDTO> findOne(Long id) {
        LOG.debug("Request to get OrganizationMember : {}", id);
        return organizationMemberRepository.findById(id).map(organizationMemberMapper::toDto);
    }

    /**
     * Delete the organizationMember by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrganizationMember : {}", id);
        organizationMemberRepository.deleteById(id);
    }
}
