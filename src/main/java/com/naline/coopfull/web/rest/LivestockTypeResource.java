package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.LivestockTypeRepository;
import com.naline.coopfull.service.LivestockTypeQueryService;
import com.naline.coopfull.service.LivestockTypeService;
import com.naline.coopfull.service.criteria.LivestockTypeCriteria;
import com.naline.coopfull.service.dto.LivestockTypeDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.LivestockType}.
 */
@RestController
@RequestMapping("/api/livestock-types")
public class LivestockTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(LivestockTypeResource.class);

    private static final String ENTITY_NAME = "livestockType";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final LivestockTypeService livestockTypeService;

    private final LivestockTypeRepository livestockTypeRepository;

    private final LivestockTypeQueryService livestockTypeQueryService;

    public LivestockTypeResource(
        LivestockTypeService livestockTypeService,
        LivestockTypeRepository livestockTypeRepository,
        LivestockTypeQueryService livestockTypeQueryService
    ) {
        this.livestockTypeService = livestockTypeService;
        this.livestockTypeRepository = livestockTypeRepository;
        this.livestockTypeQueryService = livestockTypeQueryService;
    }

    /**
     * {@code POST  /livestock-types} : Create a new livestockType.
     *
     * @param livestockTypeDTO the livestockTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livestockTypeDTO, or with status {@code 400 (Bad Request)} if the livestockType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LivestockTypeDTO> createLivestockType(@Valid @RequestBody LivestockTypeDTO livestockTypeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LivestockType : {}", livestockTypeDTO);
        if (livestockTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new livestockType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        livestockTypeDTO = livestockTypeService.save(livestockTypeDTO);
        return ResponseEntity.created(new URI("/api/livestock-types/" + livestockTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, livestockTypeDTO.getId().toString()))
            .body(livestockTypeDTO);
    }

    /**
     * {@code PUT  /livestock-types/:id} : Updates an existing livestockType.
     *
     * @param id the id of the livestockTypeDTO to save.
     * @param livestockTypeDTO the livestockTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livestockTypeDTO,
     * or with status {@code 400 (Bad Request)} if the livestockTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livestockTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LivestockTypeDTO> updateLivestockType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LivestockTypeDTO livestockTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LivestockType : {}, {}", id, livestockTypeDTO);
        if (livestockTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livestockTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livestockTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        livestockTypeDTO = livestockTypeService.update(livestockTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livestockTypeDTO.getId().toString()))
            .body(livestockTypeDTO);
    }

    /**
     * {@code PATCH  /livestock-types/:id} : Partial updates given fields of an existing livestockType, field will ignore if it is null
     *
     * @param id the id of the livestockTypeDTO to save.
     * @param livestockTypeDTO the livestockTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livestockTypeDTO,
     * or with status {@code 400 (Bad Request)} if the livestockTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the livestockTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the livestockTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LivestockTypeDTO> partialUpdateLivestockType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LivestockTypeDTO livestockTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LivestockType partially : {}, {}", id, livestockTypeDTO);
        if (livestockTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livestockTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!livestockTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LivestockTypeDTO> result = livestockTypeService.partialUpdate(livestockTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livestockTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /livestock-types} : get all the Livestock Types.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Livestock Types in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LivestockTypeDTO>> getAllLivestockTypes(LivestockTypeCriteria criteria) {
        LOG.debug("REST request to get LivestockTypes by criteria: {}", criteria);

        List<LivestockTypeDTO> entityList = livestockTypeQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /livestock-types/count} : count all the livestockTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLivestockTypes(LivestockTypeCriteria criteria) {
        LOG.debug("REST request to count LivestockTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(livestockTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /livestock-types/:id} : get the "id" livestockType.
     *
     * @param id the id of the livestockTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livestockTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LivestockTypeDTO> getLivestockType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LivestockType : {}", id);
        Optional<LivestockTypeDTO> livestockTypeDTO = livestockTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livestockTypeDTO);
    }

    /**
     * {@code DELETE  /livestock-types/:id} : delete the "id" livestockType.
     *
     * @param id the id of the livestockTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivestockType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LivestockType : {}", id);
        livestockTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
