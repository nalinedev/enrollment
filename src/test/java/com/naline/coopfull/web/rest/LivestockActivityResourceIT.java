package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.LivestockActivityAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static com.naline.coopfull.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.LivestockActivity;
import com.naline.coopfull.domain.LivestockType;
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.domain.enumeration.LivestockOwnershipType;
import com.naline.coopfull.domain.enumeration.LivestockProductionMode;
import com.naline.coopfull.domain.enumeration.LivestockProductionType;
import com.naline.coopfull.domain.enumeration.LivestockStatus;
import com.naline.coopfull.repository.LivestockActivityRepository;
import com.naline.coopfull.service.LivestockActivityService;
import com.naline.coopfull.service.dto.LivestockActivityDTO;
import com.naline.coopfull.service.mapper.LivestockActivityMapper;
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
 * Integration tests for the {@link LivestockActivityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LivestockActivityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LivestockProductionMode DEFAULT_PRODUCTION_MODE = LivestockProductionMode.FAMILY;
    private static final LivestockProductionMode UPDATED_PRODUCTION_MODE = LivestockProductionMode.INDIVIDUAL;

    private static final LivestockOwnershipType DEFAULT_OWNERSHIP_TYPE = LivestockOwnershipType.OWNER;
    private static final LivestockOwnershipType UPDATED_OWNERSHIP_TYPE = LivestockOwnershipType.LEASED;

    private static final LivestockProductionType DEFAULT_PRODUCTION_TYPE = LivestockProductionType.MEAT;
    private static final LivestockProductionType UPDATED_PRODUCTION_TYPE = LivestockProductionType.MILK;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_TOTAL_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AREA = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AREA = new BigDecimal(1 - 1);

    private static final String DEFAULT_AREA_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_AREA_UNIT = "BBBBBBBBBB";

    private static final LivestockStatus DEFAULT_STATUS = LivestockStatus.ACTIVE;
    private static final LivestockStatus UPDATED_STATUS = LivestockStatus.INACTIVE;

    private static final Integer DEFAULT_NUMBER_OF_ANIMALS = 1;
    private static final Integer UPDATED_NUMBER_OF_ANIMALS = 2;
    private static final Integer SMALLER_NUMBER_OF_ANIMALS = 1 - 1;

    private static final BigDecimal DEFAULT_ANNUAL_REVENUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ANNUAL_REVENUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ANNUAL_REVENUE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MONTHLY_REVENUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTHLY_REVENUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTHLY_REVENUE = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_EMPLOYEES = 1;
    private static final Integer UPDATED_EMPLOYEES = 2;
    private static final Integer SMALLER_EMPLOYEES = 1 - 1;

    private static final Boolean DEFAULT_VETERINARY_SERVICE_AVAILABLE = false;
    private static final Boolean UPDATED_VETERINARY_SERVICE_AVAILABLE = true;

    private static final String DEFAULT_FEED_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_FEED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_WATER_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_WATER_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_CERTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_CERTIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/livestock-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LivestockActivityRepository livestockActivityRepository;

    @Mock
    private LivestockActivityRepository livestockActivityRepositoryMock;

    @Autowired
    private LivestockActivityMapper livestockActivityMapper;

    @Mock
    private LivestockActivityService livestockActivityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLivestockActivityMockMvc;

    private LivestockActivity livestockActivity;

    private LivestockActivity insertedLivestockActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LivestockActivity createEntity() {
        return new LivestockActivity()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .productionMode(DEFAULT_PRODUCTION_MODE)
            .ownershipType(DEFAULT_OWNERSHIP_TYPE)
            .productionType(DEFAULT_PRODUCTION_TYPE)
            .startDate(DEFAULT_START_DATE)
            .totalArea(DEFAULT_TOTAL_AREA)
            .areaUnit(DEFAULT_AREA_UNIT)
            .status(DEFAULT_STATUS)
            .numberOfAnimals(DEFAULT_NUMBER_OF_ANIMALS)
            .annualRevenue(DEFAULT_ANNUAL_REVENUE)
            .monthlyRevenue(DEFAULT_MONTHLY_REVENUE)
            .employees(DEFAULT_EMPLOYEES)
            .veterinaryServiceAvailable(DEFAULT_VETERINARY_SERVICE_AVAILABLE)
            .feedSource(DEFAULT_FEED_SOURCE)
            .waterSource(DEFAULT_WATER_SOURCE)
            .certification(DEFAULT_CERTIFICATION)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LivestockActivity createUpdatedEntity() {
        return new LivestockActivity()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .productionMode(UPDATED_PRODUCTION_MODE)
            .ownershipType(UPDATED_OWNERSHIP_TYPE)
            .productionType(UPDATED_PRODUCTION_TYPE)
            .startDate(UPDATED_START_DATE)
            .totalArea(UPDATED_TOTAL_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .status(UPDATED_STATUS)
            .numberOfAnimals(UPDATED_NUMBER_OF_ANIMALS)
            .annualRevenue(UPDATED_ANNUAL_REVENUE)
            .monthlyRevenue(UPDATED_MONTHLY_REVENUE)
            .employees(UPDATED_EMPLOYEES)
            .veterinaryServiceAvailable(UPDATED_VETERINARY_SERVICE_AVAILABLE)
            .feedSource(UPDATED_FEED_SOURCE)
            .waterSource(UPDATED_WATER_SOURCE)
            .certification(UPDATED_CERTIFICATION)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        livestockActivity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLivestockActivity != null) {
            livestockActivityRepository.delete(insertedLivestockActivity);
            insertedLivestockActivity = null;
        }
    }

    @Test
    @Transactional
    void createLivestockActivity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LivestockActivity
        LivestockActivityDTO livestockActivityDTO = livestockActivityMapper.toDto(livestockActivity);
        var returnedLivestockActivityDTO = om.readValue(
            restLivestockActivityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockActivityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LivestockActivityDTO.class
        );

        // Validate the LivestockActivity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLivestockActivity = livestockActivityMapper.toEntity(returnedLivestockActivityDTO);
        assertLivestockActivityUpdatableFieldsEquals(returnedLivestockActivity, getPersistedLivestockActivity(returnedLivestockActivity));

        insertedLivestockActivity = returnedLivestockActivity;
    }

    @Test
    @Transactional
    void createLivestockActivityWithExistingId() throws Exception {
        // Create the LivestockActivity with an existing ID
        livestockActivity.setId(1L);
        LivestockActivityDTO livestockActivityDTO = livestockActivityMapper.toDto(livestockActivity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivestockActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LivestockActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        livestockActivity.setName(null);

        // Create the LivestockActivity, which fails.
        LivestockActivityDTO livestockActivityDTO = livestockActivityMapper.toDto(livestockActivity);

        restLivestockActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        livestockActivity.setStatus(null);

        // Create the LivestockActivity, which fails.
        LivestockActivityDTO livestockActivityDTO = livestockActivityMapper.toDto(livestockActivity);

        restLivestockActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLivestockActivities() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList
        restLivestockActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livestockActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productionMode").value(hasItem(DEFAULT_PRODUCTION_MODE.toString())))
            .andExpect(jsonPath("$.[*].ownershipType").value(hasItem(DEFAULT_OWNERSHIP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].productionType").value(hasItem(DEFAULT_PRODUCTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalArea").value(hasItem(sameNumber(DEFAULT_TOTAL_AREA))))
            .andExpect(jsonPath("$.[*].areaUnit").value(hasItem(DEFAULT_AREA_UNIT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].numberOfAnimals").value(hasItem(DEFAULT_NUMBER_OF_ANIMALS)))
            .andExpect(jsonPath("$.[*].annualRevenue").value(hasItem(sameNumber(DEFAULT_ANNUAL_REVENUE))))
            .andExpect(jsonPath("$.[*].monthlyRevenue").value(hasItem(sameNumber(DEFAULT_MONTHLY_REVENUE))))
            .andExpect(jsonPath("$.[*].employees").value(hasItem(DEFAULT_EMPLOYEES)))
            .andExpect(jsonPath("$.[*].veterinaryServiceAvailable").value(hasItem(DEFAULT_VETERINARY_SERVICE_AVAILABLE)))
            .andExpect(jsonPath("$.[*].feedSource").value(hasItem(DEFAULT_FEED_SOURCE)))
            .andExpect(jsonPath("$.[*].waterSource").value(hasItem(DEFAULT_WATER_SOURCE)))
            .andExpect(jsonPath("$.[*].certification").value(hasItem(DEFAULT_CERTIFICATION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLivestockActivitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(livestockActivityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLivestockActivityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(livestockActivityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLivestockActivitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(livestockActivityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLivestockActivityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(livestockActivityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLivestockActivity() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get the livestockActivity
        restLivestockActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, livestockActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(livestockActivity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.productionMode").value(DEFAULT_PRODUCTION_MODE.toString()))
            .andExpect(jsonPath("$.ownershipType").value(DEFAULT_OWNERSHIP_TYPE.toString()))
            .andExpect(jsonPath("$.productionType").value(DEFAULT_PRODUCTION_TYPE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.totalArea").value(sameNumber(DEFAULT_TOTAL_AREA)))
            .andExpect(jsonPath("$.areaUnit").value(DEFAULT_AREA_UNIT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.numberOfAnimals").value(DEFAULT_NUMBER_OF_ANIMALS))
            .andExpect(jsonPath("$.annualRevenue").value(sameNumber(DEFAULT_ANNUAL_REVENUE)))
            .andExpect(jsonPath("$.monthlyRevenue").value(sameNumber(DEFAULT_MONTHLY_REVENUE)))
            .andExpect(jsonPath("$.employees").value(DEFAULT_EMPLOYEES))
            .andExpect(jsonPath("$.veterinaryServiceAvailable").value(DEFAULT_VETERINARY_SERVICE_AVAILABLE))
            .andExpect(jsonPath("$.feedSource").value(DEFAULT_FEED_SOURCE))
            .andExpect(jsonPath("$.waterSource").value(DEFAULT_WATER_SOURCE))
            .andExpect(jsonPath("$.certification").value(DEFAULT_CERTIFICATION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getLivestockActivitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        Long id = livestockActivity.getId();

        defaultLivestockActivityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLivestockActivityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLivestockActivityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where name equals to
        defaultLivestockActivityFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where name in
        defaultLivestockActivityFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where name is not null
        defaultLivestockActivityFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where name contains
        defaultLivestockActivityFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where name does not contain
        defaultLivestockActivityFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByProductionModeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where productionMode equals to
        defaultLivestockActivityFiltering(
            "productionMode.equals=" + DEFAULT_PRODUCTION_MODE,
            "productionMode.equals=" + UPDATED_PRODUCTION_MODE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByProductionModeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where productionMode in
        defaultLivestockActivityFiltering(
            "productionMode.in=" + DEFAULT_PRODUCTION_MODE + "," + UPDATED_PRODUCTION_MODE,
            "productionMode.in=" + UPDATED_PRODUCTION_MODE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByProductionModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where productionMode is not null
        defaultLivestockActivityFiltering("productionMode.specified=true", "productionMode.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByOwnershipTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where ownershipType equals to
        defaultLivestockActivityFiltering(
            "ownershipType.equals=" + DEFAULT_OWNERSHIP_TYPE,
            "ownershipType.equals=" + UPDATED_OWNERSHIP_TYPE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByOwnershipTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where ownershipType in
        defaultLivestockActivityFiltering(
            "ownershipType.in=" + DEFAULT_OWNERSHIP_TYPE + "," + UPDATED_OWNERSHIP_TYPE,
            "ownershipType.in=" + UPDATED_OWNERSHIP_TYPE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByOwnershipTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where ownershipType is not null
        defaultLivestockActivityFiltering("ownershipType.specified=true", "ownershipType.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByProductionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where productionType equals to
        defaultLivestockActivityFiltering(
            "productionType.equals=" + DEFAULT_PRODUCTION_TYPE,
            "productionType.equals=" + UPDATED_PRODUCTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByProductionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where productionType in
        defaultLivestockActivityFiltering(
            "productionType.in=" + DEFAULT_PRODUCTION_TYPE + "," + UPDATED_PRODUCTION_TYPE,
            "productionType.in=" + UPDATED_PRODUCTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByProductionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where productionType is not null
        defaultLivestockActivityFiltering("productionType.specified=true", "productionType.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where startDate equals to
        defaultLivestockActivityFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where startDate in
        defaultLivestockActivityFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where startDate is not null
        defaultLivestockActivityFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where startDate is greater than or equal to
        defaultLivestockActivityFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where startDate is less than or equal to
        defaultLivestockActivityFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where startDate is less than
        defaultLivestockActivityFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where startDate is greater than
        defaultLivestockActivityFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByTotalAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where totalArea equals to
        defaultLivestockActivityFiltering("totalArea.equals=" + DEFAULT_TOTAL_AREA, "totalArea.equals=" + UPDATED_TOTAL_AREA);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByTotalAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where totalArea in
        defaultLivestockActivityFiltering(
            "totalArea.in=" + DEFAULT_TOTAL_AREA + "," + UPDATED_TOTAL_AREA,
            "totalArea.in=" + UPDATED_TOTAL_AREA
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByTotalAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where totalArea is not null
        defaultLivestockActivityFiltering("totalArea.specified=true", "totalArea.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByTotalAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where totalArea is greater than or equal to
        defaultLivestockActivityFiltering(
            "totalArea.greaterThanOrEqual=" + DEFAULT_TOTAL_AREA,
            "totalArea.greaterThanOrEqual=" + UPDATED_TOTAL_AREA
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByTotalAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where totalArea is less than or equal to
        defaultLivestockActivityFiltering(
            "totalArea.lessThanOrEqual=" + DEFAULT_TOTAL_AREA,
            "totalArea.lessThanOrEqual=" + SMALLER_TOTAL_AREA
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByTotalAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where totalArea is less than
        defaultLivestockActivityFiltering("totalArea.lessThan=" + UPDATED_TOTAL_AREA, "totalArea.lessThan=" + DEFAULT_TOTAL_AREA);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByTotalAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where totalArea is greater than
        defaultLivestockActivityFiltering("totalArea.greaterThan=" + SMALLER_TOTAL_AREA, "totalArea.greaterThan=" + DEFAULT_TOTAL_AREA);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByAreaUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where areaUnit equals to
        defaultLivestockActivityFiltering("areaUnit.equals=" + DEFAULT_AREA_UNIT, "areaUnit.equals=" + UPDATED_AREA_UNIT);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByAreaUnitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where areaUnit in
        defaultLivestockActivityFiltering("areaUnit.in=" + DEFAULT_AREA_UNIT + "," + UPDATED_AREA_UNIT, "areaUnit.in=" + UPDATED_AREA_UNIT);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByAreaUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where areaUnit is not null
        defaultLivestockActivityFiltering("areaUnit.specified=true", "areaUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByAreaUnitContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where areaUnit contains
        defaultLivestockActivityFiltering("areaUnit.contains=" + DEFAULT_AREA_UNIT, "areaUnit.contains=" + UPDATED_AREA_UNIT);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByAreaUnitNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where areaUnit does not contain
        defaultLivestockActivityFiltering("areaUnit.doesNotContain=" + UPDATED_AREA_UNIT, "areaUnit.doesNotContain=" + DEFAULT_AREA_UNIT);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where status equals to
        defaultLivestockActivityFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where status in
        defaultLivestockActivityFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where status is not null
        defaultLivestockActivityFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByNumberOfAnimalsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where numberOfAnimals equals to
        defaultLivestockActivityFiltering(
            "numberOfAnimals.equals=" + DEFAULT_NUMBER_OF_ANIMALS,
            "numberOfAnimals.equals=" + UPDATED_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByNumberOfAnimalsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where numberOfAnimals in
        defaultLivestockActivityFiltering(
            "numberOfAnimals.in=" + DEFAULT_NUMBER_OF_ANIMALS + "," + UPDATED_NUMBER_OF_ANIMALS,
            "numberOfAnimals.in=" + UPDATED_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByNumberOfAnimalsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where numberOfAnimals is not null
        defaultLivestockActivityFiltering("numberOfAnimals.specified=true", "numberOfAnimals.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByNumberOfAnimalsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where numberOfAnimals is greater than or equal to
        defaultLivestockActivityFiltering(
            "numberOfAnimals.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_ANIMALS,
            "numberOfAnimals.greaterThanOrEqual=" + UPDATED_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByNumberOfAnimalsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where numberOfAnimals is less than or equal to
        defaultLivestockActivityFiltering(
            "numberOfAnimals.lessThanOrEqual=" + DEFAULT_NUMBER_OF_ANIMALS,
            "numberOfAnimals.lessThanOrEqual=" + SMALLER_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByNumberOfAnimalsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where numberOfAnimals is less than
        defaultLivestockActivityFiltering(
            "numberOfAnimals.lessThan=" + UPDATED_NUMBER_OF_ANIMALS,
            "numberOfAnimals.lessThan=" + DEFAULT_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByNumberOfAnimalsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where numberOfAnimals is greater than
        defaultLivestockActivityFiltering(
            "numberOfAnimals.greaterThan=" + SMALLER_NUMBER_OF_ANIMALS,
            "numberOfAnimals.greaterThan=" + DEFAULT_NUMBER_OF_ANIMALS
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByAnnualRevenueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where annualRevenue equals to
        defaultLivestockActivityFiltering(
            "annualRevenue.equals=" + DEFAULT_ANNUAL_REVENUE,
            "annualRevenue.equals=" + UPDATED_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByAnnualRevenueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where annualRevenue in
        defaultLivestockActivityFiltering(
            "annualRevenue.in=" + DEFAULT_ANNUAL_REVENUE + "," + UPDATED_ANNUAL_REVENUE,
            "annualRevenue.in=" + UPDATED_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByAnnualRevenueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where annualRevenue is not null
        defaultLivestockActivityFiltering("annualRevenue.specified=true", "annualRevenue.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByAnnualRevenueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where annualRevenue is greater than or equal to
        defaultLivestockActivityFiltering(
            "annualRevenue.greaterThanOrEqual=" + DEFAULT_ANNUAL_REVENUE,
            "annualRevenue.greaterThanOrEqual=" + UPDATED_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByAnnualRevenueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where annualRevenue is less than or equal to
        defaultLivestockActivityFiltering(
            "annualRevenue.lessThanOrEqual=" + DEFAULT_ANNUAL_REVENUE,
            "annualRevenue.lessThanOrEqual=" + SMALLER_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByAnnualRevenueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where annualRevenue is less than
        defaultLivestockActivityFiltering(
            "annualRevenue.lessThan=" + UPDATED_ANNUAL_REVENUE,
            "annualRevenue.lessThan=" + DEFAULT_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByAnnualRevenueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where annualRevenue is greater than
        defaultLivestockActivityFiltering(
            "annualRevenue.greaterThan=" + SMALLER_ANNUAL_REVENUE,
            "annualRevenue.greaterThan=" + DEFAULT_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByMonthlyRevenueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where monthlyRevenue equals to
        defaultLivestockActivityFiltering(
            "monthlyRevenue.equals=" + DEFAULT_MONTHLY_REVENUE,
            "monthlyRevenue.equals=" + UPDATED_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByMonthlyRevenueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where monthlyRevenue in
        defaultLivestockActivityFiltering(
            "monthlyRevenue.in=" + DEFAULT_MONTHLY_REVENUE + "," + UPDATED_MONTHLY_REVENUE,
            "monthlyRevenue.in=" + UPDATED_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByMonthlyRevenueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where monthlyRevenue is not null
        defaultLivestockActivityFiltering("monthlyRevenue.specified=true", "monthlyRevenue.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByMonthlyRevenueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where monthlyRevenue is greater than or equal to
        defaultLivestockActivityFiltering(
            "monthlyRevenue.greaterThanOrEqual=" + DEFAULT_MONTHLY_REVENUE,
            "monthlyRevenue.greaterThanOrEqual=" + UPDATED_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByMonthlyRevenueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where monthlyRevenue is less than or equal to
        defaultLivestockActivityFiltering(
            "monthlyRevenue.lessThanOrEqual=" + DEFAULT_MONTHLY_REVENUE,
            "monthlyRevenue.lessThanOrEqual=" + SMALLER_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByMonthlyRevenueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where monthlyRevenue is less than
        defaultLivestockActivityFiltering(
            "monthlyRevenue.lessThan=" + UPDATED_MONTHLY_REVENUE,
            "monthlyRevenue.lessThan=" + DEFAULT_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByMonthlyRevenueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where monthlyRevenue is greater than
        defaultLivestockActivityFiltering(
            "monthlyRevenue.greaterThan=" + SMALLER_MONTHLY_REVENUE,
            "monthlyRevenue.greaterThan=" + DEFAULT_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByEmployeesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where employees equals to
        defaultLivestockActivityFiltering("employees.equals=" + DEFAULT_EMPLOYEES, "employees.equals=" + UPDATED_EMPLOYEES);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByEmployeesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where employees in
        defaultLivestockActivityFiltering(
            "employees.in=" + DEFAULT_EMPLOYEES + "," + UPDATED_EMPLOYEES,
            "employees.in=" + UPDATED_EMPLOYEES
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByEmployeesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where employees is not null
        defaultLivestockActivityFiltering("employees.specified=true", "employees.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByEmployeesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where employees is greater than or equal to
        defaultLivestockActivityFiltering(
            "employees.greaterThanOrEqual=" + DEFAULT_EMPLOYEES,
            "employees.greaterThanOrEqual=" + UPDATED_EMPLOYEES
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByEmployeesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where employees is less than or equal to
        defaultLivestockActivityFiltering(
            "employees.lessThanOrEqual=" + DEFAULT_EMPLOYEES,
            "employees.lessThanOrEqual=" + SMALLER_EMPLOYEES
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByEmployeesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where employees is less than
        defaultLivestockActivityFiltering("employees.lessThan=" + UPDATED_EMPLOYEES, "employees.lessThan=" + DEFAULT_EMPLOYEES);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByEmployeesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where employees is greater than
        defaultLivestockActivityFiltering("employees.greaterThan=" + SMALLER_EMPLOYEES, "employees.greaterThan=" + DEFAULT_EMPLOYEES);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByVeterinaryServiceAvailableIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where veterinaryServiceAvailable equals to
        defaultLivestockActivityFiltering(
            "veterinaryServiceAvailable.equals=" + DEFAULT_VETERINARY_SERVICE_AVAILABLE,
            "veterinaryServiceAvailable.equals=" + UPDATED_VETERINARY_SERVICE_AVAILABLE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByVeterinaryServiceAvailableIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where veterinaryServiceAvailable in
        defaultLivestockActivityFiltering(
            "veterinaryServiceAvailable.in=" + DEFAULT_VETERINARY_SERVICE_AVAILABLE + "," + UPDATED_VETERINARY_SERVICE_AVAILABLE,
            "veterinaryServiceAvailable.in=" + UPDATED_VETERINARY_SERVICE_AVAILABLE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByVeterinaryServiceAvailableIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where veterinaryServiceAvailable is not null
        defaultLivestockActivityFiltering("veterinaryServiceAvailable.specified=true", "veterinaryServiceAvailable.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByFeedSourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where feedSource equals to
        defaultLivestockActivityFiltering("feedSource.equals=" + DEFAULT_FEED_SOURCE, "feedSource.equals=" + UPDATED_FEED_SOURCE);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByFeedSourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where feedSource in
        defaultLivestockActivityFiltering(
            "feedSource.in=" + DEFAULT_FEED_SOURCE + "," + UPDATED_FEED_SOURCE,
            "feedSource.in=" + UPDATED_FEED_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByFeedSourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where feedSource is not null
        defaultLivestockActivityFiltering("feedSource.specified=true", "feedSource.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByFeedSourceContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where feedSource contains
        defaultLivestockActivityFiltering("feedSource.contains=" + DEFAULT_FEED_SOURCE, "feedSource.contains=" + UPDATED_FEED_SOURCE);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByFeedSourceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where feedSource does not contain
        defaultLivestockActivityFiltering(
            "feedSource.doesNotContain=" + UPDATED_FEED_SOURCE,
            "feedSource.doesNotContain=" + DEFAULT_FEED_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByWaterSourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where waterSource equals to
        defaultLivestockActivityFiltering("waterSource.equals=" + DEFAULT_WATER_SOURCE, "waterSource.equals=" + UPDATED_WATER_SOURCE);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByWaterSourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where waterSource in
        defaultLivestockActivityFiltering(
            "waterSource.in=" + DEFAULT_WATER_SOURCE + "," + UPDATED_WATER_SOURCE,
            "waterSource.in=" + UPDATED_WATER_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByWaterSourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where waterSource is not null
        defaultLivestockActivityFiltering("waterSource.specified=true", "waterSource.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByWaterSourceContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where waterSource contains
        defaultLivestockActivityFiltering("waterSource.contains=" + DEFAULT_WATER_SOURCE, "waterSource.contains=" + UPDATED_WATER_SOURCE);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByWaterSourceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where waterSource does not contain
        defaultLivestockActivityFiltering(
            "waterSource.doesNotContain=" + UPDATED_WATER_SOURCE,
            "waterSource.doesNotContain=" + DEFAULT_WATER_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByCertificationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where certification equals to
        defaultLivestockActivityFiltering("certification.equals=" + DEFAULT_CERTIFICATION, "certification.equals=" + UPDATED_CERTIFICATION);
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByCertificationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where certification in
        defaultLivestockActivityFiltering(
            "certification.in=" + DEFAULT_CERTIFICATION + "," + UPDATED_CERTIFICATION,
            "certification.in=" + UPDATED_CERTIFICATION
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByCertificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where certification is not null
        defaultLivestockActivityFiltering("certification.specified=true", "certification.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByCertificationContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where certification contains
        defaultLivestockActivityFiltering(
            "certification.contains=" + DEFAULT_CERTIFICATION,
            "certification.contains=" + UPDATED_CERTIFICATION
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByCertificationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        // Get all the livestockActivityList where certification does not contain
        defaultLivestockActivityFiltering(
            "certification.doesNotContain=" + UPDATED_CERTIFICATION,
            "certification.doesNotContain=" + DEFAULT_CERTIFICATION
        );
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByLocationIsEqualToSomething() throws Exception {
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            livestockActivityRepository.saveAndFlush(livestockActivity);
            location = LocationResourceIT.createEntity();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(location);
        em.flush();
        livestockActivity.setLocation(location);
        livestockActivityRepository.saveAndFlush(livestockActivity);
        Long locationId = location.getId();
        // Get all the livestockActivityList where location equals to locationId
        defaultLivestockActivityShouldBeFound("locationId.equals=" + locationId);

        // Get all the livestockActivityList where location equals to (locationId + 1)
        defaultLivestockActivityShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    @Test
    @Transactional
    void getAllLivestockActivitiesByLivestockTypeIsEqualToSomething() throws Exception {
        LivestockType livestockType;
        if (TestUtil.findAll(em, LivestockType.class).isEmpty()) {
            livestockActivityRepository.saveAndFlush(livestockActivity);
            livestockType = LivestockTypeResourceIT.createEntity();
        } else {
            livestockType = TestUtil.findAll(em, LivestockType.class).get(0);
        }
        em.persist(livestockType);
        em.flush();
        livestockActivity.setLivestockType(livestockType);
        livestockActivityRepository.saveAndFlush(livestockActivity);
        Long livestockTypeId = livestockType.getId();
        // Get all the livestockActivityList where livestockType equals to livestockTypeId
        defaultLivestockActivityShouldBeFound("livestockTypeId.equals=" + livestockTypeId);

        // Get all the livestockActivityList where livestockType equals to (livestockTypeId + 1)
        defaultLivestockActivityShouldNotBeFound("livestockTypeId.equals=" + (livestockTypeId + 1));
    }

    private void defaultLivestockActivityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLivestockActivityShouldBeFound(shouldBeFound);
        defaultLivestockActivityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLivestockActivityShouldBeFound(String filter) throws Exception {
        restLivestockActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livestockActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].productionMode").value(hasItem(DEFAULT_PRODUCTION_MODE.toString())))
            .andExpect(jsonPath("$.[*].ownershipType").value(hasItem(DEFAULT_OWNERSHIP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].productionType").value(hasItem(DEFAULT_PRODUCTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalArea").value(hasItem(sameNumber(DEFAULT_TOTAL_AREA))))
            .andExpect(jsonPath("$.[*].areaUnit").value(hasItem(DEFAULT_AREA_UNIT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].numberOfAnimals").value(hasItem(DEFAULT_NUMBER_OF_ANIMALS)))
            .andExpect(jsonPath("$.[*].annualRevenue").value(hasItem(sameNumber(DEFAULT_ANNUAL_REVENUE))))
            .andExpect(jsonPath("$.[*].monthlyRevenue").value(hasItem(sameNumber(DEFAULT_MONTHLY_REVENUE))))
            .andExpect(jsonPath("$.[*].employees").value(hasItem(DEFAULT_EMPLOYEES)))
            .andExpect(jsonPath("$.[*].veterinaryServiceAvailable").value(hasItem(DEFAULT_VETERINARY_SERVICE_AVAILABLE)))
            .andExpect(jsonPath("$.[*].feedSource").value(hasItem(DEFAULT_FEED_SOURCE)))
            .andExpect(jsonPath("$.[*].waterSource").value(hasItem(DEFAULT_WATER_SOURCE)))
            .andExpect(jsonPath("$.[*].certification").value(hasItem(DEFAULT_CERTIFICATION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restLivestockActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLivestockActivityShouldNotBeFound(String filter) throws Exception {
        restLivestockActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLivestockActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLivestockActivity() throws Exception {
        // Get the livestockActivity
        restLivestockActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLivestockActivity() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the livestockActivity
        LivestockActivity updatedLivestockActivity = livestockActivityRepository.findById(livestockActivity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLivestockActivity are not directly saved in db
        em.detach(updatedLivestockActivity);
        updatedLivestockActivity
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .productionMode(UPDATED_PRODUCTION_MODE)
            .ownershipType(UPDATED_OWNERSHIP_TYPE)
            .productionType(UPDATED_PRODUCTION_TYPE)
            .startDate(UPDATED_START_DATE)
            .totalArea(UPDATED_TOTAL_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .status(UPDATED_STATUS)
            .numberOfAnimals(UPDATED_NUMBER_OF_ANIMALS)
            .annualRevenue(UPDATED_ANNUAL_REVENUE)
            .monthlyRevenue(UPDATED_MONTHLY_REVENUE)
            .employees(UPDATED_EMPLOYEES)
            .veterinaryServiceAvailable(UPDATED_VETERINARY_SERVICE_AVAILABLE)
            .feedSource(UPDATED_FEED_SOURCE)
            .waterSource(UPDATED_WATER_SOURCE)
            .certification(UPDATED_CERTIFICATION)
            .notes(UPDATED_NOTES);
        LivestockActivityDTO livestockActivityDTO = livestockActivityMapper.toDto(updatedLivestockActivity);

        restLivestockActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livestockActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(livestockActivityDTO))
            )
            .andExpect(status().isOk());

        // Validate the LivestockActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLivestockActivityToMatchAllProperties(updatedLivestockActivity);
    }

    @Test
    @Transactional
    void putNonExistingLivestockActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockActivity.setId(longCount.incrementAndGet());

        // Create the LivestockActivity
        LivestockActivityDTO livestockActivityDTO = livestockActivityMapper.toDto(livestockActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivestockActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livestockActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(livestockActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivestockActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLivestockActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockActivity.setId(longCount.incrementAndGet());

        // Create the LivestockActivity
        LivestockActivityDTO livestockActivityDTO = livestockActivityMapper.toDto(livestockActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivestockActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(livestockActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivestockActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLivestockActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockActivity.setId(longCount.incrementAndGet());

        // Create the LivestockActivity
        LivestockActivityDTO livestockActivityDTO = livestockActivityMapper.toDto(livestockActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivestockActivityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockActivityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LivestockActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLivestockActivityWithPatch() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the livestockActivity using partial update
        LivestockActivity partialUpdatedLivestockActivity = new LivestockActivity();
        partialUpdatedLivestockActivity.setId(livestockActivity.getId());

        partialUpdatedLivestockActivity
            .name(UPDATED_NAME)
            .productionMode(UPDATED_PRODUCTION_MODE)
            .ownershipType(UPDATED_OWNERSHIP_TYPE)
            .startDate(UPDATED_START_DATE)
            .employees(UPDATED_EMPLOYEES)
            .veterinaryServiceAvailable(UPDATED_VETERINARY_SERVICE_AVAILABLE)
            .certification(UPDATED_CERTIFICATION)
            .notes(UPDATED_NOTES);

        restLivestockActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivestockActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLivestockActivity))
            )
            .andExpect(status().isOk());

        // Validate the LivestockActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLivestockActivityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLivestockActivity, livestockActivity),
            getPersistedLivestockActivity(livestockActivity)
        );
    }

    @Test
    @Transactional
    void fullUpdateLivestockActivityWithPatch() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the livestockActivity using partial update
        LivestockActivity partialUpdatedLivestockActivity = new LivestockActivity();
        partialUpdatedLivestockActivity.setId(livestockActivity.getId());

        partialUpdatedLivestockActivity
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .productionMode(UPDATED_PRODUCTION_MODE)
            .ownershipType(UPDATED_OWNERSHIP_TYPE)
            .productionType(UPDATED_PRODUCTION_TYPE)
            .startDate(UPDATED_START_DATE)
            .totalArea(UPDATED_TOTAL_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .status(UPDATED_STATUS)
            .numberOfAnimals(UPDATED_NUMBER_OF_ANIMALS)
            .annualRevenue(UPDATED_ANNUAL_REVENUE)
            .monthlyRevenue(UPDATED_MONTHLY_REVENUE)
            .employees(UPDATED_EMPLOYEES)
            .veterinaryServiceAvailable(UPDATED_VETERINARY_SERVICE_AVAILABLE)
            .feedSource(UPDATED_FEED_SOURCE)
            .waterSource(UPDATED_WATER_SOURCE)
            .certification(UPDATED_CERTIFICATION)
            .notes(UPDATED_NOTES);

        restLivestockActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivestockActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLivestockActivity))
            )
            .andExpect(status().isOk());

        // Validate the LivestockActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLivestockActivityUpdatableFieldsEquals(
            partialUpdatedLivestockActivity,
            getPersistedLivestockActivity(partialUpdatedLivestockActivity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingLivestockActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockActivity.setId(longCount.incrementAndGet());

        // Create the LivestockActivity
        LivestockActivityDTO livestockActivityDTO = livestockActivityMapper.toDto(livestockActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivestockActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, livestockActivityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(livestockActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivestockActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLivestockActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockActivity.setId(longCount.incrementAndGet());

        // Create the LivestockActivity
        LivestockActivityDTO livestockActivityDTO = livestockActivityMapper.toDto(livestockActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivestockActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(livestockActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivestockActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLivestockActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockActivity.setId(longCount.incrementAndGet());

        // Create the LivestockActivity
        LivestockActivityDTO livestockActivityDTO = livestockActivityMapper.toDto(livestockActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivestockActivityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(livestockActivityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LivestockActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLivestockActivity() throws Exception {
        // Initialize the database
        insertedLivestockActivity = livestockActivityRepository.saveAndFlush(livestockActivity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the livestockActivity
        restLivestockActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, livestockActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return livestockActivityRepository.count();
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

    protected LivestockActivity getPersistedLivestockActivity(LivestockActivity livestockActivity) {
        return livestockActivityRepository.findById(livestockActivity.getId()).orElseThrow();
    }

    protected void assertPersistedLivestockActivityToMatchAllProperties(LivestockActivity expectedLivestockActivity) {
        assertLivestockActivityAllPropertiesEquals(expectedLivestockActivity, getPersistedLivestockActivity(expectedLivestockActivity));
    }

    protected void assertPersistedLivestockActivityToMatchUpdatableProperties(LivestockActivity expectedLivestockActivity) {
        assertLivestockActivityAllUpdatablePropertiesEquals(
            expectedLivestockActivity,
            getPersistedLivestockActivity(expectedLivestockActivity)
        );
    }
}
