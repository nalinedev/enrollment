package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.FamilyMemberRepository;
import com.naline.coopfull.service.FamilyMemberQueryService;
import com.naline.coopfull.service.FamilyMemberService;
import com.naline.coopfull.service.criteria.FamilyMemberCriteria;
import com.naline.coopfull.service.dto.FamilyMemberDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.FamilyMember}.
 */
@RestController
@RequestMapping("/api/family-members")
public class FamilyMemberResource {

    private static final Logger LOG = LoggerFactory.getLogger(FamilyMemberResource.class);

    private static final String ENTITY_NAME = "familyMember";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final FamilyMemberService familyMemberService;

    private final FamilyMemberRepository familyMemberRepository;

    private final FamilyMemberQueryService familyMemberQueryService;

    public FamilyMemberResource(
        FamilyMemberService familyMemberService,
        FamilyMemberRepository familyMemberRepository,
        FamilyMemberQueryService familyMemberQueryService
    ) {
        this.familyMemberService = familyMemberService;
        this.familyMemberRepository = familyMemberRepository;
        this.familyMemberQueryService = familyMemberQueryService;
    }

    /**
     * {@code POST  /family-members} : Create a new familyMember.
     *
     * @param familyMemberDTO the familyMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new familyMemberDTO, or with status {@code 400 (Bad Request)} if the familyMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FamilyMemberDTO> createFamilyMember(@Valid @RequestBody FamilyMemberDTO familyMemberDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save FamilyMember : {}", familyMemberDTO);
        if (familyMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new familyMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        familyMemberDTO = familyMemberService.save(familyMemberDTO);
        return ResponseEntity.created(new URI("/api/family-members/" + familyMemberDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, familyMemberDTO.getId().toString()))
            .body(familyMemberDTO);
    }

    /**
     * {@code PUT  /family-members/:id} : Updates an existing familyMember.
     *
     * @param id the id of the familyMemberDTO to save.
     * @param familyMemberDTO the familyMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyMemberDTO,
     * or with status {@code 400 (Bad Request)} if the familyMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the familyMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FamilyMemberDTO> updateFamilyMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FamilyMemberDTO familyMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FamilyMember : {}, {}", id, familyMemberDTO);
        if (familyMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        familyMemberDTO = familyMemberService.update(familyMemberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyMemberDTO.getId().toString()))
            .body(familyMemberDTO);
    }

    /**
     * {@code PATCH  /family-members/:id} : Partial updates given fields of an existing familyMember, field will ignore if it is null
     *
     * @param id the id of the familyMemberDTO to save.
     * @param familyMemberDTO the familyMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familyMemberDTO,
     * or with status {@code 400 (Bad Request)} if the familyMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the familyMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the familyMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FamilyMemberDTO> partialUpdateFamilyMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FamilyMemberDTO familyMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FamilyMember partially : {}, {}", id, familyMemberDTO);
        if (familyMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familyMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familyMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FamilyMemberDTO> result = familyMemberService.partialUpdate(familyMemberDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familyMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /family-members} : get all the Family Members.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Family Members in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FamilyMemberDTO>> getAllFamilyMembers(FamilyMemberCriteria criteria) {
        LOG.debug("REST request to get FamilyMembers by criteria: {}", criteria);

        List<FamilyMemberDTO> entityList = familyMemberQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /family-members/count} : count all the familyMembers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countFamilyMembers(FamilyMemberCriteria criteria) {
        LOG.debug("REST request to count FamilyMembers by criteria: {}", criteria);
        return ResponseEntity.ok().body(familyMemberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /family-members/:id} : get the "id" familyMember.
     *
     * @param id the id of the familyMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the familyMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FamilyMemberDTO> getFamilyMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FamilyMember : {}", id);
        Optional<FamilyMemberDTO> familyMemberDTO = familyMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(familyMemberDTO);
    }

    /**
     * {@code DELETE  /family-members/:id} : delete the "id" familyMember.
     *
     * @param id the id of the familyMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFamilyMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FamilyMember : {}", id);
        familyMemberService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
