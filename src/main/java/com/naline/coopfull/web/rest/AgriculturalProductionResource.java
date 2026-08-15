package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.AgriculturalProductionRepository;
import com.naline.coopfull.service.AgriculturalProductionQueryService;
import com.naline.coopfull.service.AgriculturalProductionService;
import com.naline.coopfull.service.criteria.AgriculturalProductionCriteria;
import com.naline.coopfull.service.dto.AgriculturalProductionDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.AgriculturalProduction}.
 */
@RestController
@RequestMapping("/api/agricultural-productions")
public class AgriculturalProductionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AgriculturalProductionResource.class);

    private static final String ENTITY_NAME = "agriculturalProduction";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final AgriculturalProductionService agriculturalProductionService;

    private final AgriculturalProductionRepository agriculturalProductionRepository;

    private final AgriculturalProductionQueryService agriculturalProductionQueryService;

    public AgriculturalProductionResource(
        AgriculturalProductionService agriculturalProductionService,
        AgriculturalProductionRepository agriculturalProductionRepository,
        AgriculturalProductionQueryService agriculturalProductionQueryService
    ) {
        this.agriculturalProductionService = agriculturalProductionService;
        this.agriculturalProductionRepository = agriculturalProductionRepository;
        this.agriculturalProductionQueryService = agriculturalProductionQueryService;
    }

    /**
     * {@code POST  /agricultural-productions} : Create a new agriculturalProduction.
     *
     * @param agriculturalProductionDTO the agriculturalProductionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agriculturalProductionDTO, or with status {@code 400 (Bad Request)} if the agriculturalProduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AgriculturalProductionDTO> createAgriculturalProduction(
        @Valid @RequestBody AgriculturalProductionDTO agriculturalProductionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AgriculturalProduction : {}", agriculturalProductionDTO);
        if (agriculturalProductionDTO.getId() != null) {
            throw new BadRequestAlertException("A new agriculturalProduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agriculturalProductionDTO = agriculturalProductionService.save(agriculturalProductionDTO);
        return ResponseEntity.created(new URI("/api/agricultural-productions/" + agriculturalProductionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, agriculturalProductionDTO.getId().toString()))
            .body(agriculturalProductionDTO);
    }

    /**
     * {@code PUT  /agricultural-productions/:id} : Updates an existing agriculturalProduction.
     *
     * @param id the id of the agriculturalProductionDTO to save.
     * @param agriculturalProductionDTO the agriculturalProductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agriculturalProductionDTO,
     * or with status {@code 400 (Bad Request)} if the agriculturalProductionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agriculturalProductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgriculturalProductionDTO> updateAgriculturalProduction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgriculturalProductionDTO agriculturalProductionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AgriculturalProduction : {}, {}", id, agriculturalProductionDTO);
        if (agriculturalProductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agriculturalProductionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agriculturalProductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agriculturalProductionDTO = agriculturalProductionService.update(agriculturalProductionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agriculturalProductionDTO.getId().toString()))
            .body(agriculturalProductionDTO);
    }

    /**
     * {@code PATCH  /agricultural-productions/:id} : Partial updates given fields of an existing agriculturalProduction, field will ignore if it is null
     *
     * @param id the id of the agriculturalProductionDTO to save.
     * @param agriculturalProductionDTO the agriculturalProductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agriculturalProductionDTO,
     * or with status {@code 400 (Bad Request)} if the agriculturalProductionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agriculturalProductionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agriculturalProductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgriculturalProductionDTO> partialUpdateAgriculturalProduction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgriculturalProductionDTO agriculturalProductionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AgriculturalProduction partially : {}, {}", id, agriculturalProductionDTO);
        if (agriculturalProductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agriculturalProductionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agriculturalProductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgriculturalProductionDTO> result = agriculturalProductionService.partialUpdate(agriculturalProductionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agriculturalProductionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agricultural-productions} : get all the Agricultural Productions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Agricultural Productions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AgriculturalProductionDTO>> getAllAgriculturalProductions(AgriculturalProductionCriteria criteria) {
        LOG.debug("REST request to get AgriculturalProductions by criteria: {}", criteria);

        List<AgriculturalProductionDTO> entityList = agriculturalProductionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /agricultural-productions/count} : count all the agriculturalProductions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAgriculturalProductions(AgriculturalProductionCriteria criteria) {
        LOG.debug("REST request to count AgriculturalProductions by criteria: {}", criteria);
        return ResponseEntity.ok().body(agriculturalProductionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agricultural-productions/:id} : get the "id" agriculturalProduction.
     *
     * @param id the id of the agriculturalProductionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agriculturalProductionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgriculturalProductionDTO> getAgriculturalProduction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AgriculturalProduction : {}", id);
        Optional<AgriculturalProductionDTO> agriculturalProductionDTO = agriculturalProductionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agriculturalProductionDTO);
    }

    /**
     * {@code DELETE  /agricultural-productions/:id} : delete the "id" agriculturalProduction.
     *
     * @param id the id of the agriculturalProductionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgriculturalProduction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AgriculturalProduction : {}", id);
        agriculturalProductionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
