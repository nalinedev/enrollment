package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.AgriculturalActivityRepository;
import com.naline.coopfull.service.AgriculturalActivityQueryService;
import com.naline.coopfull.service.AgriculturalActivityService;
import com.naline.coopfull.service.criteria.AgriculturalActivityCriteria;
import com.naline.coopfull.service.dto.AgriculturalActivityDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.AgriculturalActivity}.
 */
@RestController
@RequestMapping("/api/agricultural-activities")
public class AgriculturalActivityResource {

    private static final Logger LOG = LoggerFactory.getLogger(AgriculturalActivityResource.class);

    private static final String ENTITY_NAME = "agriculturalActivity";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final AgriculturalActivityService agriculturalActivityService;

    private final AgriculturalActivityRepository agriculturalActivityRepository;

    private final AgriculturalActivityQueryService agriculturalActivityQueryService;

    public AgriculturalActivityResource(
        AgriculturalActivityService agriculturalActivityService,
        AgriculturalActivityRepository agriculturalActivityRepository,
        AgriculturalActivityQueryService agriculturalActivityQueryService
    ) {
        this.agriculturalActivityService = agriculturalActivityService;
        this.agriculturalActivityRepository = agriculturalActivityRepository;
        this.agriculturalActivityQueryService = agriculturalActivityQueryService;
    }

    /**
     * {@code POST  /agricultural-activities} : Create a new agriculturalActivity.
     *
     * @param agriculturalActivityDTO the agriculturalActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agriculturalActivityDTO, or with status {@code 400 (Bad Request)} if the agriculturalActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AgriculturalActivityDTO> createAgriculturalActivity(
        @Valid @RequestBody AgriculturalActivityDTO agriculturalActivityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AgriculturalActivity : {}", agriculturalActivityDTO);
        if (agriculturalActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new agriculturalActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        agriculturalActivityDTO = agriculturalActivityService.save(agriculturalActivityDTO);
        return ResponseEntity.created(new URI("/api/agricultural-activities/" + agriculturalActivityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, agriculturalActivityDTO.getId().toString()))
            .body(agriculturalActivityDTO);
    }

    /**
     * {@code PUT  /agricultural-activities/:id} : Updates an existing agriculturalActivity.
     *
     * @param id the id of the agriculturalActivityDTO to save.
     * @param agriculturalActivityDTO the agriculturalActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agriculturalActivityDTO,
     * or with status {@code 400 (Bad Request)} if the agriculturalActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agriculturalActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AgriculturalActivityDTO> updateAgriculturalActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgriculturalActivityDTO agriculturalActivityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AgriculturalActivity : {}, {}", id, agriculturalActivityDTO);
        if (agriculturalActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agriculturalActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agriculturalActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        agriculturalActivityDTO = agriculturalActivityService.update(agriculturalActivityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agriculturalActivityDTO.getId().toString()))
            .body(agriculturalActivityDTO);
    }

    /**
     * {@code PATCH  /agricultural-activities/:id} : Partial updates given fields of an existing agriculturalActivity, field will ignore if it is null
     *
     * @param id the id of the agriculturalActivityDTO to save.
     * @param agriculturalActivityDTO the agriculturalActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agriculturalActivityDTO,
     * or with status {@code 400 (Bad Request)} if the agriculturalActivityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agriculturalActivityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agriculturalActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgriculturalActivityDTO> partialUpdateAgriculturalActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgriculturalActivityDTO agriculturalActivityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AgriculturalActivity partially : {}, {}", id, agriculturalActivityDTO);
        if (agriculturalActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agriculturalActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agriculturalActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgriculturalActivityDTO> result = agriculturalActivityService.partialUpdate(agriculturalActivityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agriculturalActivityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agricultural-activities} : get all the Agricultural Activities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Agricultural Activities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AgriculturalActivityDTO>> getAllAgriculturalActivities(AgriculturalActivityCriteria criteria) {
        LOG.debug("REST request to get AgriculturalActivities by criteria: {}", criteria);

        List<AgriculturalActivityDTO> entityList = agriculturalActivityQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /agricultural-activities/count} : count all the agriculturalActivities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAgriculturalActivities(AgriculturalActivityCriteria criteria) {
        LOG.debug("REST request to count AgriculturalActivities by criteria: {}", criteria);
        return ResponseEntity.ok().body(agriculturalActivityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agricultural-activities/:id} : get the "id" agriculturalActivity.
     *
     * @param id the id of the agriculturalActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agriculturalActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AgriculturalActivityDTO> getAgriculturalActivity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AgriculturalActivity : {}", id);
        Optional<AgriculturalActivityDTO> agriculturalActivityDTO = agriculturalActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agriculturalActivityDTO);
    }

    /**
     * {@code DELETE  /agricultural-activities/:id} : delete the "id" agriculturalActivity.
     *
     * @param id the id of the agriculturalActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgriculturalActivity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AgriculturalActivity : {}", id);
        agriculturalActivityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
