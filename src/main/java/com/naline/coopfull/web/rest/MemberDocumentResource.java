package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.MemberDocumentRepository;
import com.naline.coopfull.service.MemberDocumentQueryService;
import com.naline.coopfull.service.MemberDocumentService;
import com.naline.coopfull.service.criteria.MemberDocumentCriteria;
import com.naline.coopfull.service.dto.MemberDocumentDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.MemberDocument}.
 */
@RestController
@RequestMapping("/api/member-documents")
public class MemberDocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(MemberDocumentResource.class);

    private static final String ENTITY_NAME = "memberDocument";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final MemberDocumentService memberDocumentService;

    private final MemberDocumentRepository memberDocumentRepository;

    private final MemberDocumentQueryService memberDocumentQueryService;

    public MemberDocumentResource(
        MemberDocumentService memberDocumentService,
        MemberDocumentRepository memberDocumentRepository,
        MemberDocumentQueryService memberDocumentQueryService
    ) {
        this.memberDocumentService = memberDocumentService;
        this.memberDocumentRepository = memberDocumentRepository;
        this.memberDocumentQueryService = memberDocumentQueryService;
    }

    /**
     * {@code POST  /member-documents} : Create a new memberDocument.
     *
     * @param memberDocumentDTO the memberDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberDocumentDTO, or with status {@code 400 (Bad Request)} if the memberDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MemberDocumentDTO> createMemberDocument(@Valid @RequestBody MemberDocumentDTO memberDocumentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MemberDocument : {}", memberDocumentDTO);
        if (memberDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new memberDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        memberDocumentDTO = memberDocumentService.save(memberDocumentDTO);
        return ResponseEntity.created(new URI("/api/member-documents/" + memberDocumentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, memberDocumentDTO.getId().toString()))
            .body(memberDocumentDTO);
    }

    /**
     * {@code PUT  /member-documents/:id} : Updates an existing memberDocument.
     *
     * @param id the id of the memberDocumentDTO to save.
     * @param memberDocumentDTO the memberDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the memberDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MemberDocumentDTO> updateMemberDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MemberDocumentDTO memberDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MemberDocument : {}, {}", id, memberDocumentDTO);
        if (memberDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memberDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        memberDocumentDTO = memberDocumentService.update(memberDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberDocumentDTO.getId().toString()))
            .body(memberDocumentDTO);
    }

    /**
     * {@code PATCH  /member-documents/:id} : Partial updates given fields of an existing memberDocument, field will ignore if it is null
     *
     * @param id the id of the memberDocumentDTO to save.
     * @param memberDocumentDTO the memberDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the memberDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the memberDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the memberDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MemberDocumentDTO> partialUpdateMemberDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MemberDocumentDTO memberDocumentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MemberDocument partially : {}, {}", id, memberDocumentDTO);
        if (memberDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memberDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MemberDocumentDTO> result = memberDocumentService.partialUpdate(memberDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /member-documents} : get all the Member Documents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Member Documents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MemberDocumentDTO>> getAllMemberDocuments(MemberDocumentCriteria criteria) {
        LOG.debug("REST request to get MemberDocuments by criteria: {}", criteria);

        List<MemberDocumentDTO> entityList = memberDocumentQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /member-documents/count} : count all the memberDocuments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMemberDocuments(MemberDocumentCriteria criteria) {
        LOG.debug("REST request to count MemberDocuments by criteria: {}", criteria);
        return ResponseEntity.ok().body(memberDocumentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /member-documents/:id} : get the "id" memberDocument.
     *
     * @param id the id of the memberDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberDocumentDTO> getMemberDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MemberDocument : {}", id);
        Optional<MemberDocumentDTO> memberDocumentDTO = memberDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memberDocumentDTO);
    }

    /**
     * {@code DELETE  /member-documents/:id} : delete the "id" memberDocument.
     *
     * @param id the id of the memberDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemberDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MemberDocument : {}", id);
        memberDocumentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
