package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.CooperativeUserAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.AppUser;
import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.domain.CooperativeRole;
import com.naline.coopfull.domain.CooperativeUser;
import com.naline.coopfull.repository.CooperativeUserRepository;
import com.naline.coopfull.service.CooperativeUserService;
import com.naline.coopfull.service.dto.CooperativeUserDTO;
import com.naline.coopfull.service.mapper.CooperativeUserMapper;
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
 * Integration tests for the {@link CooperativeUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CooperativeUserResourceIT {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/cooperative-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CooperativeUserRepository cooperativeUserRepository;

    @Mock
    private CooperativeUserRepository cooperativeUserRepositoryMock;

    @Autowired
    private CooperativeUserMapper cooperativeUserMapper;

    @Mock
    private CooperativeUserService cooperativeUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCooperativeUserMockMvc;

    private CooperativeUser cooperativeUser;

    private CooperativeUser insertedCooperativeUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CooperativeUser createEntity() {
        return new CooperativeUser().startDate(DEFAULT_START_DATE).endDate(DEFAULT_END_DATE).active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CooperativeUser createUpdatedEntity() {
        return new CooperativeUser().startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        cooperativeUser = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCooperativeUser != null) {
            cooperativeUserRepository.delete(insertedCooperativeUser);
            insertedCooperativeUser = null;
        }
    }

    @Test
    @Transactional
    void createCooperativeUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CooperativeUser
        CooperativeUserDTO cooperativeUserDTO = cooperativeUserMapper.toDto(cooperativeUser);
        var returnedCooperativeUserDTO = om.readValue(
            restCooperativeUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeUserDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CooperativeUserDTO.class
        );

        // Validate the CooperativeUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCooperativeUser = cooperativeUserMapper.toEntity(returnedCooperativeUserDTO);
        assertCooperativeUserUpdatableFieldsEquals(returnedCooperativeUser, getPersistedCooperativeUser(returnedCooperativeUser));

        insertedCooperativeUser = returnedCooperativeUser;
    }

    @Test
    @Transactional
    void createCooperativeUserWithExistingId() throws Exception {
        // Create the CooperativeUser with an existing ID
        cooperativeUser.setId(1L);
        CooperativeUserDTO cooperativeUserDTO = cooperativeUserMapper.toDto(cooperativeUser);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCooperativeUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CooperativeUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cooperativeUser.setStartDate(null);

        // Create the CooperativeUser, which fails.
        CooperativeUserDTO cooperativeUserDTO = cooperativeUserMapper.toDto(cooperativeUser);

        restCooperativeUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cooperativeUser.setActive(null);

        // Create the CooperativeUser, which fails.
        CooperativeUserDTO cooperativeUserDTO = cooperativeUserMapper.toDto(cooperativeUser);

        restCooperativeUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCooperativeUsers() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList
        restCooperativeUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cooperativeUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCooperativeUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(cooperativeUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCooperativeUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cooperativeUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCooperativeUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cooperativeUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCooperativeUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cooperativeUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCooperativeUser() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get the cooperativeUser
        restCooperativeUserMockMvc
            .perform(get(ENTITY_API_URL_ID, cooperativeUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cooperativeUser.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getCooperativeUsersByIdFiltering() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        Long id = cooperativeUser.getId();

        defaultCooperativeUserFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCooperativeUserFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCooperativeUserFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where startDate equals to
        defaultCooperativeUserFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where startDate in
        defaultCooperativeUserFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where startDate is not null
        defaultCooperativeUserFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where startDate is greater than or equal to
        defaultCooperativeUserFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where startDate is less than or equal to
        defaultCooperativeUserFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where startDate is less than
        defaultCooperativeUserFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where startDate is greater than
        defaultCooperativeUserFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where endDate equals to
        defaultCooperativeUserFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where endDate in
        defaultCooperativeUserFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where endDate is not null
        defaultCooperativeUserFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where endDate is greater than or equal to
        defaultCooperativeUserFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where endDate is less than or equal to
        defaultCooperativeUserFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where endDate is less than
        defaultCooperativeUserFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where endDate is greater than
        defaultCooperativeUserFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where active equals to
        defaultCooperativeUserFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where active in
        defaultCooperativeUserFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        // Get all the cooperativeUserList where active is not null
        defaultCooperativeUserFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByAppUserIsEqualToSomething() throws Exception {
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            cooperativeUserRepository.saveAndFlush(cooperativeUser);
            appUser = AppUserResourceIT.createEntity();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        em.persist(appUser);
        em.flush();
        cooperativeUser.setAppUser(appUser);
        cooperativeUserRepository.saveAndFlush(cooperativeUser);
        Long appUserId = appUser.getId();
        // Get all the cooperativeUserList where appUser equals to appUserId
        defaultCooperativeUserShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the cooperativeUserList where appUser equals to (appUserId + 1)
        defaultCooperativeUserShouldNotBeFound("appUserId.equals=" + (appUserId + 1));
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByCooperativeIsEqualToSomething() throws Exception {
        Cooperative cooperative;
        if (TestUtil.findAll(em, Cooperative.class).isEmpty()) {
            cooperativeUserRepository.saveAndFlush(cooperativeUser);
            cooperative = CooperativeResourceIT.createEntity();
        } else {
            cooperative = TestUtil.findAll(em, Cooperative.class).get(0);
        }
        em.persist(cooperative);
        em.flush();
        cooperativeUser.setCooperative(cooperative);
        cooperativeUserRepository.saveAndFlush(cooperativeUser);
        Long cooperativeId = cooperative.getId();
        // Get all the cooperativeUserList where cooperative equals to cooperativeId
        defaultCooperativeUserShouldBeFound("cooperativeId.equals=" + cooperativeId);

        // Get all the cooperativeUserList where cooperative equals to (cooperativeId + 1)
        defaultCooperativeUserShouldNotBeFound("cooperativeId.equals=" + (cooperativeId + 1));
    }

    @Test
    @Transactional
    void getAllCooperativeUsersByRoleIsEqualToSomething() throws Exception {
        CooperativeRole role;
        if (TestUtil.findAll(em, CooperativeRole.class).isEmpty()) {
            cooperativeUserRepository.saveAndFlush(cooperativeUser);
            role = CooperativeRoleResourceIT.createEntity();
        } else {
            role = TestUtil.findAll(em, CooperativeRole.class).get(0);
        }
        em.persist(role);
        em.flush();
        cooperativeUser.setRole(role);
        cooperativeUserRepository.saveAndFlush(cooperativeUser);
        Long roleId = role.getId();
        // Get all the cooperativeUserList where role equals to roleId
        defaultCooperativeUserShouldBeFound("roleId.equals=" + roleId);

        // Get all the cooperativeUserList where role equals to (roleId + 1)
        defaultCooperativeUserShouldNotBeFound("roleId.equals=" + (roleId + 1));
    }

    private void defaultCooperativeUserFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCooperativeUserShouldBeFound(shouldBeFound);
        defaultCooperativeUserShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCooperativeUserShouldBeFound(String filter) throws Exception {
        restCooperativeUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cooperativeUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restCooperativeUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCooperativeUserShouldNotBeFound(String filter) throws Exception {
        restCooperativeUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCooperativeUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCooperativeUser() throws Exception {
        // Get the cooperativeUser
        restCooperativeUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCooperativeUser() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cooperativeUser
        CooperativeUser updatedCooperativeUser = cooperativeUserRepository.findById(cooperativeUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCooperativeUser are not directly saved in db
        em.detach(updatedCooperativeUser);
        updatedCooperativeUser.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).active(UPDATED_ACTIVE);
        CooperativeUserDTO cooperativeUserDTO = cooperativeUserMapper.toDto(updatedCooperativeUser);

        restCooperativeUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cooperativeUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cooperativeUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the CooperativeUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCooperativeUserToMatchAllProperties(updatedCooperativeUser);
    }

    @Test
    @Transactional
    void putNonExistingCooperativeUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeUser.setId(longCount.incrementAndGet());

        // Create the CooperativeUser
        CooperativeUserDTO cooperativeUserDTO = cooperativeUserMapper.toDto(cooperativeUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCooperativeUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cooperativeUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cooperativeUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CooperativeUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCooperativeUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeUser.setId(longCount.incrementAndGet());

        // Create the CooperativeUser
        CooperativeUserDTO cooperativeUserDTO = cooperativeUserMapper.toDto(cooperativeUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cooperativeUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CooperativeUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCooperativeUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeUser.setId(longCount.incrementAndGet());

        // Create the CooperativeUser
        CooperativeUserDTO cooperativeUserDTO = cooperativeUserMapper.toDto(cooperativeUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CooperativeUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCooperativeUserWithPatch() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cooperativeUser using partial update
        CooperativeUser partialUpdatedCooperativeUser = new CooperativeUser();
        partialUpdatedCooperativeUser.setId(cooperativeUser.getId());

        restCooperativeUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCooperativeUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCooperativeUser))
            )
            .andExpect(status().isOk());

        // Validate the CooperativeUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCooperativeUserUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCooperativeUser, cooperativeUser),
            getPersistedCooperativeUser(cooperativeUser)
        );
    }

    @Test
    @Transactional
    void fullUpdateCooperativeUserWithPatch() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cooperativeUser using partial update
        CooperativeUser partialUpdatedCooperativeUser = new CooperativeUser();
        partialUpdatedCooperativeUser.setId(cooperativeUser.getId());

        partialUpdatedCooperativeUser.startDate(UPDATED_START_DATE).endDate(UPDATED_END_DATE).active(UPDATED_ACTIVE);

        restCooperativeUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCooperativeUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCooperativeUser))
            )
            .andExpect(status().isOk());

        // Validate the CooperativeUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCooperativeUserUpdatableFieldsEquals(
            partialUpdatedCooperativeUser,
            getPersistedCooperativeUser(partialUpdatedCooperativeUser)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCooperativeUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeUser.setId(longCount.incrementAndGet());

        // Create the CooperativeUser
        CooperativeUserDTO cooperativeUserDTO = cooperativeUserMapper.toDto(cooperativeUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCooperativeUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cooperativeUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cooperativeUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CooperativeUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCooperativeUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeUser.setId(longCount.incrementAndGet());

        // Create the CooperativeUser
        CooperativeUserDTO cooperativeUserDTO = cooperativeUserMapper.toDto(cooperativeUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cooperativeUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CooperativeUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCooperativeUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeUser.setId(longCount.incrementAndGet());

        // Create the CooperativeUser
        CooperativeUserDTO cooperativeUserDTO = cooperativeUserMapper.toDto(cooperativeUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cooperativeUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CooperativeUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCooperativeUser() throws Exception {
        // Initialize the database
        insertedCooperativeUser = cooperativeUserRepository.saveAndFlush(cooperativeUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cooperativeUser
        restCooperativeUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, cooperativeUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cooperativeUserRepository.count();
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

    protected CooperativeUser getPersistedCooperativeUser(CooperativeUser cooperativeUser) {
        return cooperativeUserRepository.findById(cooperativeUser.getId()).orElseThrow();
    }

    protected void assertPersistedCooperativeUserToMatchAllProperties(CooperativeUser expectedCooperativeUser) {
        assertCooperativeUserAllPropertiesEquals(expectedCooperativeUser, getPersistedCooperativeUser(expectedCooperativeUser));
    }

    protected void assertPersistedCooperativeUserToMatchUpdatableProperties(CooperativeUser expectedCooperativeUser) {
        assertCooperativeUserAllUpdatablePropertiesEquals(expectedCooperativeUser, getPersistedCooperativeUser(expectedCooperativeUser));
    }
}
