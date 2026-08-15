package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.LivestockProductionAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static com.naline.coopfull.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.LivestockActivity;
import com.naline.coopfull.domain.LivestockProduction;
import com.naline.coopfull.domain.enumeration.AnimalProductionStatus;
import com.naline.coopfull.domain.enumeration.AnimalSex;
import com.naline.coopfull.repository.LivestockProductionRepository;
import com.naline.coopfull.service.dto.LivestockProductionDTO;
import com.naline.coopfull.service.mapper.LivestockProductionMapper;
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
 * Integration tests for the {@link LivestockProductionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LivestockProductionResourceIT {

    private static final LocalDate DEFAULT_PRODUCTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PRODUCTION_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_PRODUCTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final AnimalSex DEFAULT_ANIMAL_SEX = AnimalSex.MALE;
    private static final AnimalSex UPDATED_ANIMAL_SEX = AnimalSex.FEMALE;

    private static final Integer DEFAULT_NUMBER_OF_ANIMALS = 1;
    private static final Integer UPDATED_NUMBER_OF_ANIMALS = 2;
    private static final Integer SMALLER_NUMBER_OF_ANIMALS = 1 - 1;

    private static final Integer DEFAULT_AVERAGE_AGE_MONTHS = 1;
    private static final Integer UPDATED_AVERAGE_AGE_MONTHS = 2;
    private static final Integer SMALLER_AVERAGE_AGE_MONTHS = 1 - 1;

    private static final BigDecimal DEFAULT_AVERAGE_WEIGHT_KG = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVERAGE_WEIGHT_KG = new BigDecimal(2);
    private static final BigDecimal SMALLER_AVERAGE_WEIGHT_KG = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PRODUCTION_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRODUCTION_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRODUCTION_QUANTITY = new BigDecimal(1 - 1);

    private static final String DEFAULT_PRODUCTION_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCTION_UNIT = "BBBBBBBBBB";

    private static final Integer DEFAULT_MORTALITY_COUNT = 1;
    private static final Integer UPDATED_MORTALITY_COUNT = 2;
    private static final Integer SMALLER_MORTALITY_COUNT = 1 - 1;

    private static final Integer DEFAULT_BIRTH_COUNT = 1;
    private static final Integer UPDATED_BIRTH_COUNT = 2;
    private static final Integer SMALLER_BIRTH_COUNT = 1 - 1;

    private static final Integer DEFAULT_SOLD_COUNT = 1;
    private static final Integer UPDATED_SOLD_COUNT = 2;
    private static final Integer SMALLER_SOLD_COUNT = 1 - 1;

    private static final AnimalProductionStatus DEFAULT_STATUS = AnimalProductionStatus.ACTIVE;
    private static final AnimalProductionStatus UPDATED_STATUS = AnimalProductionStatus.SOLD;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/livestock-productions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LivestockProductionRepository livestockProductionRepository;

    @Autowired
    private LivestockProductionMapper livestockProductionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLivestockProductionMockMvc;

    private LivestockProduction livestockProduction;

    private LivestockProduction insertedLivestockProduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LivestockProduction createEntity() {
        return new LivestockProduction()
            .productionDate(DEFAULT_PRODUCTION_DATE)
            .animalSex(DEFAULT_ANIMAL_SEX)
            .numberOfAnimals(DEFAULT_NUMBER_OF_ANIMALS)
            .averageAgeMonths(DEFAULT_AVERAGE_AGE_MONTHS)
            .averageWeightKg(DEFAULT_AVERAGE_WEIGHT_KG)
            .productionQuantity(DEFAULT_PRODUCTION_QUANTITY)
            .productionUnit(DEFAULT_PRODUCTION_UNIT)
            .mortalityCount(DEFAULT_MORTALITY_COUNT)
            .birthCount(DEFAULT_BIRTH_COUNT)
            .soldCount(DEFAULT_SOLD_COUNT)
            .status(DEFAULT_STATUS)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LivestockProduction createUpdatedEntity() {
        return new LivestockProduction()
            .productionDate(UPDATED_PRODUCTION_DATE)
            .animalSex(UPDATED_ANIMAL_SEX)
            .numberOfAnimals(UPDATED_NUMBER_OF_ANIMALS)
            .averageAgeMonths(UPDATED_AVERAGE_AGE_MONTHS)
            .averageWeightKg(UPDATED_AVERAGE_WEIGHT_KG)
            .productionQuantity(UPDATED_PRODUCTION_QUANTITY)
            .productionUnit(UPDATED_PRODUCTION_UNIT)
            .mortalityCount(UPDATED_MORTALITY_COUNT)
            .birthCount(UPDATED_BIRTH_COUNT)
            .soldCount(UPDATED_SOLD_COUNT)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        livestockProduction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLivestockProduction != null) {
            livestockProductionRepository.delete(insertedLivestockProduction);
            insertedLivestockProduction = null;
        }
    }

    @Test
    @Transactional
    void createLivestockProduction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LivestockProduction
        LivestockProductionDTO livestockProductionDTO = livestockProductionMapper.toDto(livestockProduction);
        var returnedLivestockProductionDTO = om.readValue(
            restLivestockProductionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockProductionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LivestockProductionDTO.class
        );

        // Validate the LivestockProduction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLivestockProduction = livestockProductionMapper.toEntity(returnedLivestockProductionDTO);
        assertLivestockProductionUpdatableFieldsEquals(
            returnedLivestockProduction,
            getPersistedLivestockProduction(returnedLivestockProduction)
        );

        insertedLivestockProduction = returnedLivestockProduction;
    }

    @Test
    @Transactional
    void createLivestockProductionWithExistingId() throws Exception {
        // Create the LivestockProduction with an existing ID
        livestockProduction.setId(1L);
        LivestockProductionDTO livestockProductionDTO = livestockProductionMapper.toDto(livestockProduction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivestockProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockProductionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LivestockProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumberOfAnimalsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        livestockProduction.setNumberOfAnimals(null);

        // Create the LivestockProduction, which fails.
        LivestockProductionDTO livestockProductionDTO = livestockProductionMapper.toDto(livestockProduction);

        restLivestockProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockProductionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        livestockProduction.setStatus(null);

        // Create the LivestockProduction, which fails.
        LivestockProductionDTO livestockProductionDTO = livestockProductionMapper.toDto(livestockProduction);

        restLivestockProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockProductionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLivestockProductions() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList
        restLivestockProductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livestockProduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].productionDate").value(hasItem(DEFAULT_PRODUCTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].animalSex").value(hasItem(DEFAULT_ANIMAL_SEX.toString())))
            .andExpect(jsonPath("$.[*].numberOfAnimals").value(hasItem(DEFAULT_NUMBER_OF_ANIMALS)))
            .andExpect(jsonPath("$.[*].averageAgeMonths").value(hasItem(DEFAULT_AVERAGE_AGE_MONTHS)))
            .andExpect(jsonPath("$.[*].averageWeightKg").value(hasItem(sameNumber(DEFAULT_AVERAGE_WEIGHT_KG))))
            .andExpect(jsonPath("$.[*].productionQuantity").value(hasItem(sameNumber(DEFAULT_PRODUCTION_QUANTITY))))
            .andExpect(jsonPath("$.[*].productionUnit").value(hasItem(DEFAULT_PRODUCTION_UNIT)))
            .andExpect(jsonPath("$.[*].mortalityCount").value(hasItem(DEFAULT_MORTALITY_COUNT)))
            .andExpect(jsonPath("$.[*].birthCount").value(hasItem(DEFAULT_BIRTH_COUNT)))
            .andExpect(jsonPath("$.[*].soldCount").value(hasItem(DEFAULT_SOLD_COUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getLivestockProduction() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get the livestockProduction
        restLivestockProductionMockMvc
            .perform(get(ENTITY_API_URL_ID, livestockProduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(livestockProduction.getId().intValue()))
            .andExpect(jsonPath("$.productionDate").value(DEFAULT_PRODUCTION_DATE.toString()))
            .andExpect(jsonPath("$.animalSex").value(DEFAULT_ANIMAL_SEX.toString()))
            .andExpect(jsonPath("$.numberOfAnimals").value(DEFAULT_NUMBER_OF_ANIMALS))
            .andExpect(jsonPath("$.averageAgeMonths").value(DEFAULT_AVERAGE_AGE_MONTHS))
            .andExpect(jsonPath("$.averageWeightKg").value(sameNumber(DEFAULT_AVERAGE_WEIGHT_KG)))
            .andExpect(jsonPath("$.productionQuantity").value(sameNumber(DEFAULT_PRODUCTION_QUANTITY)))
            .andExpect(jsonPath("$.productionUnit").value(DEFAULT_PRODUCTION_UNIT))
            .andExpect(jsonPath("$.mortalityCount").value(DEFAULT_MORTALITY_COUNT))
            .andExpect(jsonPath("$.birthCount").value(DEFAULT_BIRTH_COUNT))
            .andExpect(jsonPath("$.soldCount").value(DEFAULT_SOLD_COUNT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getLivestockProductionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        Long id = livestockProduction.getId();

        defaultLivestockProductionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLivestockProductionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLivestockProductionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionDate equals to
        defaultLivestockProductionFiltering(
            "productionDate.equals=" + DEFAULT_PRODUCTION_DATE,
            "productionDate.equals=" + UPDATED_PRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionDate in
        defaultLivestockProductionFiltering(
            "productionDate.in=" + DEFAULT_PRODUCTION_DATE + "," + UPDATED_PRODUCTION_DATE,
            "productionDate.in=" + UPDATED_PRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionDate is not null
        defaultLivestockProductionFiltering("productionDate.specified=true", "productionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionDate is greater than or equal to
        defaultLivestockProductionFiltering(
            "productionDate.greaterThanOrEqual=" + DEFAULT_PRODUCTION_DATE,
            "productionDate.greaterThanOrEqual=" + UPDATED_PRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionDate is less than or equal to
        defaultLivestockProductionFiltering(
            "productionDate.lessThanOrEqual=" + DEFAULT_PRODUCTION_DATE,
            "productionDate.lessThanOrEqual=" + SMALLER_PRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionDate is less than
        defaultLivestockProductionFiltering(
            "productionDate.lessThan=" + UPDATED_PRODUCTION_DATE,
            "productionDate.lessThan=" + DEFAULT_PRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionDate is greater than
        defaultLivestockProductionFiltering(
            "productionDate.greaterThan=" + SMALLER_PRODUCTION_DATE,
            "productionDate.greaterThan=" + DEFAULT_PRODUCTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAnimalSexIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where animalSex equals to
        defaultLivestockProductionFiltering("animalSex.equals=" + DEFAULT_ANIMAL_SEX, "animalSex.equals=" + UPDATED_ANIMAL_SEX);
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAnimalSexIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where animalSex in
        defaultLivestockProductionFiltering(
            "animalSex.in=" + DEFAULT_ANIMAL_SEX + "," + UPDATED_ANIMAL_SEX,
            "animalSex.in=" + UPDATED_ANIMAL_SEX
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAnimalSexIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where animalSex is not null
        defaultLivestockProductionFiltering("animalSex.specified=true", "animalSex.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByNumberOfAnimalsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where numberOfAnimals equals to
        defaultLivestockProductionFiltering(
            "numberOfAnimals.equals=" + DEFAULT_NUMBER_OF_ANIMALS,
            "numberOfAnimals.equals=" + UPDATED_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByNumberOfAnimalsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where numberOfAnimals in
        defaultLivestockProductionFiltering(
            "numberOfAnimals.in=" + DEFAULT_NUMBER_OF_ANIMALS + "," + UPDATED_NUMBER_OF_ANIMALS,
            "numberOfAnimals.in=" + UPDATED_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByNumberOfAnimalsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where numberOfAnimals is not null
        defaultLivestockProductionFiltering("numberOfAnimals.specified=true", "numberOfAnimals.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByNumberOfAnimalsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where numberOfAnimals is greater than or equal to
        defaultLivestockProductionFiltering(
            "numberOfAnimals.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_ANIMALS,
            "numberOfAnimals.greaterThanOrEqual=" + UPDATED_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByNumberOfAnimalsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where numberOfAnimals is less than or equal to
        defaultLivestockProductionFiltering(
            "numberOfAnimals.lessThanOrEqual=" + DEFAULT_NUMBER_OF_ANIMALS,
            "numberOfAnimals.lessThanOrEqual=" + SMALLER_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByNumberOfAnimalsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where numberOfAnimals is less than
        defaultLivestockProductionFiltering(
            "numberOfAnimals.lessThan=" + UPDATED_NUMBER_OF_ANIMALS,
            "numberOfAnimals.lessThan=" + DEFAULT_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByNumberOfAnimalsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where numberOfAnimals is greater than
        defaultLivestockProductionFiltering(
            "numberOfAnimals.greaterThan=" + SMALLER_NUMBER_OF_ANIMALS,
            "numberOfAnimals.greaterThan=" + DEFAULT_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageAgeMonthsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageAgeMonths equals to
        defaultLivestockProductionFiltering(
            "averageAgeMonths.equals=" + DEFAULT_AVERAGE_AGE_MONTHS,
            "averageAgeMonths.equals=" + UPDATED_AVERAGE_AGE_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageAgeMonthsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageAgeMonths in
        defaultLivestockProductionFiltering(
            "averageAgeMonths.in=" + DEFAULT_AVERAGE_AGE_MONTHS + "," + UPDATED_AVERAGE_AGE_MONTHS,
            "averageAgeMonths.in=" + UPDATED_AVERAGE_AGE_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageAgeMonthsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageAgeMonths is not null
        defaultLivestockProductionFiltering("averageAgeMonths.specified=true", "averageAgeMonths.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageAgeMonthsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageAgeMonths is greater than or equal to
        defaultLivestockProductionFiltering(
            "averageAgeMonths.greaterThanOrEqual=" + DEFAULT_AVERAGE_AGE_MONTHS,
            "averageAgeMonths.greaterThanOrEqual=" + UPDATED_AVERAGE_AGE_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageAgeMonthsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageAgeMonths is less than or equal to
        defaultLivestockProductionFiltering(
            "averageAgeMonths.lessThanOrEqual=" + DEFAULT_AVERAGE_AGE_MONTHS,
            "averageAgeMonths.lessThanOrEqual=" + SMALLER_AVERAGE_AGE_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageAgeMonthsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageAgeMonths is less than
        defaultLivestockProductionFiltering(
            "averageAgeMonths.lessThan=" + UPDATED_AVERAGE_AGE_MONTHS,
            "averageAgeMonths.lessThan=" + DEFAULT_AVERAGE_AGE_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageAgeMonthsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageAgeMonths is greater than
        defaultLivestockProductionFiltering(
            "averageAgeMonths.greaterThan=" + SMALLER_AVERAGE_AGE_MONTHS,
            "averageAgeMonths.greaterThan=" + DEFAULT_AVERAGE_AGE_MONTHS
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageWeightKgIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageWeightKg equals to
        defaultLivestockProductionFiltering(
            "averageWeightKg.equals=" + DEFAULT_AVERAGE_WEIGHT_KG,
            "averageWeightKg.equals=" + UPDATED_AVERAGE_WEIGHT_KG
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageWeightKgIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageWeightKg in
        defaultLivestockProductionFiltering(
            "averageWeightKg.in=" + DEFAULT_AVERAGE_WEIGHT_KG + "," + UPDATED_AVERAGE_WEIGHT_KG,
            "averageWeightKg.in=" + UPDATED_AVERAGE_WEIGHT_KG
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageWeightKgIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageWeightKg is not null
        defaultLivestockProductionFiltering("averageWeightKg.specified=true", "averageWeightKg.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageWeightKgIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageWeightKg is greater than or equal to
        defaultLivestockProductionFiltering(
            "averageWeightKg.greaterThanOrEqual=" + DEFAULT_AVERAGE_WEIGHT_KG,
            "averageWeightKg.greaterThanOrEqual=" + UPDATED_AVERAGE_WEIGHT_KG
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageWeightKgIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageWeightKg is less than or equal to
        defaultLivestockProductionFiltering(
            "averageWeightKg.lessThanOrEqual=" + DEFAULT_AVERAGE_WEIGHT_KG,
            "averageWeightKg.lessThanOrEqual=" + SMALLER_AVERAGE_WEIGHT_KG
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageWeightKgIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageWeightKg is less than
        defaultLivestockProductionFiltering(
            "averageWeightKg.lessThan=" + UPDATED_AVERAGE_WEIGHT_KG,
            "averageWeightKg.lessThan=" + DEFAULT_AVERAGE_WEIGHT_KG
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByAverageWeightKgIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where averageWeightKg is greater than
        defaultLivestockProductionFiltering(
            "averageWeightKg.greaterThan=" + SMALLER_AVERAGE_WEIGHT_KG,
            "averageWeightKg.greaterThan=" + DEFAULT_AVERAGE_WEIGHT_KG
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionQuantity equals to
        defaultLivestockProductionFiltering(
            "productionQuantity.equals=" + DEFAULT_PRODUCTION_QUANTITY,
            "productionQuantity.equals=" + UPDATED_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionQuantity in
        defaultLivestockProductionFiltering(
            "productionQuantity.in=" + DEFAULT_PRODUCTION_QUANTITY + "," + UPDATED_PRODUCTION_QUANTITY,
            "productionQuantity.in=" + UPDATED_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionQuantity is not null
        defaultLivestockProductionFiltering("productionQuantity.specified=true", "productionQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionQuantity is greater than or equal to
        defaultLivestockProductionFiltering(
            "productionQuantity.greaterThanOrEqual=" + DEFAULT_PRODUCTION_QUANTITY,
            "productionQuantity.greaterThanOrEqual=" + UPDATED_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionQuantity is less than or equal to
        defaultLivestockProductionFiltering(
            "productionQuantity.lessThanOrEqual=" + DEFAULT_PRODUCTION_QUANTITY,
            "productionQuantity.lessThanOrEqual=" + SMALLER_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionQuantity is less than
        defaultLivestockProductionFiltering(
            "productionQuantity.lessThan=" + UPDATED_PRODUCTION_QUANTITY,
            "productionQuantity.lessThan=" + DEFAULT_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionQuantity is greater than
        defaultLivestockProductionFiltering(
            "productionQuantity.greaterThan=" + SMALLER_PRODUCTION_QUANTITY,
            "productionQuantity.greaterThan=" + DEFAULT_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionUnit equals to
        defaultLivestockProductionFiltering(
            "productionUnit.equals=" + DEFAULT_PRODUCTION_UNIT,
            "productionUnit.equals=" + UPDATED_PRODUCTION_UNIT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionUnitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionUnit in
        defaultLivestockProductionFiltering(
            "productionUnit.in=" + DEFAULT_PRODUCTION_UNIT + "," + UPDATED_PRODUCTION_UNIT,
            "productionUnit.in=" + UPDATED_PRODUCTION_UNIT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionUnit is not null
        defaultLivestockProductionFiltering("productionUnit.specified=true", "productionUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionUnitContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionUnit contains
        defaultLivestockProductionFiltering(
            "productionUnit.contains=" + DEFAULT_PRODUCTION_UNIT,
            "productionUnit.contains=" + UPDATED_PRODUCTION_UNIT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByProductionUnitNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where productionUnit does not contain
        defaultLivestockProductionFiltering(
            "productionUnit.doesNotContain=" + UPDATED_PRODUCTION_UNIT,
            "productionUnit.doesNotContain=" + DEFAULT_PRODUCTION_UNIT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByMortalityCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where mortalityCount equals to
        defaultLivestockProductionFiltering(
            "mortalityCount.equals=" + DEFAULT_MORTALITY_COUNT,
            "mortalityCount.equals=" + UPDATED_MORTALITY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByMortalityCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where mortalityCount in
        defaultLivestockProductionFiltering(
            "mortalityCount.in=" + DEFAULT_MORTALITY_COUNT + "," + UPDATED_MORTALITY_COUNT,
            "mortalityCount.in=" + UPDATED_MORTALITY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByMortalityCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where mortalityCount is not null
        defaultLivestockProductionFiltering("mortalityCount.specified=true", "mortalityCount.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByMortalityCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where mortalityCount is greater than or equal to
        defaultLivestockProductionFiltering(
            "mortalityCount.greaterThanOrEqual=" + DEFAULT_MORTALITY_COUNT,
            "mortalityCount.greaterThanOrEqual=" + UPDATED_MORTALITY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByMortalityCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where mortalityCount is less than or equal to
        defaultLivestockProductionFiltering(
            "mortalityCount.lessThanOrEqual=" + DEFAULT_MORTALITY_COUNT,
            "mortalityCount.lessThanOrEqual=" + SMALLER_MORTALITY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByMortalityCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where mortalityCount is less than
        defaultLivestockProductionFiltering(
            "mortalityCount.lessThan=" + UPDATED_MORTALITY_COUNT,
            "mortalityCount.lessThan=" + DEFAULT_MORTALITY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByMortalityCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where mortalityCount is greater than
        defaultLivestockProductionFiltering(
            "mortalityCount.greaterThan=" + SMALLER_MORTALITY_COUNT,
            "mortalityCount.greaterThan=" + DEFAULT_MORTALITY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByBirthCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where birthCount equals to
        defaultLivestockProductionFiltering("birthCount.equals=" + DEFAULT_BIRTH_COUNT, "birthCount.equals=" + UPDATED_BIRTH_COUNT);
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByBirthCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where birthCount in
        defaultLivestockProductionFiltering(
            "birthCount.in=" + DEFAULT_BIRTH_COUNT + "," + UPDATED_BIRTH_COUNT,
            "birthCount.in=" + UPDATED_BIRTH_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByBirthCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where birthCount is not null
        defaultLivestockProductionFiltering("birthCount.specified=true", "birthCount.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByBirthCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where birthCount is greater than or equal to
        defaultLivestockProductionFiltering(
            "birthCount.greaterThanOrEqual=" + DEFAULT_BIRTH_COUNT,
            "birthCount.greaterThanOrEqual=" + UPDATED_BIRTH_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByBirthCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where birthCount is less than or equal to
        defaultLivestockProductionFiltering(
            "birthCount.lessThanOrEqual=" + DEFAULT_BIRTH_COUNT,
            "birthCount.lessThanOrEqual=" + SMALLER_BIRTH_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByBirthCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where birthCount is less than
        defaultLivestockProductionFiltering("birthCount.lessThan=" + UPDATED_BIRTH_COUNT, "birthCount.lessThan=" + DEFAULT_BIRTH_COUNT);
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByBirthCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where birthCount is greater than
        defaultLivestockProductionFiltering(
            "birthCount.greaterThan=" + SMALLER_BIRTH_COUNT,
            "birthCount.greaterThan=" + DEFAULT_BIRTH_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsBySoldCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where soldCount equals to
        defaultLivestockProductionFiltering("soldCount.equals=" + DEFAULT_SOLD_COUNT, "soldCount.equals=" + UPDATED_SOLD_COUNT);
    }

    @Test
    @Transactional
    void getAllLivestockProductionsBySoldCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where soldCount in
        defaultLivestockProductionFiltering(
            "soldCount.in=" + DEFAULT_SOLD_COUNT + "," + UPDATED_SOLD_COUNT,
            "soldCount.in=" + UPDATED_SOLD_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsBySoldCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where soldCount is not null
        defaultLivestockProductionFiltering("soldCount.specified=true", "soldCount.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockProductionsBySoldCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where soldCount is greater than or equal to
        defaultLivestockProductionFiltering(
            "soldCount.greaterThanOrEqual=" + DEFAULT_SOLD_COUNT,
            "soldCount.greaterThanOrEqual=" + UPDATED_SOLD_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsBySoldCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where soldCount is less than or equal to
        defaultLivestockProductionFiltering(
            "soldCount.lessThanOrEqual=" + DEFAULT_SOLD_COUNT,
            "soldCount.lessThanOrEqual=" + SMALLER_SOLD_COUNT
        );
    }

    @Test
    @Transactional
    void getAllLivestockProductionsBySoldCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where soldCount is less than
        defaultLivestockProductionFiltering("soldCount.lessThan=" + UPDATED_SOLD_COUNT, "soldCount.lessThan=" + DEFAULT_SOLD_COUNT);
    }

    @Test
    @Transactional
    void getAllLivestockProductionsBySoldCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where soldCount is greater than
        defaultLivestockProductionFiltering("soldCount.greaterThan=" + SMALLER_SOLD_COUNT, "soldCount.greaterThan=" + DEFAULT_SOLD_COUNT);
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where status equals to
        defaultLivestockProductionFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where status in
        defaultLivestockProductionFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        // Get all the livestockProductionList where status is not null
        defaultLivestockProductionFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockProductionsByLivestockActivityIsEqualToSomething() throws Exception {
        LivestockActivity livestockActivity;
        if (TestUtil.findAll(em, LivestockActivity.class).isEmpty()) {
            livestockProductionRepository.saveAndFlush(livestockProduction);
            livestockActivity = LivestockActivityResourceIT.createEntity();
        } else {
            livestockActivity = TestUtil.findAll(em, LivestockActivity.class).get(0);
        }
        em.persist(livestockActivity);
        em.flush();
        livestockProduction.setLivestockActivity(livestockActivity);
        livestockProductionRepository.saveAndFlush(livestockProduction);
        Long livestockActivityId = livestockActivity.getId();
        // Get all the livestockProductionList where livestockActivity equals to livestockActivityId
        defaultLivestockProductionShouldBeFound("livestockActivityId.equals=" + livestockActivityId);

        // Get all the livestockProductionList where livestockActivity equals to (livestockActivityId + 1)
        defaultLivestockProductionShouldNotBeFound("livestockActivityId.equals=" + (livestockActivityId + 1));
    }

    private void defaultLivestockProductionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLivestockProductionShouldBeFound(shouldBeFound);
        defaultLivestockProductionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLivestockProductionShouldBeFound(String filter) throws Exception {
        restLivestockProductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livestockProduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].productionDate").value(hasItem(DEFAULT_PRODUCTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].animalSex").value(hasItem(DEFAULT_ANIMAL_SEX.toString())))
            .andExpect(jsonPath("$.[*].numberOfAnimals").value(hasItem(DEFAULT_NUMBER_OF_ANIMALS)))
            .andExpect(jsonPath("$.[*].averageAgeMonths").value(hasItem(DEFAULT_AVERAGE_AGE_MONTHS)))
            .andExpect(jsonPath("$.[*].averageWeightKg").value(hasItem(sameNumber(DEFAULT_AVERAGE_WEIGHT_KG))))
            .andExpect(jsonPath("$.[*].productionQuantity").value(hasItem(sameNumber(DEFAULT_PRODUCTION_QUANTITY))))
            .andExpect(jsonPath("$.[*].productionUnit").value(hasItem(DEFAULT_PRODUCTION_UNIT)))
            .andExpect(jsonPath("$.[*].mortalityCount").value(hasItem(DEFAULT_MORTALITY_COUNT)))
            .andExpect(jsonPath("$.[*].birthCount").value(hasItem(DEFAULT_BIRTH_COUNT)))
            .andExpect(jsonPath("$.[*].soldCount").value(hasItem(DEFAULT_SOLD_COUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restLivestockProductionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLivestockProductionShouldNotBeFound(String filter) throws Exception {
        restLivestockProductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLivestockProductionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLivestockProduction() throws Exception {
        // Get the livestockProduction
        restLivestockProductionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLivestockProduction() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the livestockProduction
        LivestockProduction updatedLivestockProduction = livestockProductionRepository.findById(livestockProduction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLivestockProduction are not directly saved in db
        em.detach(updatedLivestockProduction);
        updatedLivestockProduction
            .productionDate(UPDATED_PRODUCTION_DATE)
            .animalSex(UPDATED_ANIMAL_SEX)
            .numberOfAnimals(UPDATED_NUMBER_OF_ANIMALS)
            .averageAgeMonths(UPDATED_AVERAGE_AGE_MONTHS)
            .averageWeightKg(UPDATED_AVERAGE_WEIGHT_KG)
            .productionQuantity(UPDATED_PRODUCTION_QUANTITY)
            .productionUnit(UPDATED_PRODUCTION_UNIT)
            .mortalityCount(UPDATED_MORTALITY_COUNT)
            .birthCount(UPDATED_BIRTH_COUNT)
            .soldCount(UPDATED_SOLD_COUNT)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
        LivestockProductionDTO livestockProductionDTO = livestockProductionMapper.toDto(updatedLivestockProduction);

        restLivestockProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livestockProductionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(livestockProductionDTO))
            )
            .andExpect(status().isOk());

        // Validate the LivestockProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLivestockProductionToMatchAllProperties(updatedLivestockProduction);
    }

    @Test
    @Transactional
    void putNonExistingLivestockProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockProduction.setId(longCount.incrementAndGet());

        // Create the LivestockProduction
        LivestockProductionDTO livestockProductionDTO = livestockProductionMapper.toDto(livestockProduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivestockProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livestockProductionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(livestockProductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivestockProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLivestockProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockProduction.setId(longCount.incrementAndGet());

        // Create the LivestockProduction
        LivestockProductionDTO livestockProductionDTO = livestockProductionMapper.toDto(livestockProduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivestockProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(livestockProductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivestockProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLivestockProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockProduction.setId(longCount.incrementAndGet());

        // Create the LivestockProduction
        LivestockProductionDTO livestockProductionDTO = livestockProductionMapper.toDto(livestockProduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivestockProductionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockProductionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LivestockProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLivestockProductionWithPatch() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the livestockProduction using partial update
        LivestockProduction partialUpdatedLivestockProduction = new LivestockProduction();
        partialUpdatedLivestockProduction.setId(livestockProduction.getId());

        partialUpdatedLivestockProduction
            .productionDate(UPDATED_PRODUCTION_DATE)
            .animalSex(UPDATED_ANIMAL_SEX)
            .numberOfAnimals(UPDATED_NUMBER_OF_ANIMALS)
            .averageWeightKg(UPDATED_AVERAGE_WEIGHT_KG)
            .mortalityCount(UPDATED_MORTALITY_COUNT);

        restLivestockProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivestockProduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLivestockProduction))
            )
            .andExpect(status().isOk());

        // Validate the LivestockProduction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLivestockProductionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLivestockProduction, livestockProduction),
            getPersistedLivestockProduction(livestockProduction)
        );
    }

    @Test
    @Transactional
    void fullUpdateLivestockProductionWithPatch() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the livestockProduction using partial update
        LivestockProduction partialUpdatedLivestockProduction = new LivestockProduction();
        partialUpdatedLivestockProduction.setId(livestockProduction.getId());

        partialUpdatedLivestockProduction
            .productionDate(UPDATED_PRODUCTION_DATE)
            .animalSex(UPDATED_ANIMAL_SEX)
            .numberOfAnimals(UPDATED_NUMBER_OF_ANIMALS)
            .averageAgeMonths(UPDATED_AVERAGE_AGE_MONTHS)
            .averageWeightKg(UPDATED_AVERAGE_WEIGHT_KG)
            .productionQuantity(UPDATED_PRODUCTION_QUANTITY)
            .productionUnit(UPDATED_PRODUCTION_UNIT)
            .mortalityCount(UPDATED_MORTALITY_COUNT)
            .birthCount(UPDATED_BIRTH_COUNT)
            .soldCount(UPDATED_SOLD_COUNT)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);

        restLivestockProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivestockProduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLivestockProduction))
            )
            .andExpect(status().isOk());

        // Validate the LivestockProduction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLivestockProductionUpdatableFieldsEquals(
            partialUpdatedLivestockProduction,
            getPersistedLivestockProduction(partialUpdatedLivestockProduction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingLivestockProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockProduction.setId(longCount.incrementAndGet());

        // Create the LivestockProduction
        LivestockProductionDTO livestockProductionDTO = livestockProductionMapper.toDto(livestockProduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivestockProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, livestockProductionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(livestockProductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivestockProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLivestockProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockProduction.setId(longCount.incrementAndGet());

        // Create the LivestockProduction
        LivestockProductionDTO livestockProductionDTO = livestockProductionMapper.toDto(livestockProduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivestockProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(livestockProductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivestockProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLivestockProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockProduction.setId(longCount.incrementAndGet());

        // Create the LivestockProduction
        LivestockProductionDTO livestockProductionDTO = livestockProductionMapper.toDto(livestockProduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivestockProductionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(livestockProductionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LivestockProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLivestockProduction() throws Exception {
        // Initialize the database
        insertedLivestockProduction = livestockProductionRepository.saveAndFlush(livestockProduction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the livestockProduction
        restLivestockProductionMockMvc
            .perform(delete(ENTITY_API_URL_ID, livestockProduction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return livestockProductionRepository.count();
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

    protected LivestockProduction getPersistedLivestockProduction(LivestockProduction livestockProduction) {
        return livestockProductionRepository.findById(livestockProduction.getId()).orElseThrow();
    }

    protected void assertPersistedLivestockProductionToMatchAllProperties(LivestockProduction expectedLivestockProduction) {
        assertLivestockProductionAllPropertiesEquals(
            expectedLivestockProduction,
            getPersistedLivestockProduction(expectedLivestockProduction)
        );
    }

    protected void assertPersistedLivestockProductionToMatchUpdatableProperties(LivestockProduction expectedLivestockProduction) {
        assertLivestockProductionAllUpdatablePropertiesEquals(
            expectedLivestockProduction,
            getPersistedLivestockProduction(expectedLivestockProduction)
        );
    }
}
