package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.AquaticSpeciesRepository;
import com.naline.coopfull.service.AquaticSpeciesQueryService;
import com.naline.coopfull.service.AquaticSpeciesService;
import com.naline.coopfull.service.criteria.AquaticSpeciesCriteria;
import com.naline.coopfull.service.dto.AquaticSpeciesDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.AquaticSpecies}.
 */
@RestController
@RequestMapping("/api/aquatic-species")
public class AquaticSpeciesResource {

    private static final Logger LOG = LoggerFactory.getLogger(AquaticSpeciesResource.class);

    private static final String ENTITY_NAME = "aquaticSpecies";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final AquaticSpeciesService aquaticSpeciesService;

    private final AquaticSpeciesRepository aquaticSpeciesRepository;

    private final AquaticSpeciesQueryService aquaticSpeciesQueryService;

    public AquaticSpeciesResource(
        AquaticSpeciesService aquaticSpeciesService,
        AquaticSpeciesRepository aquaticSpeciesRepository,
        AquaticSpeciesQueryService aquaticSpeciesQueryService
    ) {
        this.aquaticSpeciesService = aquaticSpeciesService;
        this.aquaticSpeciesRepository = aquaticSpeciesRepository;
        this.aquaticSpeciesQueryService = aquaticSpeciesQueryService;
    }

    /**
     * {@code POST  /aquatic-species} : Create a new aquaticSpecies.
     *
     * @param aquaticSpeciesDTO the aquaticSpeciesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aquaticSpeciesDTO, or with status {@code 400 (Bad Request)} if the aquaticSpecies has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AquaticSpeciesDTO> createAquaticSpecies(@Valid @RequestBody AquaticSpeciesDTO aquaticSpeciesDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AquaticSpecies : {}", aquaticSpeciesDTO);
        if (aquaticSpeciesDTO.getId() != null) {
            throw new BadRequestAlertException("A new aquaticSpecies cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aquaticSpeciesDTO = aquaticSpeciesService.save(aquaticSpeciesDTO);
        return ResponseEntity.created(new URI("/api/aquatic-species/" + aquaticSpeciesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aquaticSpeciesDTO.getId().toString()))
            .body(aquaticSpeciesDTO);
    }

    /**
     * {@code PUT  /aquatic-species/:id} : Updates an existing aquaticSpecies.
     *
     * @param id the id of the aquaticSpeciesDTO to save.
     * @param aquaticSpeciesDTO the aquaticSpeciesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aquaticSpeciesDTO,
     * or with status {@code 400 (Bad Request)} if the aquaticSpeciesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aquaticSpeciesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AquaticSpeciesDTO> updateAquaticSpecies(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AquaticSpeciesDTO aquaticSpeciesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AquaticSpecies : {}, {}", id, aquaticSpeciesDTO);
        if (aquaticSpeciesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aquaticSpeciesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aquaticSpeciesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aquaticSpeciesDTO = aquaticSpeciesService.update(aquaticSpeciesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aquaticSpeciesDTO.getId().toString()))
            .body(aquaticSpeciesDTO);
    }

    /**
     * {@code PATCH  /aquatic-species/:id} : Partial updates given fields of an existing aquaticSpecies, field will ignore if it is null
     *
     * @param id the id of the aquaticSpeciesDTO to save.
     * @param aquaticSpeciesDTO the aquaticSpeciesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aquaticSpeciesDTO,
     * or with status {@code 400 (Bad Request)} if the aquaticSpeciesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aquaticSpeciesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aquaticSpeciesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AquaticSpeciesDTO> partialUpdateAquaticSpecies(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AquaticSpeciesDTO aquaticSpeciesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AquaticSpecies partially : {}, {}", id, aquaticSpeciesDTO);
        if (aquaticSpeciesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aquaticSpeciesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aquaticSpeciesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AquaticSpeciesDTO> result = aquaticSpeciesService.partialUpdate(aquaticSpeciesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aquaticSpeciesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /aquatic-species} : get all the Aquatic Species.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Aquatic Species in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AquaticSpeciesDTO>> getAllAquaticSpecieses(AquaticSpeciesCriteria criteria) {
        LOG.debug("REST request to get AquaticSpecieses by criteria: {}", criteria);

        List<AquaticSpeciesDTO> entityList = aquaticSpeciesQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /aquatic-species/count} : count all the aquaticSpecieses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAquaticSpecieses(AquaticSpeciesCriteria criteria) {
        LOG.debug("REST request to count AquaticSpecieses by criteria: {}", criteria);
        return ResponseEntity.ok().body(aquaticSpeciesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /aquatic-species/:id} : get the "id" aquaticSpecies.
     *
     * @param id the id of the aquaticSpeciesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aquaticSpeciesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AquaticSpeciesDTO> getAquaticSpecies(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AquaticSpecies : {}", id);
        Optional<AquaticSpeciesDTO> aquaticSpeciesDTO = aquaticSpeciesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aquaticSpeciesDTO);
    }

    /**
     * {@code DELETE  /aquatic-species/:id} : delete the "id" aquaticSpecies.
     *
     * @param id the id of the aquaticSpeciesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAquaticSpecies(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AquaticSpecies : {}", id);
        aquaticSpeciesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
