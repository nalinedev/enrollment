package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.ProfessionalProfileRepository;
import com.naline.coopfull.service.ProfessionalProfileQueryService;
import com.naline.coopfull.service.ProfessionalProfileService;
import com.naline.coopfull.service.criteria.ProfessionalProfileCriteria;
import com.naline.coopfull.service.dto.ProfessionalProfileDTO;
import com.naline.coopfull.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.naline.coopfull.domain.ProfessionalProfile}.
 */
@RestController
@RequestMapping("/api/professional-profiles")
public class ProfessionalProfileResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfessionalProfileResource.class);

    private static final String ENTITY_NAME = "professionalProfile";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final ProfessionalProfileService professionalProfileService;

    private final ProfessionalProfileRepository professionalProfileRepository;

    private final ProfessionalProfileQueryService professionalProfileQueryService;

    public ProfessionalProfileResource(
        ProfessionalProfileService professionalProfileService,
        ProfessionalProfileRepository professionalProfileRepository,
        ProfessionalProfileQueryService professionalProfileQueryService
    ) {
        this.professionalProfileService = professionalProfileService;
        this.professionalProfileRepository = professionalProfileRepository;
        this.professionalProfileQueryService = professionalProfileQueryService;
    }

    /**
     * {@code POST  /professional-profiles} : Create a new professionalProfile.
     *
     * @param professionalProfileDTO the professionalProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalProfileDTO, or with status {@code 400 (Bad Request)} if the professionalProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfessionalProfileDTO> createProfessionalProfile(@RequestBody ProfessionalProfileDTO professionalProfileDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProfessionalProfile : {}", professionalProfileDTO);
        if (professionalProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        professionalProfileDTO = professionalProfileService.save(professionalProfileDTO);
        return ResponseEntity.created(new URI("/api/professional-profiles/" + professionalProfileDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, professionalProfileDTO.getId().toString()))
            .body(professionalProfileDTO);
    }

    /**
     * {@code PUT  /professional-profiles/:id} : Updates an existing professionalProfile.
     *
     * @param id the id of the professionalProfileDTO to save.
     * @param professionalProfileDTO the professionalProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalProfileDTO,
     * or with status {@code 400 (Bad Request)} if the professionalProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalProfileDTO> updateProfessionalProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProfessionalProfileDTO professionalProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProfessionalProfile : {}, {}", id, professionalProfileDTO);
        if (professionalProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        professionalProfileDTO = professionalProfileService.update(professionalProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professionalProfileDTO.getId().toString()))
            .body(professionalProfileDTO);
    }

    /**
     * {@code PATCH  /professional-profiles/:id} : Partial updates given fields of an existing professionalProfile, field will ignore if it is null
     *
     * @param id the id of the professionalProfileDTO to save.
     * @param professionalProfileDTO the professionalProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalProfileDTO,
     * or with status {@code 400 (Bad Request)} if the professionalProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalProfileDTO> partialUpdateProfessionalProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProfessionalProfileDTO professionalProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProfessionalProfile partially : {}, {}", id, professionalProfileDTO);
        if (professionalProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalProfileDTO> result = professionalProfileService.partialUpdate(professionalProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professionalProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-profiles} : get all the Professional Profiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Professional Profiles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfessionalProfileDTO>> getAllProfessionalProfiles(ProfessionalProfileCriteria criteria) {
        LOG.debug("REST request to get ProfessionalProfiles by criteria: {}", criteria);

        List<ProfessionalProfileDTO> entityList = professionalProfileQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /professional-profiles/count} : count all the professionalProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProfessionalProfiles(ProfessionalProfileCriteria criteria) {
        LOG.debug("REST request to count ProfessionalProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(professionalProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /professional-profiles/:id} : get the "id" professionalProfile.
     *
     * @param id the id of the professionalProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalProfileDTO> getProfessionalProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProfessionalProfile : {}", id);
        Optional<ProfessionalProfileDTO> professionalProfileDTO = professionalProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalProfileDTO);
    }

    /**
     * {@code DELETE  /professional-profiles/:id} : delete the "id" professionalProfile.
     *
     * @param id the id of the professionalProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessionalProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProfessionalProfile : {}", id);
        professionalProfileService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
