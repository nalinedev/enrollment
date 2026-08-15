package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.CooperativeBranchRepository;
import com.naline.coopfull.service.CooperativeBranchQueryService;
import com.naline.coopfull.service.CooperativeBranchService;
import com.naline.coopfull.service.criteria.CooperativeBranchCriteria;
import com.naline.coopfull.service.dto.CooperativeBranchDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.CooperativeBranch}.
 */
@RestController
@RequestMapping("/api/cooperative-branches")
public class CooperativeBranchResource {

    private static final Logger LOG = LoggerFactory.getLogger(CooperativeBranchResource.class);

    private static final String ENTITY_NAME = "cooperativeBranch";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final CooperativeBranchService cooperativeBranchService;

    private final CooperativeBranchRepository cooperativeBranchRepository;

    private final CooperativeBranchQueryService cooperativeBranchQueryService;

    public CooperativeBranchResource(
        CooperativeBranchService cooperativeBranchService,
        CooperativeBranchRepository cooperativeBranchRepository,
        CooperativeBranchQueryService cooperativeBranchQueryService
    ) {
        this.cooperativeBranchService = cooperativeBranchService;
        this.cooperativeBranchRepository = cooperativeBranchRepository;
        this.cooperativeBranchQueryService = cooperativeBranchQueryService;
    }

    /**
     * {@code POST  /cooperative-branches} : Create a new cooperativeBranch.
     *
     * @param cooperativeBranchDTO the cooperativeBranchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cooperativeBranchDTO, or with status {@code 400 (Bad Request)} if the cooperativeBranch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CooperativeBranchDTO> createCooperativeBranch(@Valid @RequestBody CooperativeBranchDTO cooperativeBranchDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CooperativeBranch : {}", cooperativeBranchDTO);
        if (cooperativeBranchDTO.getId() != null) {
            throw new BadRequestAlertException("A new cooperativeBranch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cooperativeBranchDTO = cooperativeBranchService.save(cooperativeBranchDTO);
        return ResponseEntity.created(new URI("/api/cooperative-branches/" + cooperativeBranchDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cooperativeBranchDTO.getId().toString()))
            .body(cooperativeBranchDTO);
    }

    /**
     * {@code PUT  /cooperative-branches/:id} : Updates an existing cooperativeBranch.
     *
     * @param id the id of the cooperativeBranchDTO to save.
     * @param cooperativeBranchDTO the cooperativeBranchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cooperativeBranchDTO,
     * or with status {@code 400 (Bad Request)} if the cooperativeBranchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cooperativeBranchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CooperativeBranchDTO> updateCooperativeBranch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CooperativeBranchDTO cooperativeBranchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CooperativeBranch : {}, {}", id, cooperativeBranchDTO);
        if (cooperativeBranchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cooperativeBranchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cooperativeBranchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cooperativeBranchDTO = cooperativeBranchService.update(cooperativeBranchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cooperativeBranchDTO.getId().toString()))
            .body(cooperativeBranchDTO);
    }

    /**
     * {@code PATCH  /cooperative-branches/:id} : Partial updates given fields of an existing cooperativeBranch, field will ignore if it is null
     *
     * @param id the id of the cooperativeBranchDTO to save.
     * @param cooperativeBranchDTO the cooperativeBranchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cooperativeBranchDTO,
     * or with status {@code 400 (Bad Request)} if the cooperativeBranchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cooperativeBranchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cooperativeBranchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CooperativeBranchDTO> partialUpdateCooperativeBranch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CooperativeBranchDTO cooperativeBranchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CooperativeBranch partially : {}, {}", id, cooperativeBranchDTO);
        if (cooperativeBranchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cooperativeBranchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cooperativeBranchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CooperativeBranchDTO> result = cooperativeBranchService.partialUpdate(cooperativeBranchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cooperativeBranchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cooperative-branches} : get all the Cooperative Branches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cooperative Branches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CooperativeBranchDTO>> getAllCooperativeBranches(CooperativeBranchCriteria criteria) {
        LOG.debug("REST request to get CooperativeBranches by criteria: {}", criteria);

        List<CooperativeBranchDTO> entityList = cooperativeBranchQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /cooperative-branches/count} : count all the cooperativeBranches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCooperativeBranches(CooperativeBranchCriteria criteria) {
        LOG.debug("REST request to count CooperativeBranches by criteria: {}", criteria);
        return ResponseEntity.ok().body(cooperativeBranchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cooperative-branches/:id} : get the "id" cooperativeBranch.
     *
     * @param id the id of the cooperativeBranchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cooperativeBranchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CooperativeBranchDTO> getCooperativeBranch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CooperativeBranch : {}", id);
        Optional<CooperativeBranchDTO> cooperativeBranchDTO = cooperativeBranchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cooperativeBranchDTO);
    }

    /**
     * {@code DELETE  /cooperative-branches/:id} : delete the "id" cooperativeBranch.
     *
     * @param id the id of the cooperativeBranchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCooperativeBranch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CooperativeBranch : {}", id);
        cooperativeBranchService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
