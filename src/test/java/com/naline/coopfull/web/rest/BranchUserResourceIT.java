package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.BranchUserAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.AppUser;
import com.naline.coopfull.domain.BranchUser;
import com.naline.coopfull.domain.CooperativeBranch;
import com.naline.coopfull.domain.CooperativeRole;
import com.naline.coopfull.repository.BranchUserRepository;
import com.naline.coopfull.service.BranchUserService;
import com.naline.coopfull.service.dto.BranchUserDTO;
import com.naline.coopfull.service.mapper.BranchUserMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
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
 * Integration tests for the {@link BranchUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BranchUserResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/branch-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BranchUserRepository branchUserRepository;

    @Mock
    private BranchUserRepository branchUserRepositoryMock;

    @Autowired
    private BranchUserMapper branchUserMapper;

    @Mock
    private BranchUserService branchUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBranchUserMockMvc;

    private BranchUser branchUser;

    private BranchUser insertedBranchUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchUser createEntity() {
        return new BranchUser().startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE).active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchUser createUpdatedEntity() {
        return new BranchUser().startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        branchUser = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBranchUser != null) {
            branchUserRepository.delete(insertedBranchUser);
            insertedBranchUser = null;
        }
    }

    @Test
    @Transactional
    void createBranchUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BranchUser
        BranchUserDTO branchUserDTO = branchUserMapper.toDto(branchUser);
        var returnedBranchUserDTO = om.readValue(
            restBranchUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(branchUserDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BranchUserDTO.class
        );

        // Validate the BranchUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBranchUser = branchUserMapper.toEntity(returnedBranchUserDTO);
        assertBranchUserUpdatableFieldsEquals(returnedBranchUser, getPersistedBranchUser(returnedBranchUser));

        insertedBranchUser = returnedBranchUser;
    }

    @Test
    @Transactional
    void createBranchUserWithExistingId() throws Exception {
        // Create the BranchUser with an existing ID
        branchUser.setId(1L);
        BranchUserDTO branchUserDTO = branchUserMapper.toDto(branchUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(branchUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BranchUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        branchUser.setStartDate(null);

        // Create the BranchUser, which fails.
        BranchUserDTO branchUserDTO = branchUserMapper.toDto(branchUser);

        restBranchUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(branchUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        branchUser.setActive(null);

        // Create the BranchUser, which fails.
        BranchUserDTO branchUserDTO = branchUserMapper.toDto(branchUser);

        restBranchUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(branchUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBranchUsers() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList
        restBranchUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBranchUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(branchUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBranchUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(branchUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBranchUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(branchUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBranchUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(branchUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBranchUser() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get the branchUser
        restBranchUserMockMvc
            .perform(get(ENTITY_API_URL_ID, branchUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(branchUser.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getBranchUsersByIdFiltering() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        Long id = branchUser.getId();

        defaultBranchUserFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBranchUserFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBranchUserFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBranchUsersByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where startDate equals to
        defaultBranchUserFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where startDate in
        defaultBranchUserFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where startDate is not null
        defaultBranchUserFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchUsersByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where startDate is greater than or equal to
        defaultBranchUserFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllBranchUsersByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where startDate is less than or equal to
        defaultBranchUserFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where startDate is less than
        defaultBranchUserFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where startDate is greater than
        defaultBranchUserFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where endDate equals to
        defaultBranchUserFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where endDate in
        defaultBranchUserFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where endDate is not null
        defaultBranchUserFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchUsersByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where endDate is greater than or equal to
        defaultBranchUserFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where endDate is less than or equal to
        defaultBranchUserFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where endDate is less than
        defaultBranchUserFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where endDate is greater than
        defaultBranchUserFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where active equals to
        defaultBranchUserFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where active in
        defaultBranchUserFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllBranchUsersByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        // Get all the branchUserList where active is not null
        defaultBranchUserFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllBranchUsersByAppUserIsEqualToSomething() throws Exception {
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            branchUserRepository.saveAndFlush(branchUser);
            appUser = AppUserResourceIT.createEntity();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        em.persist(appUser);
        em.flush();
        branchUser.setAppUser(appUser);
        branchUserRepository.saveAndFlush(branchUser);
        Long appUserId = appUser.getId();
        // Get all the branchUserList where appUser equals to appUserId
        defaultBranchUserShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the branchUserList where appUser equals to (appUserId + 1)
        defaultBranchUserShouldNotBeFound("appUserId.equals=" + (appUserId + 1));
    }

    @Test
    @Transactional
    void getAllBranchUsersByBranchIsEqualToSomething() throws Exception {
        CooperativeBranch branch;
        if (TestUtil.findAll(em, CooperativeBranch.class).isEmpty()) {
            branchUserRepository.saveAndFlush(branchUser);
            branch = CooperativeBranchResourceIT.createEntity();
        } else {
            branch = TestUtil.findAll(em, CooperativeBranch.class).get(0);
        }
        em.persist(branch);
        em.flush();
        branchUser.setBranch(branch);
        branchUserRepository.saveAndFlush(branchUser);
        Long branchId = branch.getId();
        // Get all the branchUserList where branch equals to branchId
        defaultBranchUserShouldBeFound("branchId.equals=" + branchId);

        // Get all the branchUserList where branch equals to (branchId + 1)
        defaultBranchUserShouldNotBeFound("branchId.equals=" + (branchId + 1));
    }

    @Test
    @Transactional
    void getAllBranchUsersByRoleIsEqualToSomething() throws Exception {
        CooperativeRole role;
        if (TestUtil.findAll(em, CooperativeRole.class).isEmpty()) {
            branchUserRepository.saveAndFlush(branchUser);
            role = CooperativeRoleResourceIT.createEntity();
        } else {
            role = TestUtil.findAll(em, CooperativeRole.class).get(0);
        }
        em.persist(role);
        em.flush();
        branchUser.setRole(role);
        branchUserRepository.saveAndFlush(branchUser);
        Long roleId = role.getId();
        // Get all the branchUserList where role equals to roleId
        defaultBranchUserShouldBeFound("roleId.equals=" + roleId);

        // Get all the branchUserList where role equals to (roleId + 1)
        defaultBranchUserShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    private void defaultBranchUserFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBranchUserShouldBeFound(shouldBeFound);
        defaultBranchUserShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBranchUserShouldBeFound(String filter) throws Exception {
        restBranchUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restBranchUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBranchUserShouldNotBeFound(String filter) throws Exception {
        restBranchUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBranchUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBranchUser() throws Exception {
        // Get the branchUser
        restBranchUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBranchUser() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the branchUser
        BranchUser updatedBranchUser = branchUserRepository.findById(branchUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBranchUser are not directly saved in db
        em.detach(updatedBranchUser);
        updatedBranchUser.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).active(UPDATED_ACTIVE);
        BranchUserDTO branchUserDTO = branchUserMapper.toDto(updatedBranchUser);

        restBranchUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(branchUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the BranchUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBranchUserToMatchAllProperties(updatedBranchUser);
    }

    @Test
    @Transactional
    void putNonExistingBranchUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        branchUser.setId(longCount.incrementAndGet());

        // Create the BranchUser
        BranchUserDTO branchUserDTO = branchUserMapper.toDto(branchUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, branchUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(branchUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBranchUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        branchUser.setId(longCount.incrementAndGet());

        // Create the BranchUser
        BranchUserDTO branchUserDTO = branchUserMapper.toDto(branchUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(branchUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBranchUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        branchUser.setId(longCount.incrementAndGet());

        // Create the BranchUser
        BranchUserDTO branchUserDTO = branchUserMapper.toDto(branchUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(branchUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BranchUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBranchUserWithPatch() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the branchUser using partial update
        BranchUser partialUpdatedBranchUser = new BranchUser();
        partialUpdatedBranchUser.setId(branchUser.getId());

        partialUpdatedBranchUser.active(UPDATED_ACTIVE);

        restBranchUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranchUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBranchUser))
            )
            .andExpect(status().isOk());

        // Validate the BranchUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBranchUserUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBranchUser, branchUser),
            getPersistedBranchUser(branchUser)
        );
    }

    @Test
    @Transactional
    void fullUpdateBranchUserWithPatch() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the branchUser using partial update
        BranchUser partialUpdatedBranchUser = new BranchUser();
        partialUpdatedBranchUser.setId(branchUser.getId());

        partialUpdatedBranchUser.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).active(UPDATED_ACTIVE);

        restBranchUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBranchUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBranchUser))
            )
            .andExpect(status().isOk());

        // Validate the BranchUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBranchUserUpdatableFieldsEquals(partialUpdatedBranchUser, getPersistedBranchUser(partialUpdatedBranchUser));
    }

    @Test
    @Transactional
    void patchNonExistingBranchUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        branchUser.setId(longCount.incrementAndGet());

        // Create the BranchUser
        BranchUserDTO branchUserDTO = branchUserMapper.toDto(branchUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, branchUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(branchUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBranchUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        branchUser.setId(longCount.incrementAndGet());

        // Create the BranchUser
        BranchUserDTO branchUserDTO = branchUserMapper.toDto(branchUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(branchUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BranchUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBranchUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        branchUser.setId(longCount.incrementAndGet());

        // Create the BranchUser
        BranchUserDTO branchUserDTO = branchUserMapper.toDto(branchUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBranchUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(branchUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BranchUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBranchUser() throws Exception {
        // Initialize the database
        insertedBranchUser = branchUserRepository.saveAndFlush(branchUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the branchUser
        restBranchUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, branchUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return branchUserRepository.count();
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

    protected BranchUser getPersistedBranchUser(BranchUser branchUser) {
        return branchUserRepository.findById(branchUser.getId()).orElseThrow();
    }

    protected void assertPersistedBranchUserToMatchAllProperties(BranchUser expectedBranchUser) {
        assertBranchUserAllPropertiesEquals(expectedBranchUser, getPersistedBranchUser(expectedBranchUser));
    }

    protected void assertPersistedBranchUserToMatchUpdatableProperties(BranchUser expectedBranchUser) {
        assertBranchUserAllUpdatablePropertiesEquals(expectedBranchUser, getPersistedBranchUser(expectedBranchUser));
    }
}
