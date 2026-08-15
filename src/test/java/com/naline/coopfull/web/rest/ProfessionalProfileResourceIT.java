package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.ProfessionalProfileAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static com.naline.coopfull.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.ProfessionalProfile;
import com.naline.coopfull.repository.ProfessionalProfileRepository;
import com.naline.coopfull.service.dto.ProfessionalProfileDTO;
import com.naline.coopfull.service.mapper.ProfessionalProfileMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ProfessionalProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfessionalProfileResourceIT {

    private static final String DEFAULT_EMPLOYMENT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYMENT_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

    private static final String DEFAULT_SECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SECTOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEARS_OF_EXPERIENCE = 1;
    private static final Integer UPDATED_YEARS_OF_EXPERIENCE = 2;
    private static final Integer SMALLER_YEARS_OF_EXPERIENCE = 1 - 1;

    private static final BigDecimal DEFAULT_MONTHLY_INCOME = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTHLY_INCOME = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTHLY_INCOME = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ANNUAL_INCOME = new BigDecimal(1);
    private static final BigDecimal UPDATED_ANNUAL_INCOME = new BigDecimal(2);
    private static final BigDecimal SMALLER_ANNUAL_INCOME = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_EMPLOYMENT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EMPLOYMENT_START_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_EMPLOYMENT_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_EMPLOYER_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYER_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/professional-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfessionalProfileRepository professionalProfileRepository;

    @Autowired
    private ProfessionalProfileMapper professionalProfileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfessionalProfileMockMvc;

    private ProfessionalProfile professionalProfile;

    private ProfessionalProfile insertedProfessionalProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalProfile createEntity() {
        return new ProfessionalProfile()
            .employmentStatus(DEFAULT_EMPLOYMENT_STATUS)
            .employerName(DEFAULT_EMPLOYER_NAME)
            .jobTitle(DEFAULT_JOB_TITLE)
            .profession(DEFAULT_PROFESSION)
            .sector(DEFAULT_SECTOR)
            .yearsOfExperience(DEFAULT_YEARS_OF_EXPERIENCE)
            .monthlyIncome(DEFAULT_MONTHLY_INCOME)
            .annualIncome(DEFAULT_ANNUAL_INCOME)
            .employmentStartDate(DEFAULT_EMPLOYMENT_START_DATE)
            .employerLocation(DEFAULT_EMPLOYER_LOCATION)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProfessionalProfile createUpdatedEntity() {
        return new ProfessionalProfile()
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .employerName(UPDATED_EMPLOYER_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .profession(UPDATED_PROFESSION)
            .sector(UPDATED_SECTOR)
            .yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE)
            .monthlyIncome(UPDATED_MONTHLY_INCOME)
            .annualIncome(UPDATED_ANNUAL_INCOME)
            .employmentStartDate(UPDATED_EMPLOYMENT_START_DATE)
            .employerLocation(UPDATED_EMPLOYER_LOCATION)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        professionalProfile = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProfessionalProfile != null) {
            professionalProfileRepository.delete(insertedProfessionalProfile);
            insertedProfessionalProfile = null;
        }
    }

    @Test
    @Transactional
    void createProfessionalProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProfessionalProfile
        ProfessionalProfileDTO professionalProfileDTO = professionalProfileMapper.toDto(professionalProfile);
        var returnedProfessionalProfileDTO = om.readValue(
            restProfessionalProfileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalProfileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfessionalProfileDTO.class
        );

        // Validate the ProfessionalProfile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfessionalProfile = professionalProfileMapper.toEntity(returnedProfessionalProfileDTO);
        assertProfessionalProfileUpdatableFieldsEquals(
            returnedProfessionalProfile,
            getPersistedProfessionalProfile(returnedProfessionalProfile)
        );

        insertedProfessionalProfile = returnedProfessionalProfile;
    }

    @Test
    @Transactional
    void createProfessionalProfileWithExistingId() throws Exception {
        // Create the ProfessionalProfile with an existing ID
        professionalProfile.setId(1L);
        ProfessionalProfileDTO professionalProfileDTO = professionalProfileMapper.toDto(professionalProfile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionalProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProfessionalProfiles() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList
        restProfessionalProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].employmentStatus").value(hasItem(DEFAULT_EMPLOYMENT_STATUS)))
            .andExpect(jsonPath("$.[*].employerName").value(hasItem(DEFAULT_EMPLOYER_NAME)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR)))
            .andExpect(jsonPath("$.[*].yearsOfExperience").value(hasItem(DEFAULT_YEARS_OF_EXPERIENCE)))
            .andExpect(jsonPath("$.[*].monthlyIncome").value(hasItem(sameNumber(DEFAULT_MONTHLY_INCOME))))
            .andExpect(jsonPath("$.[*].annualIncome").value(hasItem(sameNumber(DEFAULT_ANNUAL_INCOME))))
            .andExpect(jsonPath("$.[*].employmentStartDate").value(hasItem(DEFAULT_EMPLOYMENT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].employerLocation").value(hasItem(DEFAULT_EMPLOYER_LOCATION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getProfessionalProfile() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get the professionalProfile
        restProfessionalProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, professionalProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professionalProfile.getId().intValue()))
            .andExpect(jsonPath("$.employmentStatus").value(DEFAULT_EMPLOYMENT_STATUS))
            .andExpect(jsonPath("$.employerName").value(DEFAULT_EMPLOYER_NAME))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION))
            .andExpect(jsonPath("$.sector").value(DEFAULT_SECTOR))
            .andExpect(jsonPath("$.yearsOfExperience").value(DEFAULT_YEARS_OF_EXPERIENCE))
            .andExpect(jsonPath("$.monthlyIncome").value(sameNumber(DEFAULT_MONTHLY_INCOME)))
            .andExpect(jsonPath("$.annualIncome").value(sameNumber(DEFAULT_ANNUAL_INCOME)))
            .andExpect(jsonPath("$.employmentStartDate").value(DEFAULT_EMPLOYMENT_START_DATE.toString()))
            .andExpect(jsonPath("$.employerLocation").value(DEFAULT_EMPLOYER_LOCATION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getProfessionalProfilesByIdFiltering() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        Long id = professionalProfile.getId();

        defaultProfessionalProfileFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProfessionalProfileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProfessionalProfileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmploymentStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employmentStatus equals to
        defaultProfessionalProfileFiltering(
            "employmentStatus.equals=" + DEFAULT_EMPLOYMENT_STATUS,
            "employmentStatus.equals=" + UPDATED_EMPLOYMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmploymentStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employmentStatus in
        defaultProfessionalProfileFiltering(
            "employmentStatus.in=" + DEFAULT_EMPLOYMENT_STATUS + "," + UPDATED_EMPLOYMENT_STATUS,
            "employmentStatus.in=" + UPDATED_EMPLOYMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmploymentStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employmentStatus is not null
        defaultProfessionalProfileFiltering("employmentStatus.specified=true", "employmentStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmploymentStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employmentStatus contains
        defaultProfessionalProfileFiltering(
            "employmentStatus.contains=" + DEFAULT_EMPLOYMENT_STATUS,
            "employmentStatus.contains=" + UPDATED_EMPLOYMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmploymentStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employmentStatus does not contain
        defaultProfessionalProfileFiltering(
            "employmentStatus.doesNotContain=" + UPDATED_EMPLOYMENT_STATUS,
            "employmentStatus.doesNotContain=" + DEFAULT_EMPLOYMENT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmployerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employerName equals to
        defaultProfessionalProfileFiltering("employerName.equals=" + DEFAULT_EMPLOYER_NAME, "employerName.equals=" + UPDATED_EMPLOYER_NAME);
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmployerNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employerName in
        defaultProfessionalProfileFiltering(
            "employerName.in=" + DEFAULT_EMPLOYER_NAME + "," + UPDATED_EMPLOYER_NAME,
            "employerName.in=" + UPDATED_EMPLOYER_NAME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmployerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employerName is not null
        defaultProfessionalProfileFiltering("employerName.specified=true", "employerName.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmployerNameContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employerName contains
        defaultProfessionalProfileFiltering(
            "employerName.contains=" + DEFAULT_EMPLOYER_NAME,
            "employerName.contains=" + UPDATED_EMPLOYER_NAME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmployerNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employerName does not contain
        defaultProfessionalProfileFiltering(
            "employerName.doesNotContain=" + UPDATED_EMPLOYER_NAME,
            "employerName.doesNotContain=" + DEFAULT_EMPLOYER_NAME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByJobTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where jobTitle equals to
        defaultProfessionalProfileFiltering("jobTitle.equals=" + DEFAULT_JOB_TITLE, "jobTitle.equals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByJobTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where jobTitle in
        defaultProfessionalProfileFiltering(
            "jobTitle.in=" + DEFAULT_JOB_TITLE + "," + UPDATED_JOB_TITLE,
            "jobTitle.in=" + UPDATED_JOB_TITLE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByJobTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where jobTitle is not null
        defaultProfessionalProfileFiltering("jobTitle.specified=true", "jobTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByJobTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where jobTitle contains
        defaultProfessionalProfileFiltering("jobTitle.contains=" + DEFAULT_JOB_TITLE, "jobTitle.contains=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByJobTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where jobTitle does not contain
        defaultProfessionalProfileFiltering("jobTitle.doesNotContain=" + UPDATED_JOB_TITLE, "jobTitle.doesNotContain=" + DEFAULT_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByProfessionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where profession equals to
        defaultProfessionalProfileFiltering("profession.equals=" + DEFAULT_PROFESSION, "profession.equals=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByProfessionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where profession in
        defaultProfessionalProfileFiltering(
            "profession.in=" + DEFAULT_PROFESSION + "," + UPDATED_PROFESSION,
            "profession.in=" + UPDATED_PROFESSION
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByProfessionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where profession is not null
        defaultProfessionalProfileFiltering("profession.specified=true", "profession.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByProfessionContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where profession contains
        defaultProfessionalProfileFiltering("profession.contains=" + DEFAULT_PROFESSION, "profession.contains=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByProfessionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where profession does not contain
        defaultProfessionalProfileFiltering(
            "profession.doesNotContain=" + UPDATED_PROFESSION,
            "profession.doesNotContain=" + DEFAULT_PROFESSION
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesBySectorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where sector equals to
        defaultProfessionalProfileFiltering("sector.equals=" + DEFAULT_SECTOR, "sector.equals=" + UPDATED_SECTOR);
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesBySectorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where sector in
        defaultProfessionalProfileFiltering("sector.in=" + DEFAULT_SECTOR + "," + UPDATED_SECTOR, "sector.in=" + UPDATED_SECTOR);
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesBySectorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where sector is not null
        defaultProfessionalProfileFiltering("sector.specified=true", "sector.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesBySectorContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where sector contains
        defaultProfessionalProfileFiltering("sector.contains=" + DEFAULT_SECTOR, "sector.contains=" + UPDATED_SECTOR);
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesBySectorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where sector does not contain
        defaultProfessionalProfileFiltering("sector.doesNotContain=" + UPDATED_SECTOR, "sector.doesNotContain=" + DEFAULT_SECTOR);
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByYearsOfExperienceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where yearsOfExperience equals to
        defaultProfessionalProfileFiltering(
            "yearsOfExperience.equals=" + DEFAULT_YEARS_OF_EXPERIENCE,
            "yearsOfExperience.equals=" + UPDATED_YEARS_OF_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByYearsOfExperienceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where yearsOfExperience in
        defaultProfessionalProfileFiltering(
            "yearsOfExperience.in=" + DEFAULT_YEARS_OF_EXPERIENCE + "," + UPDATED_YEARS_OF_EXPERIENCE,
            "yearsOfExperience.in=" + UPDATED_YEARS_OF_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByYearsOfExperienceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where yearsOfExperience is not null
        defaultProfessionalProfileFiltering("yearsOfExperience.specified=true", "yearsOfExperience.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByYearsOfExperienceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where yearsOfExperience is greater than or equal to
        defaultProfessionalProfileFiltering(
            "yearsOfExperience.greaterThanOrEqual=" + DEFAULT_YEARS_OF_EXPERIENCE,
            "yearsOfExperience.greaterThanOrEqual=" + UPDATED_YEARS_OF_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByYearsOfExperienceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where yearsOfExperience is less than or equal to
        defaultProfessionalProfileFiltering(
            "yearsOfExperience.lessThanOrEqual=" + DEFAULT_YEARS_OF_EXPERIENCE,
            "yearsOfExperience.lessThanOrEqual=" + SMALLER_YEARS_OF_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByYearsOfExperienceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where yearsOfExperience is less than
        defaultProfessionalProfileFiltering(
            "yearsOfExperience.lessThan=" + UPDATED_YEARS_OF_EXPERIENCE,
            "yearsOfExperience.lessThan=" + DEFAULT_YEARS_OF_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByYearsOfExperienceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where yearsOfExperience is greater than
        defaultProfessionalProfileFiltering(
            "yearsOfExperience.greaterThan=" + SMALLER_YEARS_OF_EXPERIENCE,
            "yearsOfExperience.greaterThan=" + DEFAULT_YEARS_OF_EXPERIENCE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByMonthlyIncomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where monthlyIncome equals to
        defaultProfessionalProfileFiltering(
            "monthlyIncome.equals=" + DEFAULT_MONTHLY_INCOME,
            "monthlyIncome.equals=" + UPDATED_MONTHLY_INCOME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByMonthlyIncomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where monthlyIncome in
        defaultProfessionalProfileFiltering(
            "monthlyIncome.in=" + DEFAULT_MONTHLY_INCOME + "," + UPDATED_MONTHLY_INCOME,
            "monthlyIncome.in=" + UPDATED_MONTHLY_INCOME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByMonthlyIncomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where monthlyIncome is not null
        defaultProfessionalProfileFiltering("monthlyIncome.specified=true", "monthlyIncome.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByMonthlyIncomeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where monthlyIncome is greater than or equal to
        defaultProfessionalProfileFiltering(
            "monthlyIncome.greaterThanOrEqual=" + DEFAULT_MONTHLY_INCOME,
            "monthlyIncome.greaterThanOrEqual=" + UPDATED_MONTHLY_INCOME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByMonthlyIncomeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where monthlyIncome is less than or equal to
        defaultProfessionalProfileFiltering(
            "monthlyIncome.lessThanOrEqual=" + DEFAULT_MONTHLY_INCOME,
            "monthlyIncome.lessThanOrEqual=" + SMALLER_MONTHLY_INCOME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByMonthlyIncomeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where monthlyIncome is less than
        defaultProfessionalProfileFiltering(
            "monthlyIncome.lessThan=" + UPDATED_MONTHLY_INCOME,
            "monthlyIncome.lessThan=" + DEFAULT_MONTHLY_INCOME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByMonthlyIncomeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where monthlyIncome is greater than
        defaultProfessionalProfileFiltering(
            "monthlyIncome.greaterThan=" + SMALLER_MONTHLY_INCOME,
            "monthlyIncome.greaterThan=" + DEFAULT_MONTHLY_INCOME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByAnnualIncomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where annualIncome equals to
        defaultProfessionalProfileFiltering("annualIncome.equals=" + DEFAULT_ANNUAL_INCOME, "annualIncome.equals=" + UPDATED_ANNUAL_INCOME);
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByAnnualIncomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where annualIncome in
        defaultProfessionalProfileFiltering(
            "annualIncome.in=" + DEFAULT_ANNUAL_INCOME + "," + UPDATED_ANNUAL_INCOME,
            "annualIncome.in=" + UPDATED_ANNUAL_INCOME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByAnnualIncomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where annualIncome is not null
        defaultProfessionalProfileFiltering("annualIncome.specified=true", "annualIncome.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByAnnualIncomeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where annualIncome is greater than or equal to
        defaultProfessionalProfileFiltering(
            "annualIncome.greaterThanOrEqual=" + DEFAULT_ANNUAL_INCOME,
            "annualIncome.greaterThanOrEqual=" + UPDATED_ANNUAL_INCOME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByAnnualIncomeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where annualIncome is less than or equal to
        defaultProfessionalProfileFiltering(
            "annualIncome.lessThanOrEqual=" + DEFAULT_ANNUAL_INCOME,
            "annualIncome.lessThanOrEqual=" + SMALLER_ANNUAL_INCOME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByAnnualIncomeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where annualIncome is less than
        defaultProfessionalProfileFiltering(
            "annualIncome.lessThan=" + UPDATED_ANNUAL_INCOME,
            "annualIncome.lessThan=" + DEFAULT_ANNUAL_INCOME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByAnnualIncomeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where annualIncome is greater than
        defaultProfessionalProfileFiltering(
            "annualIncome.greaterThan=" + SMALLER_ANNUAL_INCOME,
            "annualIncome.greaterThan=" + DEFAULT_ANNUAL_INCOME
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmploymentStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employmentStartDate equals to
        defaultProfessionalProfileFiltering(
            "employmentStartDate.equals=" + DEFAULT_EMPLOYMENT_START_DATE,
            "employmentStartDate.equals=" + UPDATED_EMPLOYMENT_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmploymentStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employmentStartDate in
        defaultProfessionalProfileFiltering(
            "employmentStartDate.in=" + DEFAULT_EMPLOYMENT_START_DATE + "," + UPDATED_EMPLOYMENT_START_DATE,
            "employmentStartDate.in=" + UPDATED_EMPLOYMENT_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmploymentStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employmentStartDate is not null
        defaultProfessionalProfileFiltering("employmentStartDate.specified=true", "employmentStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmploymentStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employmentStartDate is greater than or equal to
        defaultProfessionalProfileFiltering(
            "employmentStartDate.greaterThanOrEqual=" + DEFAULT_EMPLOYMENT_START_DATE,
            "employmentStartDate.greaterThanOrEqual=" + UPDATED_EMPLOYMENT_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmploymentStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employmentStartDate is less than or equal to
        defaultProfessionalProfileFiltering(
            "employmentStartDate.lessThanOrEqual=" + DEFAULT_EMPLOYMENT_START_DATE,
            "employmentStartDate.lessThanOrEqual=" + SMALLER_EMPLOYMENT_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmploymentStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employmentStartDate is less than
        defaultProfessionalProfileFiltering(
            "employmentStartDate.lessThan=" + UPDATED_EMPLOYMENT_START_DATE,
            "employmentStartDate.lessThan=" + DEFAULT_EMPLOYMENT_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmploymentStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employmentStartDate is greater than
        defaultProfessionalProfileFiltering(
            "employmentStartDate.greaterThan=" + SMALLER_EMPLOYMENT_START_DATE,
            "employmentStartDate.greaterThan=" + DEFAULT_EMPLOYMENT_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmployerLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employerLocation equals to
        defaultProfessionalProfileFiltering(
            "employerLocation.equals=" + DEFAULT_EMPLOYER_LOCATION,
            "employerLocation.equals=" + UPDATED_EMPLOYER_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmployerLocationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employerLocation in
        defaultProfessionalProfileFiltering(
            "employerLocation.in=" + DEFAULT_EMPLOYER_LOCATION + "," + UPDATED_EMPLOYER_LOCATION,
            "employerLocation.in=" + UPDATED_EMPLOYER_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmployerLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employerLocation is not null
        defaultProfessionalProfileFiltering("employerLocation.specified=true", "employerLocation.specified=false");
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmployerLocationContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employerLocation contains
        defaultProfessionalProfileFiltering(
            "employerLocation.contains=" + DEFAULT_EMPLOYER_LOCATION,
            "employerLocation.contains=" + UPDATED_EMPLOYER_LOCATION
        );
    }

    @Test
    @Transactional
    void getAllProfessionalProfilesByEmployerLocationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        // Get all the professionalProfileList where employerLocation does not contain
        defaultProfessionalProfileFiltering(
            "employerLocation.doesNotContain=" + UPDATED_EMPLOYER_LOCATION,
            "employerLocation.doesNotContain=" + DEFAULT_EMPLOYER_LOCATION
        );
    }

    private void defaultProfessionalProfileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProfessionalProfileShouldBeFound(shouldBeFound);
        defaultProfessionalProfileShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProfessionalProfileShouldBeFound(String filter) throws Exception {
        restProfessionalProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professionalProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].employmentStatus").value(hasItem(DEFAULT_EMPLOYMENT_STATUS)))
            .andExpect(jsonPath("$.[*].employerName").value(hasItem(DEFAULT_EMPLOYER_NAME)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR)))
            .andExpect(jsonPath("$.[*].yearsOfExperience").value(hasItem(DEFAULT_YEARS_OF_EXPERIENCE)))
            .andExpect(jsonPath("$.[*].monthlyIncome").value(hasItem(sameNumber(DEFAULT_MONTHLY_INCOME))))
            .andExpect(jsonPath("$.[*].annualIncome").value(hasItem(sameNumber(DEFAULT_ANNUAL_INCOME))))
            .andExpect(jsonPath("$.[*].employmentStartDate").value(hasItem(DEFAULT_EMPLOYMENT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].employerLocation").value(hasItem(DEFAULT_EMPLOYER_LOCATION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restProfessionalProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProfessionalProfileShouldNotBeFound(String filter) throws Exception {
        restProfessionalProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfessionalProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProfessionalProfile() throws Exception {
        // Get the professionalProfile
        restProfessionalProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfessionalProfile() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalProfile
        ProfessionalProfile updatedProfessionalProfile = professionalProfileRepository.findById(professionalProfile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfessionalProfile are not directly saved in db
        em.detach(updatedProfessionalProfile);
        updatedProfessionalProfile
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .employerName(UPDATED_EMPLOYER_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .profession(UPDATED_PROFESSION)
            .sector(UPDATED_SECTOR)
            .yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE)
            .monthlyIncome(UPDATED_MONTHLY_INCOME)
            .annualIncome(UPDATED_ANNUAL_INCOME)
            .employmentStartDate(UPDATED_EMPLOYMENT_START_DATE)
            .employerLocation(UPDATED_EMPLOYER_LOCATION)
            .notes(UPDATED_NOTES);
        ProfessionalProfileDTO professionalProfileDTO = professionalProfileMapper.toDto(updatedProfessionalProfile);

        restProfessionalProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfessionalProfileToMatchAllProperties(updatedProfessionalProfile);
    }

    @Test
    @Transactional
    void putNonExistingProfessionalProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalProfile.setId(longCount.incrementAndGet());

        // Create the ProfessionalProfile
        ProfessionalProfileDTO professionalProfileDTO = professionalProfileMapper.toDto(professionalProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professionalProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfessionalProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalProfile.setId(longCount.incrementAndGet());

        // Create the ProfessionalProfile
        ProfessionalProfileDTO professionalProfileDTO = professionalProfileMapper.toDto(professionalProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(professionalProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfessionalProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalProfile.setId(longCount.incrementAndGet());

        // Create the ProfessionalProfile
        ProfessionalProfileDTO professionalProfileDTO = professionalProfileMapper.toDto(professionalProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(professionalProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfessionalProfileWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalProfile using partial update
        ProfessionalProfile partialUpdatedProfessionalProfile = new ProfessionalProfile();
        partialUpdatedProfessionalProfile.setId(professionalProfile.getId());

        partialUpdatedProfessionalProfile
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .jobTitle(UPDATED_JOB_TITLE)
            .yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE)
            .employmentStartDate(UPDATED_EMPLOYMENT_START_DATE);

        restProfessionalProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalProfile))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalProfileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfessionalProfile, professionalProfile),
            getPersistedProfessionalProfile(professionalProfile)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfessionalProfileWithPatch() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the professionalProfile using partial update
        ProfessionalProfile partialUpdatedProfessionalProfile = new ProfessionalProfile();
        partialUpdatedProfessionalProfile.setId(professionalProfile.getId());

        partialUpdatedProfessionalProfile
            .employmentStatus(UPDATED_EMPLOYMENT_STATUS)
            .employerName(UPDATED_EMPLOYER_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .profession(UPDATED_PROFESSION)
            .sector(UPDATED_SECTOR)
            .yearsOfExperience(UPDATED_YEARS_OF_EXPERIENCE)
            .monthlyIncome(UPDATED_MONTHLY_INCOME)
            .annualIncome(UPDATED_ANNUAL_INCOME)
            .employmentStartDate(UPDATED_EMPLOYMENT_START_DATE)
            .employerLocation(UPDATED_EMPLOYER_LOCATION)
            .notes(UPDATED_NOTES);

        restProfessionalProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfessionalProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfessionalProfile))
            )
            .andExpect(status().isOk());

        // Validate the ProfessionalProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfessionalProfileUpdatableFieldsEquals(
            partialUpdatedProfessionalProfile,
            getPersistedProfessionalProfile(partialUpdatedProfessionalProfile)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProfessionalProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalProfile.setId(longCount.incrementAndGet());

        // Create the ProfessionalProfile
        ProfessionalProfileDTO professionalProfileDTO = professionalProfileMapper.toDto(professionalProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfessionalProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professionalProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfessionalProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalProfile.setId(longCount.incrementAndGet());

        // Create the ProfessionalProfile
        ProfessionalProfileDTO professionalProfileDTO = professionalProfileMapper.toDto(professionalProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(professionalProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProfessionalProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfessionalProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        professionalProfile.setId(longCount.incrementAndGet());

        // Create the ProfessionalProfile
        ProfessionalProfileDTO professionalProfileDTO = professionalProfileMapper.toDto(professionalProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfessionalProfileMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(professionalProfileDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProfessionalProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfessionalProfile() throws Exception {
        // Initialize the database
        insertedProfessionalProfile = professionalProfileRepository.saveAndFlush(professionalProfile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the professionalProfile
        restProfessionalProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, professionalProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return professionalProfileRepository.count();
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

    protected ProfessionalProfile getPersistedProfessionalProfile(ProfessionalProfile professionalProfile) {
        return professionalProfileRepository.findById(professionalProfile.getId()).orElseThrow();
    }

    protected void assertPersistedProfessionalProfileToMatchAllProperties(ProfessionalProfile expectedProfessionalProfile) {
        assertProfessionalProfileAllPropertiesEquals(
            expectedProfessionalProfile,
            getPersistedProfessionalProfile(expectedProfessionalProfile)
        );
    }

    protected void assertPersistedProfessionalProfileToMatchUpdatableProperties(ProfessionalProfile expectedProfessionalProfile) {
        assertProfessionalProfileAllUpdatablePropertiesEquals(
            expectedProfessionalProfile,
            getPersistedProfessionalProfile(expectedProfessionalProfile)
        );
    }
}
