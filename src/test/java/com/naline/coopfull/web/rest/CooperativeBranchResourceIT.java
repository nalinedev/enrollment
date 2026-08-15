package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.CooperativeBranchAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.domain.CooperativeBranch;
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.domain.enumeration.CooperativeStatus;
import com.naline.coopfull.repository.CooperativeBranchRepository;
import com.naline.coopfull.service.CooperativeBranchService;
import com.naline.coopfull.service.dto.CooperativeBranchDTO;
import com.naline.coopfull.service.mapper.CooperativeBranchMapper;
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
 * Integration tests for the {@link CooperativeBranchResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CooperativeBranchResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final CooperativeStatus DEFAULT_STATUS = CooperativeStatus.ACTIVE;
    private static final CooperativeStatus UPDATED_STATUS = CooperativeStatus.INACTIVE;

    private static final LocalDate DEFAULT_OPENING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OPENING_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_OPENING_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_CLOSING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CLOSING_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_CLOSING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/cooperative-branches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CooperativeBranchRepository cooperativeBranchRepository;

    @Mock
    private CooperativeBranchRepository cooperativeBranchRepositoryMock;

    @Autowired
    private CooperativeBranchMapper cooperativeBranchMapper;

    @Mock
    private CooperativeBranchService cooperativeBranchServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCooperativeBranchMockMvc;

    private CooperativeBranch cooperativeBranch;

    private CooperativeBranch insertedCooperativeBranch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CooperativeBranch createEntity() {
        return new CooperativeBranch()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .status(DEFAULT_STATUS)
            .openingDate(DEFAULT_OPENING_DATE)
            .closingDate(DEFAULT_CLOSING_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CooperativeBranch createUpdatedEntity() {
        return new CooperativeBranch()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .status(UPDATED_STATUS)
            .openingDate(UPDATED_OPENING_DATE)
            .closingDate(UPDATED_CLOSING_DATE);
    }

    @BeforeEach
    void initTest() {
        cooperativeBranch = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCooperativeBranch != null) {
            cooperativeBranchRepository.delete(insertedCooperativeBranch);
            insertedCooperativeBranch = null;
        }
    }

    @Test
    @Transactional
    void createCooperativeBranch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CooperativeBranch
        CooperativeBranchDTO cooperativeBranchDTO = cooperativeBranchMapper.toDto(cooperativeBranch);
        var returnedCooperativeBranchDTO = om.readValue(
            restCooperativeBranchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeBranchDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CooperativeBranchDTO.class
        );

        // Validate the CooperativeBranch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCooperativeBranch = cooperativeBranchMapper.toEntity(returnedCooperativeBranchDTO);
        assertCooperativeBranchUpdatableFieldsEquals(returnedCooperativeBranch, getPersistedCooperativeBranch(returnedCooperativeBranch));

        insertedCooperativeBranch = returnedCooperativeBranch;
    }

    @Test
    @Transactional
    void createCooperativeBranchWithExistingId() throws Exception {
        // Create the CooperativeBranch with an existing ID
        cooperativeBranch.setId(1L);
        CooperativeBranchDTO cooperativeBranchDTO = cooperativeBranchMapper.toDto(cooperativeBranch);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCooperativeBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeBranchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CooperativeBranch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cooperativeBranch.setCode(null);

        // Create the CooperativeBranch, which fails.
        CooperativeBranchDTO cooperativeBranchDTO = cooperativeBranchMapper.toDto(cooperativeBranch);

        restCooperativeBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeBranchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cooperativeBranch.setName(null);

        // Create the CooperativeBranch, which fails.
        CooperativeBranchDTO cooperativeBranchDTO = cooperativeBranchMapper.toDto(cooperativeBranch);

        restCooperativeBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeBranchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cooperativeBranch.setStatus(null);

        // Create the CooperativeBranch, which fails.
        CooperativeBranchDTO cooperativeBranchDTO = cooperativeBranchMapper.toDto(cooperativeBranch);

        restCooperativeBranchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeBranchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCooperativeBranches() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList
        restCooperativeBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cooperativeBranch.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(DEFAULT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].closingDate").value(hasItem(DEFAULT_CLOSING_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCooperativeBranchesWithEagerRelationshipsIsEnabled() throws Exception {
        when(cooperativeBranchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCooperativeBranchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cooperativeBranchServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCooperativeBranchesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cooperativeBranchServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCooperativeBranchMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cooperativeBranchRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCooperativeBranch() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get the cooperativeBranch
        restCooperativeBranchMockMvc
            .perform(get(ENTITY_API_URL_ID, cooperativeBranch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cooperativeBranch.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.openingDate").value(DEFAULT_OPENING_DATE.toString()))
            .andExpect(jsonPath("$.closingDate").value(DEFAULT_CLOSING_DATE.toString()));
    }

    @Test
    @Transactional
    void getCooperativeBranchesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        Long id = cooperativeBranch.getId();

        defaultCooperativeBranchFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCooperativeBranchFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCooperativeBranchFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where code equals to
        defaultCooperativeBranchFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where code in
        defaultCooperativeBranchFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where code is not null
        defaultCooperativeBranchFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where code contains
        defaultCooperativeBranchFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where code does not contain
        defaultCooperativeBranchFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where name equals to
        defaultCooperativeBranchFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where name in
        defaultCooperativeBranchFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where name is not null
        defaultCooperativeBranchFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where name contains
        defaultCooperativeBranchFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where name does not contain
        defaultCooperativeBranchFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where description equals to
        defaultCooperativeBranchFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where description in
        defaultCooperativeBranchFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where description is not null
        defaultCooperativeBranchFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where description contains
        defaultCooperativeBranchFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where description does not contain
        defaultCooperativeBranchFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where phone equals to
        defaultCooperativeBranchFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where phone in
        defaultCooperativeBranchFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where phone is not null
        defaultCooperativeBranchFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where phone contains
        defaultCooperativeBranchFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where phone does not contain
        defaultCooperativeBranchFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where email equals to
        defaultCooperativeBranchFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where email in
        defaultCooperativeBranchFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where email is not null
        defaultCooperativeBranchFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where email contains
        defaultCooperativeBranchFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where email does not contain
        defaultCooperativeBranchFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where status equals to
        defaultCooperativeBranchFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where status in
        defaultCooperativeBranchFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where status is not null
        defaultCooperativeBranchFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByOpeningDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where openingDate equals to
        defaultCooperativeBranchFiltering("openingDate.equals=" + DEFAULT_OPENING_DATE, "openingDate.equals=" + UPDATED_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByOpeningDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where openingDate in
        defaultCooperativeBranchFiltering(
            "openingDate.in=" + DEFAULT_OPENING_DATE + "," + UPDATED_OPENING_DATE,
            "openingDate.in=" + UPDATED_OPENING_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByOpeningDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where openingDate is not null
        defaultCooperativeBranchFiltering("openingDate.specified=true", "openingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByOpeningDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where openingDate is greater than or equal to
        defaultCooperativeBranchFiltering(
            "openingDate.greaterThanOrEqual=" + DEFAULT_OPENING_DATE,
            "openingDate.greaterThanOrEqual=" + UPDATED_OPENING_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByOpeningDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where openingDate is less than or equal to
        defaultCooperativeBranchFiltering(
            "openingDate.lessThanOrEqual=" + DEFAULT_OPENING_DATE,
            "openingDate.lessThanOrEqual=" + SMALLER_OPENING_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByOpeningDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where openingDate is less than
        defaultCooperativeBranchFiltering("openingDate.lessThan=" + UPDATED_OPENING_DATE, "openingDate.lessThan=" + DEFAULT_OPENING_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByOpeningDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where openingDate is greater than
        defaultCooperativeBranchFiltering(
            "openingDate.greaterThan=" + SMALLER_OPENING_DATE,
            "openingDate.greaterThan=" + DEFAULT_OPENING_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByClosingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where closingDate equals to
        defaultCooperativeBranchFiltering("closingDate.equals=" + DEFAULT_CLOSING_DATE, "closingDate.equals=" + UPDATED_CLOSING_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByClosingDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where closingDate in
        defaultCooperativeBranchFiltering(
            "closingDate.in=" + DEFAULT_CLOSING_DATE + "," + UPDATED_CLOSING_DATE,
            "closingDate.in=" + UPDATED_CLOSING_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByClosingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where closingDate is not null
        defaultCooperativeBranchFiltering("closingDate.specified=true", "closingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByClosingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where closingDate is greater than or equal to
        defaultCooperativeBranchFiltering(
            "closingDate.greaterThanOrEqual=" + DEFAULT_CLOSING_DATE,
            "closingDate.greaterThanOrEqual=" + UPDATED_CLOSING_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByClosingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where closingDate is less than or equal to
        defaultCooperativeBranchFiltering(
            "closingDate.lessThanOrEqual=" + DEFAULT_CLOSING_DATE,
            "closingDate.lessThanOrEqual=" + SMALLER_CLOSING_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByClosingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where closingDate is less than
        defaultCooperativeBranchFiltering("closingDate.lessThan=" + UPDATED_CLOSING_DATE, "closingDate.lessThan=" + DEFAULT_CLOSING_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByClosingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        // Get all the cooperativeBranchList where closingDate is greater than
        defaultCooperativeBranchFiltering(
            "closingDate.greaterThan=" + SMALLER_CLOSING_DATE,
            "closingDate.greaterThan=" + DEFAULT_CLOSING_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByCooperativeIsEqualToSomething() throws Exception {
        Cooperative cooperative;
        if (TestUtil.findAll(em, Cooperative.class).isEmpty()) {
            cooperativeBranchRepository.saveAndFlush(cooperativeBranch);
            cooperative = CooperativeResourceIT.createEntity();
        } else {
            cooperative = TestUtil.findAll(em, Cooperative.class).get(0);
        }
        em.persist(cooperative);
        em.flush();
        cooperativeBranch.setCooperative(cooperative);
        cooperativeBranchRepository.saveAndFlush(cooperativeBranch);
        Long cooperativeId = cooperative.getId();
        // Get all the cooperativeBranchList where cooperative equals to cooperativeId
        defaultCooperativeBranchShouldBeFound("cooperativeId.equals=" + cooperativeId);

        // Get all the cooperativeBranchList where cooperative equals to (cooperativeId + 1)
        defaultCooperativeBranchShouldNotBeFound("cooperativeId.equals=" + (cooperativeId + 1));
    }

    @Test
    @Transactional
    void getAllCooperativeBranchesByLocationIsEqualToSomething() throws Exception {
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            cooperativeBranchRepository.saveAndFlush(cooperativeBranch);
            location = LocationResourceIT.createEntity();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(location);
        em.flush();
        cooperativeBranch.setLocation(location);
        cooperativeBranchRepository.saveAndFlush(cooperativeBranch);
        Long locationId = location.getId();
        // Get all the cooperativeBranchList where location equals to locationId
        defaultCooperativeBranchShouldBeFound("locationId.equals=" + locationId);

        // Get all the cooperativeBranchList where location equals to (locationId + 1)
        defaultCooperativeBranchShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    private void defaultCooperativeBranchFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCooperativeBranchShouldBeFound(shouldBeFound);
        defaultCooperativeBranchShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCooperativeBranchShouldBeFound(String filter) throws Exception {
        restCooperativeBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cooperativeBranch.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(DEFAULT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].closingDate").value(hasItem(DEFAULT_CLOSING_DATE.toString())));

        // Check, that the count call also returns 1
        restCooperativeBranchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCooperativeBranchShouldNotBeFound(String filter) throws Exception {
        restCooperativeBranchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCooperativeBranchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCooperativeBranch() throws Exception {
        // Get the cooperativeBranch
        restCooperativeBranchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCooperativeBranch() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cooperativeBranch
        CooperativeBranch updatedCooperativeBranch = cooperativeBranchRepository.findById(cooperativeBranch.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCooperativeBranch are not directly saved in db
        em.detach(updatedCooperativeBranch);
        updatedCooperativeBranch
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .status(UPDATED_STATUS)
            .openingDate(UPDATED_OPENING_DATE)
            .closingDate(UPDATED_CLOSING_DATE);
        CooperativeBranchDTO cooperativeBranchDTO = cooperativeBranchMapper.toDto(updatedCooperativeBranch);

        restCooperativeBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cooperativeBranchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cooperativeBranchDTO))
            )
            .andExpect(status().isOk());

        // Validate the CooperativeBranch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCooperativeBranchToMatchAllProperties(updatedCooperativeBranch);
    }

    @Test
    @Transactional
    void putNonExistingCooperativeBranch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeBranch.setId(longCount.incrementAndGet());

        // Create the CooperativeBranch
        CooperativeBranchDTO cooperativeBranchDTO = cooperativeBranchMapper.toDto(cooperativeBranch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCooperativeBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cooperativeBranchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cooperativeBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CooperativeBranch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCooperativeBranch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeBranch.setId(longCount.incrementAndGet());

        // Create the CooperativeBranch
        CooperativeBranchDTO cooperativeBranchDTO = cooperativeBranchMapper.toDto(cooperativeBranch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeBranchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cooperativeBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CooperativeBranch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCooperativeBranch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeBranch.setId(longCount.incrementAndGet());

        // Create the CooperativeBranch
        CooperativeBranchDTO cooperativeBranchDTO = cooperativeBranchMapper.toDto(cooperativeBranch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeBranchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeBranchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CooperativeBranch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCooperativeBranchWithPatch() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cooperativeBranch using partial update
        CooperativeBranch partialUpdatedCooperativeBranch = new CooperativeBranch();
        partialUpdatedCooperativeBranch.setId(cooperativeBranch.getId());

        partialUpdatedCooperativeBranch
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .email(UPDATED_EMAIL)
            .status(UPDATED_STATUS)
            .closingDate(UPDATED_CLOSING_DATE);

        restCooperativeBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCooperativeBranch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCooperativeBranch))
            )
            .andExpect(status().isOk());

        // Validate the CooperativeBranch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCooperativeBranchUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCooperativeBranch, cooperativeBranch),
            getPersistedCooperativeBranch(cooperativeBranch)
        );
    }

    @Test
    @Transactional
    void fullUpdateCooperativeBranchWithPatch() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cooperativeBranch using partial update
        CooperativeBranch partialUpdatedCooperativeBranch = new CooperativeBranch();
        partialUpdatedCooperativeBranch.setId(cooperativeBranch.getId());

        partialUpdatedCooperativeBranch
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .status(UPDATED_STATUS)
            .openingDate(UPDATED_OPENING_DATE)
            .closingDate(UPDATED_CLOSING_DATE);

        restCooperativeBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCooperativeBranch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCooperativeBranch))
            )
            .andExpect(status().isOk());

        // Validate the CooperativeBranch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCooperativeBranchUpdatableFieldsEquals(
            partialUpdatedCooperativeBranch,
            getPersistedCooperativeBranch(partialUpdatedCooperativeBranch)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCooperativeBranch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeBranch.setId(longCount.incrementAndGet());

        // Create the CooperativeBranch
        CooperativeBranchDTO cooperativeBranchDTO = cooperativeBranchMapper.toDto(cooperativeBranch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCooperativeBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cooperativeBranchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cooperativeBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CooperativeBranch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCooperativeBranch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeBranch.setId(longCount.incrementAndGet());

        // Create the CooperativeBranch
        CooperativeBranchDTO cooperativeBranchDTO = cooperativeBranchMapper.toDto(cooperativeBranch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeBranchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cooperativeBranchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CooperativeBranch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCooperativeBranch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperativeBranch.setId(longCount.incrementAndGet());

        // Create the CooperativeBranch
        CooperativeBranchDTO cooperativeBranchDTO = cooperativeBranchMapper.toDto(cooperativeBranch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeBranchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cooperativeBranchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CooperativeBranch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCooperativeBranch() throws Exception {
        // Initialize the database
        insertedCooperativeBranch = cooperativeBranchRepository.saveAndFlush(cooperativeBranch);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cooperativeBranch
        restCooperativeBranchMockMvc
            .perform(delete(ENTITY_API_URL_ID, cooperativeBranch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cooperativeBranchRepository.count();
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

    protected CooperativeBranch getPersistedCooperativeBranch(CooperativeBranch cooperativeBranch) {
        return cooperativeBranchRepository.findById(cooperativeBranch.getId()).orElseThrow();
    }

    protected void assertPersistedCooperativeBranchToMatchAllProperties(CooperativeBranch expectedCooperativeBranch) {
        assertCooperativeBranchAllPropertiesEquals(expectedCooperativeBranch, getPersistedCooperativeBranch(expectedCooperativeBranch));
    }

    protected void assertPersistedCooperativeBranchToMatchUpdatableProperties(CooperativeBranch expectedCooperativeBranch) {
        assertCooperativeBranchAllUpdatablePropertiesEquals(
            expectedCooperativeBranch,
            getPersistedCooperativeBranch(expectedCooperativeBranch)
        );
    }
}
