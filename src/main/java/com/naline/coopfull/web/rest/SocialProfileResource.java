package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.SocialProfileRepository;
import com.naline.coopfull.service.SocialProfileQueryService;
import com.naline.coopfull.service.SocialProfileService;
import com.naline.coopfull.service.criteria.SocialProfileCriteria;
import com.naline.coopfull.service.dto.SocialProfileDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.SocialProfile}.
 */
@RestController
@RequestMapping("/api/social-profiles")
public class SocialProfileResource {

    private static final Logger LOG = LoggerFactory.getLogger(SocialProfileResource.class);

    private static final String ENTITY_NAME = "socialProfile";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final SocialProfileService socialProfileService;

    private final SocialProfileRepository socialProfileRepository;

    private final SocialProfileQueryService socialProfileQueryService;

    public SocialProfileResource(
        SocialProfileService socialProfileService,
        SocialProfileRepository socialProfileRepository,
        SocialProfileQueryService socialProfileQueryService
    ) {
        this.socialProfileService = socialProfileService;
        this.socialProfileRepository = socialProfileRepository;
        this.socialProfileQueryService = socialProfileQueryService;
    }

    /**
     * {@code POST  /social-profiles} : Create a new socialProfile.
     *
     * @param socialProfileDTO the socialProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialProfileDTO, or with status {@code 400 (Bad Request)} if the socialProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SocialProfileDTO> createSocialProfile(@RequestBody SocialProfileDTO socialProfileDTO) throws URISyntaxException {
        LOG.debug("REST request to save SocialProfile : {}", socialProfileDTO);
        if (socialProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new socialProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        socialProfileDTO = socialProfileService.save(socialProfileDTO);
        return ResponseEntity.created(new URI("/api/social-profiles/" + socialProfileDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, socialProfileDTO.getId().toString()))
            .body(socialProfileDTO);
    }

    /**
     * {@code PUT  /social-profiles/:id} : Updates an existing socialProfile.
     *
     * @param id the id of the socialProfileDTO to save.
     * @param socialProfileDTO the socialProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialProfileDTO,
     * or with status {@code 400 (Bad Request)} if the socialProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SocialProfileDTO> updateSocialProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SocialProfileDTO socialProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SocialProfile : {}, {}", id, socialProfileDTO);
        if (socialProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        socialProfileDTO = socialProfileService.update(socialProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialProfileDTO.getId().toString()))
            .body(socialProfileDTO);
    }

    /**
     * {@code PATCH  /social-profiles/:id} : Partial updates given fields of an existing socialProfile, field will ignore if it is null
     *
     * @param id the id of the socialProfileDTO to save.
     * @param socialProfileDTO the socialProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialProfileDTO,
     * or with status {@code 400 (Bad Request)} if the socialProfileDTO is not valid,
     * or with status {@code 404 (Not Found)} if the socialProfileDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the socialProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SocialProfileDTO> partialUpdateSocialProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SocialProfileDTO socialProfileDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SocialProfile partially : {}, {}", id, socialProfileDTO);
        if (socialProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialProfileDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SocialProfileDTO> result = socialProfileService.partialUpdate(socialProfileDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialProfileDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /social-profiles} : get all the Social Profiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Social Profiles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SocialProfileDTO>> getAllSocialProfiles(SocialProfileCriteria criteria) {
        LOG.debug("REST request to get SocialProfiles by criteria: {}", criteria);

        List<SocialProfileDTO> entityList = socialProfileQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /social-profiles/count} : count all the socialProfiles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSocialProfiles(SocialProfileCriteria criteria) {
        LOG.debug("REST request to count SocialProfiles by criteria: {}", criteria);
        return ResponseEntity.ok().body(socialProfileQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /social-profiles/:id} : get the "id" socialProfile.
     *
     * @param id the id of the socialProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SocialProfileDTO> getSocialProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SocialProfile : {}", id);
        Optional<SocialProfileDTO> socialProfileDTO = socialProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(socialProfileDTO);
    }

    /**
     * {@code DELETE  /social-profiles/:id} : delete the "id" socialProfile.
     *
     * @param id the id of the socialProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSocialProfile(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SocialProfile : {}", id);
        socialProfileService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
