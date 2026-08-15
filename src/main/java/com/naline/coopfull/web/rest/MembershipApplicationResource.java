package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.MembershipApplicationRepository;
import com.naline.coopfull.service.MembershipApplicationQueryService;
import com.naline.coopfull.service.MembershipApplicationService;
import com.naline.coopfull.service.criteria.MembershipApplicationCriteria;
import com.naline.coopfull.service.dto.MembershipApplicationDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.MembershipApplication}.
 */
@RestController
@RequestMapping("/api/membership-applications")
public class MembershipApplicationResource {

    private static final Logger LOG = LoggerFactory.getLogger(MembershipApplicationResource.class);

    private static final String ENTITY_NAME = "membershipApplication";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final MembershipApplicationService membershipApplicationService;

    private final MembershipApplicationRepository membershipApplicationRepository;

    private final MembershipApplicationQueryService membershipApplicationQueryService;

    public MembershipApplicationResource(
        MembershipApplicationService membershipApplicationService,
        MembershipApplicationRepository membershipApplicationRepository,
        MembershipApplicationQueryService membershipApplicationQueryService
    ) {
        this.membershipApplicationService = membershipApplicationService;
        this.membershipApplicationRepository = membershipApplicationRepository;
        this.membershipApplicationQueryService = membershipApplicationQueryService;
    }

    /**
     * {@code POST  /membership-applications} : Create a new membershipApplication.
     *
     * @param membershipApplicationDTO the membershipApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membershipApplicationDTO, or with status {@code 400 (Bad Request)} if the membershipApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MembershipApplicationDTO> createMembershipApplication(
        @Valid @RequestBody MembershipApplicationDTO membershipApplicationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save MembershipApplication : {}", membershipApplicationDTO);
        if (membershipApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new membershipApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        membershipApplicationDTO = membershipApplicationService.save(membershipApplicationDTO);
        return ResponseEntity.created(new URI("/api/membership-applications/" + membershipApplicationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, membershipApplicationDTO.getId().toString()))
            .body(membershipApplicationDTO);
    }

    /**
     * {@code PUT  /membership-applications/:id} : Updates an existing membershipApplication.
     *
     * @param id the id of the membershipApplicationDTO to save.
     * @param membershipApplicationDTO the membershipApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the membershipApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membershipApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MembershipApplicationDTO> updateMembershipApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MembershipApplicationDTO membershipApplicationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MembershipApplication : {}, {}", id, membershipApplicationDTO);
        if (membershipApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, membershipApplicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!membershipApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        membershipApplicationDTO = membershipApplicationService.update(membershipApplicationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membershipApplicationDTO.getId().toString()))
            .body(membershipApplicationDTO);
    }

    /**
     * {@code PATCH  /membership-applications/:id} : Partial updates given fields of an existing membershipApplication, field will ignore if it is null
     *
     * @param id the id of the membershipApplicationDTO to save.
     * @param membershipApplicationDTO the membershipApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the membershipApplicationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the membershipApplicationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the membershipApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MembershipApplicationDTO> partialUpdateMembershipApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MembershipApplicationDTO membershipApplicationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MembershipApplication partially : {}, {}", id, membershipApplicationDTO);
        if (membershipApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, membershipApplicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!membershipApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MembershipApplicationDTO> result = membershipApplicationService.partialUpdate(membershipApplicationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membershipApplicationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /membership-applications} : get all the Membership Applications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Membership Applications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MembershipApplicationDTO>> getAllMembershipApplications(MembershipApplicationCriteria criteria) {
        LOG.debug("REST request to get MembershipApplications by criteria: {}", criteria);

        List<MembershipApplicationDTO> entityList = membershipApplicationQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /membership-applications/count} : count all the membershipApplications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMembershipApplications(MembershipApplicationCriteria criteria) {
        LOG.debug("REST request to count MembershipApplications by criteria: {}", criteria);
        return ResponseEntity.ok().body(membershipApplicationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /membership-applications/:id} : get the "id" membershipApplication.
     *
     * @param id the id of the membershipApplicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipApplicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MembershipApplicationDTO> getMembershipApplication(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MembershipApplication : {}", id);
        Optional<MembershipApplicationDTO> membershipApplicationDTO = membershipApplicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membershipApplicationDTO);
    }

    /**
     * {@code DELETE  /membership-applications/:id} : delete the "id" membershipApplication.
     *
     * @param id the id of the membershipApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMembershipApplication(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MembershipApplication : {}", id);
        membershipApplicationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
