package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.AgriculturalActivityAsserts.*;
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
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.domain.enumeration.AgriculturalExploitationMode;
import com.naline.coopfull.domain.enumeration.LandOwnershipType;
import com.naline.coopfull.repository.AgriculturalActivityRepository;
import com.naline.coopfull.service.AgriculturalActivityService;
import com.naline.coopfull.service.dto.AgriculturalActivityDTO;
import com.naline.coopfull.service.mapper.AgriculturalActivityMapper;
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
 * Integration tests for the {@link AgriculturalActivityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AgriculturalActivityResourceIT {

    private static final BigDecimal DEFAULT_TOTAL_AREA = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AREA = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AREA = new BigDecimal(1 - 1);

    private static final String DEFAULT_AREA_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_AREA_UNIT = "BBBBBBBBBB";

    private static final AgriculturalExploitationMode DEFAULT_EXPLOITATION_MODE = AgriculturalExploitationMode.FAMILY;
    private static final AgriculturalExploitationMode UPDATED_EXPLOITATION_MODE = AgriculturalExploitationMode.INDIVIDUAL;

    private static final LandOwnershipType DEFAULT_OWNERSHIP_TYPE = LandOwnershipType.OWNER;
    private static final LandOwnershipType UPDATED_OWNERSHIP_TYPE = LandOwnershipType.LEASED;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IRRIGATION_AVAILABLE = false;
    private static final Boolean UPDATED_IRRIGATION_AVAILABLE = true;

    private static final Boolean DEFAULT_ORGANIC_PRODUCTION = false;
    private static final Boolean UPDATED_ORGANIC_PRODUCTION = true;

    private static final String DEFAULT_CERTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_CERTIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/agricultural-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgriculturalActivityRepository agriculturalActivityRepository;

    @Mock
    private AgriculturalActivityRepository agriculturalActivityRepositoryMock;

    @Autowired
    private AgriculturalActivityMapper agriculturalActivityMapper;

    @Mock
    private AgriculturalActivityService agriculturalActivityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgriculturalActivityMockMvc;

    private AgriculturalActivity agriculturalActivity;

    private AgriculturalActivity insertedAgriculturalActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgriculturalActivity createEntity() {
        return new AgriculturalActivity()
            .totalArea(DEFAULT_TOTAL_AREA)
            .areaUnit(DEFAULT_AREA_UNIT)
            .exploitationMode(DEFAULT_EXPLOITATION_MODE)
            .ownershipType(DEFAULT_OWNERSHIP_TYPE)
            .startDate(DEFAULT_START_DATE)
            .irrigationAvailable(DEFAULT_IRRIGATION_AVAILABLE)
            .organicProduction(DEFAULT_ORGANIC_PRODUCTION)
            .certification(DEFAULT_CERTIFICATION)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgriculturalActivity createUpdatedEntity() {
        return new AgriculturalActivity()
            .totalArea(UPDATED_TOTAL_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .exploitationMode(UPDATED_EXPLOITATION_MODE)
            .ownershipType(UPDATED_OWNERSHIP_TYPE)
            .startDate(UPDATED_START_DATE)
            .irrigationAvailable(UPDATED_IRRIGATION_AVAILABLE)
            .organicProduction(UPDATED_ORGANIC_PRODUCTION)
            .certification(UPDATED_CERTIFICATION)
            .description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        agriculturalActivity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAgriculturalActivity != null) {
            agriculturalActivityRepository.delete(insertedAgriculturalActivity);
            insertedAgriculturalActivity = null;
        }
    }

    @Test
    @Transactional
    void createAgriculturalActivity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AgriculturalActivity
        AgriculturalActivityDTO agriculturalActivityDTO = agriculturalActivityMapper.toDto(agriculturalActivity);
        var returnedAgriculturalActivityDTO = om.readValue(
            restAgriculturalActivityMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculturalActivityDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AgriculturalActivityDTO.class
        );

        // Validate the AgriculturalActivity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAgriculturalActivity = agriculturalActivityMapper.toEntity(returnedAgriculturalActivityDTO);
        assertAgriculturalActivityUpdatableFieldsEquals(
            returnedAgriculturalActivity,
            getPersistedAgriculturalActivity(returnedAgriculturalActivity)
        );

        insertedAgriculturalActivity = returnedAgriculturalActivity;
    }

    @Test
    @Transactional
    void createAgriculturalActivityWithExistingId() throws Exception {
        // Create the AgriculturalActivity with an existing ID
        agriculturalActivity.setId(1L);
        AgriculturalActivityDTO agriculturalActivityDTO = agriculturalActivityMapper.toDto(agriculturalActivity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgriculturalActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculturalActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTotalAreaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agriculturalActivity.setTotalArea(null);

        // Create the AgriculturalActivity, which fails.
        AgriculturalActivityDTO agriculturalActivityDTO = agriculturalActivityMapper.toDto(agriculturalActivity);

        restAgriculturalActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculturalActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAreaUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        agriculturalActivity.setAreaUnit(null);

        // Create the AgriculturalActivity, which fails.
        AgriculturalActivityDTO agriculturalActivityDTO = agriculturalActivityMapper.toDto(agriculturalActivity);

        restAgriculturalActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculturalActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAgriculturalActivities() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList
        restAgriculturalActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agriculturalActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalArea").value(hasItem(sameNumber(DEFAULT_TOTAL_AREA))))
            .andExpect(jsonPath("$.[*].areaUnit").value(hasItem(DEFAULT_AREA_UNIT)))
            .andExpect(jsonPath("$.[*].exploitationMode").value(hasItem(DEFAULT_EXPLOITATION_MODE.toString())))
            .andExpect(jsonPath("$.[*].ownershipType").value(hasItem(DEFAULT_OWNERSHIP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].irrigationAvailable").value(hasItem(DEFAULT_IRRIGATION_AVAILABLE)))
            .andExpect(jsonPath("$.[*].organicProduction").value(hasItem(DEFAULT_ORGANIC_PRODUCTION)))
            .andExpect(jsonPath("$.[*].certification").value(hasItem(DEFAULT_CERTIFICATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgriculturalActivitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(agriculturalActivityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgriculturalActivityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(agriculturalActivityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAgriculturalActivitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(agriculturalActivityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAgriculturalActivityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(agriculturalActivityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAgriculturalActivity() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get the agriculturalActivity
        restAgriculturalActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, agriculturalActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agriculturalActivity.getId().intValue()))
            .andExpect(jsonPath("$.totalArea").value(sameNumber(DEFAULT_TOTAL_AREA)))
            .andExpect(jsonPath("$.areaUnit").value(DEFAULT_AREA_UNIT))
            .andExpect(jsonPath("$.exploitationMode").value(DEFAULT_EXPLOITATION_MODE.toString()))
            .andExpect(jsonPath("$.ownershipType").value(DEFAULT_OWNERSHIP_TYPE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.irrigationAvailable").value(DEFAULT_IRRIGATION_AVAILABLE))
            .andExpect(jsonPath("$.organicProduction").value(DEFAULT_ORGANIC_PRODUCTION))
            .andExpect(jsonPath("$.certification").value(DEFAULT_CERTIFICATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getAgriculturalActivitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        Long id = agriculturalActivity.getId();

        defaultAgriculturalActivityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAgriculturalActivityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAgriculturalActivityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByTotalAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where totalArea equals to
        defaultAgriculturalActivityFiltering("totalArea.equals=" + DEFAULT_TOTAL_AREA, "totalArea.equals=" + UPDATED_TOTAL_AREA);
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByTotalAreaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where totalArea in
        defaultAgriculturalActivityFiltering(
            "totalArea.in=" + DEFAULT_TOTAL_AREA + "," + UPDATED_TOTAL_AREA,
            "totalArea.in=" + UPDATED_TOTAL_AREA
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByTotalAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where totalArea is not null
        defaultAgriculturalActivityFiltering("totalArea.specified=true", "totalArea.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByTotalAreaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where totalArea is greater than or equal to
        defaultAgriculturalActivityFiltering(
            "totalArea.greaterThanOrEqual=" + DEFAULT_TOTAL_AREA,
            "totalArea.greaterThanOrEqual=" + UPDATED_TOTAL_AREA
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByTotalAreaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where totalArea is less than or equal to
        defaultAgriculturalActivityFiltering(
            "totalArea.lessThanOrEqual=" + DEFAULT_TOTAL_AREA,
            "totalArea.lessThanOrEqual=" + SMALLER_TOTAL_AREA
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByTotalAreaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where totalArea is less than
        defaultAgriculturalActivityFiltering("totalArea.lessThan=" + UPDATED_TOTAL_AREA, "totalArea.lessThan=" + DEFAULT_TOTAL_AREA);
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByTotalAreaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where totalArea is greater than
        defaultAgriculturalActivityFiltering("totalArea.greaterThan=" + SMALLER_TOTAL_AREA, "totalArea.greaterThan=" + DEFAULT_TOTAL_AREA);
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByAreaUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where areaUnit equals to
        defaultAgriculturalActivityFiltering("areaUnit.equals=" + DEFAULT_AREA_UNIT, "areaUnit.equals=" + UPDATED_AREA_UNIT);
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByAreaUnitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where areaUnit in
        defaultAgriculturalActivityFiltering(
            "areaUnit.in=" + DEFAULT_AREA_UNIT + "," + UPDATED_AREA_UNIT,
            "areaUnit.in=" + UPDATED_AREA_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByAreaUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where areaUnit is not null
        defaultAgriculturalActivityFiltering("areaUnit.specified=true", "areaUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByAreaUnitContainsSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where areaUnit contains
        defaultAgriculturalActivityFiltering("areaUnit.contains=" + DEFAULT_AREA_UNIT, "areaUnit.contains=" + UPDATED_AREA_UNIT);
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByAreaUnitNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where areaUnit does not contain
        defaultAgriculturalActivityFiltering(
            "areaUnit.doesNotContain=" + UPDATED_AREA_UNIT,
            "areaUnit.doesNotContain=" + DEFAULT_AREA_UNIT
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByExploitationModeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where exploitationMode equals to
        defaultAgriculturalActivityFiltering(
            "exploitationMode.equals=" + DEFAULT_EXPLOITATION_MODE,
            "exploitationMode.equals=" + UPDATED_EXPLOITATION_MODE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByExploitationModeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where exploitationMode in
        defaultAgriculturalActivityFiltering(
            "exploitationMode.in=" + DEFAULT_EXPLOITATION_MODE + "," + UPDATED_EXPLOITATION_MODE,
            "exploitationMode.in=" + UPDATED_EXPLOITATION_MODE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByExploitationModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where exploitationMode is not null
        defaultAgriculturalActivityFiltering("exploitationMode.specified=true", "exploitationMode.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByOwnershipTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where ownershipType equals to
        defaultAgriculturalActivityFiltering(
            "ownershipType.equals=" + DEFAULT_OWNERSHIP_TYPE,
            "ownershipType.equals=" + UPDATED_OWNERSHIP_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByOwnershipTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where ownershipType in
        defaultAgriculturalActivityFiltering(
            "ownershipType.in=" + DEFAULT_OWNERSHIP_TYPE + "," + UPDATED_OWNERSHIP_TYPE,
            "ownershipType.in=" + UPDATED_OWNERSHIP_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByOwnershipTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where ownershipType is not null
        defaultAgriculturalActivityFiltering("ownershipType.specified=true", "ownershipType.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where startDate equals to
        defaultAgriculturalActivityFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where startDate in
        defaultAgriculturalActivityFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where startDate is not null
        defaultAgriculturalActivityFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where startDate is greater than or equal to
        defaultAgriculturalActivityFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where startDate is less than or equal to
        defaultAgriculturalActivityFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where startDate is less than
        defaultAgriculturalActivityFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where startDate is greater than
        defaultAgriculturalActivityFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByIrrigationAvailableIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where irrigationAvailable equals to
        defaultAgriculturalActivityFiltering(
            "irrigationAvailable.equals=" + DEFAULT_IRRIGATION_AVAILABLE,
            "irrigationAvailable.equals=" + UPDATED_IRRIGATION_AVAILABLE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByIrrigationAvailableIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where irrigationAvailable in
        defaultAgriculturalActivityFiltering(
            "irrigationAvailable.in=" + DEFAULT_IRRIGATION_AVAILABLE + "," + UPDATED_IRRIGATION_AVAILABLE,
            "irrigationAvailable.in=" + UPDATED_IRRIGATION_AVAILABLE
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByIrrigationAvailableIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where irrigationAvailable is not null
        defaultAgriculturalActivityFiltering("irrigationAvailable.specified=true", "irrigationAvailable.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByOrganicProductionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where organicProduction equals to
        defaultAgriculturalActivityFiltering(
            "organicProduction.equals=" + DEFAULT_ORGANIC_PRODUCTION,
            "organicProduction.equals=" + UPDATED_ORGANIC_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByOrganicProductionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where organicProduction in
        defaultAgriculturalActivityFiltering(
            "organicProduction.in=" + DEFAULT_ORGANIC_PRODUCTION + "," + UPDATED_ORGANIC_PRODUCTION,
            "organicProduction.in=" + UPDATED_ORGANIC_PRODUCTION
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByOrganicProductionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where organicProduction is not null
        defaultAgriculturalActivityFiltering("organicProduction.specified=true", "organicProduction.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByCertificationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where certification equals to
        defaultAgriculturalActivityFiltering(
            "certification.equals=" + DEFAULT_CERTIFICATION,
            "certification.equals=" + UPDATED_CERTIFICATION
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByCertificationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where certification in
        defaultAgriculturalActivityFiltering(
            "certification.in=" + DEFAULT_CERTIFICATION + "," + UPDATED_CERTIFICATION,
            "certification.in=" + UPDATED_CERTIFICATION
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByCertificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where certification is not null
        defaultAgriculturalActivityFiltering("certification.specified=true", "certification.specified=false");
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByCertificationContainsSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where certification contains
        defaultAgriculturalActivityFiltering(
            "certification.contains=" + DEFAULT_CERTIFICATION,
            "certification.contains=" + UPDATED_CERTIFICATION
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByCertificationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        // Get all the agriculturalActivityList where certification does not contain
        defaultAgriculturalActivityFiltering(
            "certification.doesNotContain=" + UPDATED_CERTIFICATION,
            "certification.doesNotContain=" + DEFAULT_CERTIFICATION
        );
    }

    @Test
    @Transactional
    void getAllAgriculturalActivitiesByLocationIsEqualToSomething() throws Exception {
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            agriculturalActivityRepository.saveAndFlush(agriculturalActivity);
            location = LocationResourceIT.createEntity();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(location);
        em.flush();
        agriculturalActivity.setLocation(location);
        agriculturalActivityRepository.saveAndFlush(agriculturalActivity);
        Long locationId = location.getId();
        // Get all the agriculturalActivityList where location equals to locationId
        defaultAgriculturalActivityShouldBeFound("locationId.equals=" + locationId);

        // Get all the agriculturalActivityList where location equals to (locationId + 1)
        defaultAgriculturalActivityShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    private void defaultAgriculturalActivityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAgriculturalActivityShouldBeFound(shouldBeFound);
        defaultAgriculturalActivityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgriculturalActivityShouldBeFound(String filter) throws Exception {
        restAgriculturalActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agriculturalActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalArea").value(hasItem(sameNumber(DEFAULT_TOTAL_AREA))))
            .andExpect(jsonPath("$.[*].areaUnit").value(hasItem(DEFAULT_AREA_UNIT)))
            .andExpect(jsonPath("$.[*].exploitationMode").value(hasItem(DEFAULT_EXPLOITATION_MODE.toString())))
            .andExpect(jsonPath("$.[*].ownershipType").value(hasItem(DEFAULT_OWNERSHIP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].irrigationAvailable").value(hasItem(DEFAULT_IRRIGATION_AVAILABLE)))
            .andExpect(jsonPath("$.[*].organicProduction").value(hasItem(DEFAULT_ORGANIC_PRODUCTION)))
            .andExpect(jsonPath("$.[*].certification").value(hasItem(DEFAULT_CERTIFICATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restAgriculturalActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgriculturalActivityShouldNotBeFound(String filter) throws Exception {
        restAgriculturalActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgriculturalActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgriculturalActivity() throws Exception {
        // Get the agriculturalActivity
        restAgriculturalActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgriculturalActivity() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agriculturalActivity
        AgriculturalActivity updatedAgriculturalActivity = agriculturalActivityRepository
            .findById(agriculturalActivity.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAgriculturalActivity are not directly saved in db
        em.detach(updatedAgriculturalActivity);
        updatedAgriculturalActivity
            .totalArea(UPDATED_TOTAL_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .exploitationMode(UPDATED_EXPLOITATION_MODE)
            .ownershipType(UPDATED_OWNERSHIP_TYPE)
            .startDate(UPDATED_START_DATE)
            .irrigationAvailable(UPDATED_IRRIGATION_AVAILABLE)
            .organicProduction(UPDATED_ORGANIC_PRODUCTION)
            .certification(UPDATED_CERTIFICATION)
            .description(UPDATED_DESCRIPTION);
        AgriculturalActivityDTO agriculturalActivityDTO = agriculturalActivityMapper.toDto(updatedAgriculturalActivity);

        restAgriculturalActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agriculturalActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agriculturalActivityDTO))
            )
            .andExpect(status().isOk());

        // Validate the AgriculturalActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgriculturalActivityToMatchAllProperties(updatedAgriculturalActivity);
    }

    @Test
    @Transactional
    void putNonExistingAgriculturalActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculturalActivity.setId(longCount.incrementAndGet());

        // Create the AgriculturalActivity
        AgriculturalActivityDTO agriculturalActivityDTO = agriculturalActivityMapper.toDto(agriculturalActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgriculturalActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agriculturalActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agriculturalActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgriculturalActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculturalActivity.setId(longCount.incrementAndGet());

        // Create the AgriculturalActivity
        AgriculturalActivityDTO agriculturalActivityDTO = agriculturalActivityMapper.toDto(agriculturalActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculturalActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agriculturalActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgriculturalActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculturalActivity.setId(longCount.incrementAndGet());

        // Create the AgriculturalActivity
        AgriculturalActivityDTO agriculturalActivityDTO = agriculturalActivityMapper.toDto(agriculturalActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculturalActivityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agriculturalActivityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgriculturalActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgriculturalActivityWithPatch() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agriculturalActivity using partial update
        AgriculturalActivity partialUpdatedAgriculturalActivity = new AgriculturalActivity();
        partialUpdatedAgriculturalActivity.setId(agriculturalActivity.getId());

        partialUpdatedAgriculturalActivity
            .areaUnit(UPDATED_AREA_UNIT)
            .exploitationMode(UPDATED_EXPLOITATION_MODE)
            .ownershipType(UPDATED_OWNERSHIP_TYPE)
            .startDate(UPDATED_START_DATE)
            .irrigationAvailable(UPDATED_IRRIGATION_AVAILABLE)
            .organicProduction(UPDATED_ORGANIC_PRODUCTION)
            .certification(UPDATED_CERTIFICATION);

        restAgriculturalActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgriculturalActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgriculturalActivity))
            )
            .andExpect(status().isOk());

        // Validate the AgriculturalActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgriculturalActivityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAgriculturalActivity, agriculturalActivity),
            getPersistedAgriculturalActivity(agriculturalActivity)
        );
    }

    @Test
    @Transactional
    void fullUpdateAgriculturalActivityWithPatch() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agriculturalActivity using partial update
        AgriculturalActivity partialUpdatedAgriculturalActivity = new AgriculturalActivity();
        partialUpdatedAgriculturalActivity.setId(agriculturalActivity.getId());

        partialUpdatedAgriculturalActivity
            .totalArea(UPDATED_TOTAL_AREA)
            .areaUnit(UPDATED_AREA_UNIT)
            .exploitationMode(UPDATED_EXPLOITATION_MODE)
            .ownershipType(UPDATED_OWNERSHIP_TYPE)
            .startDate(UPDATED_START_DATE)
            .irrigationAvailable(UPDATED_IRRIGATION_AVAILABLE)
            .organicProduction(UPDATED_ORGANIC_PRODUCTION)
            .certification(UPDATED_CERTIFICATION)
            .description(UPDATED_DESCRIPTION);

        restAgriculturalActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgriculturalActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgriculturalActivity))
            )
            .andExpect(status().isOk());

        // Validate the AgriculturalActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgriculturalActivityUpdatableFieldsEquals(
            partialUpdatedAgriculturalActivity,
            getPersistedAgriculturalActivity(partialUpdatedAgriculturalActivity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAgriculturalActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculturalActivity.setId(longCount.incrementAndGet());

        // Create the AgriculturalActivity
        AgriculturalActivityDTO agriculturalActivityDTO = agriculturalActivityMapper.toDto(agriculturalActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgriculturalActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agriculturalActivityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agriculturalActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgriculturalActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculturalActivity.setId(longCount.incrementAndGet());

        // Create the AgriculturalActivity
        AgriculturalActivityDTO agriculturalActivityDTO = agriculturalActivityMapper.toDto(agriculturalActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculturalActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agriculturalActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AgriculturalActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgriculturalActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        agriculturalActivity.setId(longCount.incrementAndGet());

        // Create the AgriculturalActivity
        AgriculturalActivityDTO agriculturalActivityDTO = agriculturalActivityMapper.toDto(agriculturalActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgriculturalActivityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agriculturalActivityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AgriculturalActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgriculturalActivity() throws Exception {
        // Initialize the database
        insertedAgriculturalActivity = agriculturalActivityRepository.saveAndFlush(agriculturalActivity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the agriculturalActivity
        restAgriculturalActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, agriculturalActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return agriculturalActivityRepository.count();
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

    protected AgriculturalActivity getPersistedAgriculturalActivity(AgriculturalActivity agriculturalActivity) {
        return agriculturalActivityRepository.findById(agriculturalActivity.getId()).orElseThrow();
    }

    protected void assertPersistedAgriculturalActivityToMatchAllProperties(AgriculturalActivity expectedAgriculturalActivity) {
        assertAgriculturalActivityAllPropertiesEquals(
            expectedAgriculturalActivity,
            getPersistedAgriculturalActivity(expectedAgriculturalActivity)
        );
    }

    protected void assertPersistedAgriculturalActivityToMatchUpdatableProperties(AgriculturalActivity expectedAgriculturalActivity) {
        assertAgriculturalActivityAllUpdatablePropertiesEquals(
            expectedAgriculturalActivity,
            getPersistedAgriculturalActivity(expectedAgriculturalActivity)
        );
    }
}
