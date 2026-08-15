package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.NumberSequenceRepository;
import com.naline.coopfull.service.NumberSequenceQueryService;
import com.naline.coopfull.service.NumberSequenceService;
import com.naline.coopfull.service.criteria.NumberSequenceCriteria;
import com.naline.coopfull.service.dto.NumberSequenceDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.NumberSequence}.
 */
@RestController
@RequestMapping("/api/number-sequences")
public class NumberSequenceResource {

    private static final Logger LOG = LoggerFactory.getLogger(NumberSequenceResource.class);

    private static final String ENTITY_NAME = "numberSequence";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final NumberSequenceService numberSequenceService;

    private final NumberSequenceRepository numberSequenceRepository;

    private final NumberSequenceQueryService numberSequenceQueryService;

    public NumberSequenceResource(
        NumberSequenceService numberSequenceService,
        NumberSequenceRepository numberSequenceRepository,
        NumberSequenceQueryService numberSequenceQueryService
    ) {
        this.numberSequenceService = numberSequenceService;
        this.numberSequenceRepository = numberSequenceRepository;
        this.numberSequenceQueryService = numberSequenceQueryService;
    }

    /**
     * {@code POST  /number-sequences} : Create a new numberSequence.
     *
     * @param numberSequenceDTO the numberSequenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new numberSequenceDTO, or with status {@code 400 (Bad Request)} if the numberSequence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NumberSequenceDTO> createNumberSequence(@Valid @RequestBody NumberSequenceDTO numberSequenceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NumberSequence : {}", numberSequenceDTO);
        if (numberSequenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new numberSequence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        numberSequenceDTO = numberSequenceService.save(numberSequenceDTO);
        return ResponseEntity.created(new URI("/api/number-sequences/" + numberSequenceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, numberSequenceDTO.getId().toString()))
            .body(numberSequenceDTO);
    }

    /**
     * {@code PUT  /number-sequences/:id} : Updates an existing numberSequence.
     *
     * @param id the id of the numberSequenceDTO to save.
     * @param numberSequenceDTO the numberSequenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated numberSequenceDTO,
     * or with status {@code 400 (Bad Request)} if the numberSequenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the numberSequenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NumberSequenceDTO> updateNumberSequence(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NumberSequenceDTO numberSequenceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NumberSequence : {}, {}", id, numberSequenceDTO);
        if (numberSequenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, numberSequenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!numberSequenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        numberSequenceDTO = numberSequenceService.update(numberSequenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, numberSequenceDTO.getId().toString()))
            .body(numberSequenceDTO);
    }

    /**
     * {@code PATCH  /number-sequences/:id} : Partial updates given fields of an existing numberSequence, field will ignore if it is null
     *
     * @param id the id of the numberSequenceDTO to save.
     * @param numberSequenceDTO the numberSequenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated numberSequenceDTO,
     * or with status {@code 400 (Bad Request)} if the numberSequenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the numberSequenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the numberSequenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NumberSequenceDTO> partialUpdateNumberSequence(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NumberSequenceDTO numberSequenceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NumberSequence partially : {}, {}", id, numberSequenceDTO);
        if (numberSequenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, numberSequenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!numberSequenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NumberSequenceDTO> result = numberSequenceService.partialUpdate(numberSequenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, numberSequenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /number-sequences} : get all the Number Sequences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Number Sequences in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NumberSequenceDTO>> getAllNumberSequences(NumberSequenceCriteria criteria) {
        LOG.debug("REST request to get NumberSequences by criteria: {}", criteria);

        List<NumberSequenceDTO> entityList = numberSequenceQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /number-sequences/count} : count all the numberSequences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNumberSequences(NumberSequenceCriteria criteria) {
        LOG.debug("REST request to count NumberSequences by criteria: {}", criteria);
        return ResponseEntity.ok().body(numberSequenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /number-sequences/:id} : get the "id" numberSequence.
     *
     * @param id the id of the numberSequenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the numberSequenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NumberSequenceDTO> getNumberSequence(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NumberSequence : {}", id);
        Optional<NumberSequenceDTO> numberSequenceDTO = numberSequenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(numberSequenceDTO);
    }

    /**
     * {@code DELETE  /number-sequences/:id} : delete the "id" numberSequence.
     *
     * @param id the id of the numberSequenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNumberSequence(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NumberSequence : {}", id);
        numberSequenceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
