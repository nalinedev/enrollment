package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.LivestockProductionRepository;
import com.naline.coopfull.service.LivestockProductionQueryService;
import com.naline.coopfull.service.LivestockProductionService;
import com.naline.coopfull.service.criteria.LivestockProductionCriteria;
import com.naline.coopfull.service.dto.LivestockProductionDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.LivestockProduction}.
 */
@RestController
@RequestMapping("/api/livestock-productions")
public class LivestockProductionResource {

    private static final Logger LOG = LoggerFactory.getLogger(LivestockProductionResource.class);

    private static final String ENTITY_NAME = "livestockProduction";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final LivestockProductionService livestockProductionService;

    private final LivestockProductionRepository livestockProductionRepository;

    private final LivestockProductionQueryService livestockProductionQueryService;

    public LivestockProductionResource(
        LivestockProductionService livestockProductionService,
        LivestockProductionRepository livestockProductionRepository,
        LivestockProductionQueryService livestockProductionQueryService
    ) {
        this.livestockProductionService = livestockProductionService;
        this.livestockProductionRepository = livestockProductionRepository;
        this.livestockProductionQueryService = livestockProductionQueryService;
    }

    /**
     * {@code POST  /livestock-productions} : Create a new livestockProduction.
     *
     * @param livestockProductionDTO the livestockProductionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livestockProductionDTO, or with status {@code 400 (Bad Request)} if the livestockProduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LivestockProductionDTO> createLivestockProduction(
        @Valid @RequestBody LivestockProductionDTO livestockProductionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save LivestockProduction : {}", livestockProductionDTO);
        if (livestockProductionDTO.getId() != null) {
            throw new BadRequestAlertException("A new livestockProduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        livestockProductionDTO = livestockProductionService.save(livestockProductionDTO);
        return ResponseEntity.created(new URI("/api/livestock-productions/" + livestockProductionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, livestockProductionDTO.getId().toString()))
            .body(livestockProductionDTO);
    }

    /**
     * {@code PUT  /livestock-productions/:id} : Updates an existing livestockProduction.
     *
     * @param id the id of the livestockProductionDTO to save.
     * @param livestockProductionDTO the livestockProductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livestockProductionDTO,
     * or with status {@code 400 (Bad Request)} if the livestockProductionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livestockProductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LivestockProductionDTO> updateLivestockProduction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LivestockProductionDTO livestockProductionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LivestockProduction : {}, {}", id, livestockProductionDTO);
        if (livestockProductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livestockProductionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livestockProductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        livestockProductionDTO = livestockProductionService.update(livestockProductionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livestockProductionDTO.getId().toString()))
            .body(livestockProductionDTO);
    }

    /**
     * {@code PATCH  /livestock-productions/:id} : Partial updates given fields of an existing livestockProduction, field will ignore if it is null
     *
     * @param id the id of the livestockProductionDTO to save.
     * @param livestockProductionDTO the livestockProductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livestockProductionDTO,
     * or with status {@code 400 (Bad Request)} if the livestockProductionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the livestockProductionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the livestockProductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LivestockProductionDTO> partialUpdateLivestockProduction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LivestockProductionDTO livestockProductionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LivestockProduction partially : {}, {}", id, livestockProductionDTO);
        if (livestockProductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livestockProductionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livestockProductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LivestockProductionDTO> result = livestockProductionService.partialUpdate(livestockProductionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livestockProductionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /livestock-productions} : get all the Livestock Productions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Livestock Productions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LivestockProductionDTO>> getAllLivestockProductions(LivestockProductionCriteria criteria) {
        LOG.debug("REST request to get LivestockProductions by criteria: {}", criteria);

        List<LivestockProductionDTO> entityList = livestockProductionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /livestock-productions/count} : count all the livestockProductions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLivestockProductions(LivestockProductionCriteria criteria) {
        LOG.debug("REST request to count LivestockProductions by criteria: {}", criteria);
        return ResponseEntity.ok().body(livestockProductionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /livestock-productions/:id} : get the "id" livestockProduction.
     *
     * @param id the id of the livestockProductionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livestockProductionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LivestockProductionDTO> getLivestockProduction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LivestockProduction : {}", id);
        Optional<LivestockProductionDTO> livestockProductionDTO = livestockProductionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livestockProductionDTO);
    }

    /**
     * {@code DELETE  /livestock-productions/:id} : delete the "id" livestockProduction.
     *
     * @param id the id of the livestockProductionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivestockProduction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LivestockProduction : {}", id);
        livestockProductionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
