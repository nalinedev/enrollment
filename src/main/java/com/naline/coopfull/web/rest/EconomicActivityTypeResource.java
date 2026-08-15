package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.EconomicActivityTypeRepository;
import com.naline.coopfull.service.EconomicActivityTypeQueryService;
import com.naline.coopfull.service.EconomicActivityTypeService;
import com.naline.coopfull.service.criteria.EconomicActivityTypeCriteria;
import com.naline.coopfull.service.dto.EconomicActivityTypeDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.EconomicActivityType}.
 */
@RestController
@RequestMapping("/api/economic-activity-types")
public class EconomicActivityTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(EconomicActivityTypeResource.class);

    private static final String ENTITY_NAME = "economicActivityType";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final EconomicActivityTypeService economicActivityTypeService;

    private final EconomicActivityTypeRepository economicActivityTypeRepository;

    private final EconomicActivityTypeQueryService economicActivityTypeQueryService;

    public EconomicActivityTypeResource(
        EconomicActivityTypeService economicActivityTypeService,
        EconomicActivityTypeRepository economicActivityTypeRepository,
        EconomicActivityTypeQueryService economicActivityTypeQueryService
    ) {
        this.economicActivityTypeService = economicActivityTypeService;
        this.economicActivityTypeRepository = economicActivityTypeRepository;
        this.economicActivityTypeQueryService = economicActivityTypeQueryService;
    }

    /**
     * {@code POST  /economic-activity-types} : Create a new economicActivityType.
     *
     * @param economicActivityTypeDTO the economicActivityTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new economicActivityTypeDTO, or with status {@code 400 (Bad Request)} if the economicActivityType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EconomicActivityTypeDTO> createEconomicActivityType(
        @Valid @RequestBody EconomicActivityTypeDTO economicActivityTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save EconomicActivityType : {}", economicActivityTypeDTO);
        if (economicActivityTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new economicActivityType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        economicActivityTypeDTO = economicActivityTypeService.save(economicActivityTypeDTO);
        return ResponseEntity.created(new URI("/api/economic-activity-types/" + economicActivityTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, economicActivityTypeDTO.getId().toString()))
            .body(economicActivityTypeDTO);
    }

    /**
     * {@code PUT  /economic-activity-types/:id} : Updates an existing economicActivityType.
     *
     * @param id the id of the economicActivityTypeDTO to save.
     * @param economicActivityTypeDTO the economicActivityTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated economicActivityTypeDTO,
     * or with status {@code 400 (Bad Request)} if the economicActivityTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the economicActivityTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EconomicActivityTypeDTO> updateEconomicActivityType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EconomicActivityTypeDTO economicActivityTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EconomicActivityType : {}, {}", id, economicActivityTypeDTO);
        if (economicActivityTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, economicActivityTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!economicActivityTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        economicActivityTypeDTO = economicActivityTypeService.update(economicActivityTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, economicActivityTypeDTO.getId().toString()))
            .body(economicActivityTypeDTO);
    }

    /**
     * {@code PATCH  /economic-activity-types/:id} : Partial updates given fields of an existing economicActivityType, field will ignore if it is null
     *
     * @param id the id of the economicActivityTypeDTO to save.
     * @param economicActivityTypeDTO the economicActivityTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated economicActivityTypeDTO,
     * or with status {@code 400 (Bad Request)} if the economicActivityTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the economicActivityTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the economicActivityTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EconomicActivityTypeDTO> partialUpdateEconomicActivityType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EconomicActivityTypeDTO economicActivityTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EconomicActivityType partially : {}, {}", id, economicActivityTypeDTO);
        if (economicActivityTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, economicActivityTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!economicActivityTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EconomicActivityTypeDTO> result = economicActivityTypeService.partialUpdate(economicActivityTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, economicActivityTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /economic-activity-types} : get all the Economic Activity Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Economic Activity Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EconomicActivityTypeDTO>> getAllEconomicActivityTypes(EconomicActivityTypeCriteria criteria) {
        LOG.debug("REST request to get EconomicActivityTypes by criteria: {}", criteria);

        List<EconomicActivityTypeDTO> entityList = economicActivityTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /economic-activity-types/count} : count all the economicActivityTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEconomicActivityTypes(EconomicActivityTypeCriteria criteria) {
        LOG.debug("REST request to count EconomicActivityTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(economicActivityTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /economic-activity-types/:id} : get the "id" economicActivityType.
     *
     * @param id the id of the economicActivityTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the economicActivityTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EconomicActivityTypeDTO> getEconomicActivityType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EconomicActivityType : {}", id);
        Optional<EconomicActivityTypeDTO> economicActivityTypeDTO = economicActivityTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(economicActivityTypeDTO);
    }

    /**
     * {@code DELETE  /economic-activity-types/:id} : delete the "id" economicActivityType.
     *
     * @param id the id of the economicActivityTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEconomicActivityType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EconomicActivityType : {}", id);
        economicActivityTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
