package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.AquacultureActivityAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static com.naline.coopfull.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.AquacultureActivity;
import com.naline.coopfull.domain.AquaticSpecies;
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.domain.enumeration.AquacultureOwnershipType;
import com.naline.coopfull.domain.enumeration.AquacultureProductionMode;
import com.naline.coopfull.domain.enumeration.AquacultureProductionType;
import com.naline.coopfull.domain.enumeration.AquacultureStatus;
import com.naline.coopfull.domain.enumeration.AquacultureSystemType;
import com.naline.coopfull.repository.AquacultureActivityRepository;
import com.naline.coopfull.service.AquacultureActivityService;
import com.naline.coopfull.service.dto.AquacultureActivityDTO;
import com.naline.coopfull.service.mapper.AquacultureActivityMapper;
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
 * Integration tests for the {@link AquacultureActivityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AquacultureActivityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final AquacultureProductionMode DEFAULT_PRODUCTION_MODE = AquacultureProductionMode.FAMILY;
    private static final AquacultureProductionMode UPDATED_PRODUCTION_MODE = AquacultureProductionMode.INDIVIDUAL;

    private static final AquacultureOwnershipType DEFAULT_OWNERSHIP_TYPE = AquacultureOwnershipType.OWNER;
    private static final AquacultureOwnershipType UPDATED_OWNERSHIP_TYPE = AquacultureOwnershipType.LEASED;

    private static final AquacultureProductionType DEFAULT_PRODUCTION_TYPE = AquacultureProductionType.FOOD;
    private static final AquacultureProductionType UPDATED_PRODUCTION_TYPE = AquacultureProductionType.BREEDING;

    private static final AquacultureSystemType DEFAULT_SYSTEM_TYPE = AquacultureSystemType.POND;
    private static final AquacultureSystemType UPDATED_SYSTEM_TYPE = AquacultureSystemType.TANK;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_TOTAL_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AREA = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AREA = new BigDecimal(1 - 1);

    private static final String DEFAULT_AREA_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_AREA_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_WATER_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_WATER_SOURCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_PRODUCTION_UNITS = 1;
    private static final Integer UPDATED_NUMBER_OF_PRODUCTION_UNITS = 2;
    private static final Integer SMALLER_NUMBER_OF_PRODUCTION_UNITS = 1 - 1;

    private static final String DEFAULT_PRODUCTION_UNIT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCTION_UNIT_DESCRIPTION = "BBBBBBBBBB";

    private static final AquacultureStatus DEFAULT_STATUS = AquacultureStatus.ACTIVE;
    private static final AquacultureStatus UPDATED_STATUS = AquacultureStatus.INACTIVE;

    private static final BigDecimal DEFAULT_ANNUAL_REVENUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ANNUAL_REVENUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ANNUAL_REVENUE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MONTHLY_REVENUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTHLY_REVENUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTHLY_REVENUE = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_EMPLOYEES = 1;
    private static final Integer UPDATED_EMPLOYEES = 2;
    private static final Integer SMALLER_EMPLOYEES = 1 - 1;

    private static final String DEFAULT_CERTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_CERTIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/aquaculture-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AquacultureActivityRepository aquacultureActivityRepository;

    @Mock
    private AquacultureActivityRepository aquacultureActivityRepositoryMock;

    @Autowired
    private AquacultureActivityMapper aquacultureActivityMapper;

    @Mock
    private AquacultureActivityService aquacultureActivityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAquacultureActivityMockMvc;

    private AquacultureActivity aquacultureActivity;

    private AquacultureActivity insertedAquacultureActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AquacultureActivity createEntity() {
        return new AquacultureActivity()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .productionMode(DEFAULT_PRODUCTION_MODE)
            .ownershipType(DEFAULT_OWNERSHIP_TYPE)
            .productionType(DEFAULT_PRODUCTION_TYPE)
            .systemType(DEFAULT_SYSTEM_TYPE)
            .startDate(DEFAULT_START_DATE)
            .totalArea(DEFAULT_TOTAL_AREA)
            .areaUnit(DEFAULT_AREA_UNIT)
            .waterSource(DEFAULT_WATER_SOURCE)
            .numberOfProductionUnits(DEFAULT_NUMBER_OF_PRODUCTION_UNITS)
            .productionUnitDescription(DEFAULT_PRODUCTION_UNIT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .annualRevenue(DEFAULT_ANNUAL_REVENUE)
            .monthlyRevenue(DEFAULT_MONTHLY_REVENUE)
            .employees(DEFAULT_EMPLOYEES)
            .certification(DEFAULT_CERTIFICATION)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AquacultureActivity createUpdatedEntity() {
        return new AquacultureActivity()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .productionMode(UPDATED_PRODUCTION_MODE)
            .ownershipType(UPDATED_OWNERSHIP_TYPE)
            .productionType(UPDATED_PRODUCTION_TYPE)
            .systemType(UPDATED_SYSTEM_TYPE)
            .startDate(UPDATED_START_DATE)
            .totalArea(UPDATED_TOTAL_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .waterSource(UPDATED_WATER_SOURCE)
            .numberOfProductionUnits(UPDATED_NUMBER_OF_PRODUCTION_UNITS)
            .productionUnitDescription(UPDATED_PRODUCTION_UNIT_DESCRIPTION)
            .status(UPDATED_STATUS)
            .annualRevenue(UPDATED_ANNUAL_REVENUE)
            .monthlyRevenue(UPDATED_MONTHLY_REVENUE)
            .employees(UPDATED_EMPLOYEES)
            .certification(UPDATED_CERTIFICATION)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        aquacultureActivity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAquacultureActivity != null) {
            aquacultureActivityRepository.delete(insertedAquacultureActivity);
            insertedAquacultureActivity = null;
        }
    }

    @Test
    @Transactional
    void createAquacultureActivity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AquacultureActivity
        AquacultureActivityDTO aquacultureActivityDTO = aquacultureActivityMapper.toDto(aquacultureActivity);
        var returnedAquacultureActivityDTO = om.readValue(
            restAquacultureActivityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquacultureActivityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AquacultureActivityDTO.class
        );

        // Validate the AquacultureActivity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAquacultureActivity = aquacultureActivityMapper.toEntity(returnedAquacultureActivityDTO);
        assertAquacultureActivityUpdatableFieldsEquals(
            returnedAquacultureActivity,
            getPersistedAquacultureActivity(returnedAquacultureActivity)
        );

        insertedAquacultureActivity = returnedAquacultureActivity;
    }

    @Test
    @Transactional
    void createAquacultureActivityWithExistingId() throws Exception {
        // Create the AquacultureActivity with an existing ID
        aquacultureActivity.setId(1L);
        AquacultureActivityDTO aquacultureActivityDTO = aquacultureActivityMapper.toDto(aquacultureActivity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAquacultureActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquacultureActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AquacultureActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aquacultureActivity.setName(null);

        // Create the AquacultureActivity, which fails.
        AquacultureActivityDTO aquacultureActivityDTO = aquacultureActivityMapper.toDto(aquacultureActivity);

        restAquacultureActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquacultureActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aquacultureActivity.setStatus(null);

        // Create the AquacultureActivity, which fails.
        AquacultureActivityDTO aquacultureActivityDTO = aquacultureActivityMapper.toDto(aquacultureActivity);

        restAquacultureActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquacultureActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAquacultureActivities() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList
        restAquacultureActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aquacultureActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productionMode").value(hasItem(DEFAULT_PRODUCTION_MODE.toString())))
            .andExpect(jsonPath("$.[*].ownershipType").value(hasItem(DEFAULT_OWNERSHIP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].productionType").value(hasItem(DEFAULT_PRODUCTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].systemType").value(hasItem(DEFAULT_SYSTEM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalArea").value(hasItem(sameNumber(DEFAULT_TOTAL_AREA))))
            .andExpect(jsonPath("$.[*].areaUnit").value(hasItem(DEFAULT_AREA_UNIT)))
            .andExpect(jsonPath("$.[*].waterSource").value(hasItem(DEFAULT_WATER_SOURCE)))
            .andExpect(jsonPath("$.[*].numberOfProductionUnits").value(hasItem(DEFAULT_NUMBER_OF_PRODUCTION_UNITS)))
            .andExpect(jsonPath("$.[*].productionUnitDescription").value(hasItem(DEFAULT_PRODUCTION_UNIT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].annualRevenue").value(hasItem(sameNumber(DEFAULT_ANNUAL_REVENUE))))
            .andExpect(jsonPath("$.[*].monthlyRevenue").value(hasItem(sameNumber(DEFAULT_MONTHLY_REVENUE))))
            .andExpect(jsonPath("$.[*].employees").value(hasItem(DEFAULT_EMPLOYEES)))
            .andExpect(jsonPath("$.[*].certification").value(hasItem(DEFAULT_CERTIFICATION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAquacultureActivitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(aquacultureActivityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAquacultureActivityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(aquacultureActivityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAquacultureActivitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(aquacultureActivityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAquacultureActivityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(aquacultureActivityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAquacultureActivity() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get the aquacultureActivity
        restAquacultureActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, aquacultureActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aquacultureActivity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.productionMode").value(DEFAULT_PRODUCTION_MODE.toString()))
            .andExpect(jsonPath("$.ownershipType").value(DEFAULT_OWNERSHIP_TYPE.toString()))
            .andExpect(jsonPath("$.productionType").value(DEFAULT_PRODUCTION_TYPE.toString()))
            .andExpect(jsonPath("$.systemType").value(DEFAULT_SYSTEM_TYPE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.totalArea").value(sameNumber(DEFAULT_TOTAL_AREA)))
            .andExpect(jsonPath("$.areaUnit").value(DEFAULT_AREA_UNIT))
            .andExpect(jsonPath("$.waterSource").value(DEFAULT_WATER_SOURCE))
            .andExpect(jsonPath("$.numberOfProductionUnits").value(DEFAULT_NUMBER_OF_PRODUCTION_UNITS))
            .andExpect(jsonPath("$.productionUnitDescription").value(DEFAULT_PRODUCTION_UNIT_DESCRIPTION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.annualRevenue").value(sameNumber(DEFAULT_ANNUAL_REVENUE)))
            .andExpect(jsonPath("$.monthlyRevenue").value(sameNumber(DEFAULT_MONTHLY_REVENUE)))
            .andExpect(jsonPath("$.employees").value(DEFAULT_EMPLOYEES))
            .andExpect(jsonPath("$.certification").value(DEFAULT_CERTIFICATION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getAquacultureActivitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        Long id = aquacultureActivity.getId();

        defaultAquacultureActivityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAquacultureActivityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAquacultureActivityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where name equals to
        defaultAquacultureActivityFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where name in
        defaultAquacultureActivityFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where name is not null
        defaultAquacultureActivityFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where name contains
        defaultAquacultureActivityFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where name does not contain
        defaultAquacultureActivityFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByProductionModeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where productionMode equals to
        defaultAquacultureActivityFiltering(
            "productionMode.equals=" + DEFAULT_PRODUCTION_MODE,
            "productionMode.equals=" + UPDATED_PRODUCTION_MODE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByProductionModeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where productionMode in
        defaultAquacultureActivityFiltering(
            "productionMode.in=" + DEFAULT_PRODUCTION_MODE + "," + UPDATED_PRODUCTION_MODE,
            "productionMode.in=" + UPDATED_PRODUCTION_MODE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByProductionModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where productionMode is not null
        defaultAquacultureActivityFiltering("productionMode.specified=true", "productionMode.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByOwnershipTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where ownershipType equals to
        defaultAquacultureActivityFiltering(
            "ownershipType.equals=" + DEFAULT_OWNERSHIP_TYPE,
            "ownershipType.equals=" + UPDATED_OWNERSHIP_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByOwnershipTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where ownershipType in
        defaultAquacultureActivityFiltering(
            "ownershipType.in=" + DEFAULT_OWNERSHIP_TYPE + "," + UPDATED_OWNERSHIP_TYPE,
            "ownershipType.in=" + UPDATED_OWNERSHIP_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByOwnershipTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where ownershipType is not null
        defaultAquacultureActivityFiltering("ownershipType.specified=true", "ownershipType.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByProductionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where productionType equals to
        defaultAquacultureActivityFiltering(
            "productionType.equals=" + DEFAULT_PRODUCTION_TYPE,
            "productionType.equals=" + UPDATED_PRODUCTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByProductionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where productionType in
        defaultAquacultureActivityFiltering(
            "productionType.in=" + DEFAULT_PRODUCTION_TYPE + "," + UPDATED_PRODUCTION_TYPE,
            "productionType.in=" + UPDATED_PRODUCTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByProductionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where productionType is not null
        defaultAquacultureActivityFiltering("productionType.specified=true", "productionType.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesBySystemTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where systemType equals to
        defaultAquacultureActivityFiltering("systemType.equals=" + DEFAULT_SYSTEM_TYPE, "systemType.equals=" + UPDATED_SYSTEM_TYPE);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesBySystemTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where systemType in
        defaultAquacultureActivityFiltering(
            "systemType.in=" + DEFAULT_SYSTEM_TYPE + "," + UPDATED_SYSTEM_TYPE,
            "systemType.in=" + UPDATED_SYSTEM_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesBySystemTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where systemType is not null
        defaultAquacultureActivityFiltering("systemType.specified=true", "systemType.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where startDate equals to
        defaultAquacultureActivityFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where startDate in
        defaultAquacultureActivityFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where startDate is not null
        defaultAquacultureActivityFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where startDate is greater than or equal to
        defaultAquacultureActivityFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where startDate is less than or equal to
        defaultAquacultureActivityFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where startDate is less than
        defaultAquacultureActivityFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where startDate is greater than
        defaultAquacultureActivityFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByTotalAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where totalArea equals to
        defaultAquacultureActivityFiltering("totalArea.equals=" + DEFAULT_TOTAL_AREA, "totalArea.equals=" + UPDATED_TOTAL_AREA);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByTotalAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where totalArea in
        defaultAquacultureActivityFiltering(
            "totalArea.in=" + DEFAULT_TOTAL_AREA + "," + UPDATED_TOTAL_AREA,
            "totalArea.in=" + UPDATED_TOTAL_AREA
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByTotalAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where totalArea is not null
        defaultAquacultureActivityFiltering("totalArea.specified=true", "totalArea.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByTotalAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where totalArea is greater than or equal to
        defaultAquacultureActivityFiltering(
            "totalArea.greaterThanOrEqual=" + DEFAULT_TOTAL_AREA,
            "totalArea.greaterThanOrEqual=" + UPDATED_TOTAL_AREA
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByTotalAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where totalArea is less than or equal to
        defaultAquacultureActivityFiltering(
            "totalArea.lessThanOrEqual=" + DEFAULT_TOTAL_AREA,
            "totalArea.lessThanOrEqual=" + SMALLER_TOTAL_AREA
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByTotalAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where totalArea is less than
        defaultAquacultureActivityFiltering("totalArea.lessThan=" + UPDATED_TOTAL_AREA, "totalArea.lessThan=" + DEFAULT_TOTAL_AREA);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByTotalAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where totalArea is greater than
        defaultAquacultureActivityFiltering("totalArea.greaterThan=" + SMALLER_TOTAL_AREA, "totalArea.greaterThan=" + DEFAULT_TOTAL_AREA);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAreaUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where areaUnit equals to
        defaultAquacultureActivityFiltering("areaUnit.equals=" + DEFAULT_AREA_UNIT, "areaUnit.equals=" + UPDATED_AREA_UNIT);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAreaUnitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where areaUnit in
        defaultAquacultureActivityFiltering(
            "areaUnit.in=" + DEFAULT_AREA_UNIT + "," + UPDATED_AREA_UNIT,
            "areaUnit.in=" + UPDATED_AREA_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAreaUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where areaUnit is not null
        defaultAquacultureActivityFiltering("areaUnit.specified=true", "areaUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAreaUnitContainsSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where areaUnit contains
        defaultAquacultureActivityFiltering("areaUnit.contains=" + DEFAULT_AREA_UNIT, "areaUnit.contains=" + UPDATED_AREA_UNIT);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAreaUnitNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where areaUnit does not contain
        defaultAquacultureActivityFiltering("areaUnit.doesNotContain=" + UPDATED_AREA_UNIT, "areaUnit.doesNotContain=" + DEFAULT_AREA_UNIT);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByWaterSourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where waterSource equals to
        defaultAquacultureActivityFiltering("waterSource.equals=" + DEFAULT_WATER_SOURCE, "waterSource.equals=" + UPDATED_WATER_SOURCE);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByWaterSourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where waterSource in
        defaultAquacultureActivityFiltering(
            "waterSource.in=" + DEFAULT_WATER_SOURCE + "," + UPDATED_WATER_SOURCE,
            "waterSource.in=" + UPDATED_WATER_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByWaterSourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where waterSource is not null
        defaultAquacultureActivityFiltering("waterSource.specified=true", "waterSource.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByWaterSourceContainsSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where waterSource contains
        defaultAquacultureActivityFiltering("waterSource.contains=" + DEFAULT_WATER_SOURCE, "waterSource.contains=" + UPDATED_WATER_SOURCE);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByWaterSourceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where waterSource does not contain
        defaultAquacultureActivityFiltering(
            "waterSource.doesNotContain=" + UPDATED_WATER_SOURCE,
            "waterSource.doesNotContain=" + DEFAULT_WATER_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByNumberOfProductionUnitsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where numberOfProductionUnits equals to
        defaultAquacultureActivityFiltering(
            "numberOfProductionUnits.equals=" + DEFAULT_NUMBER_OF_PRODUCTION_UNITS,
            "numberOfProductionUnits.equals=" + UPDATED_NUMBER_OF_PRODUCTION_UNITS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByNumberOfProductionUnitsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where numberOfProductionUnits in
        defaultAquacultureActivityFiltering(
            "numberOfProductionUnits.in=" + DEFAULT_NUMBER_OF_PRODUCTION_UNITS + "," + UPDATED_NUMBER_OF_PRODUCTION_UNITS,
            "numberOfProductionUnits.in=" + UPDATED_NUMBER_OF_PRODUCTION_UNITS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByNumberOfProductionUnitsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where numberOfProductionUnits is not null
        defaultAquacultureActivityFiltering("numberOfProductionUnits.specified=true", "numberOfProductionUnits.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByNumberOfProductionUnitsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where numberOfProductionUnits is greater than or equal to
        defaultAquacultureActivityFiltering(
            "numberOfProductionUnits.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PRODUCTION_UNITS,
            "numberOfProductionUnits.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PRODUCTION_UNITS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByNumberOfProductionUnitsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where numberOfProductionUnits is less than or equal to
        defaultAquacultureActivityFiltering(
            "numberOfProductionUnits.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PRODUCTION_UNITS,
            "numberOfProductionUnits.lessThanOrEqual=" + SMALLER_NUMBER_OF_PRODUCTION_UNITS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByNumberOfProductionUnitsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where numberOfProductionUnits is less than
        defaultAquacultureActivityFiltering(
            "numberOfProductionUnits.lessThan=" + UPDATED_NUMBER_OF_PRODUCTION_UNITS,
            "numberOfProductionUnits.lessThan=" + DEFAULT_NUMBER_OF_PRODUCTION_UNITS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByNumberOfProductionUnitsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where numberOfProductionUnits is greater than
        defaultAquacultureActivityFiltering(
            "numberOfProductionUnits.greaterThan=" + SMALLER_NUMBER_OF_PRODUCTION_UNITS,
            "numberOfProductionUnits.greaterThan=" + DEFAULT_NUMBER_OF_PRODUCTION_UNITS
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByProductionUnitDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where productionUnitDescription equals to
        defaultAquacultureActivityFiltering(
            "productionUnitDescription.equals=" + DEFAULT_PRODUCTION_UNIT_DESCRIPTION,
            "productionUnitDescription.equals=" + UPDATED_PRODUCTION_UNIT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByProductionUnitDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where productionUnitDescription in
        defaultAquacultureActivityFiltering(
            "productionUnitDescription.in=" + DEFAULT_PRODUCTION_UNIT_DESCRIPTION + "," + UPDATED_PRODUCTION_UNIT_DESCRIPTION,
            "productionUnitDescription.in=" + UPDATED_PRODUCTION_UNIT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByProductionUnitDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where productionUnitDescription is not null
        defaultAquacultureActivityFiltering("productionUnitDescription.specified=true", "productionUnitDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByProductionUnitDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where productionUnitDescription contains
        defaultAquacultureActivityFiltering(
            "productionUnitDescription.contains=" + DEFAULT_PRODUCTION_UNIT_DESCRIPTION,
            "productionUnitDescription.contains=" + UPDATED_PRODUCTION_UNIT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByProductionUnitDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where productionUnitDescription does not contain
        defaultAquacultureActivityFiltering(
            "productionUnitDescription.doesNotContain=" + UPDATED_PRODUCTION_UNIT_DESCRIPTION,
            "productionUnitDescription.doesNotContain=" + DEFAULT_PRODUCTION_UNIT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where status equals to
        defaultAquacultureActivityFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where status in
        defaultAquacultureActivityFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where status is not null
        defaultAquacultureActivityFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAnnualRevenueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where annualRevenue equals to
        defaultAquacultureActivityFiltering(
            "annualRevenue.equals=" + DEFAULT_ANNUAL_REVENUE,
            "annualRevenue.equals=" + UPDATED_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAnnualRevenueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where annualRevenue in
        defaultAquacultureActivityFiltering(
            "annualRevenue.in=" + DEFAULT_ANNUAL_REVENUE + "," + UPDATED_ANNUAL_REVENUE,
            "annualRevenue.in=" + UPDATED_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAnnualRevenueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where annualRevenue is not null
        defaultAquacultureActivityFiltering("annualRevenue.specified=true", "annualRevenue.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAnnualRevenueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where annualRevenue is greater than or equal to
        defaultAquacultureActivityFiltering(
            "annualRevenue.greaterThanOrEqual=" + DEFAULT_ANNUAL_REVENUE,
            "annualRevenue.greaterThanOrEqual=" + UPDATED_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAnnualRevenueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where annualRevenue is less than or equal to
        defaultAquacultureActivityFiltering(
            "annualRevenue.lessThanOrEqual=" + DEFAULT_ANNUAL_REVENUE,
            "annualRevenue.lessThanOrEqual=" + SMALLER_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAnnualRevenueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where annualRevenue is less than
        defaultAquacultureActivityFiltering(
            "annualRevenue.lessThan=" + UPDATED_ANNUAL_REVENUE,
            "annualRevenue.lessThan=" + DEFAULT_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAnnualRevenueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where annualRevenue is greater than
        defaultAquacultureActivityFiltering(
            "annualRevenue.greaterThan=" + SMALLER_ANNUAL_REVENUE,
            "annualRevenue.greaterThan=" + DEFAULT_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByMonthlyRevenueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where monthlyRevenue equals to
        defaultAquacultureActivityFiltering(
            "monthlyRevenue.equals=" + DEFAULT_MONTHLY_REVENUE,
            "monthlyRevenue.equals=" + UPDATED_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByMonthlyRevenueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where monthlyRevenue in
        defaultAquacultureActivityFiltering(
            "monthlyRevenue.in=" + DEFAULT_MONTHLY_REVENUE + "," + UPDATED_MONTHLY_REVENUE,
            "monthlyRevenue.in=" + UPDATED_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByMonthlyRevenueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where monthlyRevenue is not null
        defaultAquacultureActivityFiltering("monthlyRevenue.specified=true", "monthlyRevenue.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByMonthlyRevenueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where monthlyRevenue is greater than or equal to
        defaultAquacultureActivityFiltering(
            "monthlyRevenue.greaterThanOrEqual=" + DEFAULT_MONTHLY_REVENUE,
            "monthlyRevenue.greaterThanOrEqual=" + UPDATED_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByMonthlyRevenueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where monthlyRevenue is less than or equal to
        defaultAquacultureActivityFiltering(
            "monthlyRevenue.lessThanOrEqual=" + DEFAULT_MONTHLY_REVENUE,
            "monthlyRevenue.lessThanOrEqual=" + SMALLER_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByMonthlyRevenueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where monthlyRevenue is less than
        defaultAquacultureActivityFiltering(
            "monthlyRevenue.lessThan=" + UPDATED_MONTHLY_REVENUE,
            "monthlyRevenue.lessThan=" + DEFAULT_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByMonthlyRevenueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where monthlyRevenue is greater than
        defaultAquacultureActivityFiltering(
            "monthlyRevenue.greaterThan=" + SMALLER_MONTHLY_REVENUE,
            "monthlyRevenue.greaterThan=" + DEFAULT_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByEmployeesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where employees equals to
        defaultAquacultureActivityFiltering("employees.equals=" + DEFAULT_EMPLOYEES, "employees.equals=" + UPDATED_EMPLOYEES);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByEmployeesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where employees in
        defaultAquacultureActivityFiltering(
            "employees.in=" + DEFAULT_EMPLOYEES + "," + UPDATED_EMPLOYEES,
            "employees.in=" + UPDATED_EMPLOYEES
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByEmployeesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where employees is not null
        defaultAquacultureActivityFiltering("employees.specified=true", "employees.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByEmployeesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where employees is greater than or equal to
        defaultAquacultureActivityFiltering(
            "employees.greaterThanOrEqual=" + DEFAULT_EMPLOYEES,
            "employees.greaterThanOrEqual=" + UPDATED_EMPLOYEES
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByEmployeesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where employees is less than or equal to
        defaultAquacultureActivityFiltering(
            "employees.lessThanOrEqual=" + DEFAULT_EMPLOYEES,
            "employees.lessThanOrEqual=" + SMALLER_EMPLOYEES
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByEmployeesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where employees is less than
        defaultAquacultureActivityFiltering("employees.lessThan=" + UPDATED_EMPLOYEES, "employees.lessThan=" + DEFAULT_EMPLOYEES);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByEmployeesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where employees is greater than
        defaultAquacultureActivityFiltering("employees.greaterThan=" + SMALLER_EMPLOYEES, "employees.greaterThan=" + DEFAULT_EMPLOYEES);
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByCertificationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where certification equals to
        defaultAquacultureActivityFiltering(
            "certification.equals=" + DEFAULT_CERTIFICATION,
            "certification.equals=" + UPDATED_CERTIFICATION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByCertificationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where certification in
        defaultAquacultureActivityFiltering(
            "certification.in=" + DEFAULT_CERTIFICATION + "," + UPDATED_CERTIFICATION,
            "certification.in=" + UPDATED_CERTIFICATION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByCertificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where certification is not null
        defaultAquacultureActivityFiltering("certification.specified=true", "certification.specified=false");
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByCertificationContainsSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where certification contains
        defaultAquacultureActivityFiltering(
            "certification.contains=" + DEFAULT_CERTIFICATION,
            "certification.contains=" + UPDATED_CERTIFICATION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByCertificationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        // Get all the aquacultureActivityList where certification does not contain
        defaultAquacultureActivityFiltering(
            "certification.doesNotContain=" + UPDATED_CERTIFICATION,
            "certification.doesNotContain=" + DEFAULT_CERTIFICATION
        );
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByLocationIsEqualToSomething() throws Exception {
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            aquacultureActivityRepository.saveAndFlush(aquacultureActivity);
            location = LocationResourceIT.createEntity();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(location);
        em.flush();
        aquacultureActivity.setLocation(location);
        aquacultureActivityRepository.saveAndFlush(aquacultureActivity);
        Long locationId = location.getId();
        // Get all the aquacultureActivityList where location equals to locationId
        defaultAquacultureActivityShouldBeFound("locationId.equals=" + locationId);

        // Get all the aquacultureActivityList where location equals to (locationId + 1)
        defaultAquacultureActivityShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    @Test
    @Transactional
    void getAllAquacultureActivitiesByAquaticSpeciesIsEqualToSomething() throws Exception {
        AquaticSpecies aquaticSpecies;
        if (TestUtil.findAll(em, AquaticSpecies.class).isEmpty()) {
            aquacultureActivityRepository.saveAndFlush(aquacultureActivity);
            aquaticSpecies = AquaticSpeciesResourceIT.createEntity();
        } else {
            aquaticSpecies = TestUtil.findAll(em, AquaticSpecies.class).get(0);
        }
        em.persist(aquaticSpecies);
        em.flush();
        aquacultureActivity.setAquaticSpecies(aquaticSpecies);
        aquacultureActivityRepository.saveAndFlush(aquacultureActivity);
        Long aquaticSpeciesId = aquaticSpecies.getId();
        // Get all the aquacultureActivityList where aquaticSpecies equals to aquaticSpeciesId
        defaultAquacultureActivityShouldBeFound("aquaticSpeciesId.equals=" + aquaticSpeciesId);

        // Get all the aquacultureActivityList where aquaticSpecies equals to (aquaticSpeciesId + 1)
        defaultAquacultureActivityShouldNotBeFound("aquaticSpeciesId.equals=" + (aquaticSpeciesId + 1));
    }

    private void defaultAquacultureActivityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAquacultureActivityShouldBeFound(shouldBeFound);
        defaultAquacultureActivityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAquacultureActivityShouldBeFound(String filter) throws Exception {
        restAquacultureActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aquacultureActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productionMode").value(hasItem(DEFAULT_PRODUCTION_MODE.toString())))
            .andExpect(jsonPath("$.[*].ownershipType").value(hasItem(DEFAULT_OWNERSHIP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].productionType").value(hasItem(DEFAULT_PRODUCTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].systemType").value(hasItem(DEFAULT_SYSTEM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalArea").value(hasItem(sameNumber(DEFAULT_TOTAL_AREA))))
            .andExpect(jsonPath("$.[*].areaUnit").value(hasItem(DEFAULT_AREA_UNIT)))
            .andExpect(jsonPath("$.[*].waterSource").value(hasItem(DEFAULT_WATER_SOURCE)))
            .andExpect(jsonPath("$.[*].numberOfProductionUnits").value(hasItem(DEFAULT_NUMBER_OF_PRODUCTION_UNITS)))
            .andExpect(jsonPath("$.[*].productionUnitDescription").value(hasItem(DEFAULT_PRODUCTION_UNIT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].annualRevenue").value(hasItem(sameNumber(DEFAULT_ANNUAL_REVENUE))))
            .andExpect(jsonPath("$.[*].monthlyRevenue").value(hasItem(sameNumber(DEFAULT_MONTHLY_REVENUE))))
            .andExpect(jsonPath("$.[*].employees").value(hasItem(DEFAULT_EMPLOYEES)))
            .andExpect(jsonPath("$.[*].certification").value(hasItem(DEFAULT_CERTIFICATION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restAquacultureActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAquacultureActivityShouldNotBeFound(String filter) throws Exception {
        restAquacultureActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAquacultureActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAquacultureActivity() throws Exception {
        // Get the aquacultureActivity
        restAquacultureActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAquacultureActivity() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aquacultureActivity
        AquacultureActivity updatedAquacultureActivity = aquacultureActivityRepository.findById(aquacultureActivity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAquacultureActivity are not directly saved in db
        em.detach(updatedAquacultureActivity);
        updatedAquacultureActivity
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .productionMode(UPDATED_PRODUCTION_MODE)
            .ownershipType(UPDATED_OWNERSHIP_TYPE)
            .productionType(UPDATED_PRODUCTION_TYPE)
            .systemType(UPDATED_SYSTEM_TYPE)
            .startDate(UPDATED_START_DATE)
            .totalArea(UPDATED_TOTAL_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .waterSource(UPDATED_WATER_SOURCE)
            .numberOfProductionUnits(UPDATED_NUMBER_OF_PRODUCTION_UNITS)
            .productionUnitDescription(UPDATED_PRODUCTION_UNIT_DESCRIPTION)
            .status(UPDATED_STATUS)
            .annualRevenue(UPDATED_ANNUAL_REVENUE)
            .monthlyRevenue(UPDATED_MONTHLY_REVENUE)
            .employees(UPDATED_EMPLOYEES)
            .certification(UPDATED_CERTIFICATION)
            .notes(UPDATED_NOTES);
        AquacultureActivityDTO aquacultureActivityDTO = aquacultureActivityMapper.toDto(updatedAquacultureActivity);

        restAquacultureActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aquacultureActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aquacultureActivityDTO))
            )
            .andExpect(status().isOk());

        // Validate the AquacultureActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAquacultureActivityToMatchAllProperties(updatedAquacultureActivity);
    }

    @Test
    @Transactional
    void putNonExistingAquacultureActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquacultureActivity.setId(longCount.incrementAndGet());

        // Create the AquacultureActivity
        AquacultureActivityDTO aquacultureActivityDTO = aquacultureActivityMapper.toDto(aquacultureActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAquacultureActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aquacultureActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aquacultureActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AquacultureActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAquacultureActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquacultureActivity.setId(longCount.incrementAndGet());

        // Create the AquacultureActivity
        AquacultureActivityDTO aquacultureActivityDTO = aquacultureActivityMapper.toDto(aquacultureActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAquacultureActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aquacultureActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AquacultureActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAquacultureActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquacultureActivity.setId(longCount.incrementAndGet());

        // Create the AquacultureActivity
        AquacultureActivityDTO aquacultureActivityDTO = aquacultureActivityMapper.toDto(aquacultureActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAquacultureActivityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aquacultureActivityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AquacultureActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAquacultureActivityWithPatch() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aquacultureActivity using partial update
        AquacultureActivity partialUpdatedAquacultureActivity = new AquacultureActivity();
        partialUpdatedAquacultureActivity.setId(aquacultureActivity.getId());

        partialUpdatedAquacultureActivity
            .description(UPDATED_DESCRIPTION)
            .ownershipType(UPDATED_OWNERSHIP_TYPE)
            .startDate(UPDATED_START_DATE)
            .totalArea(UPDATED_TOTAL_AREA)
            .productionUnitDescription(UPDATED_PRODUCTION_UNIT_DESCRIPTION)
            .status(UPDATED_STATUS)
            .monthlyRevenue(UPDATED_MONTHLY_REVENUE)
            .notes(UPDATED_NOTES);

        restAquacultureActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAquacultureActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAquacultureActivity))
            )
            .andExpect(status().isOk());

        // Validate the AquacultureActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAquacultureActivityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAquacultureActivity, aquacultureActivity),
            getPersistedAquacultureActivity(aquacultureActivity)
        );
    }

    @Test
    @Transactional
    void fullUpdateAquacultureActivityWithPatch() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aquacultureActivity using partial update
        AquacultureActivity partialUpdatedAquacultureActivity = new AquacultureActivity();
        partialUpdatedAquacultureActivity.setId(aquacultureActivity.getId());

        partialUpdatedAquacultureActivity
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .productionMode(UPDATED_PRODUCTION_MODE)
            .ownershipType(UPDATED_OWNERSHIP_TYPE)
            .productionType(UPDATED_PRODUCTION_TYPE)
            .systemType(UPDATED_SYSTEM_TYPE)
            .startDate(UPDATED_START_DATE)
            .totalArea(UPDATED_TOTAL_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .waterSource(UPDATED_WATER_SOURCE)
            .numberOfProductionUnits(UPDATED_NUMBER_OF_PRODUCTION_UNITS)
            .productionUnitDescription(UPDATED_PRODUCTION_UNIT_DESCRIPTION)
            .status(UPDATED_STATUS)
            .annualRevenue(UPDATED_ANNUAL_REVENUE)
            .monthlyRevenue(UPDATED_MONTHLY_REVENUE)
            .employees(UPDATED_EMPLOYEES)
            .certification(UPDATED_CERTIFICATION)
            .notes(UPDATED_NOTES);

        restAquacultureActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAquacultureActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAquacultureActivity))
            )
            .andExpect(status().isOk());

        // Validate the AquacultureActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAquacultureActivityUpdatableFieldsEquals(
            partialUpdatedAquacultureActivity,
            getPersistedAquacultureActivity(partialUpdatedAquacultureActivity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAquacultureActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquacultureActivity.setId(longCount.incrementAndGet());

        // Create the AquacultureActivity
        AquacultureActivityDTO aquacultureActivityDTO = aquacultureActivityMapper.toDto(aquacultureActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAquacultureActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aquacultureActivityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aquacultureActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AquacultureActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAquacultureActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquacultureActivity.setId(longCount.incrementAndGet());

        // Create the AquacultureActivity
        AquacultureActivityDTO aquacultureActivityDTO = aquacultureActivityMapper.toDto(aquacultureActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAquacultureActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aquacultureActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AquacultureActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAquacultureActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aquacultureActivity.setId(longCount.incrementAndGet());

        // Create the AquacultureActivity
        AquacultureActivityDTO aquacultureActivityDTO = aquacultureActivityMapper.toDto(aquacultureActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAquacultureActivityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aquacultureActivityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AquacultureActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAquacultureActivity() throws Exception {
        // Initialize the database
        insertedAquacultureActivity = aquacultureActivityRepository.saveAndFlush(aquacultureActivity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aquacultureActivity
        restAquacultureActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, aquacultureActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aquacultureActivityRepository.count();
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

    protected AquacultureActivity getPersistedAquacultureActivity(AquacultureActivity aquacultureActivity) {
        return aquacultureActivityRepository.findById(aquacultureActivity.getId()).orElseThrow();
    }

    protected void assertPersistedAquacultureActivityToMatchAllProperties(AquacultureActivity expectedAquacultureActivity) {
        assertAquacultureActivityAllPropertiesEquals(
            expectedAquacultureActivity,
            getPersistedAquacultureActivity(expectedAquacultureActivity)
        );
    }

    protected void assertPersistedAquacultureActivityToMatchUpdatableProperties(AquacultureActivity expectedAquacultureActivity) {
        assertAquacultureActivityAllUpdatablePropertiesEquals(
            expectedAquacultureActivity,
            getPersistedAquacultureActivity(expectedAquacultureActivity)
        );
    }
}
