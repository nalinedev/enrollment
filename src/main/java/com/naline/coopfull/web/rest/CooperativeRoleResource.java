package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.CooperativeRoleRepository;
import com.naline.coopfull.service.CooperativeRoleQueryService;
import com.naline.coopfull.service.CooperativeRoleService;
import com.naline.coopfull.service.criteria.CooperativeRoleCriteria;
import com.naline.coopfull.service.dto.CooperativeRoleDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.CooperativeRole}.
 */
@RestController
@RequestMapping("/api/cooperative-roles")
public class CooperativeRoleResource {

    private static final Logger LOG = LoggerFactory.getLogger(CooperativeRoleResource.class);

    private static final String ENTITY_NAME = "cooperativeRole";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final CooperativeRoleService cooperativeRoleService;

    private final CooperativeRoleRepository cooperativeRoleRepository;

    private final CooperativeRoleQueryService cooperativeRoleQueryService;

    public CooperativeRoleResource(
        CooperativeRoleService cooperativeRoleService,
        CooperativeRoleRepository cooperativeRoleRepository,
        CooperativeRoleQueryService cooperativeRoleQueryService
    ) {
        this.cooperativeRoleService = cooperativeRoleService;
        this.cooperativeRoleRepository = cooperativeRoleRepository;
        this.cooperativeRoleQueryService = cooperativeRoleQueryService;
    }

    /**
     * {@code POST  /cooperative-roles} : Create a new cooperativeRole.
     *
     * @param cooperativeRoleDTO the cooperativeRoleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cooperativeRoleDTO, or with status {@code 400 (Bad Request)} if the cooperativeRole has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CooperativeRoleDTO> createCooperativeRole(@Valid @RequestBody CooperativeRoleDTO cooperativeRoleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CooperativeRole : {}", cooperativeRoleDTO);
        if (cooperativeRoleDTO.getId() != null) {
            throw new BadRequestAlertException("A new cooperativeRole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cooperativeRoleDTO = cooperativeRoleService.save(cooperativeRoleDTO);
        return ResponseEntity.created(new URI("/api/cooperative-roles/" + cooperativeRoleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cooperativeRoleDTO.getId().toString()))
            .body(cooperativeRoleDTO);
    }

    /**
     * {@code PUT  /cooperative-roles/:id} : Updates an existing cooperativeRole.
     *
     * @param id the id of the cooperativeRoleDTO to save.
     * @param cooperativeRoleDTO the cooperativeRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cooperativeRoleDTO,
     * or with status {@code 400 (Bad Request)} if the cooperativeRoleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cooperativeRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CooperativeRoleDTO> updateCooperativeRole(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CooperativeRoleDTO cooperativeRoleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CooperativeRole : {}, {}", id, cooperativeRoleDTO);
        if (cooperativeRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cooperativeRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cooperativeRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cooperativeRoleDTO = cooperativeRoleService.update(cooperativeRoleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cooperativeRoleDTO.getId().toString()))
            .body(cooperativeRoleDTO);
    }

    /**
     * {@code PATCH  /cooperative-roles/:id} : Partial updates given fields of an existing cooperativeRole, field will ignore if it is null
     *
     * @param id the id of the cooperativeRoleDTO to save.
     * @param cooperativeRoleDTO the cooperativeRoleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cooperativeRoleDTO,
     * or with status {@code 400 (Bad Request)} if the cooperativeRoleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cooperativeRoleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cooperativeRoleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CooperativeRoleDTO> partialUpdateCooperativeRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CooperativeRoleDTO cooperativeRoleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CooperativeRole partially : {}, {}", id, cooperativeRoleDTO);
        if (cooperativeRoleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cooperativeRoleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cooperativeRoleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CooperativeRoleDTO> result = cooperativeRoleService.partialUpdate(cooperativeRoleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cooperativeRoleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cooperative-roles} : get all the Cooperative Roles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cooperative Roles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CooperativeRoleDTO>> getAllCooperativeRoles(CooperativeRoleCriteria criteria) {
        LOG.debug("REST request to get CooperativeRoles by criteria: {}", criteria);

        List<CooperativeRoleDTO> entityList = cooperativeRoleQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /cooperative-roles/count} : count all the cooperativeRoles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCooperativeRoles(CooperativeRoleCriteria criteria) {
        LOG.debug("REST request to count CooperativeRoles by criteria: {}", criteria);
        return ResponseEntity.ok().body(cooperativeRoleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cooperative-roles/:id} : get the "id" cooperativeRole.
     *
     * @param id the id of the cooperativeRoleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cooperativeRoleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CooperativeRoleDTO> getCooperativeRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CooperativeRole : {}", id);
        Optional<CooperativeRoleDTO> cooperativeRoleDTO = cooperativeRoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cooperativeRoleDTO);
    }

    /**
     * {@code DELETE  /cooperative-roles/:id} : delete the "id" cooperativeRole.
     *
     * @param id the id of the cooperativeRoleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCooperativeRole(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CooperativeRole : {}", id);
        cooperativeRoleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
