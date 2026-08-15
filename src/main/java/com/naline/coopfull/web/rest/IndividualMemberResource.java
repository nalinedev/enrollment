package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.IndividualMemberRepository;
import com.naline.coopfull.service.IndividualMemberQueryService;
import com.naline.coopfull.service.IndividualMemberService;
import com.naline.coopfull.service.criteria.IndividualMemberCriteria;
import com.naline.coopfull.service.dto.IndividualMemberDTO;
import com.naline.coopfull.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.naline.coopfull.domain.IndividualMember}.
 */
@RestController
@RequestMapping("/api/individual-members")
public class IndividualMemberResource {

    private static final Logger LOG = LoggerFactory.getLogger(IndividualMemberResource.class);

    private static final String ENTITY_NAME = "individualMember";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final IndividualMemberService individualMemberService;

    private final IndividualMemberRepository individualMemberRepository;

    private final IndividualMemberQueryService individualMemberQueryService;

    public IndividualMemberResource(
        IndividualMemberService individualMemberService,
        IndividualMemberRepository individualMemberRepository,
        IndividualMemberQueryService individualMemberQueryService
    ) {
        this.individualMemberService = individualMemberService;
        this.individualMemberRepository = individualMemberRepository;
        this.individualMemberQueryService = individualMemberQueryService;
    }

    /**
     * {@code POST  /individual-members} : Create a new individualMember.
     *
     * @param individualMemberDTO the individualMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new individualMemberDTO, or with status {@code 400 (Bad Request)} if the individualMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IndividualMemberDTO> createIndividualMember(@Valid @RequestBody IndividualMemberDTO individualMemberDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save IndividualMember : {}", individualMemberDTO);
        if (individualMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new individualMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        individualMemberDTO = individualMemberService.save(individualMemberDTO);
        return ResponseEntity.created(new URI("/api/individual-members/" + individualMemberDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, individualMemberDTO.getId().toString()))
            .body(individualMemberDTO);
    }

    /**
     * {@code PUT  /individual-members/:id} : Updates an existing individualMember.
     *
     * @param id the id of the individualMemberDTO to save.
     * @param individualMemberDTO the individualMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated individualMemberDTO,
     * or with status {@code 400 (Bad Request)} if the individualMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the individualMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IndividualMemberDTO> updateIndividualMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IndividualMemberDTO individualMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IndividualMember : {}, {}", id, individualMemberDTO);
        if (individualMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, individualMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!individualMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        individualMemberDTO = individualMemberService.update(individualMemberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, individualMemberDTO.getId().toString()))
            .body(individualMemberDTO);
    }

    /**
     * {@code PATCH  /individual-members/:id} : Partial updates given fields of an existing individualMember, field will ignore if it is null
     *
     * @param id the id of the individualMemberDTO to save.
     * @param individualMemberDTO the individualMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated individualMemberDTO,
     * or with status {@code 400 (Bad Request)} if the individualMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the individualMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the individualMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IndividualMemberDTO> partialUpdateIndividualMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IndividualMemberDTO individualMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IndividualMember partially : {}, {}", id, individualMemberDTO);
        if (individualMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, individualMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!individualMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IndividualMemberDTO> result = individualMemberService.partialUpdate(individualMemberDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, individualMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /individual-members} : get all the Individual Members.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Individual Members in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IndividualMemberDTO>> getAllIndividualMembers(IndividualMemberCriteria criteria) {
        LOG.debug("REST request to get IndividualMembers by criteria: {}", criteria);

        List<IndividualMemberDTO> entityList = individualMemberQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /individual-members/count} : count all the individualMembers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIndividualMembers(IndividualMemberCriteria criteria) {
        LOG.debug("REST request to count IndividualMembers by criteria: {}", criteria);
        return ResponseEntity.ok().body(individualMemberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /individual-members/:id} : get the "id" individualMember.
     *
     * @param id the id of the individualMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the individualMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IndividualMemberDTO> getIndividualMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IndividualMember : {}", id);
        Optional<IndividualMemberDTO> individualMemberDTO = individualMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(individualMemberDTO);
    }

    /**
     * {@code DELETE  /individual-members/:id} : delete the "id" individualMember.
     *
     * @param id the id of the individualMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIndividualMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IndividualMember : {}", id);
        individualMemberService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
