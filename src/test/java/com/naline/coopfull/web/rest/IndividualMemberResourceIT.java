package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.IndividualMemberAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.IndividualMember;
import com.naline.coopfull.repository.IndividualMemberRepository;
import com.naline.coopfull.service.dto.IndividualMemberDTO;
import com.naline.coopfull.service.mapper.IndividualMemberMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link IndividualMemberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IndividualMemberResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MAIDEN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MAIDEN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_BIRTH_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_OCCUPATION = "AAAAAAAAAA";
    private static final String UPDATED_OCCUPATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/individual-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IndividualMemberRepository individualMemberRepository;

    @Autowired
    private IndividualMemberMapper individualMemberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIndividualMemberMockMvc;

    private IndividualMember individualMember;

    private IndividualMember insertedIndividualMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndividualMember createEntity() {
        return new IndividualMember()
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .maidenName(DEFAULT_MAIDEN_NAME)
            .gender(DEFAULT_GENDER)
            .birthDate(DEFAULT_BIRTH_DATE)
            .birthPlace(DEFAULT_BIRTH_PLACE)
            .nationality(DEFAULT_NATIONALITY)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .occupation(DEFAULT_OCCUPATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IndividualMember createUpdatedEntity() {
        return new IndividualMember()
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .maidenName(UPDATED_MAIDEN_NAME)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .nationality(UPDATED_NATIONALITY)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .occupation(UPDATED_OCCUPATION);
    }

    @BeforeEach
    void initTest() {
        individualMember = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIndividualMember != null) {
            individualMemberRepository.delete(insertedIndividualMember);
            insertedIndividualMember = null;
        }
    }

    @Test
    @Transactional
    void createIndividualMember() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IndividualMember
        IndividualMemberDTO individualMemberDTO = individualMemberMapper.toDto(individualMember);
        var returnedIndividualMemberDTO = om.readValue(
            restIndividualMemberMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(individualMemberDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IndividualMemberDTO.class
        );

        // Validate the IndividualMember in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIndividualMember = individualMemberMapper.toEntity(returnedIndividualMemberDTO);
        assertIndividualMemberUpdatableFieldsEquals(returnedIndividualMember, getPersistedIndividualMember(returnedIndividualMember));

        insertedIndividualMember = returnedIndividualMember;
    }

    @Test
    @Transactional
    void createIndividualMemberWithExistingId() throws Exception {
        // Create the IndividualMember with an existing ID
        individualMember.setId(1L);
        IndividualMemberDTO individualMemberDTO = individualMemberMapper.toDto(individualMember);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndividualMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(individualMemberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IndividualMember in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        individualMember.setFirstName(null);

        // Create the IndividualMember, which fails.
        IndividualMemberDTO individualMemberDTO = individualMemberMapper.toDto(individualMember);

        restIndividualMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(individualMemberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        individualMember.setLastName(null);

        // Create the IndividualMember, which fails.
        IndividualMemberDTO individualMemberDTO = individualMemberMapper.toDto(individualMember);

        restIndividualMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(individualMemberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIndividualMembers() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList
        restIndividualMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(individualMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].maidenName").value(hasItem(DEFAULT_MAIDEN_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].occupation").value(hasItem(DEFAULT_OCCUPATION)));
    }

    @Test
    @Transactional
    void getIndividualMember() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get the individualMember
        restIndividualMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, individualMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(individualMember.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.maidenName").value(DEFAULT_MAIDEN_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.birthPlace").value(DEFAULT_BIRTH_PLACE))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.occupation").value(DEFAULT_OCCUPATION));
    }

    @Test
    @Transactional
    void getIndividualMembersByIdFiltering() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        Long id = individualMember.getId();

        defaultIndividualMemberFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIndividualMemberFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIndividualMemberFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where firstName equals to
        defaultIndividualMemberFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where firstName in
        defaultIndividualMemberFiltering(
            "firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME,
            "firstName.in=" + UPDATED_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where firstName is not null
        defaultIndividualMemberFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllIndividualMembersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where firstName contains
        defaultIndividualMemberFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where firstName does not contain
        defaultIndividualMemberFiltering(
            "firstName.doesNotContain=" + UPDATED_FIRST_NAME,
            "firstName.doesNotContain=" + DEFAULT_FIRST_NAME
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where middleName equals to
        defaultIndividualMemberFiltering("middleName.equals=" + DEFAULT_MIDDLE_NAME, "middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where middleName in
        defaultIndividualMemberFiltering(
            "middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME,
            "middleName.in=" + UPDATED_MIDDLE_NAME
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where middleName is not null
        defaultIndividualMemberFiltering("middleName.specified=true", "middleName.specified=false");
    }

    @Test
    @Transactional
    void getAllIndividualMembersByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where middleName contains
        defaultIndividualMemberFiltering("middleName.contains=" + DEFAULT_MIDDLE_NAME, "middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where middleName does not contain
        defaultIndividualMemberFiltering(
            "middleName.doesNotContain=" + UPDATED_MIDDLE_NAME,
            "middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where lastName equals to
        defaultIndividualMemberFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where lastName in
        defaultIndividualMemberFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where lastName is not null
        defaultIndividualMemberFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllIndividualMembersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where lastName contains
        defaultIndividualMemberFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where lastName does not contain
        defaultIndividualMemberFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByMaidenNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where maidenName equals to
        defaultIndividualMemberFiltering("maidenName.equals=" + DEFAULT_MAIDEN_NAME, "maidenName.equals=" + UPDATED_MAIDEN_NAME);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByMaidenNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where maidenName in
        defaultIndividualMemberFiltering(
            "maidenName.in=" + DEFAULT_MAIDEN_NAME + "," + UPDATED_MAIDEN_NAME,
            "maidenName.in=" + UPDATED_MAIDEN_NAME
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByMaidenNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where maidenName is not null
        defaultIndividualMemberFiltering("maidenName.specified=true", "maidenName.specified=false");
    }

    @Test
    @Transactional
    void getAllIndividualMembersByMaidenNameContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where maidenName contains
        defaultIndividualMemberFiltering("maidenName.contains=" + DEFAULT_MAIDEN_NAME, "maidenName.contains=" + UPDATED_MAIDEN_NAME);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByMaidenNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where maidenName does not contain
        defaultIndividualMemberFiltering(
            "maidenName.doesNotContain=" + UPDATED_MAIDEN_NAME,
            "maidenName.doesNotContain=" + DEFAULT_MAIDEN_NAME
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where gender equals to
        defaultIndividualMemberFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where gender in
        defaultIndividualMemberFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where gender is not null
        defaultIndividualMemberFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllIndividualMembersByGenderContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where gender contains
        defaultIndividualMemberFiltering("gender.contains=" + DEFAULT_GENDER, "gender.contains=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByGenderNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where gender does not contain
        defaultIndividualMemberFiltering("gender.doesNotContain=" + UPDATED_GENDER, "gender.doesNotContain=" + DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where birthDate equals to
        defaultIndividualMemberFiltering("birthDate.equals=" + DEFAULT_BIRTH_DATE, "birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where birthDate in
        defaultIndividualMemberFiltering(
            "birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE,
            "birthDate.in=" + UPDATED_BIRTH_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where birthDate is not null
        defaultIndividualMemberFiltering("birthDate.specified=true", "birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIndividualMembersByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where birthDate is greater than or equal to
        defaultIndividualMemberFiltering(
            "birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE,
            "birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where birthDate is less than or equal to
        defaultIndividualMemberFiltering(
            "birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE,
            "birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where birthDate is less than
        defaultIndividualMemberFiltering("birthDate.lessThan=" + UPDATED_BIRTH_DATE, "birthDate.lessThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where birthDate is greater than
        defaultIndividualMemberFiltering("birthDate.greaterThan=" + SMALLER_BIRTH_DATE, "birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByBirthPlaceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where birthPlace equals to
        defaultIndividualMemberFiltering("birthPlace.equals=" + DEFAULT_BIRTH_PLACE, "birthPlace.equals=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByBirthPlaceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where birthPlace in
        defaultIndividualMemberFiltering(
            "birthPlace.in=" + DEFAULT_BIRTH_PLACE + "," + UPDATED_BIRTH_PLACE,
            "birthPlace.in=" + UPDATED_BIRTH_PLACE
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByBirthPlaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where birthPlace is not null
        defaultIndividualMemberFiltering("birthPlace.specified=true", "birthPlace.specified=false");
    }

    @Test
    @Transactional
    void getAllIndividualMembersByBirthPlaceContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where birthPlace contains
        defaultIndividualMemberFiltering("birthPlace.contains=" + DEFAULT_BIRTH_PLACE, "birthPlace.contains=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByBirthPlaceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where birthPlace does not contain
        defaultIndividualMemberFiltering(
            "birthPlace.doesNotContain=" + UPDATED_BIRTH_PLACE,
            "birthPlace.doesNotContain=" + DEFAULT_BIRTH_PLACE
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByNationalityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where nationality equals to
        defaultIndividualMemberFiltering("nationality.equals=" + DEFAULT_NATIONALITY, "nationality.equals=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByNationalityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where nationality in
        defaultIndividualMemberFiltering(
            "nationality.in=" + DEFAULT_NATIONALITY + "," + UPDATED_NATIONALITY,
            "nationality.in=" + UPDATED_NATIONALITY
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByNationalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where nationality is not null
        defaultIndividualMemberFiltering("nationality.specified=true", "nationality.specified=false");
    }

    @Test
    @Transactional
    void getAllIndividualMembersByNationalityContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where nationality contains
        defaultIndividualMemberFiltering("nationality.contains=" + DEFAULT_NATIONALITY, "nationality.contains=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByNationalityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where nationality does not contain
        defaultIndividualMemberFiltering(
            "nationality.doesNotContain=" + UPDATED_NATIONALITY,
            "nationality.doesNotContain=" + DEFAULT_NATIONALITY
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where email equals to
        defaultIndividualMemberFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where email in
        defaultIndividualMemberFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where email is not null
        defaultIndividualMemberFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllIndividualMembersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where email contains
        defaultIndividualMemberFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where email does not contain
        defaultIndividualMemberFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where phoneNumber equals to
        defaultIndividualMemberFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where phoneNumber in
        defaultIndividualMemberFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where phoneNumber is not null
        defaultIndividualMemberFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllIndividualMembersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where phoneNumber contains
        defaultIndividualMemberFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where phoneNumber does not contain
        defaultIndividualMemberFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByOccupationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where occupation equals to
        defaultIndividualMemberFiltering("occupation.equals=" + DEFAULT_OCCUPATION, "occupation.equals=" + UPDATED_OCCUPATION);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByOccupationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where occupation in
        defaultIndividualMemberFiltering(
            "occupation.in=" + DEFAULT_OCCUPATION + "," + UPDATED_OCCUPATION,
            "occupation.in=" + UPDATED_OCCUPATION
        );
    }

    @Test
    @Transactional
    void getAllIndividualMembersByOccupationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where occupation is not null
        defaultIndividualMemberFiltering("occupation.specified=true", "occupation.specified=false");
    }

    @Test
    @Transactional
    void getAllIndividualMembersByOccupationContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where occupation contains
        defaultIndividualMemberFiltering("occupation.contains=" + DEFAULT_OCCUPATION, "occupation.contains=" + UPDATED_OCCUPATION);
    }

    @Test
    @Transactional
    void getAllIndividualMembersByOccupationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        // Get all the individualMemberList where occupation does not contain
        defaultIndividualMemberFiltering(
            "occupation.doesNotContain=" + UPDATED_OCCUPATION,
            "occupation.doesNotContain=" + DEFAULT_OCCUPATION
        );
    }

    private void defaultIndividualMemberFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIndividualMemberShouldBeFound(shouldBeFound);
        defaultIndividualMemberShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIndividualMemberShouldBeFound(String filter) throws Exception {
        restIndividualMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(individualMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].maidenName").value(hasItem(DEFAULT_MAIDEN_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].occupation").value(hasItem(DEFAULT_OCCUPATION)));

        // Check, that the count call also returns 1
        restIndividualMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIndividualMemberShouldNotBeFound(String filter) throws Exception {
        restIndividualMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIndividualMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIndividualMember() throws Exception {
        // Get the individualMember
        restIndividualMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIndividualMember() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the individualMember
        IndividualMember updatedIndividualMember = individualMemberRepository.findById(individualMember.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIndividualMember are not directly saved in db
        em.detach(updatedIndividualMember);
        updatedIndividualMember
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .maidenName(UPDATED_MAIDEN_NAME)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .nationality(UPDATED_NATIONALITY)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .occupation(UPDATED_OCCUPATION);
        IndividualMemberDTO individualMemberDTO = individualMemberMapper.toDto(updatedIndividualMember);

        restIndividualMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, individualMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(individualMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the IndividualMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIndividualMemberToMatchAllProperties(updatedIndividualMember);
    }

    @Test
    @Transactional
    void putNonExistingIndividualMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        individualMember.setId(longCount.incrementAndGet());

        // Create the IndividualMember
        IndividualMemberDTO individualMemberDTO = individualMemberMapper.toDto(individualMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndividualMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, individualMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(individualMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIndividualMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        individualMember.setId(longCount.incrementAndGet());

        // Create the IndividualMember
        IndividualMemberDTO individualMemberDTO = individualMemberMapper.toDto(individualMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(individualMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIndividualMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        individualMember.setId(longCount.incrementAndGet());

        // Create the IndividualMember
        IndividualMemberDTO individualMemberDTO = individualMemberMapper.toDto(individualMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(individualMemberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndividualMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIndividualMemberWithPatch() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the individualMember using partial update
        IndividualMember partialUpdatedIndividualMember = new IndividualMember();
        partialUpdatedIndividualMember.setId(individualMember.getId());

        partialUpdatedIndividualMember.middleName(UPDATED_MIDDLE_NAME).phoneNumber(UPDATED_PHONE_NUMBER).occupation(UPDATED_OCCUPATION);

        restIndividualMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndividualMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndividualMember))
            )
            .andExpect(status().isOk());

        // Validate the IndividualMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndividualMemberUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIndividualMember, individualMember),
            getPersistedIndividualMember(individualMember)
        );
    }

    @Test
    @Transactional
    void fullUpdateIndividualMemberWithPatch() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the individualMember using partial update
        IndividualMember partialUpdatedIndividualMember = new IndividualMember();
        partialUpdatedIndividualMember.setId(individualMember.getId());

        partialUpdatedIndividualMember
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .maidenName(UPDATED_MAIDEN_NAME)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .nationality(UPDATED_NATIONALITY)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .occupation(UPDATED_OCCUPATION);

        restIndividualMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIndividualMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIndividualMember))
            )
            .andExpect(status().isOk());

        // Validate the IndividualMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIndividualMemberUpdatableFieldsEquals(
            partialUpdatedIndividualMember,
            getPersistedIndividualMember(partialUpdatedIndividualMember)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIndividualMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        individualMember.setId(longCount.incrementAndGet());

        // Create the IndividualMember
        IndividualMemberDTO individualMemberDTO = individualMemberMapper.toDto(individualMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIndividualMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, individualMemberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(individualMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIndividualMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        individualMember.setId(longCount.incrementAndGet());

        // Create the IndividualMember
        IndividualMemberDTO individualMemberDTO = individualMemberMapper.toDto(individualMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(individualMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IndividualMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIndividualMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        individualMember.setId(longCount.incrementAndGet());

        // Create the IndividualMember
        IndividualMemberDTO individualMemberDTO = individualMemberMapper.toDto(individualMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIndividualMemberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(individualMemberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IndividualMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIndividualMember() throws Exception {
        // Initialize the database
        insertedIndividualMember = individualMemberRepository.saveAndFlush(individualMember);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the individualMember
        restIndividualMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, individualMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return individualMemberRepository.count();
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

    protected IndividualMember getPersistedIndividualMember(IndividualMember individualMember) {
        return individualMemberRepository.findById(individualMember.getId()).orElseThrow();
    }

    protected void assertPersistedIndividualMemberToMatchAllProperties(IndividualMember expectedIndividualMember) {
        assertIndividualMemberAllPropertiesEquals(expectedIndividualMember, getPersistedIndividualMember(expectedIndividualMember));
    }

    protected void assertPersistedIndividualMemberToMatchUpdatableProperties(IndividualMember expectedIndividualMember) {
        assertIndividualMemberAllUpdatablePropertiesEquals(
            expectedIndividualMember,
            getPersistedIndividualMember(expectedIndividualMember)
        );
    }
}
