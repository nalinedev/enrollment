package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.SocialProfileAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.SocialProfile;
import com.naline.coopfull.domain.enumeration.MaritalStatus;
import com.naline.coopfull.repository.SocialProfileRepository;
import com.naline.coopfull.service.dto.SocialProfileDTO;
import com.naline.coopfull.service.mapper.SocialProfileMapper;
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
 * Integration tests for the {@link SocialProfileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SocialProfileResourceIT {

    private static final MaritalStatus DEFAULT_MARITAL_STATUS = MaritalStatus.SINGLE;
    private static final MaritalStatus UPDATED_MARITAL_STATUS = MaritalStatus.MARRIED;

    private static final Integer DEFAULT_NUMBER_OF_CHILDREN = 1;
    private static final Integer UPDATED_NUMBER_OF_CHILDREN = 2;
    private static final Integer SMALLER_NUMBER_OF_CHILDREN = 1 - 1;

    private static final Integer DEFAULT_NUMBER_OF_DEPENDENTS = 1;
    private static final Integer UPDATED_NUMBER_OF_DEPENDENTS = 2;
    private static final Integer SMALLER_NUMBER_OF_DEPENDENTS = 1 - 1;

    private static final String DEFAULT_EDUCATION_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_EDUCATION_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_HOUSING_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_HOUSING_STATUS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_RESIDENCE_SINCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RESIDENCE_SINCE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_RESIDENCE_SINCE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_DISABILITY_STATUS = false;
    private static final Boolean UPDATED_DISABILITY_STATUS = true;

    private static final String DEFAULT_DISABILITY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DISABILITY_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SOCIAL_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_SOCIAL_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/social-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SocialProfileRepository socialProfileRepository;

    @Autowired
    private SocialProfileMapper socialProfileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocialProfileMockMvc;

    private SocialProfile socialProfile;

    private SocialProfile insertedSocialProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialProfile createEntity() {
        return new SocialProfile()
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .numberOfChildren(DEFAULT_NUMBER_OF_CHILDREN)
            .numberOfDependents(DEFAULT_NUMBER_OF_DEPENDENTS)
            .educationLevel(DEFAULT_EDUCATION_LEVEL)
            .housingStatus(DEFAULT_HOUSING_STATUS)
            .residenceSince(DEFAULT_RESIDENCE_SINCE)
            .disabilityStatus(DEFAULT_DISABILITY_STATUS)
            .disabilityDescription(DEFAULT_DISABILITY_DESCRIPTION)
            .socialCategory(DEFAULT_SOCIAL_CATEGORY)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialProfile createUpdatedEntity() {
        return new SocialProfile()
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .numberOfDependents(UPDATED_NUMBER_OF_DEPENDENTS)
            .educationLevel(UPDATED_EDUCATION_LEVEL)
            .housingStatus(UPDATED_HOUSING_STATUS)
            .residenceSince(UPDATED_RESIDENCE_SINCE)
            .disabilityStatus(UPDATED_DISABILITY_STATUS)
            .disabilityDescription(UPDATED_DISABILITY_DESCRIPTION)
            .socialCategory(UPDATED_SOCIAL_CATEGORY)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        socialProfile = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSocialProfile != null) {
            socialProfileRepository.delete(insertedSocialProfile);
            insertedSocialProfile = null;
        }
    }

    @Test
    @Transactional
    void createSocialProfile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SocialProfile
        SocialProfileDTO socialProfileDTO = socialProfileMapper.toDto(socialProfile);
        var returnedSocialProfileDTO = om.readValue(
            restSocialProfileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialProfileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SocialProfileDTO.class
        );

        // Validate the SocialProfile in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSocialProfile = socialProfileMapper.toEntity(returnedSocialProfileDTO);
        assertSocialProfileUpdatableFieldsEquals(returnedSocialProfile, getPersistedSocialProfile(returnedSocialProfile));

        insertedSocialProfile = returnedSocialProfile;
    }

    @Test
    @Transactional
    void createSocialProfileWithExistingId() throws Exception {
        // Create the SocialProfile with an existing ID
        socialProfile.setId(1L);
        SocialProfileDTO socialProfileDTO = socialProfileMapper.toDto(socialProfile);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SocialProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSocialProfiles() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList
        restSocialProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].numberOfChildren").value(hasItem(DEFAULT_NUMBER_OF_CHILDREN)))
            .andExpect(jsonPath("$.[*].numberOfDependents").value(hasItem(DEFAULT_NUMBER_OF_DEPENDENTS)))
            .andExpect(jsonPath("$.[*].educationLevel").value(hasItem(DEFAULT_EDUCATION_LEVEL)))
            .andExpect(jsonPath("$.[*].housingStatus").value(hasItem(DEFAULT_HOUSING_STATUS)))
            .andExpect(jsonPath("$.[*].residenceSince").value(hasItem(DEFAULT_RESIDENCE_SINCE.toString())))
            .andExpect(jsonPath("$.[*].disabilityStatus").value(hasItem(DEFAULT_DISABILITY_STATUS)))
            .andExpect(jsonPath("$.[*].disabilityDescription").value(hasItem(DEFAULT_DISABILITY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].socialCategory").value(hasItem(DEFAULT_SOCIAL_CATEGORY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getSocialProfile() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get the socialProfile
        restSocialProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, socialProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(socialProfile.getId().intValue()))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.numberOfChildren").value(DEFAULT_NUMBER_OF_CHILDREN))
            .andExpect(jsonPath("$.numberOfDependents").value(DEFAULT_NUMBER_OF_DEPENDENTS))
            .andExpect(jsonPath("$.educationLevel").value(DEFAULT_EDUCATION_LEVEL))
            .andExpect(jsonPath("$.housingStatus").value(DEFAULT_HOUSING_STATUS))
            .andExpect(jsonPath("$.residenceSince").value(DEFAULT_RESIDENCE_SINCE.toString()))
            .andExpect(jsonPath("$.disabilityStatus").value(DEFAULT_DISABILITY_STATUS))
            .andExpect(jsonPath("$.disabilityDescription").value(DEFAULT_DISABILITY_DESCRIPTION))
            .andExpect(jsonPath("$.socialCategory").value(DEFAULT_SOCIAL_CATEGORY))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getSocialProfilesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        Long id = socialProfile.getId();

        defaultSocialProfileFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSocialProfileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSocialProfileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSocialProfilesByMaritalStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where maritalStatus equals to
        defaultSocialProfileFiltering("maritalStatus.equals=" + DEFAULT_MARITAL_STATUS, "maritalStatus.equals=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllSocialProfilesByMaritalStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where maritalStatus in
        defaultSocialProfileFiltering(
            "maritalStatus.in=" + DEFAULT_MARITAL_STATUS + "," + UPDATED_MARITAL_STATUS,
            "maritalStatus.in=" + UPDATED_MARITAL_STATUS
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByMaritalStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where maritalStatus is not null
        defaultSocialProfileFiltering("maritalStatus.specified=true", "maritalStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfChildren equals to
        defaultSocialProfileFiltering(
            "numberOfChildren.equals=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.equals=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfChildrenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfChildren in
        defaultSocialProfileFiltering(
            "numberOfChildren.in=" + DEFAULT_NUMBER_OF_CHILDREN + "," + UPDATED_NUMBER_OF_CHILDREN,
            "numberOfChildren.in=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfChildrenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfChildren is not null
        defaultSocialProfileFiltering("numberOfChildren.specified=true", "numberOfChildren.specified=false");
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfChildrenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfChildren is greater than or equal to
        defaultSocialProfileFiltering(
            "numberOfChildren.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.greaterThanOrEqual=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfChildrenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfChildren is less than or equal to
        defaultSocialProfileFiltering(
            "numberOfChildren.lessThanOrEqual=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.lessThanOrEqual=" + SMALLER_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfChildrenIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfChildren is less than
        defaultSocialProfileFiltering(
            "numberOfChildren.lessThan=" + UPDATED_NUMBER_OF_CHILDREN,
            "numberOfChildren.lessThan=" + DEFAULT_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfChildrenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfChildren is greater than
        defaultSocialProfileFiltering(
            "numberOfChildren.greaterThan=" + SMALLER_NUMBER_OF_CHILDREN,
            "numberOfChildren.greaterThan=" + DEFAULT_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfDependentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfDependents equals to
        defaultSocialProfileFiltering(
            "numberOfDependents.equals=" + DEFAULT_NUMBER_OF_DEPENDENTS,
            "numberOfDependents.equals=" + UPDATED_NUMBER_OF_DEPENDENTS
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfDependentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfDependents in
        defaultSocialProfileFiltering(
            "numberOfDependents.in=" + DEFAULT_NUMBER_OF_DEPENDENTS + "," + UPDATED_NUMBER_OF_DEPENDENTS,
            "numberOfDependents.in=" + UPDATED_NUMBER_OF_DEPENDENTS
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfDependentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfDependents is not null
        defaultSocialProfileFiltering("numberOfDependents.specified=true", "numberOfDependents.specified=false");
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfDependentsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfDependents is greater than or equal to
        defaultSocialProfileFiltering(
            "numberOfDependents.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_DEPENDENTS,
            "numberOfDependents.greaterThanOrEqual=" + UPDATED_NUMBER_OF_DEPENDENTS
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfDependentsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfDependents is less than or equal to
        defaultSocialProfileFiltering(
            "numberOfDependents.lessThanOrEqual=" + DEFAULT_NUMBER_OF_DEPENDENTS,
            "numberOfDependents.lessThanOrEqual=" + SMALLER_NUMBER_OF_DEPENDENTS
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfDependentsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfDependents is less than
        defaultSocialProfileFiltering(
            "numberOfDependents.lessThan=" + UPDATED_NUMBER_OF_DEPENDENTS,
            "numberOfDependents.lessThan=" + DEFAULT_NUMBER_OF_DEPENDENTS
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByNumberOfDependentsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where numberOfDependents is greater than
        defaultSocialProfileFiltering(
            "numberOfDependents.greaterThan=" + SMALLER_NUMBER_OF_DEPENDENTS,
            "numberOfDependents.greaterThan=" + DEFAULT_NUMBER_OF_DEPENDENTS
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByEducationLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where educationLevel equals to
        defaultSocialProfileFiltering(
            "educationLevel.equals=" + DEFAULT_EDUCATION_LEVEL,
            "educationLevel.equals=" + UPDATED_EDUCATION_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByEducationLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where educationLevel in
        defaultSocialProfileFiltering(
            "educationLevel.in=" + DEFAULT_EDUCATION_LEVEL + "," + UPDATED_EDUCATION_LEVEL,
            "educationLevel.in=" + UPDATED_EDUCATION_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByEducationLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where educationLevel is not null
        defaultSocialProfileFiltering("educationLevel.specified=true", "educationLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllSocialProfilesByEducationLevelContainsSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where educationLevel contains
        defaultSocialProfileFiltering(
            "educationLevel.contains=" + DEFAULT_EDUCATION_LEVEL,
            "educationLevel.contains=" + UPDATED_EDUCATION_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByEducationLevelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where educationLevel does not contain
        defaultSocialProfileFiltering(
            "educationLevel.doesNotContain=" + UPDATED_EDUCATION_LEVEL,
            "educationLevel.doesNotContain=" + DEFAULT_EDUCATION_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByHousingStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where housingStatus equals to
        defaultSocialProfileFiltering("housingStatus.equals=" + DEFAULT_HOUSING_STATUS, "housingStatus.equals=" + UPDATED_HOUSING_STATUS);
    }

    @Test
    @Transactional
    void getAllSocialProfilesByHousingStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where housingStatus in
        defaultSocialProfileFiltering(
            "housingStatus.in=" + DEFAULT_HOUSING_STATUS + "," + UPDATED_HOUSING_STATUS,
            "housingStatus.in=" + UPDATED_HOUSING_STATUS
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByHousingStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where housingStatus is not null
        defaultSocialProfileFiltering("housingStatus.specified=true", "housingStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllSocialProfilesByHousingStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where housingStatus contains
        defaultSocialProfileFiltering(
            "housingStatus.contains=" + DEFAULT_HOUSING_STATUS,
            "housingStatus.contains=" + UPDATED_HOUSING_STATUS
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByHousingStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where housingStatus does not contain
        defaultSocialProfileFiltering(
            "housingStatus.doesNotContain=" + UPDATED_HOUSING_STATUS,
            "housingStatus.doesNotContain=" + DEFAULT_HOUSING_STATUS
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByResidenceSinceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where residenceSince equals to
        defaultSocialProfileFiltering(
            "residenceSince.equals=" + DEFAULT_RESIDENCE_SINCE,
            "residenceSince.equals=" + UPDATED_RESIDENCE_SINCE
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByResidenceSinceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where residenceSince in
        defaultSocialProfileFiltering(
            "residenceSince.in=" + DEFAULT_RESIDENCE_SINCE + "," + UPDATED_RESIDENCE_SINCE,
            "residenceSince.in=" + UPDATED_RESIDENCE_SINCE
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByResidenceSinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where residenceSince is not null
        defaultSocialProfileFiltering("residenceSince.specified=true", "residenceSince.specified=false");
    }

    @Test
    @Transactional
    void getAllSocialProfilesByResidenceSinceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where residenceSince is greater than or equal to
        defaultSocialProfileFiltering(
            "residenceSince.greaterThanOrEqual=" + DEFAULT_RESIDENCE_SINCE,
            "residenceSince.greaterThanOrEqual=" + UPDATED_RESIDENCE_SINCE
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByResidenceSinceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where residenceSince is less than or equal to
        defaultSocialProfileFiltering(
            "residenceSince.lessThanOrEqual=" + DEFAULT_RESIDENCE_SINCE,
            "residenceSince.lessThanOrEqual=" + SMALLER_RESIDENCE_SINCE
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByResidenceSinceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where residenceSince is less than
        defaultSocialProfileFiltering(
            "residenceSince.lessThan=" + UPDATED_RESIDENCE_SINCE,
            "residenceSince.lessThan=" + DEFAULT_RESIDENCE_SINCE
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByResidenceSinceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where residenceSince is greater than
        defaultSocialProfileFiltering(
            "residenceSince.greaterThan=" + SMALLER_RESIDENCE_SINCE,
            "residenceSince.greaterThan=" + DEFAULT_RESIDENCE_SINCE
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByDisabilityStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where disabilityStatus equals to
        defaultSocialProfileFiltering(
            "disabilityStatus.equals=" + DEFAULT_DISABILITY_STATUS,
            "disabilityStatus.equals=" + UPDATED_DISABILITY_STATUS
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByDisabilityStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where disabilityStatus in
        defaultSocialProfileFiltering(
            "disabilityStatus.in=" + DEFAULT_DISABILITY_STATUS + "," + UPDATED_DISABILITY_STATUS,
            "disabilityStatus.in=" + UPDATED_DISABILITY_STATUS
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesByDisabilityStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where disabilityStatus is not null
        defaultSocialProfileFiltering("disabilityStatus.specified=true", "disabilityStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllSocialProfilesBySocialCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where socialCategory equals to
        defaultSocialProfileFiltering(
            "socialCategory.equals=" + DEFAULT_SOCIAL_CATEGORY,
            "socialCategory.equals=" + UPDATED_SOCIAL_CATEGORY
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesBySocialCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where socialCategory in
        defaultSocialProfileFiltering(
            "socialCategory.in=" + DEFAULT_SOCIAL_CATEGORY + "," + UPDATED_SOCIAL_CATEGORY,
            "socialCategory.in=" + UPDATED_SOCIAL_CATEGORY
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesBySocialCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where socialCategory is not null
        defaultSocialProfileFiltering("socialCategory.specified=true", "socialCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllSocialProfilesBySocialCategoryContainsSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where socialCategory contains
        defaultSocialProfileFiltering(
            "socialCategory.contains=" + DEFAULT_SOCIAL_CATEGORY,
            "socialCategory.contains=" + UPDATED_SOCIAL_CATEGORY
        );
    }

    @Test
    @Transactional
    void getAllSocialProfilesBySocialCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        // Get all the socialProfileList where socialCategory does not contain
        defaultSocialProfileFiltering(
            "socialCategory.doesNotContain=" + UPDATED_SOCIAL_CATEGORY,
            "socialCategory.doesNotContain=" + DEFAULT_SOCIAL_CATEGORY
        );
    }

    private void defaultSocialProfileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSocialProfileShouldBeFound(shouldBeFound);
        defaultSocialProfileShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSocialProfileShouldBeFound(String filter) throws Exception {
        restSocialProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].numberOfChildren").value(hasItem(DEFAULT_NUMBER_OF_CHILDREN)))
            .andExpect(jsonPath("$.[*].numberOfDependents").value(hasItem(DEFAULT_NUMBER_OF_DEPENDENTS)))
            .andExpect(jsonPath("$.[*].educationLevel").value(hasItem(DEFAULT_EDUCATION_LEVEL)))
            .andExpect(jsonPath("$.[*].housingStatus").value(hasItem(DEFAULT_HOUSING_STATUS)))
            .andExpect(jsonPath("$.[*].residenceSince").value(hasItem(DEFAULT_RESIDENCE_SINCE.toString())))
            .andExpect(jsonPath("$.[*].disabilityStatus").value(hasItem(DEFAULT_DISABILITY_STATUS)))
            .andExpect(jsonPath("$.[*].disabilityDescription").value(hasItem(DEFAULT_DISABILITY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].socialCategory").value(hasItem(DEFAULT_SOCIAL_CATEGORY)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restSocialProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSocialProfileShouldNotBeFound(String filter) throws Exception {
        restSocialProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSocialProfileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSocialProfile() throws Exception {
        // Get the socialProfile
        restSocialProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSocialProfile() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the socialProfile
        SocialProfile updatedSocialProfile = socialProfileRepository.findById(socialProfile.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSocialProfile are not directly saved in db
        em.detach(updatedSocialProfile);
        updatedSocialProfile
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .numberOfDependents(UPDATED_NUMBER_OF_DEPENDENTS)
            .educationLevel(UPDATED_EDUCATION_LEVEL)
            .housingStatus(UPDATED_HOUSING_STATUS)
            .residenceSince(UPDATED_RESIDENCE_SINCE)
            .disabilityStatus(UPDATED_DISABILITY_STATUS)
            .disabilityDescription(UPDATED_DISABILITY_DESCRIPTION)
            .socialCategory(UPDATED_SOCIAL_CATEGORY)
            .notes(UPDATED_NOTES);
        SocialProfileDTO socialProfileDTO = socialProfileMapper.toDto(updatedSocialProfile);

        restSocialProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(socialProfileDTO))
            )
            .andExpect(status().isOk());

        // Validate the SocialProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSocialProfileToMatchAllProperties(updatedSocialProfile);
    }

    @Test
    @Transactional
    void putNonExistingSocialProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialProfile.setId(longCount.incrementAndGet());

        // Create the SocialProfile
        SocialProfileDTO socialProfileDTO = socialProfileMapper.toDto(socialProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialProfileDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(socialProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSocialProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialProfile.setId(longCount.incrementAndGet());

        // Create the SocialProfile
        SocialProfileDTO socialProfileDTO = socialProfileMapper.toDto(socialProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(socialProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSocialProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialProfile.setId(longCount.incrementAndGet());

        // Create the SocialProfile
        SocialProfileDTO socialProfileDTO = socialProfileMapper.toDto(socialProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(socialProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSocialProfileWithPatch() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the socialProfile using partial update
        SocialProfile partialUpdatedSocialProfile = new SocialProfile();
        partialUpdatedSocialProfile.setId(socialProfile.getId());

        partialUpdatedSocialProfile
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .educationLevel(UPDATED_EDUCATION_LEVEL)
            .housingStatus(UPDATED_HOUSING_STATUS)
            .residenceSince(UPDATED_RESIDENCE_SINCE)
            .disabilityStatus(UPDATED_DISABILITY_STATUS)
            .notes(UPDATED_NOTES);

        restSocialProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSocialProfile))
            )
            .andExpect(status().isOk());

        // Validate the SocialProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSocialProfileUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSocialProfile, socialProfile),
            getPersistedSocialProfile(socialProfile)
        );
    }

    @Test
    @Transactional
    void fullUpdateSocialProfileWithPatch() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the socialProfile using partial update
        SocialProfile partialUpdatedSocialProfile = new SocialProfile();
        partialUpdatedSocialProfile.setId(socialProfile.getId());

        partialUpdatedSocialProfile
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .numberOfDependents(UPDATED_NUMBER_OF_DEPENDENTS)
            .educationLevel(UPDATED_EDUCATION_LEVEL)
            .housingStatus(UPDATED_HOUSING_STATUS)
            .residenceSince(UPDATED_RESIDENCE_SINCE)
            .disabilityStatus(UPDATED_DISABILITY_STATUS)
            .disabilityDescription(UPDATED_DISABILITY_DESCRIPTION)
            .socialCategory(UPDATED_SOCIAL_CATEGORY)
            .notes(UPDATED_NOTES);

        restSocialProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSocialProfile))
            )
            .andExpect(status().isOk());

        // Validate the SocialProfile in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSocialProfileUpdatableFieldsEquals(partialUpdatedSocialProfile, getPersistedSocialProfile(partialUpdatedSocialProfile));
    }

    @Test
    @Transactional
    void patchNonExistingSocialProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialProfile.setId(longCount.incrementAndGet());

        // Create the SocialProfile
        SocialProfileDTO socialProfileDTO = socialProfileMapper.toDto(socialProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, socialProfileDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(socialProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSocialProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialProfile.setId(longCount.incrementAndGet());

        // Create the SocialProfile
        SocialProfileDTO socialProfileDTO = socialProfileMapper.toDto(socialProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(socialProfileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSocialProfile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        socialProfile.setId(longCount.incrementAndGet());

        // Create the SocialProfile
        SocialProfileDTO socialProfileDTO = socialProfileMapper.toDto(socialProfile);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialProfileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(socialProfileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialProfile in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSocialProfile() throws Exception {
        // Initialize the database
        insertedSocialProfile = socialProfileRepository.saveAndFlush(socialProfile);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the socialProfile
        restSocialProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, socialProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return socialProfileRepository.count();
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

    protected SocialProfile getPersistedSocialProfile(SocialProfile socialProfile) {
        return socialProfileRepository.findById(socialProfile.getId()).orElseThrow();
    }

    protected void assertPersistedSocialProfileToMatchAllProperties(SocialProfile expectedSocialProfile) {
        assertSocialProfileAllPropertiesEquals(expectedSocialProfile, getPersistedSocialProfile(expectedSocialProfile));
    }

    protected void assertPersistedSocialProfileToMatchUpdatableProperties(SocialProfile expectedSocialProfile) {
        assertSocialProfileAllUpdatablePropertiesEquals(expectedSocialProfile, getPersistedSocialProfile(expectedSocialProfile));
    }
}
