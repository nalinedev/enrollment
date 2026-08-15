package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.FamilyMemberAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.FamilyMember;
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.repository.FamilyMemberRepository;
import com.naline.coopfull.service.FamilyMemberService;
import com.naline.coopfull.service.dto.FamilyMemberDTO;
import com.naline.coopfull.service.mapper.FamilyMemberMapper;
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
 * Integration tests for the {@link FamilyMemberResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FamilyMemberResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RELATIONSHIP = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_BIRTH_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_PLACE = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_OCCUPATION = "AAAAAAAAAA";
    private static final String UPDATED_OCCUPATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEPENDENT = false;
    private static final Boolean UPDATED_DEPENDENT = true;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/family-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Mock
    private FamilyMemberRepository familyMemberRepositoryMock;

    @Autowired
    private FamilyMemberMapper familyMemberMapper;

    @Mock
    private FamilyMemberService familyMemberServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamilyMemberMockMvc;

    private FamilyMember familyMember;

    private FamilyMember insertedFamilyMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyMember createEntity() {
        return new FamilyMember()
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .relationship(DEFAULT_RELATIONSHIP)
            .gender(DEFAULT_GENDER)
            .birthDate(DEFAULT_BIRTH_DATE)
            .birthPlace(DEFAULT_BIRTH_PLACE)
            .nationality(DEFAULT_NATIONALITY)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .occupation(DEFAULT_OCCUPATION)
            .dependent(DEFAULT_DEPENDENT)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilyMember createUpdatedEntity() {
        return new FamilyMember()
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .relationship(UPDATED_RELATIONSHIP)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .nationality(UPDATED_NATIONALITY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .occupation(UPDATED_OCCUPATION)
            .dependent(UPDATED_DEPENDENT)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        familyMember = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFamilyMember != null) {
            familyMemberRepository.delete(insertedFamilyMember);
            insertedFamilyMember = null;
        }
    }

    @Test
    @Transactional
    void createFamilyMember() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);
        var returnedFamilyMemberDTO = om.readValue(
            restFamilyMemberMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyMemberDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FamilyMemberDTO.class
        );

        // Validate the FamilyMember in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFamilyMember = familyMemberMapper.toEntity(returnedFamilyMemberDTO);
        assertFamilyMemberUpdatableFieldsEquals(returnedFamilyMember, getPersistedFamilyMember(returnedFamilyMember));

        insertedFamilyMember = returnedFamilyMember;
    }

    @Test
    @Transactional
    void createFamilyMemberWithExistingId() throws Exception {
        // Create the FamilyMember with an existing ID
        familyMember.setId(1L);
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamilyMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyMemberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FamilyMember in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        familyMember.setFirstName(null);

        // Create the FamilyMember, which fails.
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        restFamilyMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyMemberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        familyMember.setLastName(null);

        // Create the FamilyMember, which fails.
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        restFamilyMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyMemberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelationshipIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        familyMember.setRelationship(null);

        // Create the FamilyMember, which fails.
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        restFamilyMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyMemberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFamilyMembers() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList
        restFamilyMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].relationship").value(hasItem(DEFAULT_RELATIONSHIP)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].occupation").value(hasItem(DEFAULT_OCCUPATION)))
            .andExpect(jsonPath("$.[*].dependent").value(hasItem(DEFAULT_DEPENDENT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFamilyMembersWithEagerRelationshipsIsEnabled() throws Exception {
        when(familyMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFamilyMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(familyMemberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFamilyMembersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(familyMemberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFamilyMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(familyMemberRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFamilyMember() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get the familyMember
        restFamilyMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, familyMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(familyMember.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.relationship").value(DEFAULT_RELATIONSHIP))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.birthPlace").value(DEFAULT_BIRTH_PLACE))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.occupation").value(DEFAULT_OCCUPATION))
            .andExpect(jsonPath("$.dependent").value(DEFAULT_DEPENDENT))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getFamilyMembersByIdFiltering() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        Long id = familyMember.getId();

        defaultFamilyMemberFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFamilyMemberFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFamilyMemberFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where firstName equals to
        defaultFamilyMemberFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where firstName in
        defaultFamilyMemberFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where firstName is not null
        defaultFamilyMemberFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyMembersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where firstName contains
        defaultFamilyMemberFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where firstName does not contain
        defaultFamilyMemberFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where middleName equals to
        defaultFamilyMemberFiltering("middleName.equals=" + DEFAULT_MIDDLE_NAME, "middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where middleName in
        defaultFamilyMemberFiltering(
            "middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME,
            "middleName.in=" + UPDATED_MIDDLE_NAME
        );
    }

    @Test
    @Transactional
    void getAllFamilyMembersByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where middleName is not null
        defaultFamilyMemberFiltering("middleName.specified=true", "middleName.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyMembersByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where middleName contains
        defaultFamilyMemberFiltering("middleName.contains=" + DEFAULT_MIDDLE_NAME, "middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where middleName does not contain
        defaultFamilyMemberFiltering(
            "middleName.doesNotContain=" + UPDATED_MIDDLE_NAME,
            "middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME
        );
    }

    @Test
    @Transactional
    void getAllFamilyMembersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where lastName equals to
        defaultFamilyMemberFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where lastName in
        defaultFamilyMemberFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where lastName is not null
        defaultFamilyMemberFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyMembersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where lastName contains
        defaultFamilyMemberFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where lastName does not contain
        defaultFamilyMemberFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByRelationshipIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where relationship equals to
        defaultFamilyMemberFiltering("relationship.equals=" + DEFAULT_RELATIONSHIP, "relationship.equals=" + UPDATED_RELATIONSHIP);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByRelationshipIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where relationship in
        defaultFamilyMemberFiltering(
            "relationship.in=" + DEFAULT_RELATIONSHIP + "," + UPDATED_RELATIONSHIP,
            "relationship.in=" + UPDATED_RELATIONSHIP
        );
    }

    @Test
    @Transactional
    void getAllFamilyMembersByRelationshipIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where relationship is not null
        defaultFamilyMemberFiltering("relationship.specified=true", "relationship.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyMembersByRelationshipContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where relationship contains
        defaultFamilyMemberFiltering("relationship.contains=" + DEFAULT_RELATIONSHIP, "relationship.contains=" + UPDATED_RELATIONSHIP);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByRelationshipNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where relationship does not contain
        defaultFamilyMemberFiltering(
            "relationship.doesNotContain=" + UPDATED_RELATIONSHIP,
            "relationship.doesNotContain=" + DEFAULT_RELATIONSHIP
        );
    }

    @Test
    @Transactional
    void getAllFamilyMembersByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where gender equals to
        defaultFamilyMemberFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where gender in
        defaultFamilyMemberFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where gender is not null
        defaultFamilyMemberFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyMembersByGenderContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where gender contains
        defaultFamilyMemberFiltering("gender.contains=" + DEFAULT_GENDER, "gender.contains=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByGenderNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where gender does not contain
        defaultFamilyMemberFiltering("gender.doesNotContain=" + UPDATED_GENDER, "gender.doesNotContain=" + DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where birthDate equals to
        defaultFamilyMemberFiltering("birthDate.equals=" + DEFAULT_BIRTH_DATE, "birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where birthDate in
        defaultFamilyMemberFiltering("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE, "birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where birthDate is not null
        defaultFamilyMemberFiltering("birthDate.specified=true", "birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyMembersByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where birthDate is greater than or equal to
        defaultFamilyMemberFiltering(
            "birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE,
            "birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE
        );
    }

    @Test
    @Transactional
    void getAllFamilyMembersByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where birthDate is less than or equal to
        defaultFamilyMemberFiltering("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE, "birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where birthDate is less than
        defaultFamilyMemberFiltering("birthDate.lessThan=" + UPDATED_BIRTH_DATE, "birthDate.lessThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where birthDate is greater than
        defaultFamilyMemberFiltering("birthDate.greaterThan=" + SMALLER_BIRTH_DATE, "birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByBirthPlaceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where birthPlace equals to
        defaultFamilyMemberFiltering("birthPlace.equals=" + DEFAULT_BIRTH_PLACE, "birthPlace.equals=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByBirthPlaceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where birthPlace in
        defaultFamilyMemberFiltering(
            "birthPlace.in=" + DEFAULT_BIRTH_PLACE + "," + UPDATED_BIRTH_PLACE,
            "birthPlace.in=" + UPDATED_BIRTH_PLACE
        );
    }

    @Test
    @Transactional
    void getAllFamilyMembersByBirthPlaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where birthPlace is not null
        defaultFamilyMemberFiltering("birthPlace.specified=true", "birthPlace.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyMembersByBirthPlaceContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where birthPlace contains
        defaultFamilyMemberFiltering("birthPlace.contains=" + DEFAULT_BIRTH_PLACE, "birthPlace.contains=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByBirthPlaceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where birthPlace does not contain
        defaultFamilyMemberFiltering(
            "birthPlace.doesNotContain=" + UPDATED_BIRTH_PLACE,
            "birthPlace.doesNotContain=" + DEFAULT_BIRTH_PLACE
        );
    }

    @Test
    @Transactional
    void getAllFamilyMembersByNationalityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where nationality equals to
        defaultFamilyMemberFiltering("nationality.equals=" + DEFAULT_NATIONALITY, "nationality.equals=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByNationalityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where nationality in
        defaultFamilyMemberFiltering(
            "nationality.in=" + DEFAULT_NATIONALITY + "," + UPDATED_NATIONALITY,
            "nationality.in=" + UPDATED_NATIONALITY
        );
    }

    @Test
    @Transactional
    void getAllFamilyMembersByNationalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where nationality is not null
        defaultFamilyMemberFiltering("nationality.specified=true", "nationality.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyMembersByNationalityContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where nationality contains
        defaultFamilyMemberFiltering("nationality.contains=" + DEFAULT_NATIONALITY, "nationality.contains=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByNationalityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where nationality does not contain
        defaultFamilyMemberFiltering(
            "nationality.doesNotContain=" + UPDATED_NATIONALITY,
            "nationality.doesNotContain=" + DEFAULT_NATIONALITY
        );
    }

    @Test
    @Transactional
    void getAllFamilyMembersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where phoneNumber equals to
        defaultFamilyMemberFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where phoneNumber in
        defaultFamilyMemberFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllFamilyMembersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where phoneNumber is not null
        defaultFamilyMemberFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyMembersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where phoneNumber contains
        defaultFamilyMemberFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where phoneNumber does not contain
        defaultFamilyMemberFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllFamilyMembersByOccupationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where occupation equals to
        defaultFamilyMemberFiltering("occupation.equals=" + DEFAULT_OCCUPATION, "occupation.equals=" + UPDATED_OCCUPATION);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByOccupationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where occupation in
        defaultFamilyMemberFiltering(
            "occupation.in=" + DEFAULT_OCCUPATION + "," + UPDATED_OCCUPATION,
            "occupation.in=" + UPDATED_OCCUPATION
        );
    }

    @Test
    @Transactional
    void getAllFamilyMembersByOccupationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where occupation is not null
        defaultFamilyMemberFiltering("occupation.specified=true", "occupation.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyMembersByOccupationContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where occupation contains
        defaultFamilyMemberFiltering("occupation.contains=" + DEFAULT_OCCUPATION, "occupation.contains=" + UPDATED_OCCUPATION);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByOccupationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where occupation does not contain
        defaultFamilyMemberFiltering("occupation.doesNotContain=" + UPDATED_OCCUPATION, "occupation.doesNotContain=" + DEFAULT_OCCUPATION);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByDependentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where dependent equals to
        defaultFamilyMemberFiltering("dependent.equals=" + DEFAULT_DEPENDENT, "dependent.equals=" + UPDATED_DEPENDENT);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByDependentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where dependent in
        defaultFamilyMemberFiltering("dependent.in=" + DEFAULT_DEPENDENT + "," + UPDATED_DEPENDENT, "dependent.in=" + UPDATED_DEPENDENT);
    }

    @Test
    @Transactional
    void getAllFamilyMembersByDependentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        // Get all the familyMemberList where dependent is not null
        defaultFamilyMemberFiltering("dependent.specified=true", "dependent.specified=false");
    }

    @Test
    @Transactional
    void getAllFamilyMembersByMemberIsEqualToSomething() throws Exception {
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            familyMemberRepository.saveAndFlush(familyMember);
            member = MemberResourceIT.createEntity();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        em.persist(member);
        em.flush();
        familyMember.setMember(member);
        familyMemberRepository.saveAndFlush(familyMember);
        Long memberId = member.getId();
        // Get all the familyMemberList where member equals to memberId
        defaultFamilyMemberShouldBeFound("memberId.equals=" + memberId);

        // Get all the familyMemberList where member equals to (memberId + 1)
        defaultFamilyMemberShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    private void defaultFamilyMemberFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFamilyMemberShouldBeFound(shouldBeFound);
        defaultFamilyMemberShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFamilyMemberShouldBeFound(String filter) throws Exception {
        restFamilyMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familyMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].relationship").value(hasItem(DEFAULT_RELATIONSHIP)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].occupation").value(hasItem(DEFAULT_OCCUPATION)))
            .andExpect(jsonPath("$.[*].dependent").value(hasItem(DEFAULT_DEPENDENT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restFamilyMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFamilyMemberShouldNotBeFound(String filter) throws Exception {
        restFamilyMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFamilyMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFamilyMember() throws Exception {
        // Get the familyMember
        restFamilyMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFamilyMember() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the familyMember
        FamilyMember updatedFamilyMember = familyMemberRepository.findById(familyMember.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFamilyMember are not directly saved in db
        em.detach(updatedFamilyMember);
        updatedFamilyMember
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .relationship(UPDATED_RELATIONSHIP)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .nationality(UPDATED_NATIONALITY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .occupation(UPDATED_OCCUPATION)
            .dependent(UPDATED_DEPENDENT)
            .notes(UPDATED_NOTES);
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(updatedFamilyMember);

        restFamilyMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familyMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(familyMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the FamilyMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFamilyMemberToMatchAllProperties(updatedFamilyMember);
    }

    @Test
    @Transactional
    void putNonExistingFamilyMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyMember.setId(longCount.incrementAndGet());

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familyMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamilyMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyMember.setId(longCount.incrementAndGet());

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamilyMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyMember.setId(longCount.incrementAndGet());

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(familyMemberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamilyMemberWithPatch() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the familyMember using partial update
        FamilyMember partialUpdatedFamilyMember = new FamilyMember();
        partialUpdatedFamilyMember.setId(familyMember.getId());

        partialUpdatedFamilyMember
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .relationship(UPDATED_RELATIONSHIP)
            .gender(UPDATED_GENDER)
            .nationality(UPDATED_NATIONALITY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .notes(UPDATED_NOTES);

        restFamilyMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFamilyMember))
            )
            .andExpect(status().isOk());

        // Validate the FamilyMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFamilyMemberUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFamilyMember, familyMember),
            getPersistedFamilyMember(familyMember)
        );
    }

    @Test
    @Transactional
    void fullUpdateFamilyMemberWithPatch() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the familyMember using partial update
        FamilyMember partialUpdatedFamilyMember = new FamilyMember();
        partialUpdatedFamilyMember.setId(familyMember.getId());

        partialUpdatedFamilyMember
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .relationship(UPDATED_RELATIONSHIP)
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .nationality(UPDATED_NATIONALITY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .occupation(UPDATED_OCCUPATION)
            .dependent(UPDATED_DEPENDENT)
            .notes(UPDATED_NOTES);

        restFamilyMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilyMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFamilyMember))
            )
            .andExpect(status().isOk());

        // Validate the FamilyMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFamilyMemberUpdatableFieldsEquals(partialUpdatedFamilyMember, getPersistedFamilyMember(partialUpdatedFamilyMember));
    }

    @Test
    @Transactional
    void patchNonExistingFamilyMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyMember.setId(longCount.incrementAndGet());

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, familyMemberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamilyMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyMember.setId(longCount.incrementAndGet());

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(familyMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FamilyMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamilyMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        familyMember.setId(longCount.incrementAndGet());

        // Create the FamilyMember
        FamilyMemberDTO familyMemberDTO = familyMemberMapper.toDto(familyMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamilyMemberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(familyMemberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FamilyMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamilyMember() throws Exception {
        // Initialize the database
        insertedFamilyMember = familyMemberRepository.saveAndFlush(familyMember);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the familyMember
        restFamilyMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, familyMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return familyMemberRepository.count();
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

    protected FamilyMember getPersistedFamilyMember(FamilyMember familyMember) {
        return familyMemberRepository.findById(familyMember.getId()).orElseThrow();
    }

    protected void assertPersistedFamilyMemberToMatchAllProperties(FamilyMember expectedFamilyMember) {
        assertFamilyMemberAllPropertiesEquals(expectedFamilyMember, getPersistedFamilyMember(expectedFamilyMember));
    }

    protected void assertPersistedFamilyMemberToMatchUpdatableProperties(FamilyMember expectedFamilyMember) {
        assertFamilyMemberAllUpdatablePropertiesEquals(expectedFamilyMember, getPersistedFamilyMember(expectedFamilyMember));
    }
}
