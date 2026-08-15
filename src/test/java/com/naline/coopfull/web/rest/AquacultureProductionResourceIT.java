package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.AquacultureProductionAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static com.naline.coopfull.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.AquacultureActivity;
import com.naline.coopfull.domain.AquacultureProduction;
import com.naline.coopfull.domain.enumeration.AquacultureProductionStatus;
import com.naline.coopfull.repository.AquacultureProductionRepository;
import com.naline.coopfull.service.dto.AquacultureProductionDTO;
import com.naline.coopfull.service.mapper.AquacultureProductionMapper;
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
 * Integration tests for the {@link AquacultureProductionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AquacultureProductionResourceIT {

    private static final LocalDate DEFAULT_PRODUCTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRODUCTION_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_PRODUCTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_NUMBER_OF_ANIMALS = 1;
    private static final Integer UPDATED_NUMBER_OF_ANIMALS = 2;
    private static final Integer SMALLER_NUMBER_OF_ANIMALS = 1 - 1;

    private static final BigDecimal DEFAULT_STOCKING_DENSITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_STOCKING_DENSITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_STOCKING_DENSITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PRODUCTION_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRODUCTION_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRODUCTION_QUANTITY = new BigDecimal(1 - 1);

    private static final String DEFAULT_PRODUCTION_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCTION_UNIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AVERAGE_WEIGHT_GRAMS = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVERAGE_WEIGHT_GRAMS = new BigDecimal(2);
    private static final BigDecimal SMALLER_AVERAGE_WEIGHT_GRAMS = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_MORTALITY_COUNT = 1;
    private static final Integer UPDATED_MORTALITY_COUNT = 2;
    private static final Integer SMALLER_MORTALITY_COUNT = 1 - 1;

    private static final Integer DEFAULT_STOCKING_COUNT = 1;
    private static final Integer UPDATED_STOCKING_COUNT = 2;
    private static final Integer SMALLER_STOCKING_COUNT = 1 - 1;

    private static final Integer DEFAULT_HARVESTED_COUNT = 1;
    private static final Integer UPDATED_HARVESTED_COUNT = 2;
    private static final Integer SMALLER_HARVESTED_COUNT = 1 - 1;

    private static final BigDecimal DEFAULT_EXPECTED_PRODUCTION = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXPECTED_PRODUCTION = new BigDecimal(2);
    private static final BigDecimal SMALLER_EXPECTED_PRODUCTION = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_EXPECTED_HARVEST_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPECTED_HARVEST_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_EXPECTED_HARVEST_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_ACTUAL_HARVEST_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTUAL_HARVEST_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_ACTUAL_HARVEST_DATE = LocalDate.ofEpochDay(-1L);

    private static final AquacultureProductionStatus DEFAULT_STATUS = AquacultureProductionStatus.PLANNED;
    private static final AquacultureProductionStatus UPDATED_STATUS = AquacultureProductionStatus.ACTIVE;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/aquaculture-productions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AquacultureProductionRepository aquacultureProductionRepository;

    @Autowired
    private AquacultureProductionMapper aquacultureProductionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAquacultureProductionMockMvc;

    private AquacultureProduction aquacultureProduction;

    private AquacultureProduction insertedAquacultureProduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AquacultureProduction createEntity() {
        return new AquacultureProduction()
            .productionDate(DEFAULT_PRODUCTION_DATE)
            .numberOfAnimals(DEFAULT_NUMBER_OF_ANIMALS)
            .stockingDensity(DEFAULT_STOCKING_DENSITY)
            .productionQuantity(DEFAULT_PRODUCTION_QUANTITY)
            .productionUnit(DEFAULT_PRODUCTION_UNIT)
            .averageWeightGrams(DEFAULT_AVERAGE_WEIGHT_GRAMS)
            .mortalityCount(DEFAULT_MORTALITY_COUNT)
            .stockingCount(DEFAULT_STOCKING_COUNT)
            .harvestedCount(DEFAULT_HARVESTED_COUNT)
            .expectedProduction(DEFAULT_EXPECTED_PRODUCTION)
            .expectedHarvestDate(DEFAULT_EXPECTED_HARVEST_DATE)
            .actualHarvestDate(DEFAULT_ACTUAL_HARVEST_DATE)
            .status(DEFAULT_STATUS)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AquacultureProduction createUpdatedEntity() {
        return new AquacultureProduction()
            .productionDate(UPDATED_PRODUCTION_DATE)
            .numberOfAnimals(UPDATED_NUMBER_OF_ANIMALS)
            .stockingDensity(UPDATED_STOCKING_DENSITY)
            .productionQuantity(UPDATED_PRODUCTION_QUANTITY)
            .productionUnit(UPDATED_PRODUCTION_UNIT)
            .averageWeightGrams(UPDATED_AVERAGE_WEIGHT_GRAMS)
            .mortalityCount(UPDATED_MORTALITY_COUNT)
            .stockingCount(UPDATED_STOCKING_COUNT)
            .harvestedCount(UPDATED_HARVESTED_COUNT)
            .expectedProduction(UPDATED_EXPECTED_PRODUCTION)
            .expectedHarvestDate(UPDATED_EXPECTED_HARVEST_DATE)
            .actualHarvestDate(UPDATED_ACTUAL_HARVEST_DATE)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        aquacultureProduction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAquacultureProduction != null) {
            aquacultureProductionRepository.delete(insertedAquacultureProduction);
            insertedAquacultureProduction = null;
        }
    }

    @Test
    @Transactional
    void createAquacultureProduction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AquacultureProduction
        AquacultureProductionDTO aquacultureProductionDTO = aquacultureProductionMapper.toDto(aquacultureProduction);
        var returnedAquacultureProductionDTO = om.readValue(
            restAquacultureProductionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquacultureProductionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AquacultureProductionDTO.class
        );

        // Validate the AquacultureProduction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAquacultureProduction = aquacultureProductionMapper.toEntity(returnedAquacultureProductionDTO);
        assertAquacultureProductionUpdatableFieldsEquals(
            returnedAquacultureProduction,
            getPersistedAquacultureProduction(returnedAquacultureProduction)
        );

        insertedAquacultureProduction = returnedAquacultureProduction;
    }

    @Test
    @Transactional
    void createAquacultureProductionWithExistingId() throws Exception {
        // Create the AquacultureProduction with an existing ID
        aquacultureProduction.setId(1L);
        AquacultureProductionDTO aquacultureProductionDTO = aquacultureProductionMapper.toDto(aquacultureProduction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAquacultureProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquacultureProductionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AquacultureProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aquacultureProduction.setStatus(null);

        // Create the AquacultureProduction, which fails.
        AquacultureProductionDTO aquacultureProductionDTO = aquacultureProductionMapper.toDto(aquacultureProduction);

        restAquacultureProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquacultureProductionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAquacultureProductions() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList
        restAquacultureProductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aquacultureProduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].productionDate").value(hasItem(DEFAULT_PRODUCTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].numberOfAnimals").value(hasItem(DEFAULT_NUMBER_OF_ANIMALS)))
            .andExpect(jsonPath("$.[*].stockingDensity").value(hasItem(sameNumber(DEFAULT_STOCKING_DENSITY))))
            .andExpect(jsonPath("$.[*].productionQuantity").value(hasItem(sameNumber(DEFAULT_PRODUCTION_QUANTITY))))
            .andExpect(jsonPath("$.[*].productionUnit").value(hasItem(DEFAULT_PRODUCTION_UNIT)))
            .andExpect(jsonPath("$.[*].averageWeightGrams").value(hasItem(sameNumber(DEFAULT_AVERAGE_WEIGHT_GRAMS))))
            .andExpect(jsonPath("$.[*].mortalityCount").value(hasItem(DEFAULT_MORTALITY_COUNT)))
            .andExpect(jsonPath("$.[*].stockingCount").value(hasItem(DEFAULT_STOCKING_COUNT)))
            .andExpect(jsonPath("$.[*].harvestedCount").value(hasItem(DEFAULT_HARVESTED_COUNT)))
            .andExpect(jsonPath("$.[*].expectedProduction").value(hasItem(sameNumber(DEFAULT_EXPECTED_PRODUCTION))))
            .andExpect(jsonPath("$.[*].expectedHarvestDate").value(hasItem(DEFAULT_EXPECTED_HARVEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualHarvestDate").value(hasItem(DEFAULT_ACTUAL_HARVEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getAquacultureProduction() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get the aquacultureProduction
        restAquacultureProductionMockMvc
            .perform(get(ENTITY_API_URL_ID, aquacultureProduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aquacultureProduction.getId().intValue()))
            .andExpect(jsonPath("$.productionDate").value(DEFAULT_PRODUCTION_DATE.toString()))
            .andExpect(jsonPath("$.numberOfAnimals").value(DEFAULT_NUMBER_OF_ANIMALS))
            .andExpect(jsonPath("$.stockingDensity").value(sameNumber(DEFAULT_STOCKING_DENSITY)))
            .andExpect(jsonPath("$.productionQuantity").value(sameNumber(DEFAULT_PRODUCTION_QUANTITY)))
            .andExpect(jsonPath("$.productionUnit").value(DEFAULT_PRODUCTION_UNIT))
            .andExpect(jsonPath("$.averageWeightGrams").value(sameNumber(DEFAULT_AVERAGE_WEIGHT_GRAMS)))
            .andExpect(jsonPath("$.mortalityCount").value(DEFAULT_MORTALITY_COUNT))
            .andExpect(jsonPath("$.stockingCount").value(DEFAULT_STOCKING_COUNT))
            .andExpect(jsonPath("$.harvestedCount").value(DEFAULT_HARVESTED_COUNT))
            .andExpect(jsonPath("$.expectedProduction").value(sameNumber(DEFAULT_EXPECTED_PRODUCTION)))
            .andExpect(jsonPath("$.expectedHarvestDate").value(DEFAULT_EXPECTED_HARVEST_DATE.toString()))
            .andExpect(jsonPath("$.actualHarvestDate").value(DEFAULT_ACTUAL_HARVEST_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getAquacultureProductionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        Long id = aquacultureProduction.getId();

        defaultAquacultureProductionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAquacultureProductionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAquacultureProductionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionDate equals to
        defaultAquacultureProductionFiltering(
            "productionDate.equals=" + DEFAULT_PRODUCTION_DATE,
            "productionDate.equals=" + UPDATED_PRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionDate in
        defaultAquacultureProductionFiltering(
            "productionDate.in=" + DEFAULT_PRODUCTION_DATE + "," + UPDATED_PRODUCTION_DATE,
            "productionDate.in=" + UPDATED_PRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionDate is not null
        defaultAquacultureProductionFiltering("productionDate.specified=true", "productionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionDate is greater than or equal to
        defaultAquacultureProductionFiltering(
            "productionDate.greaterThanOrEqual=" + DEFAULT_PRODUCTION_DATE,
            "productionDate.greaterThanOrEqual=" + UPDATED_PRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionDate is less than or equal to
        defaultAquacultureProductionFiltering(
            "productionDate.lessThanOrEqual=" + DEFAULT_PRODUCTION_DATE,
            "productionDate.lessThanOrEqual=" + SMALLER_PRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionDate is less than
        defaultAquacultureProductionFiltering(
            "productionDate.lessThan=" + UPDATED_PRODUCTION_DATE,
            "productionDate.lessThan=" + DEFAULT_PRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionDate is greater than
        defaultAquacultureProductionFiltering(
            "productionDate.greaterThan=" + SMALLER_PRODUCTION_DATE,
            "productionDate.greaterThan=" + DEFAULT_PRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByNumberOfAnimalsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where numberOfAnimals equals to
        defaultAquacultureProductionFiltering(
            "numberOfAnimals.equals=" + DEFAULT_NUMBER_OF_ANIMALS,
            "numberOfAnimals.equals=" + UPDATED_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByNumberOfAnimalsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where numberOfAnimals in
        defaultAquacultureProductionFiltering(
            "numberOfAnimals.in=" + DEFAULT_NUMBER_OF_ANIMALS + "," + UPDATED_NUMBER_OF_ANIMALS,
            "numberOfAnimals.in=" + UPDATED_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByNumberOfAnimalsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where numberOfAnimals is not null
        defaultAquacultureProductionFiltering("numberOfAnimals.specified=true", "numberOfAnimals.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByNumberOfAnimalsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where numberOfAnimals is greater than or equal to
        defaultAquacultureProductionFiltering(
            "numberOfAnimals.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_ANIMALS,
            "numberOfAnimals.greaterThanOrEqual=" + UPDATED_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByNumberOfAnimalsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where numberOfAnimals is less than or equal to
        defaultAquacultureProductionFiltering(
            "numberOfAnimals.lessThanOrEqual=" + DEFAULT_NUMBER_OF_ANIMALS,
            "numberOfAnimals.lessThanOrEqual=" + SMALLER_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByNumberOfAnimalsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where numberOfAnimals is less than
        defaultAquacultureProductionFiltering(
            "numberOfAnimals.lessThan=" + UPDATED_NUMBER_OF_ANIMALS,
            "numberOfAnimals.lessThan=" + DEFAULT_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByNumberOfAnimalsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where numberOfAnimals is greater than
        defaultAquacultureProductionFiltering(
            "numberOfAnimals.greaterThan=" + SMALLER_NUMBER_OF_ANIMALS,
            "numberOfAnimals.greaterThan=" + DEFAULT_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingDensityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingDensity equals to
        defaultAquacultureProductionFiltering(
            "stockingDensity.equals=" + DEFAULT_STOCKING_DENSITY,
            "stockingDensity.equals=" + UPDATED_STOCKING_DENSITY
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingDensityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingDensity in
        defaultAquacultureProductionFiltering(
            "stockingDensity.in=" + DEFAULT_STOCKING_DENSITY + "," + UPDATED_STOCKING_DENSITY,
            "stockingDensity.in=" + UPDATED_STOCKING_DENSITY
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingDensityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingDensity is not null
        defaultAquacultureProductionFiltering("stockingDensity.specified=true", "stockingDensity.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingDensityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingDensity is greater than or equal to
        defaultAquacultureProductionFiltering(
            "stockingDensity.greaterThanOrEqual=" + DEFAULT_STOCKING_DENSITY,
            "stockingDensity.greaterThanOrEqual=" + UPDATED_STOCKING_DENSITY
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingDensityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingDensity is less than or equal to
        defaultAquacultureProductionFiltering(
            "stockingDensity.lessThanOrEqual=" + DEFAULT_STOCKING_DENSITY,
            "stockingDensity.lessThanOrEqual=" + SMALLER_STOCKING_DENSITY
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingDensityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingDensity is less than
        defaultAquacultureProductionFiltering(
            "stockingDensity.lessThan=" + UPDATED_STOCKING_DENSITY,
            "stockingDensity.lessThan=" + DEFAULT_STOCKING_DENSITY
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingDensityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingDensity is greater than
        defaultAquacultureProductionFiltering(
            "stockingDensity.greaterThan=" + SMALLER_STOCKING_DENSITY,
            "stockingDensity.greaterThan=" + DEFAULT_STOCKING_DENSITY
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionQuantity equals to
        defaultAquacultureProductionFiltering(
            "productionQuantity.equals=" + DEFAULT_PRODUCTION_QUANTITY,
            "productionQuantity.equals=" + UPDATED_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionQuantity in
        defaultAquacultureProductionFiltering(
            "productionQuantity.in=" + DEFAULT_PRODUCTION_QUANTITY + "," + UPDATED_PRODUCTION_QUANTITY,
            "productionQuantity.in=" + UPDATED_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionQuantity is not null
        defaultAquacultureProductionFiltering("productionQuantity.specified=true", "productionQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionQuantity is greater than or equal to
        defaultAquacultureProductionFiltering(
            "productionQuantity.greaterThanOrEqual=" + DEFAULT_PRODUCTION_QUANTITY,
            "productionQuantity.greaterThanOrEqual=" + UPDATED_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionQuantity is less than or equal to
        defaultAquacultureProductionFiltering(
            "productionQuantity.lessThanOrEqual=" + DEFAULT_PRODUCTION_QUANTITY,
            "productionQuantity.lessThanOrEqual=" + SMALLER_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionQuantity is less than
        defaultAquacultureProductionFiltering(
            "productionQuantity.lessThan=" + UPDATED_PRODUCTION_QUANTITY,
            "productionQuantity.lessThan=" + DEFAULT_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionQuantity is greater than
        defaultAquacultureProductionFiltering(
            "productionQuantity.greaterThan=" + SMALLER_PRODUCTION_QUANTITY,
            "productionQuantity.greaterThan=" + DEFAULT_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionUnit equals to
        defaultAquacultureProductionFiltering(
            "productionUnit.equals=" + DEFAULT_PRODUCTION_UNIT,
            "productionUnit.equals=" + UPDATED_PRODUCTION_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionUnitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionUnit in
        defaultAquacultureProductionFiltering(
            "productionUnit.in=" + DEFAULT_PRODUCTION_UNIT + "," + UPDATED_PRODUCTION_UNIT,
            "productionUnit.in=" + UPDATED_PRODUCTION_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionUnit is not null
        defaultAquacultureProductionFiltering("productionUnit.specified=true", "productionUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionUnitContainsSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionUnit contains
        defaultAquacultureProductionFiltering(
            "productionUnit.contains=" + DEFAULT_PRODUCTION_UNIT,
            "productionUnit.contains=" + UPDATED_PRODUCTION_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByProductionUnitNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where productionUnit does not contain
        defaultAquacultureProductionFiltering(
            "productionUnit.doesNotContain=" + UPDATED_PRODUCTION_UNIT,
            "productionUnit.doesNotContain=" + DEFAULT_PRODUCTION_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByAverageWeightGramsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where averageWeightGrams equals to
        defaultAquacultureProductionFiltering(
            "averageWeightGrams.equals=" + DEFAULT_AVERAGE_WEIGHT_GRAMS,
            "averageWeightGrams.equals=" + UPDATED_AVERAGE_WEIGHT_GRAMS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByAverageWeightGramsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where averageWeightGrams in
        defaultAquacultureProductionFiltering(
            "averageWeightGrams.in=" + DEFAULT_AVERAGE_WEIGHT_GRAMS + "," + UPDATED_AVERAGE_WEIGHT_GRAMS,
            "averageWeightGrams.in=" + UPDATED_AVERAGE_WEIGHT_GRAMS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByAverageWeightGramsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where averageWeightGrams is not null
        defaultAquacultureProductionFiltering("averageWeightGrams.specified=true", "averageWeightGrams.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByAverageWeightGramsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where averageWeightGrams is greater than or equal to
        defaultAquacultureProductionFiltering(
            "averageWeightGrams.greaterThanOrEqual=" + DEFAULT_AVERAGE_WEIGHT_GRAMS,
            "averageWeightGrams.greaterThanOrEqual=" + UPDATED_AVERAGE_WEIGHT_GRAMS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByAverageWeightGramsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where averageWeightGrams is less than or equal to
        defaultAquacultureProductionFiltering(
            "averageWeightGrams.lessThanOrEqual=" + DEFAULT_AVERAGE_WEIGHT_GRAMS,
            "averageWeightGrams.lessThanOrEqual=" + SMALLER_AVERAGE_WEIGHT_GRAMS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByAverageWeightGramsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where averageWeightGrams is less than
        defaultAquacultureProductionFiltering(
            "averageWeightGrams.lessThan=" + UPDATED_AVERAGE_WEIGHT_GRAMS,
            "averageWeightGrams.lessThan=" + DEFAULT_AVERAGE_WEIGHT_GRAMS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByAverageWeightGramsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where averageWeightGrams is greater than
        defaultAquacultureProductionFiltering(
            "averageWeightGrams.greaterThan=" + SMALLER_AVERAGE_WEIGHT_GRAMS,
            "averageWeightGrams.greaterThan=" + DEFAULT_AVERAGE_WEIGHT_GRAMS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByMortalityCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where mortalityCount equals to
        defaultAquacultureProductionFiltering(
            "mortalityCount.equals=" + DEFAULT_MORTALITY_COUNT,
            "mortalityCount.equals=" + UPDATED_MORTALITY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByMortalityCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where mortalityCount in
        defaultAquacultureProductionFiltering(
            "mortalityCount.in=" + DEFAULT_MORTALITY_COUNT + "," + UPDATED_MORTALITY_COUNT,
            "mortalityCount.in=" + UPDATED_MORTALITY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByMortalityCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where mortalityCount is not null
        defaultAquacultureProductionFiltering("mortalityCount.specified=true", "mortalityCount.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByMortalityCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where mortalityCount is greater than or equal to
        defaultAquacultureProductionFiltering(
            "mortalityCount.greaterThanOrEqual=" + DEFAULT_MORTALITY_COUNT,
            "mortalityCount.greaterThanOrEqual=" + UPDATED_MORTALITY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByMortalityCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where mortalityCount is less than or equal to
        defaultAquacultureProductionFiltering(
            "mortalityCount.lessThanOrEqual=" + DEFAULT_MORTALITY_COUNT,
            "mortalityCount.lessThanOrEqual=" + SMALLER_MORTALITY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByMortalityCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where mortalityCount is less than
        defaultAquacultureProductionFiltering(
            "mortalityCount.lessThan=" + UPDATED_MORTALITY_COUNT,
            "mortalityCount.lessThan=" + DEFAULT_MORTALITY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByMortalityCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where mortalityCount is greater than
        defaultAquacultureProductionFiltering(
            "mortalityCount.greaterThan=" + SMALLER_MORTALITY_COUNT,
            "mortalityCount.greaterThan=" + DEFAULT_MORTALITY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingCount equals to
        defaultAquacultureProductionFiltering(
            "stockingCount.equals=" + DEFAULT_STOCKING_COUNT,
            "stockingCount.equals=" + UPDATED_STOCKING_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingCount in
        defaultAquacultureProductionFiltering(
            "stockingCount.in=" + DEFAULT_STOCKING_COUNT + "," + UPDATED_STOCKING_COUNT,
            "stockingCount.in=" + UPDATED_STOCKING_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingCount is not null
        defaultAquacultureProductionFiltering("stockingCount.specified=true", "stockingCount.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingCount is greater than or equal to
        defaultAquacultureProductionFiltering(
            "stockingCount.greaterThanOrEqual=" + DEFAULT_STOCKING_COUNT,
            "stockingCount.greaterThanOrEqual=" + UPDATED_STOCKING_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingCount is less than or equal to
        defaultAquacultureProductionFiltering(
            "stockingCount.lessThanOrEqual=" + DEFAULT_STOCKING_COUNT,
            "stockingCount.lessThanOrEqual=" + SMALLER_STOCKING_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingCount is less than
        defaultAquacultureProductionFiltering(
            "stockingCount.lessThan=" + UPDATED_STOCKING_COUNT,
            "stockingCount.lessThan=" + DEFAULT_STOCKING_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStockingCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where stockingCount is greater than
        defaultAquacultureProductionFiltering(
            "stockingCount.greaterThan=" + SMALLER_STOCKING_COUNT,
            "stockingCount.greaterThan=" + DEFAULT_STOCKING_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByHarvestedCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where harvestedCount equals to
        defaultAquacultureProductionFiltering(
            "harvestedCount.equals=" + DEFAULT_HARVESTED_COUNT,
            "harvestedCount.equals=" + UPDATED_HARVESTED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByHarvestedCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where harvestedCount in
        defaultAquacultureProductionFiltering(
            "harvestedCount.in=" + DEFAULT_HARVESTED_COUNT + "," + UPDATED_HARVESTED_COUNT,
            "harvestedCount.in=" + UPDATED_HARVESTED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByHarvestedCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where harvestedCount is not null
        defaultAquacultureProductionFiltering("harvestedCount.specified=true", "harvestedCount.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByHarvestedCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where harvestedCount is greater than or equal to
        defaultAquacultureProductionFiltering(
            "harvestedCount.greaterThanOrEqual=" + DEFAULT_HARVESTED_COUNT,
            "harvestedCount.greaterThanOrEqual=" + UPDATED_HARVESTED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByHarvestedCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where harvestedCount is less than or equal to
        defaultAquacultureProductionFiltering(
            "harvestedCount.lessThanOrEqual=" + DEFAULT_HARVESTED_COUNT,
            "harvestedCount.lessThanOrEqual=" + SMALLER_HARVESTED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByHarvestedCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where harvestedCount is less than
        defaultAquacultureProductionFiltering(
            "harvestedCount.lessThan=" + UPDATED_HARVESTED_COUNT,
            "harvestedCount.lessThan=" + DEFAULT_HARVESTED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByHarvestedCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where harvestedCount is greater than
        defaultAquacultureProductionFiltering(
            "harvestedCount.greaterThan=" + SMALLER_HARVESTED_COUNT,
            "harvestedCount.greaterThan=" + DEFAULT_HARVESTED_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedProductionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedProduction equals to
        defaultAquacultureProductionFiltering(
            "expectedProduction.equals=" + DEFAULT_EXPECTED_PRODUCTION,
            "expectedProduction.equals=" + UPDATED_EXPECTED_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedProductionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedProduction in
        defaultAquacultureProductionFiltering(
            "expectedProduction.in=" + DEFAULT_EXPECTED_PRODUCTION + "," + UPDATED_EXPECTED_PRODUCTION,
            "expectedProduction.in=" + UPDATED_EXPECTED_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedProductionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedProduction is not null
        defaultAquacultureProductionFiltering("expectedProduction.specified=true", "expectedProduction.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedProductionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedProduction is greater than or equal to
        defaultAquacultureProductionFiltering(
            "expectedProduction.greaterThanOrEqual=" + DEFAULT_EXPECTED_PRODUCTION,
            "expectedProduction.greaterThanOrEqual=" + UPDATED_EXPECTED_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedProductionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedProduction is less than or equal to
        defaultAquacultureProductionFiltering(
            "expectedProduction.lessThanOrEqual=" + DEFAULT_EXPECTED_PRODUCTION,
            "expectedProduction.lessThanOrEqual=" + SMALLER_EXPECTED_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedProductionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedProduction is less than
        defaultAquacultureProductionFiltering(
            "expectedProduction.lessThan=" + UPDATED_EXPECTED_PRODUCTION,
            "expectedProduction.lessThan=" + DEFAULT_EXPECTED_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedProductionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedProduction is greater than
        defaultAquacultureProductionFiltering(
            "expectedProduction.greaterThan=" + SMALLER_EXPECTED_PRODUCTION,
            "expectedProduction.greaterThan=" + DEFAULT_EXPECTED_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedHarvestDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedHarvestDate equals to
        defaultAquacultureProductionFiltering(
            "expectedHarvestDate.equals=" + DEFAULT_EXPECTED_HARVEST_DATE,
            "expectedHarvestDate.equals=" + UPDATED_EXPECTED_HARVEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedHarvestDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedHarvestDate in
        defaultAquacultureProductionFiltering(
            "expectedHarvestDate.in=" + DEFAULT_EXPECTED_HARVEST_DATE + "," + UPDATED_EXPECTED_HARVEST_DATE,
            "expectedHarvestDate.in=" + UPDATED_EXPECTED_HARVEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedHarvestDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedHarvestDate is not null
        defaultAquacultureProductionFiltering("expectedHarvestDate.specified=true", "expectedHarvestDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedHarvestDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedHarvestDate is greater than or equal to
        defaultAquacultureProductionFiltering(
            "expectedHarvestDate.greaterThanOrEqual=" + DEFAULT_EXPECTED_HARVEST_DATE,
            "expectedHarvestDate.greaterThanOrEqual=" + UPDATED_EXPECTED_HARVEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedHarvestDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedHarvestDate is less than or equal to
        defaultAquacultureProductionFiltering(
            "expectedHarvestDate.lessThanOrEqual=" + DEFAULT_EXPECTED_HARVEST_DATE,
            "expectedHarvestDate.lessThanOrEqual=" + SMALLER_EXPECTED_HARVEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedHarvestDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedHarvestDate is less than
        defaultAquacultureProductionFiltering(
            "expectedHarvestDate.lessThan=" + UPDATED_EXPECTED_HARVEST_DATE,
            "expectedHarvestDate.lessThan=" + DEFAULT_EXPECTED_HARVEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByExpectedHarvestDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where expectedHarvestDate is greater than
        defaultAquacultureProductionFiltering(
            "expectedHarvestDate.greaterThan=" + SMALLER_EXPECTED_HARVEST_DATE,
            "expectedHarvestDate.greaterThan=" + DEFAULT_EXPECTED_HARVEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByActualHarvestDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where actualHarvestDate equals to
        defaultAquacultureProductionFiltering(
            "actualHarvestDate.equals=" + DEFAULT_ACTUAL_HARVEST_DATE,
            "actualHarvestDate.equals=" + UPDATED_ACTUAL_HARVEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByActualHarvestDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where actualHarvestDate in
        defaultAquacultureProductionFiltering(
            "actualHarvestDate.in=" + DEFAULT_ACTUAL_HARVEST_DATE + "," + UPDATED_ACTUAL_HARVEST_DATE,
            "actualHarvestDate.in=" + UPDATED_ACTUAL_HARVEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByActualHarvestDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where actualHarvestDate is not null
        defaultAquacultureProductionFiltering("actualHarvestDate.specified=true", "actualHarvestDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByActualHarvestDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where actualHarvestDate is greater than or equal to
        defaultAquacultureProductionFiltering(
            "actualHarvestDate.greaterThanOrEqual=" + DEFAULT_ACTUAL_HARVEST_DATE,
            "actualHarvestDate.greaterThanOrEqual=" + UPDATED_ACTUAL_HARVEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByActualHarvestDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where actualHarvestDate is less than or equal to
        defaultAquacultureProductionFiltering(
            "actualHarvestDate.lessThanOrEqual=" + DEFAULT_ACTUAL_HARVEST_DATE,
            "actualHarvestDate.lessThanOrEqual=" + SMALLER_ACTUAL_HARVEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByActualHarvestDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where actualHarvestDate is less than
        defaultAquacultureProductionFiltering(
            "actualHarvestDate.lessThan=" + UPDATED_ACTUAL_HARVEST_DATE,
            "actualHarvestDate.lessThan=" + DEFAULT_ACTUAL_HARVEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByActualHarvestDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where actualHarvestDate is greater than
        defaultAquacultureProductionFiltering(
            "actualHarvestDate.greaterThan=" + SMALLER_ACTUAL_HARVEST_DATE,
            "actualHarvestDate.greaterThan=" + DEFAULT_ACTUAL_HARVEST_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where status equals to
        defaultAquacultureProductionFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where status in
        defaultAquacultureProductionFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        // Get all the aquacultureProductionList where status is not null
        defaultAquacultureProductionFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureProductionsByAquacultureActivityIsEqualToSomething() throws Exception {
        AquacultureActivity aquacultureActivity;
        if (TestUtil.findAll(em, AquacultureActivity.class).isEmpty()) {
            aquacultureProductionRepository.saveAndFlush(aquacultureProduction);
            aquacultureActivity = AquacultureActivityResourceIT.createEntity();
        } else {
            aquacultureActivity = TestUtil.findAll(em, AquacultureActivity.class).get(0);
        }
        em.persist(aquacultureActivity);
        em.flush();
        aquacultureProduction.setAquacultureActivity(aquacultureActivity);
        aquacultureProductionRepository.saveAndFlush(aquacultureProduction);
        Long aquacultureActivityId = aquacultureActivity.getId();
        // Get all the aquacultureProductionList where aquacultureActivity equals to aquacultureActivityId
        defaultAquacultureProductionShouldBeFound("aquacultureActivityId.equals=" + aquacultureActivityId);

        // Get all the aquacultureProductionList where aquacultureActivity equals to (aquacultureActivityId + 1)
        defaultAquacultureProductionShouldNotBeFound("aquacultureActivityId.equals=" + (aquacultureActivityId + 1));
    }

    private void defaultAquacultureProductionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAquacultureProductionShouldBeFound(shouldBeFound);
        defaultAquacultureProductionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAquacultureProductionShouldBeFound(String filter) throws Exception {
        restAquacultureProductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aquacultureProduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].productionDate").value(hasItem(DEFAULT_PRODUCTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].numberOfAnimals").value(hasItem(DEFAULT_NUMBER_OF_ANIMALS)))
            .andExpect(jsonPath("$.[*].stockingDensity").value(hasItem(sameNumber(DEFAULT_STOCKING_DENSITY))))
            .andExpect(jsonPath("$.[*].productionQuantity").value(hasItem(sameNumber(DEFAULT_PRODUCTION_QUANTITY))))
            .andExpect(jsonPath("$.[*].productionUnit").value(hasItem(DEFAULT_PRODUCTION_UNIT)))
            .andExpect(jsonPath("$.[*].averageWeightGrams").value(hasItem(sameNumber(DEFAULT_AVERAGE_WEIGHT_GRAMS))))
            .andExpect(jsonPath("$.[*].mortalityCount").value(hasItem(DEFAULT_MORTALITY_COUNT)))
            .andExpect(jsonPath("$.[*].stockingCount").value(hasItem(DEFAULT_STOCKING_COUNT)))
            .andExpect(jsonPath("$.[*].harvestedCount").value(hasItem(DEFAULT_HARVESTED_COUNT)))
            .andExpect(jsonPath("$.[*].expectedProduction").value(hasItem(sameNumber(DEFAULT_EXPECTED_PRODUCTION))))
            .andExpect(jsonPath("$.[*].expectedHarvestDate").value(hasItem(DEFAULT_EXPECTED_HARVEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualHarvestDate").value(hasItem(DEFAULT_ACTUAL_HARVEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restAquacultureProductionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAquacultureProductionShouldNotBeFound(String filter) throws Exception {
        restAquacultureProductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAquacultureProductionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAquacultureProduction() throws Exception {
        // Get the aquacultureProduction
        restAquacultureProductionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAquacultureProduction() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aquacultureProduction
        AquacultureProduction updatedAquacultureProduction = aquacultureProductionRepository
            .findById(aquacultureProduction.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAquacultureProduction are not directly saved in db
        em.detach(updatedAquacultureProduction);
        updatedAquacultureProduction
            .productionDate(UPDATED_PRODUCTION_DATE)
            .numberOfAnimals(UPDATED_NUMBER_OF_ANIMALS)
            .stockingDensity(UPDATED_STOCKING_DENSITY)
            .productionQuantity(UPDATED_PRODUCTION_QUANTITY)
            .productionUnit(UPDATED_PRODUCTION_UNIT)
            .averageWeightGrams(UPDATED_AVERAGE_WEIGHT_GRAMS)
            .mortalityCount(UPDATED_MORTALITY_COUNT)
            .stockingCount(UPDATED_STOCKING_COUNT)
            .harvestedCount(UPDATED_HARVESTED_COUNT)
            .expectedProduction(UPDATED_EXPECTED_PRODUCTION)
            .expectedHarvestDate(UPDATED_EXPECTED_HARVEST_DATE)
            .actualHarvestDate(UPDATED_ACTUAL_HARVEST_DATE)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
        AquacultureProductionDTO aquacultureProductionDTO = aquacultureProductionMapper.toDto(updatedAquacultureProduction);

        restAquacultureProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aquacultureProductionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aquacultureProductionDTO))
            )
            .andExpect(status().isOk());

        // Validate the AquacultureProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAquacultureProductionToMatchAllProperties(updatedAquacultureProduction);
    }

    @Test
    @Transactional
    void putNonExistingAquacultureProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquacultureProduction.setId(longCount.incrementAndGet());

        // Create the AquacultureProduction
        AquacultureProductionDTO aquacultureProductionDTO = aquacultureProductionMapper.toDto(aquacultureProduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAquacultureProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aquacultureProductionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aquacultureProductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AquacultureProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAquacultureProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquacultureProduction.setId(longCount.incrementAndGet());

        // Create the AquacultureProduction
        AquacultureProductionDTO aquacultureProductionDTO = aquacultureProductionMapper.toDto(aquacultureProduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAquacultureProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aquacultureProductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AquacultureProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAquacultureProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquacultureProduction.setId(longCount.incrementAndGet());

        // Create the AquacultureProduction
        AquacultureProductionDTO aquacultureProductionDTO = aquacultureProductionMapper.toDto(aquacultureProduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAquacultureProductionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquacultureProductionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AquacultureProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAquacultureProductionWithPatch() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aquacultureProduction using partial update
        AquacultureProduction partialUpdatedAquacultureProduction = new AquacultureProduction();
        partialUpdatedAquacultureProduction.setId(aquacultureProduction.getId());

        partialUpdatedAquacultureProduction
            .productionDate(UPDATED_PRODUCTION_DATE)
            .numberOfAnimals(UPDATED_NUMBER_OF_ANIMALS)
            .stockingDensity(UPDATED_STOCKING_DENSITY)
            .productionUnit(UPDATED_PRODUCTION_UNIT)
            .harvestedCount(UPDATED_HARVESTED_COUNT)
            .actualHarvestDate(UPDATED_ACTUAL_HARVEST_DATE)
            .notes(UPDATED_NOTES);

        restAquacultureProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAquacultureProduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAquacultureProduction))
            )
            .andExpect(status().isOk());

        // Validate the AquacultureProduction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAquacultureProductionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAquacultureProduction, aquacultureProduction),
            getPersistedAquacultureProduction(aquacultureProduction)
        );
    }

    @Test
    @Transactional
    void fullUpdateAquacultureProductionWithPatch() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aquacultureProduction using partial update
        AquacultureProduction partialUpdatedAquacultureProduction = new AquacultureProduction();
        partialUpdatedAquacultureProduction.setId(aquacultureProduction.getId());

        partialUpdatedAquacultureProduction
            .productionDate(UPDATED_PRODUCTION_DATE)
            .numberOfAnimals(UPDATED_NUMBER_OF_ANIMALS)
            .stockingDensity(UPDATED_STOCKING_DENSITY)
            .productionQuantity(UPDATED_PRODUCTION_QUANTITY)
            .productionUnit(UPDATED_PRODUCTION_UNIT)
            .averageWeightGrams(UPDATED_AVERAGE_WEIGHT_GRAMS)
            .mortalityCount(UPDATED_MORTALITY_COUNT)
            .stockingCount(UPDATED_STOCKING_COUNT)
            .harvestedCount(UPDATED_HARVESTED_COUNT)
            .expectedProduction(UPDATED_EXPECTED_PRODUCTION)
            .expectedHarvestDate(UPDATED_EXPECTED_HARVEST_DATE)
            .actualHarvestDate(UPDATED_ACTUAL_HARVEST_DATE)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);

        restAquacultureProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAquacultureProduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAquacultureProduction))
            )
            .andExpect(status().isOk());

        // Validate the AquacultureProduction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAquacultureProductionUpdatableFieldsEquals(
            partialUpdatedAquacultureProduction,
            getPersistedAquacultureProduction(partialUpdatedAquacultureProduction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAquacultureProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquacultureProduction.setId(longCount.incrementAndGet());

        // Create the AquacultureProduction
        AquacultureProductionDTO aquacultureProductionDTO = aquacultureProductionMapper.toDto(aquacultureProduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAquacultureProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aquacultureProductionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aquacultureProductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AquacultureProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAquacultureProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquacultureProduction.setId(longCount.incrementAndGet());

        // Create the AquacultureProduction
        AquacultureProductionDTO aquacultureProductionDTO = aquacultureProductionMapper.toDto(aquacultureProduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAquacultureProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aquacultureProductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AquacultureProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAquacultureProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquacultureProduction.setId(longCount.incrementAndGet());

        // Create the AquacultureProduction
        AquacultureProductionDTO aquacultureProductionDTO = aquacultureProductionMapper.toDto(aquacultureProduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAquacultureProductionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aquacultureProductionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AquacultureProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAquacultureProduction() throws Exception {
        // Initialize the database
        insertedAquacultureProduction = aquacultureProductionRepository.saveAndFlush(aquacultureProduction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aquacultureProduction
        restAquacultureProductionMockMvc
            .perform(delete(ENTITY_API_URL_ID, aquacultureProduction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aquacultureProductionRepository.count();
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

    protected AquacultureProduction getPersistedAquacultureProduction(AquacultureProduction aquacultureProduction) {
        return aquacultureProductionRepository.findById(aquacultureProduction.getId()).orElseThrow();
    }

    protected void assertPersistedAquacultureProductionToMatchAllProperties(AquacultureProduction expectedAquacultureProduction) {
        assertAquacultureProductionAllPropertiesEquals(
            expectedAquacultureProduction,
            getPersistedAquacultureProduction(expectedAquacultureProduction)
        );
    }

    protected void assertPersistedAquacultureProductionToMatchUpdatableProperties(AquacultureProduction expectedAquacultureProduction) {
        assertAquacultureProductionAllUpdatablePropertiesEquals(
            expectedAquacultureProduction,
            getPersistedAquacultureProduction(expectedAquacultureProduction)
        );
    }
}
