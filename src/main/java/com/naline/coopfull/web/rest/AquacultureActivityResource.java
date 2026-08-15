package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.AquacultureActivityRepository;
import com.naline.coopfull.service.AquacultureActivityQueryService;
import com.naline.coopfull.service.AquacultureActivityService;
import com.naline.coopfull.service.criteria.AquacultureActivityCriteria;
import com.naline.coopfull.service.dto.AquacultureActivityDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.AquacultureActivity}.
 */
@RestController
@RequestMapping("/api/aquaculture-activities")
public class AquacultureActivityResource {

    private static final Logger LOG = LoggerFactory.getLogger(AquacultureActivityResource.class);

    private static final String ENTITY_NAME = "aquacultureActivity";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final AquacultureActivityService aquacultureActivityService;

    private final AquacultureActivityRepository aquacultureActivityRepository;

    private final AquacultureActivityQueryService aquacultureActivityQueryService;

    public AquacultureActivityResource(
        AquacultureActivityService aquacultureActivityService,
        AquacultureActivityRepository aquacultureActivityRepository,
        AquacultureActivityQueryService aquacultureActivityQueryService
    ) {
        this.aquacultureActivityService = aquacultureActivityService;
        this.aquacultureActivityRepository = aquacultureActivityRepository;
        this.aquacultureActivityQueryService = aquacultureActivityQueryService;
    }

    /**
     * {@code POST  /aquaculture-activities} : Create a new aquacultureActivity.
     *
     * @param aquacultureActivityDTO the aquacultureActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aquacultureActivityDTO, or with status {@code 400 (Bad Request)} if the aquacultureActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AquacultureActivityDTO> createAquacultureActivity(
        @Valid @RequestBody AquacultureActivityDTO aquacultureActivityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AquacultureActivity : {}", aquacultureActivityDTO);
        if (aquacultureActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new aquacultureActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aquacultureActivityDTO = aquacultureActivityService.save(aquacultureActivityDTO);
        return ResponseEntity.created(new URI("/api/aquaculture-activities/" + aquacultureActivityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aquacultureActivityDTO.getId().toString()))
            .body(aquacultureActivityDTO);
    }

    /**
     * {@code PUT  /aquaculture-activities/:id} : Updates an existing aquacultureActivity.
     *
     * @param id the id of the aquacultureActivityDTO to save.
     * @param aquacultureActivityDTO the aquacultureActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aquacultureActivityDTO,
     * or with status {@code 400 (Bad Request)} if the aquacultureActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aquacultureActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AquacultureActivityDTO> updateAquacultureActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AquacultureActivityDTO aquacultureActivityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AquacultureActivity : {}, {}", id, aquacultureActivityDTO);
        if (aquacultureActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aquacultureActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aquacultureActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aquacultureActivityDTO = aquacultureActivityService.update(aquacultureActivityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aquacultureActivityDTO.getId().toString()))
            .body(aquacultureActivityDTO);
    }

    /**
     * {@code PATCH  /aquaculture-activities/:id} : Partial updates given fields of an existing aquacultureActivity, field will ignore if it is null
     *
     * @param id the id of the aquacultureActivityDTO to save.
     * @param aquacultureActivityDTO the aquacultureActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aquacultureActivityDTO,
     * or with status {@code 400 (Bad Request)} if the aquacultureActivityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aquacultureActivityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aquacultureActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AquacultureActivityDTO> partialUpdateAquacultureActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AquacultureActivityDTO aquacultureActivityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AquacultureActivity partially : {}, {}", id, aquacultureActivityDTO);
        if (aquacultureActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aquacultureActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aquacultureActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AquacultureActivityDTO> result = aquacultureActivityService.partialUpdate(aquacultureActivityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aquacultureActivityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /aquaculture-activities} : get all the Aquaculture Activities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Aquaculture Activities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AquacultureActivityDTO>> getAllAquacultureActivities(AquacultureActivityCriteria criteria) {
        LOG.debug("REST request to get AquacultureActivities by criteria: {}", criteria);

        List<AquacultureActivityDTO> entityList = aquacultureActivityQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /aquaculture-activities/count} : count all the aquacultureActivities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAquacultureActivities(AquacultureActivityCriteria criteria) {
        LOG.debug("REST request to count AquacultureActivities by criteria: {}", criteria);
        return ResponseEntity.ok().body(aquacultureActivityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /aquaculture-activities/:id} : get the "id" aquacultureActivity.
     *
     * @param id the id of the aquacultureActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aquacultureActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AquacultureActivityDTO> getAquacultureActivity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AquacultureActivity : {}", id);
        Optional<AquacultureActivityDTO> aquacultureActivityDTO = aquacultureActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aquacultureActivityDTO);
    }

    /**
     * {@code DELETE  /aquaculture-activities/:id} : delete the "id" aquacultureActivity.
     *
     * @param id the id of the aquacultureActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAquacultureActivity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AquacultureActivity : {}", id);
        aquacultureActivityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
