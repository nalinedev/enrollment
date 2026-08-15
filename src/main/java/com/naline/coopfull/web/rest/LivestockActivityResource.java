package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.LivestockActivityRepository;
import com.naline.coopfull.service.LivestockActivityQueryService;
import com.naline.coopfull.service.LivestockActivityService;
import com.naline.coopfull.service.criteria.LivestockActivityCriteria;
import com.naline.coopfull.service.dto.LivestockActivityDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.LivestockActivity}.
 */
@RestController
@RequestMapping("/api/livestock-activities")
public class LivestockActivityResource {

    private static final Logger LOG = LoggerFactory.getLogger(LivestockActivityResource.class);

    private static final String ENTITY_NAME = "livestockActivity";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final LivestockActivityService livestockActivityService;

    private final LivestockActivityRepository livestockActivityRepository;

    private final LivestockActivityQueryService livestockActivityQueryService;

    public LivestockActivityResource(
        LivestockActivityService livestockActivityService,
        LivestockActivityRepository livestockActivityRepository,
        LivestockActivityQueryService livestockActivityQueryService
    ) {
        this.livestockActivityService = livestockActivityService;
        this.livestockActivityRepository = livestockActivityRepository;
        this.livestockActivityQueryService = livestockActivityQueryService;
    }

    /**
     * {@code POST  /livestock-activities} : Create a new livestockActivity.
     *
     * @param livestockActivityDTO the livestockActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livestockActivityDTO, or with status {@code 400 (Bad Request)} if the livestockActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LivestockActivityDTO> createLivestockActivity(@Valid @RequestBody LivestockActivityDTO livestockActivityDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LivestockActivity : {}", livestockActivityDTO);
        if (livestockActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new livestockActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        livestockActivityDTO = livestockActivityService.save(livestockActivityDTO);
        return ResponseEntity.created(new URI("/api/livestock-activities/" + livestockActivityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, livestockActivityDTO.getId().toString()))
            .body(livestockActivityDTO);
    }

    /**
     * {@code PUT  /livestock-activities/:id} : Updates an existing livestockActivity.
     *
     * @param id the id of the livestockActivityDTO to save.
     * @param livestockActivityDTO the livestockActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livestockActivityDTO,
     * or with status {@code 400 (Bad Request)} if the livestockActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livestockActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LivestockActivityDTO> updateLivestockActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LivestockActivityDTO livestockActivityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LivestockActivity : {}, {}", id, livestockActivityDTO);
        if (livestockActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livestockActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livestockActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        livestockActivityDTO = livestockActivityService.update(livestockActivityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livestockActivityDTO.getId().toString()))
            .body(livestockActivityDTO);
    }

    /**
     * {@code PATCH  /livestock-activities/:id} : Partial updates given fields of an existing livestockActivity, field will ignore if it is null
     *
     * @param id the id of the livestockActivityDTO to save.
     * @param livestockActivityDTO the livestockActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livestockActivityDTO,
     * or with status {@code 400 (Bad Request)} if the livestockActivityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the livestockActivityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the livestockActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LivestockActivityDTO> partialUpdateLivestockActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LivestockActivityDTO livestockActivityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LivestockActivity partially : {}, {}", id, livestockActivityDTO);
        if (livestockActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livestockActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livestockActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LivestockActivityDTO> result = livestockActivityService.partialUpdate(livestockActivityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livestockActivityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /livestock-activities} : get all the Livestock Activities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Livestock Activities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LivestockActivityDTO>> getAllLivestockActivities(LivestockActivityCriteria criteria) {
        LOG.debug("REST request to get LivestockActivities by criteria: {}", criteria);

        List<LivestockActivityDTO> entityList = livestockActivityQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /livestock-activities/count} : count all the livestockActivities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLivestockActivities(LivestockActivityCriteria criteria) {
        LOG.debug("REST request to count LivestockActivities by criteria: {}", criteria);
        return ResponseEntity.ok().body(livestockActivityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /livestock-activities/:id} : get the "id" livestockActivity.
     *
     * @param id the id of the livestockActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livestockActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LivestockActivityDTO> getLivestockActivity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LivestockActivity : {}", id);
        Optional<LivestockActivityDTO> livestockActivityDTO = livestockActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livestockActivityDTO);
    }

    /**
     * {@code DELETE  /livestock-activities/:id} : delete the "id" livestockActivity.
     *
     * @param id the id of the livestockActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivestockActivity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LivestockActivity : {}", id);
        livestockActivityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
