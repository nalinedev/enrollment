package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.IdentityDocumentRepository;
import com.naline.coopfull.service.IdentityDocumentQueryService;
import com.naline.coopfull.service.IdentityDocumentService;
import com.naline.coopfull.service.criteria.IdentityDocumentCriteria;
import com.naline.coopfull.service.dto.IdentityDocumentDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.IdentityDocument}.
 */
@RestController
@RequestMapping("/api/identity-documents")
public class IdentityDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityDocumentResource.class);

    private static final String ENTITY_NAME = "identityDocument";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final IdentityDocumentService identityDocumentService;

    private final IdentityDocumentRepository identityDocumentRepository;

    private final IdentityDocumentQueryService identityDocumentQueryService;

    public IdentityDocumentResource(
        IdentityDocumentService identityDocumentService,
        IdentityDocumentRepository identityDocumentRepository,
        IdentityDocumentQueryService identityDocumentQueryService
    ) {
        this.identityDocumentService = identityDocumentService;
        this.identityDocumentRepository = identityDocumentRepository;
        this.identityDocumentQueryService = identityDocumentQueryService;
    }

    /**
     * {@code POST  /identity-documents} : Create a new identityDocument.
     *
     * @param identityDocumentDTO the identityDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new identityDocumentDTO, or with status {@code 400 (Bad Request)} if the identityDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<IdentityDocumentDTO> createIdentityDocument(@Valid @RequestBody IdentityDocumentDTO identityDocumentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save IdentityDocument : {}", identityDocumentDTO);
        if (identityDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new identityDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        identityDocumentDTO = identityDocumentService.save(identityDocumentDTO);
        return ResponseEntity.created(new URI("/api/identity-documents/" + identityDocumentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, identityDocumentDTO.getId().toString()))
            .body(identityDocumentDTO);
    }

    /**
     * {@code PUT  /identity-documents/:id} : Updates an existing identityDocument.
     *
     * @param id the id of the identityDocumentDTO to save.
     * @param identityDocumentDTO the identityDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identityDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the identityDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the identityDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<IdentityDocumentDTO> updateIdentityDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IdentityDocumentDTO identityDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update IdentityDocument : {}, {}", id, identityDocumentDTO);
        if (identityDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identityDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!identityDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        identityDocumentDTO = identityDocumentService.update(identityDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, identityDocumentDTO.getId().toString()))
            .body(identityDocumentDTO);
    }

    /**
     * {@code PATCH  /identity-documents/:id} : Partial updates given fields of an existing identityDocument, field will ignore if it is null
     *
     * @param id the id of the identityDocumentDTO to save.
     * @param identityDocumentDTO the identityDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated identityDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the identityDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the identityDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the identityDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IdentityDocumentDTO> partialUpdateIdentityDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IdentityDocumentDTO identityDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update IdentityDocument partially : {}, {}", id, identityDocumentDTO);
        if (identityDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, identityDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!identityDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IdentityDocumentDTO> result = identityDocumentService.partialUpdate(identityDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, identityDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /identity-documents} : get all the Identity Documents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Identity Documents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<IdentityDocumentDTO>> getAllIdentityDocuments(IdentityDocumentCriteria criteria) {
        LOG.debug("REST request to get IdentityDocuments by criteria: {}", criteria);

        List<IdentityDocumentDTO> entityList = identityDocumentQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /identity-documents/count} : count all the identityDocuments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countIdentityDocuments(IdentityDocumentCriteria criteria) {
        LOG.debug("REST request to count IdentityDocuments by criteria: {}", criteria);
        return ResponseEntity.ok().body(identityDocumentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /identity-documents/:id} : get the "id" identityDocument.
     *
     * @param id the id of the identityDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the identityDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<IdentityDocumentDTO> getIdentityDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to get IdentityDocument : {}", id);
        Optional<IdentityDocumentDTO> identityDocumentDTO = identityDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(identityDocumentDTO);
    }

    /**
     * {@code DELETE  /identity-documents/:id} : delete the "id" identityDocument.
     *
     * @param id the id of the identityDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIdentityDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete IdentityDocument : {}", id);
        identityDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
