package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.BranchUserRepository;
import com.naline.coopfull.service.BranchUserQueryService;
import com.naline.coopfull.service.BranchUserService;
import com.naline.coopfull.service.criteria.BranchUserCriteria;
import com.naline.coopfull.service.dto.BranchUserDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.BranchUser}.
 */
@RestController
@RequestMapping("/api/branch-users")
public class BranchUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(BranchUserResource.class);

    private static final String ENTITY_NAME = "branchUser";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final BranchUserService branchUserService;

    private final BranchUserRepository branchUserRepository;

    private final BranchUserQueryService branchUserQueryService;

    public BranchUserResource(
        BranchUserService branchUserService,
        BranchUserRepository branchUserRepository,
        BranchUserQueryService branchUserQueryService
    ) {
        this.branchUserService = branchUserService;
        this.branchUserRepository = branchUserRepository;
        this.branchUserQueryService = branchUserQueryService;
    }

    /**
     * {@code POST  /branch-users} : Create a new branchUser.
     *
     * @param branchUserDTO the branchUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new branchUserDTO, or with status {@code 400 (Bad Request)} if the branchUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BranchUserDTO> createBranchUser(@Valid @RequestBody BranchUserDTO branchUserDTO) throws URISyntaxException {
        LOG.debug("REST request to save BranchUser : {}", branchUserDTO);
        if (branchUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new branchUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        branchUserDTO = branchUserService.save(branchUserDTO);
        return ResponseEntity.created(new URI("/api/branch-users/" + branchUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, branchUserDTO.getId().toString()))
            .body(branchUserDTO);
    }

    /**
     * {@code PUT  /branch-users/:id} : Updates an existing branchUser.
     *
     * @param id the id of the branchUserDTO to save.
     * @param branchUserDTO the branchUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchUserDTO,
     * or with status {@code 400 (Bad Request)} if the branchUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the branchUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BranchUserDTO> updateBranchUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BranchUserDTO branchUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BranchUser : {}, {}", id, branchUserDTO);
        if (branchUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        branchUserDTO = branchUserService.update(branchUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, branchUserDTO.getId().toString()))
            .body(branchUserDTO);
    }

    /**
     * {@code PATCH  /branch-users/:id} : Partial updates given fields of an existing branchUser, field will ignore if it is null
     *
     * @param id the id of the branchUserDTO to save.
     * @param branchUserDTO the branchUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated branchUserDTO,
     * or with status {@code 400 (Bad Request)} if the branchUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the branchUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the branchUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BranchUserDTO> partialUpdateBranchUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BranchUserDTO branchUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BranchUser partially : {}, {}", id, branchUserDTO);
        if (branchUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, branchUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!branchUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BranchUserDTO> result = branchUserService.partialUpdate(branchUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, branchUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /branch-users} : get all the Branch Users.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Branch Users in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BranchUserDTO>> getAllBranchUsers(BranchUserCriteria criteria) {
        LOG.debug("REST request to get BranchUsers by criteria: {}", criteria);

        List<BranchUserDTO> entityList = branchUserQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /branch-users/count} : count all the branchUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBranchUsers(BranchUserCriteria criteria) {
        LOG.debug("REST request to count BranchUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(branchUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /branch-users/:id} : get the "id" branchUser.
     *
     * @param id the id of the branchUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the branchUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BranchUserDTO> getBranchUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BranchUser : {}", id);
        Optional<BranchUserDTO> branchUserDTO = branchUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(branchUserDTO);
    }

    /**
     * {@code DELETE  /branch-users/:id} : delete the "id" branchUser.
     *
     * @param id the id of the branchUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranchUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BranchUser : {}", id);
        branchUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
