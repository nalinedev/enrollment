package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.AquacultureProductionRepository;
import com.naline.coopfull.service.AquacultureProductionQueryService;
import com.naline.coopfull.service.AquacultureProductionService;
import com.naline.coopfull.service.criteria.AquacultureProductionCriteria;
import com.naline.coopfull.service.dto.AquacultureProductionDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.AquacultureProduction}.
 */
@RestController
@RequestMapping("/api/aquaculture-productions")
public class AquacultureProductionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AquacultureProductionResource.class);

    private static final String ENTITY_NAME = "aquacultureProduction";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final AquacultureProductionService aquacultureProductionService;

    private final AquacultureProductionRepository aquacultureProductionRepository;

    private final AquacultureProductionQueryService aquacultureProductionQueryService;

    public AquacultureProductionResource(
        AquacultureProductionService aquacultureProductionService,
        AquacultureProductionRepository aquacultureProductionRepository,
        AquacultureProductionQueryService aquacultureProductionQueryService
    ) {
        this.aquacultureProductionService = aquacultureProductionService;
        this.aquacultureProductionRepository = aquacultureProductionRepository;
        this.aquacultureProductionQueryService = aquacultureProductionQueryService;
    }

    /**
     * {@code POST  /aquaculture-productions} : Create a new aquacultureProduction.
     *
     * @param aquacultureProductionDTO the aquacultureProductionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aquacultureProductionDTO, or with status {@code 400 (Bad Request)} if the aquacultureProduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AquacultureProductionDTO> createAquacultureProduction(
        @Valid @RequestBody AquacultureProductionDTO aquacultureProductionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AquacultureProduction : {}", aquacultureProductionDTO);
        if (aquacultureProductionDTO.getId() != null) {
            throw new BadRequestAlertException("A new aquacultureProduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aquacultureProductionDTO = aquacultureProductionService.save(aquacultureProductionDTO);
        return ResponseEntity.created(new URI("/api/aquaculture-productions/" + aquacultureProductionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aquacultureProductionDTO.getId().toString()))
            .body(aquacultureProductionDTO);
    }

    /**
     * {@code PUT  /aquaculture-productions/:id} : Updates an existing aquacultureProduction.
     *
     * @param id the id of the aquacultureProductionDTO to save.
     * @param aquacultureProductionDTO the aquacultureProductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aquacultureProductionDTO,
     * or with status {@code 400 (Bad Request)} if the aquacultureProductionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aquacultureProductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AquacultureProductionDTO> updateAquacultureProduction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AquacultureProductionDTO aquacultureProductionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AquacultureProduction : {}, {}", id, aquacultureProductionDTO);
        if (aquacultureProductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aquacultureProductionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aquacultureProductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aquacultureProductionDTO = aquacultureProductionService.update(aquacultureProductionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aquacultureProductionDTO.getId().toString()))
            .body(aquacultureProductionDTO);
    }

    /**
     * {@code PATCH  /aquaculture-productions/:id} : Partial updates given fields of an existing aquacultureProduction, field will ignore if it is null
     *
     * @param id the id of the aquacultureProductionDTO to save.
     * @param aquacultureProductionDTO the aquacultureProductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aquacultureProductionDTO,
     * or with status {@code 400 (Bad Request)} if the aquacultureProductionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aquacultureProductionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aquacultureProductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AquacultureProductionDTO> partialUpdateAquacultureProduction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AquacultureProductionDTO aquacultureProductionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AquacultureProduction partially : {}, {}", id, aquacultureProductionDTO);
        if (aquacultureProductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aquacultureProductionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aquacultureProductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AquacultureProductionDTO> result = aquacultureProductionService.partialUpdate(aquacultureProductionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aquacultureProductionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /aquaculture-productions} : get all the Aquaculture Productions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Aquaculture Productions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AquacultureProductionDTO>> getAllAquacultureProductions(AquacultureProductionCriteria criteria) {
        LOG.debug("REST request to get AquacultureProductions by criteria: {}", criteria);

        List<AquacultureProductionDTO> entityList = aquacultureProductionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /aquaculture-productions/count} : count all the aquacultureProductions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAquacultureProductions(AquacultureProductionCriteria criteria) {
        LOG.debug("REST request to count AquacultureProductions by criteria: {}", criteria);
        return ResponseEntity.ok().body(aquacultureProductionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /aquaculture-productions/:id} : get the "id" aquacultureProduction.
     *
     * @param id the id of the aquacultureProductionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aquacultureProductionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AquacultureProductionDTO> getAquacultureProduction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AquacultureProduction : {}", id);
        Optional<AquacultureProductionDTO> aquacultureProductionDTO = aquacultureProductionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aquacultureProductionDTO);
    }

    /**
     * {@code DELETE  /aquaculture-productions/:id} : delete the "id" aquacultureProduction.
     *
     * @param id the id of the aquacultureProductionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAquacultureProduction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AquacultureProduction : {}", id);
        aquacultureProductionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
