package com.naline.coopfull.service;

import com.naline.coopfull.domain.IndividualMember;
import com.naline.coopfull.repository.IndividualMemberRepository;
import com.naline.coopfull.service.dto.IndividualMemberDTO;
import com.naline.coopfull.service.mapper.IndividualMemberMapper;
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
 * Service Implementation for managing {@link com.naline.coopfull.domain.IndividualMember}.
 */
@Service
@Transactional
public class IndividualMemberService {

    private static final Logger LOG = LoggerFactory.getLogger(IndividualMemberService.class);

    private final IndividualMemberRepository individualMemberRepository;

    private final IndividualMemberMapper individualMemberMapper;

    public IndividualMemberService(IndividualMemberRepository individualMemberRepository, IndividualMemberMapper individualMemberMapper) {
        this.individualMemberRepository = individualMemberRepository;
        this.individualMemberMapper = individualMemberMapper;
    }

    /**
     * Save a individualMember.
     *
     * @param individualMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public IndividualMemberDTO save(IndividualMemberDTO individualMemberDTO) {
        LOG.debug("Request to save IndividualMember : {}", individualMemberDTO);
        IndividualMember individualMember = individualMemberMapper.toEntity(individualMemberDTO);
        individualMember = individualMemberRepository.save(individualMember);
        return individualMemberMapper.toDto(individualMember);
    }

    /**
     * Update a individualMember.
     *
     * @param individualMemberDTO the entity to save.
     * @return the persisted entity.
     */
    public IndividualMemberDTO update(IndividualMemberDTO individualMemberDTO) {
        LOG.debug("Request to update IndividualMember : {}", individualMemberDTO);
        IndividualMember individualMember = individualMemberMapper.toEntity(individualMemberDTO);
        individualMember = individualMemberRepository.save(individualMember);
        return individualMemberMapper.toDto(individualMember);
    }

    /**
     * Partially update a individualMember.
     *
     * @param individualMemberDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IndividualMemberDTO> partialUpdate(IndividualMemberDTO individualMemberDTO) {
        LOG.debug("Request to partially update IndividualMember : {}", individualMemberDTO);

        return individualMemberRepository
            .findById(individualMemberDTO.getId())
            .map(existingIndividualMember -> {
                individualMemberMapper.partialUpdate(existingIndividualMember, individualMemberDTO);

                return existingIndividualMember;
            })
            .map(individualMemberRepository::save)
            .map(individualMemberMapper::toDto);
    }

    /**
     *  Get all the individualMembers where Member is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IndividualMemberDTO> findAllWhereMemberIsNull() {
        LOG.debug("Request to get all individualMembers where Member is null");
        return StreamSupport.stream(individualMemberRepository.findAll().spliterator(), false)
            .filter(individualMember -> individualMember.getMember() == null)
            .map(individualMemberMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one individualMember by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IndividualMemberDTO> findOne(Long id) {
        LOG.debug("Request to get IndividualMember : {}", id);
        return individualMemberRepository.findById(id).map(individualMemberMapper::toDto);
    }

    /**
     * Delete the individualMember by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete IndividualMember : {}", id);
        individualMemberRepository.deleteById(id);
    }
}
