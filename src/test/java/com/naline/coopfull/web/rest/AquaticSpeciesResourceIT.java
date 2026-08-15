package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.AquaticSpeciesAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.AquaticSpecies;
import com.naline.coopfull.repository.AquaticSpeciesRepository;
import com.naline.coopfull.service.dto.AquaticSpeciesDTO;
import com.naline.coopfull.service.mapper.AquaticSpeciesMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AquaticSpeciesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AquaticSpeciesResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SCIENTIFIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SCIENTIFIC_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FRESHWATER = false;
    private static final Boolean UPDATED_FRESHWATER = true;

    private static final Boolean DEFAULT_SALTWATER = false;
    private static final Boolean UPDATED_SALTWATER = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/aquatic-species";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AquaticSpeciesRepository aquaticSpeciesRepository;

    @Autowired
    private AquaticSpeciesMapper aquaticSpeciesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAquaticSpeciesMockMvc;

    private AquaticSpecies aquaticSpecies;

    private AquaticSpecies insertedAquaticSpecies;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AquaticSpecies createEntity() {
        return new AquaticSpecies()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .scientificName(DEFAULT_SCIENTIFIC_NAME)
            .category(DEFAULT_CATEGORY)
            .description(DEFAULT_DESCRIPTION)
            .freshwater(DEFAULT_FRESHWATER)
            .saltwater(DEFAULT_SALTWATER)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AquaticSpecies createUpdatedEntity() {
        return new AquaticSpecies()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .freshwater(UPDATED_FRESHWATER)
            .saltwater(UPDATED_SALTWATER)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        aquaticSpecies = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAquaticSpecies != null) {
            aquaticSpeciesRepository.delete(insertedAquaticSpecies);
            insertedAquaticSpecies = null;
        }
    }

    @Test
    @Transactional
    void createAquaticSpecies() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AquaticSpecies
        AquaticSpeciesDTO aquaticSpeciesDTO = aquaticSpeciesMapper.toDto(aquaticSpecies);
        var returnedAquaticSpeciesDTO = om.readValue(
            restAquaticSpeciesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquaticSpeciesDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AquaticSpeciesDTO.class
        );

        // Validate the AquaticSpecies in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAquaticSpecies = aquaticSpeciesMapper.toEntity(returnedAquaticSpeciesDTO);
        assertAquaticSpeciesUpdatableFieldsEquals(returnedAquaticSpecies, getPersistedAquaticSpecies(returnedAquaticSpecies));

        insertedAquaticSpecies = returnedAquaticSpecies;
    }

    @Test
    @Transactional
    void createAquaticSpeciesWithExistingId() throws Exception {
        // Create the AquaticSpecies with an existing ID
        aquaticSpecies.setId(1L);
        AquaticSpeciesDTO aquaticSpeciesDTO = aquaticSpeciesMapper.toDto(aquaticSpecies);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAquaticSpeciesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquaticSpeciesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AquaticSpecies in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aquaticSpecies.setCode(null);

        // Create the AquaticSpecies, which fails.
        AquaticSpeciesDTO aquaticSpeciesDTO = aquaticSpeciesMapper.toDto(aquaticSpecies);

        restAquaticSpeciesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquaticSpeciesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aquaticSpecies.setName(null);

        // Create the AquaticSpecies, which fails.
        AquaticSpeciesDTO aquaticSpeciesDTO = aquaticSpeciesMapper.toDto(aquaticSpecies);

        restAquaticSpeciesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquaticSpeciesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aquaticSpecies.setActive(null);

        // Create the AquaticSpecies, which fails.
        AquaticSpeciesDTO aquaticSpeciesDTO = aquaticSpeciesMapper.toDto(aquaticSpecies);

        restAquaticSpeciesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquaticSpeciesDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAquaticSpecieses() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList
        restAquaticSpeciesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aquaticSpecies.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].scientificName").value(hasItem(DEFAULT_SCIENTIFIC_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].freshwater").value(hasItem(DEFAULT_FRESHWATER)))
            .andExpect(jsonPath("$.[*].saltwater").value(hasItem(DEFAULT_SALTWATER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getAquaticSpecies() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get the aquaticSpecies
        restAquaticSpeciesMockMvc
            .perform(get(ENTITY_API_URL_ID, aquaticSpecies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aquaticSpecies.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.scientificName").value(DEFAULT_SCIENTIFIC_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.freshwater").value(DEFAULT_FRESHWATER))
            .andExpect(jsonPath("$.saltwater").value(DEFAULT_SALTWATER))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getAquaticSpeciesesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        Long id = aquaticSpecies.getId();

        defaultAquaticSpeciesFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAquaticSpeciesFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAquaticSpeciesFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where code equals to
        defaultAquaticSpeciesFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where code in
        defaultAquaticSpeciesFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where code is not null
        defaultAquaticSpeciesFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where code contains
        defaultAquaticSpeciesFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where code does not contain
        defaultAquaticSpeciesFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where name equals to
        defaultAquaticSpeciesFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where name in
        defaultAquaticSpeciesFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where name is not null
        defaultAquaticSpeciesFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where name contains
        defaultAquaticSpeciesFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where name does not contain
        defaultAquaticSpeciesFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByScientificNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where scientificName equals to
        defaultAquaticSpeciesFiltering(
            "scientificName.equals=" + DEFAULT_SCIENTIFIC_NAME,
            "scientificName.equals=" + UPDATED_SCIENTIFIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByScientificNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where scientificName in
        defaultAquaticSpeciesFiltering(
            "scientificName.in=" + DEFAULT_SCIENTIFIC_NAME + "," + UPDATED_SCIENTIFIC_NAME,
            "scientificName.in=" + UPDATED_SCIENTIFIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByScientificNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where scientificName is not null
        defaultAquaticSpeciesFiltering("scientificName.specified=true", "scientificName.specified=false");
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByScientificNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where scientificName contains
        defaultAquaticSpeciesFiltering(
            "scientificName.contains=" + DEFAULT_SCIENTIFIC_NAME,
            "scientificName.contains=" + UPDATED_SCIENTIFIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByScientificNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where scientificName does not contain
        defaultAquaticSpeciesFiltering(
            "scientificName.doesNotContain=" + UPDATED_SCIENTIFIC_NAME,
            "scientificName.doesNotContain=" + DEFAULT_SCIENTIFIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where category equals to
        defaultAquaticSpeciesFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where category in
        defaultAquaticSpeciesFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where category is not null
        defaultAquaticSpeciesFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByCategoryContainsSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where category contains
        defaultAquaticSpeciesFiltering("category.contains=" + DEFAULT_CATEGORY, "category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where category does not contain
        defaultAquaticSpeciesFiltering("category.doesNotContain=" + UPDATED_CATEGORY, "category.doesNotContain=" + DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByFreshwaterIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where freshwater equals to
        defaultAquaticSpeciesFiltering("freshwater.equals=" + DEFAULT_FRESHWATER, "freshwater.equals=" + UPDATED_FRESHWATER);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByFreshwaterIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where freshwater in
        defaultAquaticSpeciesFiltering(
            "freshwater.in=" + DEFAULT_FRESHWATER + "," + UPDATED_FRESHWATER,
            "freshwater.in=" + UPDATED_FRESHWATER
        );
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByFreshwaterIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where freshwater is not null
        defaultAquaticSpeciesFiltering("freshwater.specified=true", "freshwater.specified=false");
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesBySaltwaterIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where saltwater equals to
        defaultAquaticSpeciesFiltering("saltwater.equals=" + DEFAULT_SALTWATER, "saltwater.equals=" + UPDATED_SALTWATER);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesBySaltwaterIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where saltwater in
        defaultAquaticSpeciesFiltering("saltwater.in=" + DEFAULT_SALTWATER + "," + UPDATED_SALTWATER, "saltwater.in=" + UPDATED_SALTWATER);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesBySaltwaterIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where saltwater is not null
        defaultAquaticSpeciesFiltering("saltwater.specified=true", "saltwater.specified=false");
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where active equals to
        defaultAquaticSpeciesFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where active in
        defaultAquaticSpeciesFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAquaticSpeciesesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        // Get all the aquaticSpeciesList where active is not null
        defaultAquaticSpeciesFiltering("active.specified=true", "active.specified=false");
    }

    private void defaultAquaticSpeciesFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAquaticSpeciesShouldBeFound(shouldBeFound);
        defaultAquaticSpeciesShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAquaticSpeciesShouldBeFound(String filter) throws Exception {
        restAquaticSpeciesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aquaticSpecies.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].scientificName").value(hasItem(DEFAULT_SCIENTIFIC_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].freshwater").value(hasItem(DEFAULT_FRESHWATER)))
            .andExpect(jsonPath("$.[*].saltwater").value(hasItem(DEFAULT_SALTWATER)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restAquaticSpeciesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAquaticSpeciesShouldNotBeFound(String filter) throws Exception {
        restAquaticSpeciesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAquaticSpeciesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAquaticSpecies() throws Exception {
        // Get the aquaticSpecies
        restAquaticSpeciesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAquaticSpecies() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aquaticSpecies
        AquaticSpecies updatedAquaticSpecies = aquaticSpeciesRepository.findById(aquaticSpecies.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAquaticSpecies are not directly saved in db
        em.detach(updatedAquaticSpecies);
        updatedAquaticSpecies
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .freshwater(UPDATED_FRESHWATER)
            .saltwater(UPDATED_SALTWATER)
            .active(UPDATED_ACTIVE);
        AquaticSpeciesDTO aquaticSpeciesDTO = aquaticSpeciesMapper.toDto(updatedAquaticSpecies);

        restAquaticSpeciesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aquaticSpeciesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aquaticSpeciesDTO))
            )
            .andExpect(status().isOk());

        // Validate the AquaticSpecies in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAquaticSpeciesToMatchAllProperties(updatedAquaticSpecies);
    }

    @Test
    @Transactional
    void putNonExistingAquaticSpecies() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquaticSpecies.setId(longCount.incrementAndGet());

        // Create the AquaticSpecies
        AquaticSpeciesDTO aquaticSpeciesDTO = aquaticSpeciesMapper.toDto(aquaticSpecies);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAquaticSpeciesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aquaticSpeciesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aquaticSpeciesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AquaticSpecies in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAquaticSpecies() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquaticSpecies.setId(longCount.incrementAndGet());

        // Create the AquaticSpecies
        AquaticSpeciesDTO aquaticSpeciesDTO = aquaticSpeciesMapper.toDto(aquaticSpecies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAquaticSpeciesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aquaticSpeciesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AquaticSpecies in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAquaticSpecies() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquaticSpecies.setId(longCount.incrementAndGet());

        // Create the AquaticSpecies
        AquaticSpeciesDTO aquaticSpeciesDTO = aquaticSpeciesMapper.toDto(aquaticSpecies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAquaticSpeciesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquaticSpeciesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AquaticSpecies in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAquaticSpeciesWithPatch() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aquaticSpecies using partial update
        AquaticSpecies partialUpdatedAquaticSpecies = new AquaticSpecies();
        partialUpdatedAquaticSpecies.setId(aquaticSpecies.getId());

        partialUpdatedAquaticSpecies
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .category(UPDATED_CATEGORY)
            .freshwater(UPDATED_FRESHWATER)
            .active(UPDATED_ACTIVE);

        restAquaticSpeciesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAquaticSpecies.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAquaticSpecies))
            )
            .andExpect(status().isOk());

        // Validate the AquaticSpecies in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAquaticSpeciesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAquaticSpecies, aquaticSpecies),
            getPersistedAquaticSpecies(aquaticSpecies)
        );
    }

    @Test
    @Transactional
    void fullUpdateAquaticSpeciesWithPatch() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aquaticSpecies using partial update
        AquaticSpecies partialUpdatedAquaticSpecies = new AquaticSpecies();
        partialUpdatedAquaticSpecies.setId(aquaticSpecies.getId());

        partialUpdatedAquaticSpecies
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .freshwater(UPDATED_FRESHWATER)
            .saltwater(UPDATED_SALTWATER)
            .active(UPDATED_ACTIVE);

        restAquaticSpeciesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAquaticSpecies.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAquaticSpecies))
            )
            .andExpect(status().isOk());

        // Validate the AquaticSpecies in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAquaticSpeciesUpdatableFieldsEquals(partialUpdatedAquaticSpecies, getPersistedAquaticSpecies(partialUpdatedAquaticSpecies));
    }

    @Test
    @Transactional
    void patchNonExistingAquaticSpecies() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquaticSpecies.setId(longCount.incrementAndGet());

        // Create the AquaticSpecies
        AquaticSpeciesDTO aquaticSpeciesDTO = aquaticSpeciesMapper.toDto(aquaticSpecies);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAquaticSpeciesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aquaticSpeciesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aquaticSpeciesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AquaticSpecies in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAquaticSpecies() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquaticSpecies.setId(longCount.incrementAndGet());

        // Create the AquaticSpecies
        AquaticSpeciesDTO aquaticSpeciesDTO = aquaticSpeciesMapper.toDto(aquaticSpecies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAquaticSpeciesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aquaticSpeciesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AquaticSpecies in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAquaticSpecies() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquaticSpecies.setId(longCount.incrementAndGet());

        // Create the AquaticSpecies
        AquaticSpeciesDTO aquaticSpeciesDTO = aquaticSpeciesMapper.toDto(aquaticSpecies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAquaticSpeciesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aquaticSpeciesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AquaticSpecies in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAquaticSpecies() throws Exception {
        // Initialize the database
        insertedAquaticSpecies = aquaticSpeciesRepository.saveAndFlush(aquaticSpecies);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aquaticSpecies
        restAquaticSpeciesMockMvc
            .perform(delete(ENTITY_API_URL_ID, aquaticSpecies.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aquaticSpeciesRepository.count();
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

    protected AquaticSpecies getPersistedAquaticSpecies(AquaticSpecies aquaticSpecies) {
        return aquaticSpeciesRepository.findById(aquaticSpecies.getId()).orElseThrow();
    }

    protected void assertPersistedAquaticSpeciesToMatchAllProperties(AquaticSpecies expectedAquaticSpecies) {
        assertAquaticSpeciesAllPropertiesEquals(expectedAquaticSpecies, getPersistedAquaticSpecies(expectedAquaticSpecies));
    }

    protected void assertPersistedAquaticSpeciesToMatchUpdatableProperties(AquaticSpecies expectedAquaticSpecies) {
        assertAquaticSpeciesAllUpdatablePropertiesEquals(expectedAquaticSpecies, getPersistedAquaticSpecies(expectedAquaticSpecies));
    }
}
