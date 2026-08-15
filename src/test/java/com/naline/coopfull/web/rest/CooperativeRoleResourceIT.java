package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.CooperativeRoleAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.CooperativeRole;
import com.naline.coopfull.domain.Permission;
import com.naline.coopfull.domain.enumeration.CooperativeRoleStatus;
import com.naline.coopfull.repository.CooperativeRoleRepository;
import com.naline.coopfull.service.CooperativeRoleService;
import com.naline.coopfull.service.dto.CooperativeRoleDTO;
import com.naline.coopfull.service.mapper.CooperativeRoleMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CooperativeRoleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CooperativeRoleResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final CooperativeRoleStatus DEFAULT_STATUS = CooperativeRoleStatus.ACTIVE;
    private static final CooperativeRoleStatus UPDATED_STATUS = CooperativeRoleStatus.INACTIVE;

    private static final String ENTITY_API_URL = "/api/cooperative-roles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CooperativeRoleRepository cooperativeRoleRepository;

    @Mock
    private CooperativeRoleRepository cooperativeRoleRepositoryMock;

    @Autowired
    private CooperativeRoleMapper cooperativeRoleMapper;

    @Mock
    private CooperativeRoleService cooperativeRoleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCooperativeRoleMockMvc;

    private CooperativeRole cooperativeRole;

    private CooperativeRole insertedCooperativeRole;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CooperativeRole createEntity() {
        return new CooperativeRole().code(DEFAULT_CODE).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CooperativeRole createUpdatedEntity() {
        return new CooperativeRole().code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        cooperativeRole = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCooperativeRole != null) {
            cooperativeRoleRepository.delete(insertedCooperativeRole);
            insertedCooperativeRole = null;
        }
    }

    @Test
    @Transactional
    void createCooperativeRole() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CooperativeRole
        CooperativeRoleDTO cooperativeRoleDTO = cooperativeRoleMapper.toDto(cooperativeRole);
        var returnedCooperativeRoleDTO = om.readValue(
            restCooperativeRoleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeRoleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CooperativeRoleDTO.class
        );

        // Validate the CooperativeRole in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCooperativeRole = cooperativeRoleMapper.toEntity(returnedCooperativeRoleDTO);
        assertCooperativeRoleUpdatableFieldsEquals(returnedCooperativeRole, getPersistedCooperativeRole(returnedCooperativeRole));

        insertedCooperativeRole = returnedCooperativeRole;
    }

    @Test
    @Transactional
    void createCooperativeRoleWithExistingId() throws Exception {
        // Create the CooperativeRole with an existing ID
        cooperativeRole.setId(1L);
        CooperativeRoleDTO cooperativeRoleDTO = cooperativeRoleMapper.toDto(cooperativeRole);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCooperativeRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeRoleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CooperativeRole in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cooperativeRole.setCode(null);

        // Create the CooperativeRole, which fails.
        CooperativeRoleDTO cooperativeRoleDTO = cooperativeRoleMapper.toDto(cooperativeRole);

        restCooperativeRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeRoleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cooperativeRole.setName(null);

        // Create the CooperativeRole, which fails.
        CooperativeRoleDTO cooperativeRoleDTO = cooperativeRoleMapper.toDto(cooperativeRole);

        restCooperativeRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeRoleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cooperativeRole.setStatus(null);

        // Create the CooperativeRole, which fails.
        CooperativeRoleDTO cooperativeRoleDTO = cooperativeRoleMapper.toDto(cooperativeRole);

        restCooperativeRoleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeRoleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCooperativeRoles() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList
        restCooperativeRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cooperativeRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCooperativeRolesWithEagerRelationshipsIsEnabled() throws Exception {
        when(cooperativeRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCooperativeRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cooperativeRoleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCooperativeRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cooperativeRoleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCooperativeRoleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cooperativeRoleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCooperativeRole() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get the cooperativeRole
        restCooperativeRoleMockMvc
            .perform(get(ENTITY_API_URL_ID, cooperativeRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cooperativeRole.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getCooperativeRolesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        Long id = cooperativeRole.getId();

        defaultCooperativeRoleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCooperativeRoleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCooperativeRoleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where code equals to
        defaultCooperativeRoleFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where code in
        defaultCooperativeRoleFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where code is not null
        defaultCooperativeRoleFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where code contains
        defaultCooperativeRoleFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where code does not contain
        defaultCooperativeRoleFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where name equals to
        defaultCooperativeRoleFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where name in
        defaultCooperativeRoleFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where name is not null
        defaultCooperativeRoleFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where name contains
        defaultCooperativeRoleFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where name does not contain
        defaultCooperativeRoleFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where status equals to
        defaultCooperativeRoleFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where status in
        defaultCooperativeRoleFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        // Get all the cooperativeRoleList where status is not null
        defaultCooperativeRoleFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeRolesByPermissionsIsEqualToSomething() throws Exception {
        Permission permissions;
        if (TestUtil.findAll(em, Permission.class).isEmpty()) {
            cooperativeRoleRepository.saveAndFlush(cooperativeRole);
            permissions = PermissionResourceIT.createEntity();
        } else {
            permissions = TestUtil.findAll(em, Permission.class).get(0);
        }
        em.persist(permissions);
        em.flush();
        cooperativeRole.addPermissions(permissions);
        cooperativeRoleRepository.saveAndFlush(cooperativeRole);
        Long permissionsId = permissions.getId();
        // Get all the cooperativeRoleList where permissions equals to permissionsId
        defaultCooperativeRoleShouldBeFound("permissionsId.equals=" + permissionsId);

        // Get all the cooperativeRoleList where permissions equals to (permissionsId + 1)
        defaultCooperativeRoleShouldNotBeFound("permissionsId.equals=" + (permissionsId + 1));
    }

    private void defaultCooperativeRoleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCooperativeRoleShouldBeFound(shouldBeFound);
        defaultCooperativeRoleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCooperativeRoleShouldBeFound(String filter) throws Exception {
        restCooperativeRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cooperativeRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restCooperativeRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCooperativeRoleShouldNotBeFound(String filter) throws Exception {
        restCooperativeRoleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCooperativeRoleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCooperativeRole() throws Exception {
        // Get the cooperativeRole
        restCooperativeRoleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCooperativeRole() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cooperativeRole
        CooperativeRole updatedCooperativeRole = cooperativeRoleRepository.findById(cooperativeRole.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCooperativeRole are not directly saved in db
        em.detach(updatedCooperativeRole);
        updatedCooperativeRole.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);
        CooperativeRoleDTO cooperativeRoleDTO = cooperativeRoleMapper.toDto(updatedCooperativeRole);

        restCooperativeRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cooperativeRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cooperativeRoleDTO))
            )
            .andExpect(status().isOk());

        // Validate the CooperativeRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCooperativeRoleToMatchAllProperties(updatedCooperativeRole);
    }

    @Test
    @Transactional
    void putNonExistingCooperativeRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeRole.setId(longCount.incrementAndGet());

        // Create the CooperativeRole
        CooperativeRoleDTO cooperativeRoleDTO = cooperativeRoleMapper.toDto(cooperativeRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCooperativeRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cooperativeRoleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cooperativeRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CooperativeRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCooperativeRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeRole.setId(longCount.incrementAndGet());

        // Create the CooperativeRole
        CooperativeRoleDTO cooperativeRoleDTO = cooperativeRoleMapper.toDto(cooperativeRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeRoleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cooperativeRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CooperativeRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCooperativeRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeRole.setId(longCount.incrementAndGet());

        // Create the CooperativeRole
        CooperativeRoleDTO cooperativeRoleDTO = cooperativeRoleMapper.toDto(cooperativeRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeRoleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeRoleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CooperativeRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCooperativeRoleWithPatch() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cooperativeRole using partial update
        CooperativeRole partialUpdatedCooperativeRole = new CooperativeRole();
        partialUpdatedCooperativeRole.setId(cooperativeRole.getId());

        partialUpdatedCooperativeRole.name(UPDATED_NAME);

        restCooperativeRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCooperativeRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCooperativeRole))
            )
            .andExpect(status().isOk());

        // Validate the CooperativeRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCooperativeRoleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCooperativeRole, cooperativeRole),
            getPersistedCooperativeRole(cooperativeRole)
        );
    }

    @Test
    @Transactional
    void fullUpdateCooperativeRoleWithPatch() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cooperativeRole using partial update
        CooperativeRole partialUpdatedCooperativeRole = new CooperativeRole();
        partialUpdatedCooperativeRole.setId(cooperativeRole.getId());

        partialUpdatedCooperativeRole.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);

        restCooperativeRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCooperativeRole.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCooperativeRole))
            )
            .andExpect(status().isOk());

        // Validate the CooperativeRole in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCooperativeRoleUpdatableFieldsEquals(
            partialUpdatedCooperativeRole,
            getPersistedCooperativeRole(partialUpdatedCooperativeRole)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCooperativeRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeRole.setId(longCount.incrementAndGet());

        // Create the CooperativeRole
        CooperativeRoleDTO cooperativeRoleDTO = cooperativeRoleMapper.toDto(cooperativeRole);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCooperativeRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cooperativeRoleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cooperativeRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CooperativeRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCooperativeRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeRole.setId(longCount.incrementAndGet());

        // Create the CooperativeRole
        CooperativeRoleDTO cooperativeRoleDTO = cooperativeRoleMapper.toDto(cooperativeRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeRoleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cooperativeRoleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CooperativeRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCooperativeRole() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeRole.setId(longCount.incrementAndGet());

        // Create the CooperativeRole
        CooperativeRoleDTO cooperativeRoleDTO = cooperativeRoleMapper.toDto(cooperativeRole);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeRoleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cooperativeRoleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CooperativeRole in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCooperativeRole() throws Exception {
        // Initialize the database
        insertedCooperativeRole = cooperativeRoleRepository.saveAndFlush(cooperativeRole);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cooperativeRole
        restCooperativeRoleMockMvc
            .perform(delete(ENTITY_API_URL_ID, cooperativeRole.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cooperativeRoleRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CooperativeRole getPersistedCooperativeRole(CooperativeRole cooperativeRole) {
        return cooperativeRoleRepository.findById(cooperativeRole.getId()).orElseThrow();
    }

    protected void assertPersistedCooperativeRoleToMatchAllProperties(CooperativeRole expectedCooperativeRole) {
        assertCooperativeRoleAllPropertiesEquals(expectedCooperativeRole, getPersistedCooperativeRole(expectedCooperativeRole));
    }

    protected void assertPersistedCooperativeRoleToMatchUpdatableProperties(CooperativeRole expectedCooperativeRole) {
        assertCooperativeRoleAllUpdatablePropertiesEquals(expectedCooperativeRole, getPersistedCooperativeRole(expectedCooperativeRole));
    }
}
