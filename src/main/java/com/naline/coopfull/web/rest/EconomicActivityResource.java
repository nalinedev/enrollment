package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.EconomicActivityRepository;
import com.naline.coopfull.service.EconomicActivityQueryService;
import com.naline.coopfull.service.EconomicActivityService;
import com.naline.coopfull.service.criteria.EconomicActivityCriteria;
import com.naline.coopfull.service.dto.EconomicActivityDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.EconomicActivity}.
 */
@RestController
@RequestMapping("/api/economic-activities")
public class EconomicActivityResource {

    private static final Logger LOG = LoggerFactory.getLogger(EconomicActivityResource.class);

    private static final String ENTITY_NAME = "economicActivity";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final EconomicActivityService economicActivityService;

    private final EconomicActivityRepository economicActivityRepository;

    private final EconomicActivityQueryService economicActivityQueryService;

    public EconomicActivityResource(
        EconomicActivityService economicActivityService,
        EconomicActivityRepository economicActivityRepository,
        EconomicActivityQueryService economicActivityQueryService
    ) {
        this.economicActivityService = economicActivityService;
        this.economicActivityRepository = economicActivityRepository;
        this.economicActivityQueryService = economicActivityQueryService;
    }

    /**
     * {@code POST  /economic-activities} : Create a new economicActivity.
     *
     * @param economicActivityDTO the economicActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new economicActivityDTO, or with status {@code 400 (Bad Request)} if the economicActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EconomicActivityDTO> createEconomicActivity(@Valid @RequestBody EconomicActivityDTO economicActivityDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EconomicActivity : {}", economicActivityDTO);
        if (economicActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new economicActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        economicActivityDTO = economicActivityService.save(economicActivityDTO);
        return ResponseEntity.created(new URI("/api/economic-activities/" + economicActivityDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, economicActivityDTO.getId().toString()))
            .body(economicActivityDTO);
    }

    /**
     * {@code PUT  /economic-activities/:id} : Updates an existing economicActivity.
     *
     * @param id the id of the economicActivityDTO to save.
     * @param economicActivityDTO the economicActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated economicActivityDTO,
     * or with status {@code 400 (Bad Request)} if the economicActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the economicActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EconomicActivityDTO> updateEconomicActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EconomicActivityDTO economicActivityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EconomicActivity : {}, {}", id, economicActivityDTO);
        if (economicActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, economicActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!economicActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        economicActivityDTO = economicActivityService.update(economicActivityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, economicActivityDTO.getId().toString()))
            .body(economicActivityDTO);
    }

    /**
     * {@code PATCH  /economic-activities/:id} : Partial updates given fields of an existing economicActivity, field will ignore if it is null
     *
     * @param id the id of the economicActivityDTO to save.
     * @param economicActivityDTO the economicActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated economicActivityDTO,
     * or with status {@code 400 (Bad Request)} if the economicActivityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the economicActivityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the economicActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EconomicActivityDTO> partialUpdateEconomicActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EconomicActivityDTO economicActivityDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EconomicActivity partially : {}, {}", id, economicActivityDTO);
        if (economicActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, economicActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!economicActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EconomicActivityDTO> result = economicActivityService.partialUpdate(economicActivityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, economicActivityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /economic-activities} : get all the Economic Activities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Economic Activities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EconomicActivityDTO>> getAllEconomicActivities(EconomicActivityCriteria criteria) {
        LOG.debug("REST request to get EconomicActivities by criteria: {}", criteria);

        List<EconomicActivityDTO> entityList = economicActivityQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /economic-activities/count} : count all the economicActivities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEconomicActivities(EconomicActivityCriteria criteria) {
        LOG.debug("REST request to count EconomicActivities by criteria: {}", criteria);
        return ResponseEntity.ok().body(economicActivityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /economic-activities/:id} : get the "id" economicActivity.
     *
     * @param id the id of the economicActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the economicActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EconomicActivityDTO> getEconomicActivity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EconomicActivity : {}", id);
        Optional<EconomicActivityDTO> economicActivityDTO = economicActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(economicActivityDTO);
    }

    /**
     * {@code DELETE  /economic-activities/:id} : delete the "id" economicActivity.
     *
     * @param id the id of the economicActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEconomicActivity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EconomicActivity : {}", id);
        economicActivityService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
