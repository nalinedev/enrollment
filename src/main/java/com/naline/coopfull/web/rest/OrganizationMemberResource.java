package com.naline.coopfull.web.rest;

import com.naline.coopfull.repository.OrganizationMemberRepository;
import com.naline.coopfull.service.OrganizationMemberQueryService;
import com.naline.coopfull.service.OrganizationMemberService;
import com.naline.coopfull.service.criteria.OrganizationMemberCriteria;
import com.naline.coopfull.service.dto.OrganizationMemberDTO;
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
 * REST controller for managing {@link com.naline.coopfull.domain.OrganizationMember}.
 */
@RestController
@RequestMapping("/api/organization-members")
public class OrganizationMemberResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrganizationMemberResource.class);

    private static final String ENTITY_NAME = "organizationMember";

    @Value("${jhipster.clientApp.name:coopfull}")
    private String applicationName;

    private final OrganizationMemberService organizationMemberService;

    private final OrganizationMemberRepository organizationMemberRepository;

    private final OrganizationMemberQueryService organizationMemberQueryService;

    public OrganizationMemberResource(
        OrganizationMemberService organizationMemberService,
        OrganizationMemberRepository organizationMemberRepository,
        OrganizationMemberQueryService organizationMemberQueryService
    ) {
        this.organizationMemberService = organizationMemberService;
        this.organizationMemberRepository = organizationMemberRepository;
        this.organizationMemberQueryService = organizationMemberQueryService;
    }

    /**
     * {@code POST  /organization-members} : Create a new organizationMember.
     *
     * @param organizationMemberDTO the organizationMemberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationMemberDTO, or with status {@code 400 (Bad Request)} if the organizationMember has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrganizationMemberDTO> createOrganizationMember(@Valid @RequestBody OrganizationMemberDTO organizationMemberDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save OrganizationMember : {}", organizationMemberDTO);
        if (organizationMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new organizationMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        organizationMemberDTO = organizationMemberService.save(organizationMemberDTO);
        return ResponseEntity.created(new URI("/api/organization-members/" + organizationMemberDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, organizationMemberDTO.getId().toString()))
            .body(organizationMemberDTO);
    }

    /**
     * {@code PUT  /organization-members/:id} : Updates an existing organizationMember.
     *
     * @param id the id of the organizationMemberDTO to save.
     * @param organizationMemberDTO the organizationMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationMemberDTO,
     * or with status {@code 400 (Bad Request)} if the organizationMemberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrganizationMemberDTO> updateOrganizationMember(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrganizationMemberDTO organizationMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrganizationMember : {}, {}", id, organizationMemberDTO);
        if (organizationMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        organizationMemberDTO = organizationMemberService.update(organizationMemberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationMemberDTO.getId().toString()))
            .body(organizationMemberDTO);
    }

    /**
     * {@code PATCH  /organization-members/:id} : Partial updates given fields of an existing organizationMember, field will ignore if it is null
     *
     * @param id the id of the organizationMemberDTO to save.
     * @param organizationMemberDTO the organizationMemberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationMemberDTO,
     * or with status {@code 400 (Bad Request)} if the organizationMemberDTO is not valid,
     * or with status {@code 404 (Not Found)} if the organizationMemberDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the organizationMemberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrganizationMemberDTO> partialUpdateOrganizationMember(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrganizationMemberDTO organizationMemberDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrganizationMember partially : {}, {}", id, organizationMemberDTO);
        if (organizationMemberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationMemberDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationMemberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrganizationMemberDTO> result = organizationMemberService.partialUpdate(organizationMemberDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationMemberDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /organization-members} : get all the Organization Members.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Organization Members in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrganizationMemberDTO>> getAllOrganizationMembers(OrganizationMemberCriteria criteria) {
        LOG.debug("REST request to get OrganizationMembers by criteria: {}", criteria);

        List<OrganizationMemberDTO> entityList = organizationMemberQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /organization-members/count} : count all the organizationMembers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrganizationMembers(OrganizationMemberCriteria criteria) {
        LOG.debug("REST request to count OrganizationMembers by criteria: {}", criteria);
        return ResponseEntity.ok().body(organizationMemberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /organization-members/:id} : get the "id" organizationMember.
     *
     * @param id the id of the organizationMemberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationMemberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationMemberDTO> getOrganizationMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrganizationMember : {}", id);
        Optional<OrganizationMemberDTO> organizationMemberDTO = organizationMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationMemberDTO);
    }

    /**
     * {@code DELETE  /organization-members/:id} : delete the "id" organizationMember.
     *
     * @param id the id of the organizationMemberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganizationMember(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrganizationMember : {}", id);
        organizationMemberService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
