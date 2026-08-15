package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.CooperativeUserRepository;
import com.naline.coopfull.service.CooperativeUserQueryService;
import com.naline.coopfull.service.CooperativeUserService;
import com.naline.coopfull.service.criteria.CooperativeUserCriteria;
import com.naline.coopfull.service.dto.CooperativeUserDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.CooperativeUser}.
 */
@RestController
@RequestMapping("/api/cooperative-users")
public class CooperativeUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(CooperativeUserResource.class);

    private static final String ENTITY_NAME = "cooperativeUser";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final CooperativeUserService cooperativeUserService;

    private final CooperativeUserRepository cooperativeUserRepository;

    private final CooperativeUserQueryService cooperativeUserQueryService;

    public CooperativeUserResource(
        CooperativeUserService cooperativeUserService,
        CooperativeUserRepository cooperativeUserRepository,
        CooperativeUserQueryService cooperativeUserQueryService
    ) {
        this.cooperativeUserService = cooperativeUserService;
        this.cooperativeUserRepository = cooperativeUserRepository;
        this.cooperativeUserQueryService = cooperativeUserQueryService;
    }

    /**
     * {@code POST  /cooperative-users} : Create a new cooperativeUser.
     *
     * @param cooperativeUserDTO the cooperativeUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cooperativeUserDTO, or with status {@code 400 (Bad Request)} if the cooperativeUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CooperativeUserDTO> createCooperativeUser(@Valid @RequestBody CooperativeUserDTO cooperativeUserDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CooperativeUser : {}", cooperativeUserDTO);
        if (cooperativeUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new cooperativeUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cooperativeUserDTO = cooperativeUserService.save(cooperativeUserDTO);
        return ResponseEntity.created(new URI("/api/cooperative-users/" + cooperativeUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cooperativeUserDTO.getId().toString()))
            .body(cooperativeUserDTO);
    }

    /**
     * {@code PUT  /cooperative-users/:id} : Updates an existing cooperativeUser.
     *
     * @param id the id of the cooperativeUserDTO to save.
     * @param cooperativeUserDTO the cooperativeUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cooperativeUserDTO,
     * or with status {@code 400 (Bad Request)} if the cooperativeUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cooperativeUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CooperativeUserDTO> updateCooperativeUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CooperativeUserDTO cooperativeUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CooperativeUser : {}, {}", id, cooperativeUserDTO);
        if (cooperativeUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cooperativeUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cooperativeUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cooperativeUserDTO = cooperativeUserService.update(cooperativeUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cooperativeUserDTO.getId().toString()))
            .body(cooperativeUserDTO);
    }

    /**
     * {@code PATCH  /cooperative-users/:id} : Partial updates given fields of an existing cooperativeUser, field will ignore if it is null
     *
     * @param id the id of the cooperativeUserDTO to save.
     * @param cooperativeUserDTO the cooperativeUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cooperativeUserDTO,
     * or with status {@code 400 (Bad Request)} if the cooperativeUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cooperativeUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cooperativeUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CooperativeUserDTO> partialUpdateCooperativeUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CooperativeUserDTO cooperativeUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CooperativeUser partially : {}, {}", id, cooperativeUserDTO);
        if (cooperativeUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cooperativeUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cooperativeUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CooperativeUserDTO> result = cooperativeUserService.partialUpdate(cooperativeUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cooperativeUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cooperative-users} : get all the Cooperative Users.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cooperative Users in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CooperativeUserDTO>> getAllCooperativeUsers(CooperativeUserCriteria criteria) {
        LOG.debug("REST request to get CooperativeUsers by criteria: {}", criteria);

        List<CooperativeUserDTO> entityList = cooperativeUserQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /cooperative-users/count} : count all the cooperativeUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCooperativeUsers(CooperativeUserCriteria criteria) {
        LOG.debug("REST request to count CooperativeUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(cooperativeUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cooperative-users/:id} : get the "id" cooperativeUser.
     *
     * @param id the id of the cooperativeUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cooperativeUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CooperativeUserDTO> getCooperativeUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CooperativeUser : {}", id);
        Optional<CooperativeUserDTO> cooperativeUserDTO = cooperativeUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cooperativeUserDTO);
    }

    /**
     * {@code DELETE  /cooperative-users/:id} : delete the "id" cooperativeUser.
     *
     * @param id the id of the cooperativeUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCooperativeUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CooperativeUser : {}", id);
        cooperativeUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
