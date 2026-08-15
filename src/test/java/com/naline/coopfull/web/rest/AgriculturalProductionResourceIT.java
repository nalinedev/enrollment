package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.AgriculturalProductionAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static com.naline.coopfull.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.AgriculturalActivity;
import com.naline.coopfull.domain.AgriculturalProduction;
import com.naline.coopfull.domain.Crop;
import com.naline.coopfull.domain.CropVariety;
import com.naline.coopfull.domain.enumeration.ProductionStatus;
import com.naline.coopfull.repository.AgriculturalProductionRepository;
import com.naline.coopfull.service.AgriculturalProductionService;
import com.naline.coopfull.service.dto.AgriculturalProductionDTO;
import com.naline.coopfull.service.mapper.AgriculturalProductionMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link AgriculturalProductionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AgriculturalProductionResourceIT {

    private static final BigDecimal DEFAULT_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_AREA = new BigDecimal(2);
    private static final BigDecimal SMALLER_AREA = new BigDecimal(1 - 1);

    private static final String DEFAULT_AREA_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_AREA_UNIT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PLANTING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PLANTING_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_PLANTING_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_HARVEST_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HARVEST_START_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_HARVEST_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_HARVEST_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_HARVEST_END_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_HARVEST_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_PRODUCTION_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRODUCTION_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRODUCTION_QUANTITY = new BigDecimal(1 - 1);

    private static final String DEFAULT_PRODUCTION_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCTION_UNIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_EXPECTED_ANNUAL_PRODUCTION = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXPECTED_ANNUAL_PRODUCTION = new BigDecimal(2);
    private static final BigDecimal SMALLER_EXPECTED_ANNUAL_PRODUCTION = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_NUMBER_OF_PLANTS = 1;
    private static final Integer UPDATED_NUMBER_OF_PLANTS = 2;
    private static final Integer SMALLER_NUMBER_OF_PLANTS = 1 - 1;

    private static final BigDecimal DEFAULT_PLANTING_DENSITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_PLANTING_DENSITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_PLANTING_DENSITY = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_PRODUCTION_YEAR = 1;
    private static final Integer UPDATED_PRODUCTION_YEAR = 2;
    private static final Integer SMALLER_PRODUCTION_YEAR = 1 - 1;

    private static final ProductionStatus DEFAULT_STATUS = ProductionStatus.PLANNED;
    private static final ProductionStatus UPDATED_STATUS = ProductionStatus.ACTIVE;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/agricultural-productions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgriculturalProductionRepository agriculturalProductionRepository;

    @Mock
    private AgriculturalProductionRepository agriculturalProductionRepositoryMock;

    @Autowired
    private AgriculturalProductionMapper agriculturalProductionMapper;

    @Mock
    private AgriculturalProductionService agriculturalProductionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgriculturalProductionMockMvc;

    private AgriculturalProduction agriculturalProduction;

    private AgriculturalProduction insertedAgriculturalProduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgriculturalProduction createEntity() {
        return new AgriculturalProduction()
            .area(DEFAULT_AREA)
            .areaUnit(DEFAULT_AREA_UNIT)
            .plantingDate(DEFAULT_PLANTING_DATE)
            .harvestStartDate(DEFAULT_HARVEST_START_DATE)
            .harvestEndDate(DEFAULT_HARVEST_END_DATE)
            .productionQuantity(DEFAULT_PRODUCTION_QUANTITY)
            .productionUnit(DEFAULT_PRODUCTION_UNIT)
            .expectedAnnualProduction(DEFAULT_EXPECTED_ANNUAL_PRODUCTION)
            .numberOfPlants(DEFAULT_NUMBER_OF_PLANTS)
            .plantingDensity(DEFAULT_PLANTING_DENSITY)
            .productionYear(DEFAULT_PRODUCTION_YEAR)
            .status(DEFAULT_STATUS)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgriculturalProduction createUpdatedEntity() {
        return new AgriculturalProduction()
            .area(UPDATED_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .plantingDate(UPDATED_PLANTING_DATE)
            .harvestStartDate(UPDATED_HARVEST_START_DATE)
            .harvestEndDate(UPDATED_HARVEST_END_DATE)
            .productionQuantity(UPDATED_PRODUCTION_QUANTITY)
            .productionUnit(UPDATED_PRODUCTION_UNIT)
            .expectedAnnualProduction(UPDATED_EXPECTED_ANNUAL_PRODUCTION)
            .numberOfPlants(UPDATED_NUMBER_OF_PLANTS)
            .plantingDensity(UPDATED_PLANTING_DENSITY)
            .productionYear(UPDATED_PRODUCTION_YEAR)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        agriculturalProduction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAgriculturalProduction != null) {
            agriculturalProductionRepository.delete(insertedAgriculturalProduction);
            insertedAgriculturalProduction = null;
        }
    }

    @Test
    @Transactional
    void createAgriculturalProduction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AgriculturalProduction
        AgriculturalProductionDTO agriculturalProductionDTO = agriculturalProductionMapper.toDto(agriculturalProduction);
        var returnedAgriculturalProductionDTO = om.readValue(
            restAgriculturalProductionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculturalProductionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AgriculturalProductionDTO.class
        );

        // Validate the AgriculturalProduction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAgriculturalProduction = agriculturalProductionMapper.toEntity(returnedAgriculturalProductionDTO);
        assertAgriculturalProductionUpdatableFieldsEquals(
            returnedAgriculturalProduction,
            getPersistedAgriculturalProduction(returnedAgriculturalProduction)
        );

        insertedAgriculturalProduction = returnedAgriculturalProduction;
    }

    @Test
    @Transactional
    void createAgriculturalProductionWithExistingId() throws Exception {
        // Create the AgriculturalProduction with an existing ID
        agriculturalProduction.setId(1L);
        AgriculturalProductionDTO agriculturalProductionDTO = agriculturalProductionMapper.toDto(agriculturalProduction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgriculturalProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculturalProductionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAreaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agriculturalProduction.setArea(null);

        // Create the AgriculturalProduction, which fails.
        AgriculturalProductionDTO agriculturalProductionDTO = agriculturalProductionMapper.toDto(agriculturalProduction);

        restAgriculturalProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculturalProductionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAreaUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agriculturalProduction.setAreaUnit(null);

        // Create the AgriculturalProduction, which fails.
        AgriculturalProductionDTO agriculturalProductionDTO = agriculturalProductionMapper.toDto(agriculturalProduction);

        restAgriculturalProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculturalProductionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agriculturalProduction.setStatus(null);

        // Create the AgriculturalProduction, which fails.
        AgriculturalProductionDTO agriculturalProductionDTO = agriculturalProductionMapper.toDto(agriculturalProduction);

        restAgriculturalProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculturalProductionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAgriculturalProductions() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList
        restAgriculturalProductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agriculturalProduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(sameNumber(DEFAULT_AREA))))
            .andExpect(jsonPath("$.[*].areaUnit").value(hasItem(DEFAULT_AREA_UNIT)))
            .andExpect(jsonPath("$.[*].plantingDate").value(hasItem(DEFAULT_PLANTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].harvestStartDate").value(hasItem(DEFAULT_HARVEST_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].harvestEndDate").value(hasItem(DEFAULT_HARVEST_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].productionQuantity").value(hasItem(sameNumber(DEFAULT_PRODUCTION_QUANTITY))))
            .andExpect(jsonPath("$.[*].productionUnit").value(hasItem(DEFAULT_PRODUCTION_UNIT)))
            .andExpect(jsonPath("$.[*].expectedAnnualProduction").value(hasItem(sameNumber(DEFAULT_EXPECTED_ANNUAL_PRODUCTION))))
            .andExpect(jsonPath("$.[*].numberOfPlants").value(hasItem(DEFAULT_NUMBER_OF_PLANTS)))
            .andExpect(jsonPath("$.[*].plantingDensity").value(hasItem(sameNumber(DEFAULT_PLANTING_DENSITY))))
            .andExpect(jsonPath("$.[*].productionYear").value(hasItem(DEFAULT_PRODUCTION_YEAR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgriculturalProductionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(agriculturalProductionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgriculturalProductionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(agriculturalProductionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgriculturalProductionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(agriculturalProductionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgriculturalProductionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(agriculturalProductionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAgriculturalProduction() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get the agriculturalProduction
        restAgriculturalProductionMockMvc
            .perform(get(ENTITY_API_URL_ID, agriculturalProduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agriculturalProduction.getId().intValue()))
            .andExpect(jsonPath("$.area").value(sameNumber(DEFAULT_AREA)))
            .andExpect(jsonPath("$.areaUnit").value(DEFAULT_AREA_UNIT))
            .andExpect(jsonPath("$.plantingDate").value(DEFAULT_PLANTING_DATE.toString()))
            .andExpect(jsonPath("$.harvestStartDate").value(DEFAULT_HARVEST_START_DATE.toString()))
            .andExpect(jsonPath("$.harvestEndDate").value(DEFAULT_HARVEST_END_DATE.toString()))
            .andExpect(jsonPath("$.productionQuantity").value(sameNumber(DEFAULT_PRODUCTION_QUANTITY)))
            .andExpect(jsonPath("$.productionUnit").value(DEFAULT_PRODUCTION_UNIT))
            .andExpect(jsonPath("$.expectedAnnualProduction").value(sameNumber(DEFAULT_EXPECTED_ANNUAL_PRODUCTION)))
            .andExpect(jsonPath("$.numberOfPlants").value(DEFAULT_NUMBER_OF_PLANTS))
            .andExpect(jsonPath("$.plantingDensity").value(sameNumber(DEFAULT_PLANTING_DENSITY)))
            .andExpect(jsonPath("$.productionYear").value(DEFAULT_PRODUCTION_YEAR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getAgriculturalProductionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        Long id = agriculturalProduction.getId();

        defaultAgriculturalProductionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAgriculturalProductionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAgriculturalProductionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where area equals to
        defaultAgriculturalProductionFiltering("area.equals=" + DEFAULT_AREA, "area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where area in
        defaultAgriculturalProductionFiltering("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA, "area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where area is not null
        defaultAgriculturalProductionFiltering("area.specified=true", "area.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where area is greater than or equal to
        defaultAgriculturalProductionFiltering("area.greaterThanOrEqual=" + DEFAULT_AREA, "area.greaterThanOrEqual=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where area is less than or equal to
        defaultAgriculturalProductionFiltering("area.lessThanOrEqual=" + DEFAULT_AREA, "area.lessThanOrEqual=" + SMALLER_AREA);
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where area is less than
        defaultAgriculturalProductionFiltering("area.lessThan=" + UPDATED_AREA, "area.lessThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where area is greater than
        defaultAgriculturalProductionFiltering("area.greaterThan=" + SMALLER_AREA, "area.greaterThan=" + DEFAULT_AREA);
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAreaUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where areaUnit equals to
        defaultAgriculturalProductionFiltering("areaUnit.equals=" + DEFAULT_AREA_UNIT, "areaUnit.equals=" + UPDATED_AREA_UNIT);
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAreaUnitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where areaUnit in
        defaultAgriculturalProductionFiltering(
            "areaUnit.in=" + DEFAULT_AREA_UNIT + "," + UPDATED_AREA_UNIT,
            "areaUnit.in=" + UPDATED_AREA_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAreaUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where areaUnit is not null
        defaultAgriculturalProductionFiltering("areaUnit.specified=true", "areaUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAreaUnitContainsSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where areaUnit contains
        defaultAgriculturalProductionFiltering("areaUnit.contains=" + DEFAULT_AREA_UNIT, "areaUnit.contains=" + UPDATED_AREA_UNIT);
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAreaUnitNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where areaUnit does not contain
        defaultAgriculturalProductionFiltering(
            "areaUnit.doesNotContain=" + UPDATED_AREA_UNIT,
            "areaUnit.doesNotContain=" + DEFAULT_AREA_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDate equals to
        defaultAgriculturalProductionFiltering(
            "plantingDate.equals=" + DEFAULT_PLANTING_DATE,
            "plantingDate.equals=" + UPDATED_PLANTING_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDate in
        defaultAgriculturalProductionFiltering(
            "plantingDate.in=" + DEFAULT_PLANTING_DATE + "," + UPDATED_PLANTING_DATE,
            "plantingDate.in=" + UPDATED_PLANTING_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDate is not null
        defaultAgriculturalProductionFiltering("plantingDate.specified=true", "plantingDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDate is greater than or equal to
        defaultAgriculturalProductionFiltering(
            "plantingDate.greaterThanOrEqual=" + DEFAULT_PLANTING_DATE,
            "plantingDate.greaterThanOrEqual=" + UPDATED_PLANTING_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDate is less than or equal to
        defaultAgriculturalProductionFiltering(
            "plantingDate.lessThanOrEqual=" + DEFAULT_PLANTING_DATE,
            "plantingDate.lessThanOrEqual=" + SMALLER_PLANTING_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDate is less than
        defaultAgriculturalProductionFiltering(
            "plantingDate.lessThan=" + UPDATED_PLANTING_DATE,
            "plantingDate.lessThan=" + DEFAULT_PLANTING_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDate is greater than
        defaultAgriculturalProductionFiltering(
            "plantingDate.greaterThan=" + SMALLER_PLANTING_DATE,
            "plantingDate.greaterThan=" + DEFAULT_PLANTING_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestStartDate equals to
        defaultAgriculturalProductionFiltering(
            "harvestStartDate.equals=" + DEFAULT_HARVEST_START_DATE,
            "harvestStartDate.equals=" + UPDATED_HARVEST_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestStartDate in
        defaultAgriculturalProductionFiltering(
            "harvestStartDate.in=" + DEFAULT_HARVEST_START_DATE + "," + UPDATED_HARVEST_START_DATE,
            "harvestStartDate.in=" + UPDATED_HARVEST_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestStartDate is not null
        defaultAgriculturalProductionFiltering("harvestStartDate.specified=true", "harvestStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestStartDate is greater than or equal to
        defaultAgriculturalProductionFiltering(
            "harvestStartDate.greaterThanOrEqual=" + DEFAULT_HARVEST_START_DATE,
            "harvestStartDate.greaterThanOrEqual=" + UPDATED_HARVEST_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestStartDate is less than or equal to
        defaultAgriculturalProductionFiltering(
            "harvestStartDate.lessThanOrEqual=" + DEFAULT_HARVEST_START_DATE,
            "harvestStartDate.lessThanOrEqual=" + SMALLER_HARVEST_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestStartDate is less than
        defaultAgriculturalProductionFiltering(
            "harvestStartDate.lessThan=" + UPDATED_HARVEST_START_DATE,
            "harvestStartDate.lessThan=" + DEFAULT_HARVEST_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestStartDate is greater than
        defaultAgriculturalProductionFiltering(
            "harvestStartDate.greaterThan=" + SMALLER_HARVEST_START_DATE,
            "harvestStartDate.greaterThan=" + DEFAULT_HARVEST_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestEndDate equals to
        defaultAgriculturalProductionFiltering(
            "harvestEndDate.equals=" + DEFAULT_HARVEST_END_DATE,
            "harvestEndDate.equals=" + UPDATED_HARVEST_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestEndDate in
        defaultAgriculturalProductionFiltering(
            "harvestEndDate.in=" + DEFAULT_HARVEST_END_DATE + "," + UPDATED_HARVEST_END_DATE,
            "harvestEndDate.in=" + UPDATED_HARVEST_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestEndDate is not null
        defaultAgriculturalProductionFiltering("harvestEndDate.specified=true", "harvestEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestEndDate is greater than or equal to
        defaultAgriculturalProductionFiltering(
            "harvestEndDate.greaterThanOrEqual=" + DEFAULT_HARVEST_END_DATE,
            "harvestEndDate.greaterThanOrEqual=" + UPDATED_HARVEST_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestEndDate is less than or equal to
        defaultAgriculturalProductionFiltering(
            "harvestEndDate.lessThanOrEqual=" + DEFAULT_HARVEST_END_DATE,
            "harvestEndDate.lessThanOrEqual=" + SMALLER_HARVEST_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestEndDate is less than
        defaultAgriculturalProductionFiltering(
            "harvestEndDate.lessThan=" + UPDATED_HARVEST_END_DATE,
            "harvestEndDate.lessThan=" + DEFAULT_HARVEST_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByHarvestEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where harvestEndDate is greater than
        defaultAgriculturalProductionFiltering(
            "harvestEndDate.greaterThan=" + SMALLER_HARVEST_END_DATE,
            "harvestEndDate.greaterThan=" + DEFAULT_HARVEST_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionQuantity equals to
        defaultAgriculturalProductionFiltering(
            "productionQuantity.equals=" + DEFAULT_PRODUCTION_QUANTITY,
            "productionQuantity.equals=" + UPDATED_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionQuantity in
        defaultAgriculturalProductionFiltering(
            "productionQuantity.in=" + DEFAULT_PRODUCTION_QUANTITY + "," + UPDATED_PRODUCTION_QUANTITY,
            "productionQuantity.in=" + UPDATED_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionQuantity is not null
        defaultAgriculturalProductionFiltering("productionQuantity.specified=true", "productionQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionQuantity is greater than or equal to
        defaultAgriculturalProductionFiltering(
            "productionQuantity.greaterThanOrEqual=" + DEFAULT_PRODUCTION_QUANTITY,
            "productionQuantity.greaterThanOrEqual=" + UPDATED_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionQuantity is less than or equal to
        defaultAgriculturalProductionFiltering(
            "productionQuantity.lessThanOrEqual=" + DEFAULT_PRODUCTION_QUANTITY,
            "productionQuantity.lessThanOrEqual=" + SMALLER_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionQuantity is less than
        defaultAgriculturalProductionFiltering(
            "productionQuantity.lessThan=" + UPDATED_PRODUCTION_QUANTITY,
            "productionQuantity.lessThan=" + DEFAULT_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionQuantity is greater than
        defaultAgriculturalProductionFiltering(
            "productionQuantity.greaterThan=" + SMALLER_PRODUCTION_QUANTITY,
            "productionQuantity.greaterThan=" + DEFAULT_PRODUCTION_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionUnit equals to
        defaultAgriculturalProductionFiltering(
            "productionUnit.equals=" + DEFAULT_PRODUCTION_UNIT,
            "productionUnit.equals=" + UPDATED_PRODUCTION_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionUnitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionUnit in
        defaultAgriculturalProductionFiltering(
            "productionUnit.in=" + DEFAULT_PRODUCTION_UNIT + "," + UPDATED_PRODUCTION_UNIT,
            "productionUnit.in=" + UPDATED_PRODUCTION_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionUnit is not null
        defaultAgriculturalProductionFiltering("productionUnit.specified=true", "productionUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionUnitContainsSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionUnit contains
        defaultAgriculturalProductionFiltering(
            "productionUnit.contains=" + DEFAULT_PRODUCTION_UNIT,
            "productionUnit.contains=" + UPDATED_PRODUCTION_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionUnitNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionUnit does not contain
        defaultAgriculturalProductionFiltering(
            "productionUnit.doesNotContain=" + UPDATED_PRODUCTION_UNIT,
            "productionUnit.doesNotContain=" + DEFAULT_PRODUCTION_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByExpectedAnnualProductionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where expectedAnnualProduction equals to
        defaultAgriculturalProductionFiltering(
            "expectedAnnualProduction.equals=" + DEFAULT_EXPECTED_ANNUAL_PRODUCTION,
            "expectedAnnualProduction.equals=" + UPDATED_EXPECTED_ANNUAL_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByExpectedAnnualProductionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where expectedAnnualProduction in
        defaultAgriculturalProductionFiltering(
            "expectedAnnualProduction.in=" + DEFAULT_EXPECTED_ANNUAL_PRODUCTION + "," + UPDATED_EXPECTED_ANNUAL_PRODUCTION,
            "expectedAnnualProduction.in=" + UPDATED_EXPECTED_ANNUAL_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByExpectedAnnualProductionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where expectedAnnualProduction is not null
        defaultAgriculturalProductionFiltering("expectedAnnualProduction.specified=true", "expectedAnnualProduction.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByExpectedAnnualProductionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where expectedAnnualProduction is greater than or equal to
        defaultAgriculturalProductionFiltering(
            "expectedAnnualProduction.greaterThanOrEqual=" + DEFAULT_EXPECTED_ANNUAL_PRODUCTION,
            "expectedAnnualProduction.greaterThanOrEqual=" + UPDATED_EXPECTED_ANNUAL_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByExpectedAnnualProductionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where expectedAnnualProduction is less than or equal to
        defaultAgriculturalProductionFiltering(
            "expectedAnnualProduction.lessThanOrEqual=" + DEFAULT_EXPECTED_ANNUAL_PRODUCTION,
            "expectedAnnualProduction.lessThanOrEqual=" + SMALLER_EXPECTED_ANNUAL_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByExpectedAnnualProductionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where expectedAnnualProduction is less than
        defaultAgriculturalProductionFiltering(
            "expectedAnnualProduction.lessThan=" + UPDATED_EXPECTED_ANNUAL_PRODUCTION,
            "expectedAnnualProduction.lessThan=" + DEFAULT_EXPECTED_ANNUAL_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByExpectedAnnualProductionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where expectedAnnualProduction is greater than
        defaultAgriculturalProductionFiltering(
            "expectedAnnualProduction.greaterThan=" + SMALLER_EXPECTED_ANNUAL_PRODUCTION,
            "expectedAnnualProduction.greaterThan=" + DEFAULT_EXPECTED_ANNUAL_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByNumberOfPlantsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where numberOfPlants equals to
        defaultAgriculturalProductionFiltering(
            "numberOfPlants.equals=" + DEFAULT_NUMBER_OF_PLANTS,
            "numberOfPlants.equals=" + UPDATED_NUMBER_OF_PLANTS
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByNumberOfPlantsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where numberOfPlants in
        defaultAgriculturalProductionFiltering(
            "numberOfPlants.in=" + DEFAULT_NUMBER_OF_PLANTS + "," + UPDATED_NUMBER_OF_PLANTS,
            "numberOfPlants.in=" + UPDATED_NUMBER_OF_PLANTS
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByNumberOfPlantsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where numberOfPlants is not null
        defaultAgriculturalProductionFiltering("numberOfPlants.specified=true", "numberOfPlants.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByNumberOfPlantsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where numberOfPlants is greater than or equal to
        defaultAgriculturalProductionFiltering(
            "numberOfPlants.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PLANTS,
            "numberOfPlants.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PLANTS
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByNumberOfPlantsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where numberOfPlants is less than or equal to
        defaultAgriculturalProductionFiltering(
            "numberOfPlants.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PLANTS,
            "numberOfPlants.lessThanOrEqual=" + SMALLER_NUMBER_OF_PLANTS
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByNumberOfPlantsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where numberOfPlants is less than
        defaultAgriculturalProductionFiltering(
            "numberOfPlants.lessThan=" + UPDATED_NUMBER_OF_PLANTS,
            "numberOfPlants.lessThan=" + DEFAULT_NUMBER_OF_PLANTS
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByNumberOfPlantsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where numberOfPlants is greater than
        defaultAgriculturalProductionFiltering(
            "numberOfPlants.greaterThan=" + SMALLER_NUMBER_OF_PLANTS,
            "numberOfPlants.greaterThan=" + DEFAULT_NUMBER_OF_PLANTS
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDensityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDensity equals to
        defaultAgriculturalProductionFiltering(
            "plantingDensity.equals=" + DEFAULT_PLANTING_DENSITY,
            "plantingDensity.equals=" + UPDATED_PLANTING_DENSITY
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDensityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDensity in
        defaultAgriculturalProductionFiltering(
            "plantingDensity.in=" + DEFAULT_PLANTING_DENSITY + "," + UPDATED_PLANTING_DENSITY,
            "plantingDensity.in=" + UPDATED_PLANTING_DENSITY
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDensityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDensity is not null
        defaultAgriculturalProductionFiltering("plantingDensity.specified=true", "plantingDensity.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDensityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDensity is greater than or equal to
        defaultAgriculturalProductionFiltering(
            "plantingDensity.greaterThanOrEqual=" + DEFAULT_PLANTING_DENSITY,
            "plantingDensity.greaterThanOrEqual=" + UPDATED_PLANTING_DENSITY
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDensityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDensity is less than or equal to
        defaultAgriculturalProductionFiltering(
            "plantingDensity.lessThanOrEqual=" + DEFAULT_PLANTING_DENSITY,
            "plantingDensity.lessThanOrEqual=" + SMALLER_PLANTING_DENSITY
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDensityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDensity is less than
        defaultAgriculturalProductionFiltering(
            "plantingDensity.lessThan=" + UPDATED_PLANTING_DENSITY,
            "plantingDensity.lessThan=" + DEFAULT_PLANTING_DENSITY
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByPlantingDensityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where plantingDensity is greater than
        defaultAgriculturalProductionFiltering(
            "plantingDensity.greaterThan=" + SMALLER_PLANTING_DENSITY,
            "plantingDensity.greaterThan=" + DEFAULT_PLANTING_DENSITY
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionYear equals to
        defaultAgriculturalProductionFiltering(
            "productionYear.equals=" + DEFAULT_PRODUCTION_YEAR,
            "productionYear.equals=" + UPDATED_PRODUCTION_YEAR
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionYear in
        defaultAgriculturalProductionFiltering(
            "productionYear.in=" + DEFAULT_PRODUCTION_YEAR + "," + UPDATED_PRODUCTION_YEAR,
            "productionYear.in=" + UPDATED_PRODUCTION_YEAR
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionYear is not null
        defaultAgriculturalProductionFiltering("productionYear.specified=true", "productionYear.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionYear is greater than or equal to
        defaultAgriculturalProductionFiltering(
            "productionYear.greaterThanOrEqual=" + DEFAULT_PRODUCTION_YEAR,
            "productionYear.greaterThanOrEqual=" + UPDATED_PRODUCTION_YEAR
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionYear is less than or equal to
        defaultAgriculturalProductionFiltering(
            "productionYear.lessThanOrEqual=" + DEFAULT_PRODUCTION_YEAR,
            "productionYear.lessThanOrEqual=" + SMALLER_PRODUCTION_YEAR
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionYear is less than
        defaultAgriculturalProductionFiltering(
            "productionYear.lessThan=" + UPDATED_PRODUCTION_YEAR,
            "productionYear.lessThan=" + DEFAULT_PRODUCTION_YEAR
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByProductionYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where productionYear is greater than
        defaultAgriculturalProductionFiltering(
            "productionYear.greaterThan=" + SMALLER_PRODUCTION_YEAR,
            "productionYear.greaterThan=" + DEFAULT_PRODUCTION_YEAR
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where status equals to
        defaultAgriculturalProductionFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where status in
        defaultAgriculturalProductionFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        // Get all the agriculturalProductionList where status is not null
        defaultAgriculturalProductionFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByAgriculturalActivityIsEqualToSomething() throws Exception {
        AgriculturalActivity agriculturalActivity;
        if (TestUtil.findAll(em, AgriculturalActivity.class).isEmpty()) {
            agriculturalProductionRepository.saveAndFlush(agriculturalProduction);
            agriculturalActivity = AgriculturalActivityResourceIT.createEntity();
        } else {
            agriculturalActivity = TestUtil.findAll(em, AgriculturalActivity.class).get(0);
        }
        em.persist(agriculturalActivity);
        em.flush();
        agriculturalProduction.setAgriculturalActivity(agriculturalActivity);
        agriculturalProductionRepository.saveAndFlush(agriculturalProduction);
        Long agriculturalActivityId = agriculturalActivity.getId();
        // Get all the agriculturalProductionList where agriculturalActivity equals to agriculturalActivityId
        defaultAgriculturalProductionShouldBeFound("agriculturalActivityId.equals=" + agriculturalActivityId);

        // Get all the agriculturalProductionList where agriculturalActivity equals to (agriculturalActivityId + 1)
        defaultAgriculturalProductionShouldNotBeFound("agriculturalActivityId.equals=" + (agriculturalActivityId + 1));
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByCropIsEqualToSomething() throws Exception {
        Crop crop;
        if (TestUtil.findAll(em, Crop.class).isEmpty()) {
            agriculturalProductionRepository.saveAndFlush(agriculturalProduction);
            crop = CropResourceIT.createEntity();
        } else {
            crop = TestUtil.findAll(em, Crop.class).get(0);
        }
        em.persist(crop);
        em.flush();
        agriculturalProduction.setCrop(crop);
        agriculturalProductionRepository.saveAndFlush(agriculturalProduction);
        Long cropId = crop.getId();
        // Get all the agriculturalProductionList where crop equals to cropId
        defaultAgriculturalProductionShouldBeFound("cropId.equals=" + cropId);

        // Get all the agriculturalProductionList where crop equals to (cropId + 1)
        defaultAgriculturalProductionShouldNotBeFound("cropId.equals=" + (cropId + 1));
    }

    @Test
    @Transactional
    void getAllAgriculturalProductionsByCropVarietyIsEqualToSomething() throws Exception {
        CropVariety cropVariety;
        if (TestUtil.findAll(em, CropVariety.class).isEmpty()) {
            agriculturalProductionRepository.saveAndFlush(agriculturalProduction);
            cropVariety = CropVarietyResourceIT.createEntity();
        } else {
            cropVariety = TestUtil.findAll(em, CropVariety.class).get(0);
        }
        em.persist(cropVariety);
        em.flush();
        agriculturalProduction.setCropVariety(cropVariety);
        agriculturalProductionRepository.saveAndFlush(agriculturalProduction);
        Long cropVarietyId = cropVariety.getId();
        // Get all the agriculturalProductionList where cropVariety equals to cropVarietyId
        defaultAgriculturalProductionShouldBeFound("cropVarietyId.equals=" + cropVarietyId);

        // Get all the agriculturalProductionList where cropVariety equals to (cropVarietyId + 1)
        defaultAgriculturalProductionShouldNotBeFound("cropVarietyId.equals=" + (cropVarietyId + 1));
    }

    private void defaultAgriculturalProductionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAgriculturalProductionShouldBeFound(shouldBeFound);
        defaultAgriculturalProductionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgriculturalProductionShouldBeFound(String filter) throws Exception {
        restAgriculturalProductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agriculturalProduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(sameNumber(DEFAULT_AREA))))
            .andExpect(jsonPath("$.[*].areaUnit").value(hasItem(DEFAULT_AREA_UNIT)))
            .andExpect(jsonPath("$.[*].plantingDate").value(hasItem(DEFAULT_PLANTING_DATE.toString())))
            .andExpect(jsonPath("$.[*].harvestStartDate").value(hasItem(DEFAULT_HARVEST_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].harvestEndDate").value(hasItem(DEFAULT_HARVEST_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].productionQuantity").value(hasItem(sameNumber(DEFAULT_PRODUCTION_QUANTITY))))
            .andExpect(jsonPath("$.[*].productionUnit").value(hasItem(DEFAULT_PRODUCTION_UNIT)))
            .andExpect(jsonPath("$.[*].expectedAnnualProduction").value(hasItem(sameNumber(DEFAULT_EXPECTED_ANNUAL_PRODUCTION))))
            .andExpect(jsonPath("$.[*].numberOfPlants").value(hasItem(DEFAULT_NUMBER_OF_PLANTS)))
            .andExpect(jsonPath("$.[*].plantingDensity").value(hasItem(sameNumber(DEFAULT_PLANTING_DENSITY))))
            .andExpect(jsonPath("$.[*].productionYear").value(hasItem(DEFAULT_PRODUCTION_YEAR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restAgriculturalProductionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgriculturalProductionShouldNotBeFound(String filter) throws Exception {
        restAgriculturalProductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgriculturalProductionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgriculturalProduction() throws Exception {
        // Get the agriculturalProduction
        restAgriculturalProductionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgriculturalProduction() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agriculturalProduction
        AgriculturalProduction updatedAgriculturalProduction = agriculturalProductionRepository
            .findById(agriculturalProduction.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAgriculturalProduction are not directly saved in db
        em.detach(updatedAgriculturalProduction);
        updatedAgriculturalProduction
            .area(UPDATED_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .plantingDate(UPDATED_PLANTING_DATE)
            .harvestStartDate(UPDATED_HARVEST_START_DATE)
            .harvestEndDate(UPDATED_HARVEST_END_DATE)
            .productionQuantity(UPDATED_PRODUCTION_QUANTITY)
            .productionUnit(UPDATED_PRODUCTION_UNIT)
            .expectedAnnualProduction(UPDATED_EXPECTED_ANNUAL_PRODUCTION)
            .numberOfPlants(UPDATED_NUMBER_OF_PLANTS)
            .plantingDensity(UPDATED_PLANTING_DENSITY)
            .productionYear(UPDATED_PRODUCTION_YEAR)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
        AgriculturalProductionDTO agriculturalProductionDTO = agriculturalProductionMapper.toDto(updatedAgriculturalProduction);

        restAgriculturalProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agriculturalProductionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agriculturalProductionDTO))
            )
            .andExpect(status().isOk());

        // Validate the AgriculturalProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgriculturalProductionToMatchAllProperties(updatedAgriculturalProduction);
    }

    @Test
    @Transactional
    void putNonExistingAgriculturalProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculturalProduction.setId(longCount.incrementAndGet());

        // Create the AgriculturalProduction
        AgriculturalProductionDTO agriculturalProductionDTO = agriculturalProductionMapper.toDto(agriculturalProduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgriculturalProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agriculturalProductionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agriculturalProductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgriculturalProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculturalProduction.setId(longCount.incrementAndGet());

        // Create the AgriculturalProduction
        AgriculturalProductionDTO agriculturalProductionDTO = agriculturalProductionMapper.toDto(agriculturalProduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculturalProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agriculturalProductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgriculturalProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculturalProduction.setId(longCount.incrementAndGet());

        // Create the AgriculturalProduction
        AgriculturalProductionDTO agriculturalProductionDTO = agriculturalProductionMapper.toDto(agriculturalProduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculturalProductionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculturalProductionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgriculturalProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgriculturalProductionWithPatch() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agriculturalProduction using partial update
        AgriculturalProduction partialUpdatedAgriculturalProduction = new AgriculturalProduction();
        partialUpdatedAgriculturalProduction.setId(agriculturalProduction.getId());

        partialUpdatedAgriculturalProduction
            .area(UPDATED_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .plantingDate(UPDATED_PLANTING_DATE)
            .harvestStartDate(UPDATED_HARVEST_START_DATE)
            .harvestEndDate(UPDATED_HARVEST_END_DATE)
            .productionQuantity(UPDATED_PRODUCTION_QUANTITY)
            .expectedAnnualProduction(UPDATED_EXPECTED_ANNUAL_PRODUCTION)
            .numberOfPlants(UPDATED_NUMBER_OF_PLANTS)
            .productionYear(UPDATED_PRODUCTION_YEAR)
            .notes(UPDATED_NOTES);

        restAgriculturalProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgriculturalProduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgriculturalProduction))
            )
            .andExpect(status().isOk());

        // Validate the AgriculturalProduction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgriculturalProductionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAgriculturalProduction, agriculturalProduction),
            getPersistedAgriculturalProduction(agriculturalProduction)
        );
    }

    @Test
    @Transactional
    void fullUpdateAgriculturalProductionWithPatch() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agriculturalProduction using partial update
        AgriculturalProduction partialUpdatedAgriculturalProduction = new AgriculturalProduction();
        partialUpdatedAgriculturalProduction.setId(agriculturalProduction.getId());

        partialUpdatedAgriculturalProduction
            .area(UPDATED_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .plantingDate(UPDATED_PLANTING_DATE)
            .harvestStartDate(UPDATED_HARVEST_START_DATE)
            .harvestEndDate(UPDATED_HARVEST_END_DATE)
            .productionQuantity(UPDATED_PRODUCTION_QUANTITY)
            .productionUnit(UPDATED_PRODUCTION_UNIT)
            .expectedAnnualProduction(UPDATED_EXPECTED_ANNUAL_PRODUCTION)
            .numberOfPlants(UPDATED_NUMBER_OF_PLANTS)
            .plantingDensity(UPDATED_PLANTING_DENSITY)
            .productionYear(UPDATED_PRODUCTION_YEAR)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);

        restAgriculturalProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgriculturalProduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgriculturalProduction))
            )
            .andExpect(status().isOk());

        // Validate the AgriculturalProduction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgriculturalProductionUpdatableFieldsEquals(
            partialUpdatedAgriculturalProduction,
            getPersistedAgriculturalProduction(partialUpdatedAgriculturalProduction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAgriculturalProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculturalProduction.setId(longCount.incrementAndGet());

        // Create the AgriculturalProduction
        AgriculturalProductionDTO agriculturalProductionDTO = agriculturalProductionMapper.toDto(agriculturalProduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgriculturalProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agriculturalProductionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agriculturalProductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgriculturalProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculturalProduction.setId(longCount.incrementAndGet());

        // Create the AgriculturalProduction
        AgriculturalProductionDTO agriculturalProductionDTO = agriculturalProductionMapper.toDto(agriculturalProduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculturalProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agriculturalProductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgriculturalProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculturalProduction.setId(longCount.incrementAndGet());

        // Create the AgriculturalProduction
        AgriculturalProductionDTO agriculturalProductionDTO = agriculturalProductionMapper.toDto(agriculturalProduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculturalProductionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agriculturalProductionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgriculturalProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgriculturalProduction() throws Exception {
        // Initialize the database
        insertedAgriculturalProduction = agriculturalProductionRepository.saveAndFlush(agriculturalProduction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agriculturalProduction
        restAgriculturalProductionMockMvc
            .perform(delete(ENTITY_API_URL_ID, agriculturalProduction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agriculturalProductionRepository.count();
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

    protected AgriculturalProduction getPersistedAgriculturalProduction(AgriculturalProduction agriculturalProduction) {
        return agriculturalProductionRepository.findById(agriculturalProduction.getId()).orElseThrow();
    }

    protected void assertPersistedAgriculturalProductionToMatchAllProperties(AgriculturalProduction expectedAgriculturalProduction) {
        assertAgriculturalProductionAllPropertiesEquals(
            expectedAgriculturalProduction,
            getPersistedAgriculturalProduction(expectedAgriculturalProduction)
        );
    }

    protected void assertPersistedAgriculturalProductionToMatchUpdatableProperties(AgriculturalProduction expectedAgriculturalProduction) {
        assertAgriculturalProductionAllUpdatablePropertiesEquals(
            expectedAgriculturalProduction,
            getPersistedAgriculturalProduction(expectedAgriculturalProduction)
        );
    }
}
