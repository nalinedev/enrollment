package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.CooperativeAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.domain.enumeration.CooperativeStatus;
import com.naline.coopfull.repository.CooperativeRepository;
import com.naline.coopfull.service.dto.CooperativeDTO;
import com.naline.coopfull.service.mapper.CooperativeMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CooperativeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CooperativeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LEGAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TAX_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final CooperativeStatus DEFAULT_STATUS = CooperativeStatus.ACTIVE;
    private static final CooperativeStatus UPDATED_STATUS = CooperativeStatus.INACTIVE;

    private static final LocalDate DEFAULT_FOUNDED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FOUNDED_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_FOUNDED_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1786816059661L);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1786816059661L);

    private static final String ENTITY_API_URL = "/api/cooperatives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CooperativeRepository cooperativeRepository;

    @Autowired
    private CooperativeMapper cooperativeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCooperativeMockMvc;

    private Cooperative cooperative;

    private Cooperative insertedCooperative;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cooperative createEntity() {
        return new Cooperative()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .legalName(DEFAULT_LEGAL_NAME)
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER)
            .taxNumber(DEFAULT_TAX_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .foundedDate(DEFAULT_FOUNDED_DATE)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .website(DEFAULT_WEBSITE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cooperative createUpdatedEntity() {
        return new Cooperative()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .legalName(UPDATED_LEGAL_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .taxNumber(UPDATED_TAX_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .foundedDate(UPDATED_FOUNDED_DATE)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .website(UPDATED_WEBSITE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        cooperative = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCooperative != null) {
            cooperativeRepository.delete(insertedCooperative);
            insertedCooperative = null;
        }
    }

    @Test
    @Transactional
    void createCooperative() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);
        var returnedCooperativeDTO = om.readValue(
            restCooperativeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CooperativeDTO.class
        );

        // Validate the Cooperative in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCooperative = cooperativeMapper.toEntity(returnedCooperativeDTO);
        assertCooperativeUpdatableFieldsEquals(returnedCooperative, getPersistedCooperative(returnedCooperative));

        insertedCooperative = returnedCooperative;
    }

    @Test
    @Transactional
    void createCooperativeWithExistingId() throws Exception {
        // Create the Cooperative with an existing ID
        cooperative.setId(1L);
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCooperativeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cooperative in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cooperative.setCode(null);

        // Create the Cooperative, which fails.
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        restCooperativeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cooperative.setName(null);

        // Create the Cooperative, which fails.
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        restCooperativeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cooperative.setStatus(null);

        // Create the Cooperative, which fails.
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        restCooperativeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cooperative.setCreatedDate(null);

        // Create the Cooperative, which fails.
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        restCooperativeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCooperatives() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList
        restCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cooperative.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].legalName").value(hasItem(DEFAULT_LEGAL_NAME)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].foundedDate").value(hasItem(DEFAULT_FOUNDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCooperative() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get the cooperative
        restCooperativeMockMvc
            .perform(get(ENTITY_API_URL_ID, cooperative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cooperative.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.legalName").value(DEFAULT_LEGAL_NAME))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER))
            .andExpect(jsonPath("$.taxNumber").value(DEFAULT_TAX_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.foundedDate").value(DEFAULT_FOUNDED_DATE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getCooperativesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        Long id = cooperative.getId();

        defaultCooperativeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCooperativeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCooperativeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCooperativesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where code equals to
        defaultCooperativeFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCooperativesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where code in
        defaultCooperativeFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCooperativesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where code is not null
        defaultCooperativeFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where code contains
        defaultCooperativeFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCooperativesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where code does not contain
        defaultCooperativeFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllCooperativesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where name equals to
        defaultCooperativeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where name in
        defaultCooperativeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where name is not null
        defaultCooperativeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where name contains
        defaultCooperativeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where name does not contain
        defaultCooperativeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativesByLegalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where legalName equals to
        defaultCooperativeFiltering("legalName.equals=" + DEFAULT_LEGAL_NAME, "legalName.equals=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativesByLegalNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where legalName in
        defaultCooperativeFiltering("legalName.in=" + DEFAULT_LEGAL_NAME + "," + UPDATED_LEGAL_NAME, "legalName.in=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativesByLegalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where legalName is not null
        defaultCooperativeFiltering("legalName.specified=true", "legalName.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativesByLegalNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where legalName contains
        defaultCooperativeFiltering("legalName.contains=" + DEFAULT_LEGAL_NAME, "legalName.contains=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativesByLegalNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where legalName does not contain
        defaultCooperativeFiltering("legalName.doesNotContain=" + UPDATED_LEGAL_NAME, "legalName.doesNotContain=" + DEFAULT_LEGAL_NAME);
    }

    @Test
    @Transactional
    void getAllCooperativesByRegistrationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where registrationNumber equals to
        defaultCooperativeFiltering(
            "registrationNumber.equals=" + DEFAULT_REGISTRATION_NUMBER,
            "registrationNumber.equals=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCooperativesByRegistrationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where registrationNumber in
        defaultCooperativeFiltering(
            "registrationNumber.in=" + DEFAULT_REGISTRATION_NUMBER + "," + UPDATED_REGISTRATION_NUMBER,
            "registrationNumber.in=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCooperativesByRegistrationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where registrationNumber is not null
        defaultCooperativeFiltering("registrationNumber.specified=true", "registrationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativesByRegistrationNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where registrationNumber contains
        defaultCooperativeFiltering(
            "registrationNumber.contains=" + DEFAULT_REGISTRATION_NUMBER,
            "registrationNumber.contains=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCooperativesByRegistrationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where registrationNumber does not contain
        defaultCooperativeFiltering(
            "registrationNumber.doesNotContain=" + UPDATED_REGISTRATION_NUMBER,
            "registrationNumber.doesNotContain=" + DEFAULT_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllCooperativesByTaxNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where taxNumber equals to
        defaultCooperativeFiltering("taxNumber.equals=" + DEFAULT_TAX_NUMBER, "taxNumber.equals=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCooperativesByTaxNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where taxNumber in
        defaultCooperativeFiltering("taxNumber.in=" + DEFAULT_TAX_NUMBER + "," + UPDATED_TAX_NUMBER, "taxNumber.in=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCooperativesByTaxNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where taxNumber is not null
        defaultCooperativeFiltering("taxNumber.specified=true", "taxNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativesByTaxNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where taxNumber contains
        defaultCooperativeFiltering("taxNumber.contains=" + DEFAULT_TAX_NUMBER, "taxNumber.contains=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCooperativesByTaxNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where taxNumber does not contain
        defaultCooperativeFiltering("taxNumber.doesNotContain=" + UPDATED_TAX_NUMBER, "taxNumber.doesNotContain=" + DEFAULT_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCooperativesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where status equals to
        defaultCooperativeFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCooperativesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where status in
        defaultCooperativeFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCooperativesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where status is not null
        defaultCooperativeFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativesByFoundedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where foundedDate equals to
        defaultCooperativeFiltering("foundedDate.equals=" + DEFAULT_FOUNDED_DATE, "foundedDate.equals=" + UPDATED_FOUNDED_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativesByFoundedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where foundedDate in
        defaultCooperativeFiltering(
            "foundedDate.in=" + DEFAULT_FOUNDED_DATE + "," + UPDATED_FOUNDED_DATE,
            "foundedDate.in=" + UPDATED_FOUNDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativesByFoundedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where foundedDate is not null
        defaultCooperativeFiltering("foundedDate.specified=true", "foundedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativesByFoundedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where foundedDate is greater than or equal to
        defaultCooperativeFiltering(
            "foundedDate.greaterThanOrEqual=" + DEFAULT_FOUNDED_DATE,
            "foundedDate.greaterThanOrEqual=" + UPDATED_FOUNDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativesByFoundedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where foundedDate is less than or equal to
        defaultCooperativeFiltering(
            "foundedDate.lessThanOrEqual=" + DEFAULT_FOUNDED_DATE,
            "foundedDate.lessThanOrEqual=" + SMALLER_FOUNDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativesByFoundedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where foundedDate is less than
        defaultCooperativeFiltering("foundedDate.lessThan=" + UPDATED_FOUNDED_DATE, "foundedDate.lessThan=" + DEFAULT_FOUNDED_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativesByFoundedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where foundedDate is greater than
        defaultCooperativeFiltering("foundedDate.greaterThan=" + SMALLER_FOUNDED_DATE, "foundedDate.greaterThan=" + DEFAULT_FOUNDED_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where email equals to
        defaultCooperativeFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCooperativesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where email in
        defaultCooperativeFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCooperativesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where email is not null
        defaultCooperativeFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativesByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where email contains
        defaultCooperativeFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllCooperativesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where email does not contain
        defaultCooperativeFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllCooperativesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where phone equals to
        defaultCooperativeFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCooperativesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where phone in
        defaultCooperativeFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCooperativesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where phone is not null
        defaultCooperativeFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where phone contains
        defaultCooperativeFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllCooperativesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where phone does not contain
        defaultCooperativeFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllCooperativesByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where website equals to
        defaultCooperativeFiltering("website.equals=" + DEFAULT_WEBSITE, "website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCooperativesByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where website in
        defaultCooperativeFiltering("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE, "website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCooperativesByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where website is not null
        defaultCooperativeFiltering("website.specified=true", "website.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativesByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where website contains
        defaultCooperativeFiltering("website.contains=" + DEFAULT_WEBSITE, "website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCooperativesByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where website does not contain
        defaultCooperativeFiltering("website.doesNotContain=" + UPDATED_WEBSITE, "website.doesNotContain=" + DEFAULT_WEBSITE);
    }

    @Test
    @Transactional
    void getAllCooperativesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where createdDate equals to
        defaultCooperativeFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCooperativesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where createdDate in
        defaultCooperativeFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where createdDate is not null
        defaultCooperativeFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCooperativesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where lastModifiedDate equals to
        defaultCooperativeFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where lastModifiedDate in
        defaultCooperativeFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCooperativesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        // Get all the cooperativeList where lastModifiedDate is not null
        defaultCooperativeFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultCooperativeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCooperativeShouldBeFound(shouldBeFound);
        defaultCooperativeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCooperativeShouldBeFound(String filter) throws Exception {
        restCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cooperative.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].legalName").value(hasItem(DEFAULT_LEGAL_NAME)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].foundedDate").value(hasItem(DEFAULT_FOUNDED_DATE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCooperativeShouldNotBeFound(String filter) throws Exception {
        restCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCooperativeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCooperative() throws Exception {
        // Get the cooperative
        restCooperativeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCooperative() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cooperative
        Cooperative updatedCooperative = cooperativeRepository.findById(cooperative.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCooperative are not directly saved in db
        em.detach(updatedCooperative);
        updatedCooperative
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .legalName(UPDATED_LEGAL_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .taxNumber(UPDATED_TAX_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .foundedDate(UPDATED_FOUNDED_DATE)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .website(UPDATED_WEBSITE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(updatedCooperative);

        restCooperativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cooperativeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cooperativeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cooperative in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCooperativeToMatchAllProperties(updatedCooperative);
    }

    @Test
    @Transactional
    void putNonExistingCooperative() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperative.setId(longCount.incrementAndGet());

        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCooperativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cooperativeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cooperative in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCooperative() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperative.setId(longCount.incrementAndGet());

        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cooperative in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCooperative() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperative.setId(longCount.incrementAndGet());

        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cooperativeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cooperative in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCooperativeWithPatch() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cooperative using partial update
        Cooperative partialUpdatedCooperative = new Cooperative();
        partialUpdatedCooperative.setId(cooperative.getId());

        partialUpdatedCooperative
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .status(UPDATED_STATUS)
            .foundedDate(UPDATED_FOUNDED_DATE)
            .phone(UPDATED_PHONE);

        restCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCooperative.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCooperative))
            )
            .andExpect(status().isOk());

        // Validate the Cooperative in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCooperativeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCooperative, cooperative),
            getPersistedCooperative(cooperative)
        );
    }

    @Test
    @Transactional
    void fullUpdateCooperativeWithPatch() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cooperative using partial update
        Cooperative partialUpdatedCooperative = new Cooperative();
        partialUpdatedCooperative.setId(cooperative.getId());

        partialUpdatedCooperative
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .legalName(UPDATED_LEGAL_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .taxNumber(UPDATED_TAX_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .foundedDate(UPDATED_FOUNDED_DATE)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .website(UPDATED_WEBSITE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCooperative.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCooperative))
            )
            .andExpect(status().isOk());

        // Validate the Cooperative in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCooperativeUpdatableFieldsEquals(partialUpdatedCooperative, getPersistedCooperative(partialUpdatedCooperative));
    }

    @Test
    @Transactional
    void patchNonExistingCooperative() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperative.setId(longCount.incrementAndGet());

        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cooperativeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cooperative in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCooperative() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperative.setId(longCount.incrementAndGet());

        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cooperativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cooperative in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCooperative() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cooperative.setId(longCount.incrementAndGet());

        // Create the Cooperative
        CooperativeDTO cooperativeDTO = cooperativeMapper.toDto(cooperative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCooperativeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cooperativeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cooperative in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCooperative() throws Exception {
        // Initialize the database
        insertedCooperative = cooperativeRepository.saveAndFlush(cooperative);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cooperative
        restCooperativeMockMvc
            .perform(delete(ENTITY_API_URL_ID, cooperative.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cooperativeRepository.count();
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

    protected Cooperative getPersistedCooperative(Cooperative cooperative) {
        return cooperativeRepository.findById(cooperative.getId()).orElseThrow();
    }

    protected void assertPersistedCooperativeToMatchAllProperties(Cooperative expectedCooperative) {
        assertCooperativeAllPropertiesEquals(expectedCooperative, getPersistedCooperative(expectedCooperative));
    }

    protected void assertPersistedCooperativeToMatchUpdatableProperties(Cooperative expectedCooperative) {
        assertCooperativeAllUpdatablePropertiesEquals(expectedCooperative, getPersistedCooperative(expectedCooperative));
    }
}
