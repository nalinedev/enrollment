package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.CropVarietyRepository;
import com.naline.coopfull.service.CropVarietyQueryService;
import com.naline.coopfull.service.CropVarietyService;
import com.naline.coopfull.service.criteria.CropVarietyCriteria;
import com.naline.coopfull.service.dto.CropVarietyDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.CropVariety}.
 */
@RestController
@RequestMapping("/api/crop-varieties")
public class CropVarietyResource {

    private static final Logger LOG = LoggerFactory.getLogger(CropVarietyResource.class);

    private static final String ENTITY_NAME = "cropVariety";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final CropVarietyService cropVarietyService;

    private final CropVarietyRepository cropVarietyRepository;

    private final CropVarietyQueryService cropVarietyQueryService;

    public CropVarietyResource(
        CropVarietyService cropVarietyService,
        CropVarietyRepository cropVarietyRepository,
        CropVarietyQueryService cropVarietyQueryService
    ) {
        this.cropVarietyService = cropVarietyService;
        this.cropVarietyRepository = cropVarietyRepository;
        this.cropVarietyQueryService = cropVarietyQueryService;
    }

    /**
     * {@code POST  /crop-varieties} : Create a new cropVariety.
     *
     * @param cropVarietyDTO the cropVarietyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cropVarietyDTO, or with status {@code 400 (Bad Request)} if the cropVariety has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CropVarietyDTO> createCropVariety(@Valid @RequestBody CropVarietyDTO cropVarietyDTO) throws URISyntaxException {
        LOG.debug("REST request to save CropVariety : {}", cropVarietyDTO);
        if (cropVarietyDTO.getId() != null) {
            throw new BadRequestAlertException("A new cropVariety cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cropVarietyDTO = cropVarietyService.save(cropVarietyDTO);
        return ResponseEntity.created(new URI("/api/crop-varieties/" + cropVarietyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cropVarietyDTO.getId().toString()))
            .body(cropVarietyDTO);
    }

    /**
     * {@code PUT  /crop-varieties/:id} : Updates an existing cropVariety.
     *
     * @param id the id of the cropVarietyDTO to save.
     * @param cropVarietyDTO the cropVarietyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropVarietyDTO,
     * or with status {@code 400 (Bad Request)} if the cropVarietyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cropVarietyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CropVarietyDTO> updateCropVariety(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CropVarietyDTO cropVarietyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CropVariety : {}, {}", id, cropVarietyDTO);
        if (cropVarietyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropVarietyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropVarietyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cropVarietyDTO = cropVarietyService.update(cropVarietyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cropVarietyDTO.getId().toString()))
            .body(cropVarietyDTO);
    }

    /**
     * {@code PATCH  /crop-varieties/:id} : Partial updates given fields of an existing cropVariety, field will ignore if it is null
     *
     * @param id the id of the cropVarietyDTO to save.
     * @param cropVarietyDTO the cropVarietyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cropVarietyDTO,
     * or with status {@code 400 (Bad Request)} if the cropVarietyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cropVarietyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cropVarietyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CropVarietyDTO> partialUpdateCropVariety(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CropVarietyDTO cropVarietyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CropVariety partially : {}, {}", id, cropVarietyDTO);
        if (cropVarietyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cropVarietyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cropVarietyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CropVarietyDTO> result = cropVarietyService.partialUpdate(cropVarietyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cropVarietyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crop-varieties} : get all the Crop Varieties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Crop Varieties in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CropVarietyDTO>> getAllCropVarieties(CropVarietyCriteria criteria) {
        LOG.debug("REST request to get CropVarieties by criteria: {}", criteria);

        List<CropVarietyDTO> entityList = cropVarietyQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /crop-varieties/count} : count all the cropVarieties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCropVarieties(CropVarietyCriteria criteria) {
        LOG.debug("REST request to count CropVarieties by criteria: {}", criteria);
        return ResponseEntity.ok().body(cropVarietyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crop-varieties/:id} : get the "id" cropVariety.
     *
     * @param id the id of the cropVarietyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cropVarietyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CropVarietyDTO> getCropVariety(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CropVariety : {}", id);
        Optional<CropVarietyDTO> cropVarietyDTO = cropVarietyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cropVarietyDTO);
    }

    /**
     * {@code DELETE  /crop-varieties/:id} : delete the "id" cropVariety.
     *
     * @param id the id of the cropVarietyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCropVariety(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CropVariety : {}", id);
        cropVarietyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
