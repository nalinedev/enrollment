package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.EconomicActivityAsserts.*;
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
import com.naline.coopfull.domain.AquacultureActivity;
import com.naline.coopfull.domain.EconomicActivity;
import com.naline.coopfull.domain.EconomicActivityType;
import com.naline.coopfull.domain.LivestockActivity;
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.domain.enumeration.EconomicActivityStatus;
import com.naline.coopfull.repository.EconomicActivityRepository;
import com.naline.coopfull.service.EconomicActivityService;
import com.naline.coopfull.service.dto.EconomicActivityDTO;
import com.naline.coopfull.service.mapper.EconomicActivityMapper;
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
 * Integration tests for the {@link EconomicActivityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EconomicActivityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MAIN_ACTIVITY = false;
    private static final Boolean UPDATED_MAIN_ACTIVITY = true;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_ANNUAL_REVENUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ANNUAL_REVENUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ANNUAL_REVENUE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MONTHLY_REVENUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTHLY_REVENUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_MONTHLY_REVENUE = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_NUMBER_OF_EMPLOYEES = 1;
    private static final Integer UPDATED_NUMBER_OF_EMPLOYEES = 2;
    private static final Integer SMALLER_NUMBER_OF_EMPLOYEES = 1 - 1;

    private static final EconomicActivityStatus DEFAULT_STATUS = EconomicActivityStatus.ACTIVE;
    private static final EconomicActivityStatus UPDATED_STATUS = EconomicActivityStatus.INACTIVE;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/economic-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EconomicActivityRepository economicActivityRepository;

    @Mock
    private EconomicActivityRepository economicActivityRepositoryMock;

    @Autowired
    private EconomicActivityMapper economicActivityMapper;

    @Mock
    private EconomicActivityService economicActivityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEconomicActivityMockMvc;

    private EconomicActivity economicActivity;

    private EconomicActivity insertedEconomicActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EconomicActivity createEntity() {
        return new EconomicActivity()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .mainActivity(DEFAULT_MAIN_ACTIVITY)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .annualRevenue(DEFAULT_ANNUAL_REVENUE)
            .monthlyRevenue(DEFAULT_MONTHLY_REVENUE)
            .numberOfEmployees(DEFAULT_NUMBER_OF_EMPLOYEES)
            .status(DEFAULT_STATUS)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EconomicActivity createUpdatedEntity() {
        return new EconomicActivity()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .mainActivity(UPDATED_MAIN_ACTIVITY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .annualRevenue(UPDATED_ANNUAL_REVENUE)
            .monthlyRevenue(UPDATED_MONTHLY_REVENUE)
            .numberOfEmployees(UPDATED_NUMBER_OF_EMPLOYEES)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        economicActivity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEconomicActivity != null) {
            economicActivityRepository.delete(insertedEconomicActivity);
            insertedEconomicActivity = null;
        }
    }

    @Test
    @Transactional
    void createEconomicActivity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EconomicActivity
        EconomicActivityDTO economicActivityDTO = economicActivityMapper.toDto(economicActivity);
        var returnedEconomicActivityDTO = om.readValue(
            restEconomicActivityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(economicActivityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EconomicActivityDTO.class
        );

        // Validate the EconomicActivity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEconomicActivity = economicActivityMapper.toEntity(returnedEconomicActivityDTO);
        assertEconomicActivityUpdatableFieldsEquals(returnedEconomicActivity, getPersistedEconomicActivity(returnedEconomicActivity));

        insertedEconomicActivity = returnedEconomicActivity;
    }

    @Test
    @Transactional
    void createEconomicActivityWithExistingId() throws Exception {
        // Create the EconomicActivity with an existing ID
        economicActivity.setId(1L);
        EconomicActivityDTO economicActivityDTO = economicActivityMapper.toDto(economicActivity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEconomicActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(economicActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EconomicActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        economicActivity.setName(null);

        // Create the EconomicActivity, which fails.
        EconomicActivityDTO economicActivityDTO = economicActivityMapper.toDto(economicActivity);

        restEconomicActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(economicActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMainActivityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        economicActivity.setMainActivity(null);

        // Create the EconomicActivity, which fails.
        EconomicActivityDTO economicActivityDTO = economicActivityMapper.toDto(economicActivity);

        restEconomicActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(economicActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        economicActivity.setStatus(null);

        // Create the EconomicActivity, which fails.
        EconomicActivityDTO economicActivityDTO = economicActivityMapper.toDto(economicActivity);

        restEconomicActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(economicActivityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEconomicActivities() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList
        restEconomicActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(economicActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].mainActivity").value(hasItem(DEFAULT_MAIN_ACTIVITY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].annualRevenue").value(hasItem(sameNumber(DEFAULT_ANNUAL_REVENUE))))
            .andExpect(jsonPath("$.[*].monthlyRevenue").value(hasItem(sameNumber(DEFAULT_MONTHLY_REVENUE))))
            .andExpect(jsonPath("$.[*].numberOfEmployees").value(hasItem(DEFAULT_NUMBER_OF_EMPLOYEES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEconomicActivitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(economicActivityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEconomicActivityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(economicActivityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEconomicActivitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(economicActivityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEconomicActivityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(economicActivityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEconomicActivity() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get the economicActivity
        restEconomicActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, economicActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(economicActivity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.mainActivity").value(DEFAULT_MAIN_ACTIVITY))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.annualRevenue").value(sameNumber(DEFAULT_ANNUAL_REVENUE)))
            .andExpect(jsonPath("$.monthlyRevenue").value(sameNumber(DEFAULT_MONTHLY_REVENUE)))
            .andExpect(jsonPath("$.numberOfEmployees").value(DEFAULT_NUMBER_OF_EMPLOYEES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getEconomicActivitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        Long id = economicActivity.getId();

        defaultEconomicActivityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEconomicActivityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEconomicActivityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where name equals to
        defaultEconomicActivityFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where name in
        defaultEconomicActivityFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where name is not null
        defaultEconomicActivityFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where name contains
        defaultEconomicActivityFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where name does not contain
        defaultEconomicActivityFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByMainActivityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where mainActivity equals to
        defaultEconomicActivityFiltering("mainActivity.equals=" + DEFAULT_MAIN_ACTIVITY, "mainActivity.equals=" + UPDATED_MAIN_ACTIVITY);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByMainActivityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where mainActivity in
        defaultEconomicActivityFiltering(
            "mainActivity.in=" + DEFAULT_MAIN_ACTIVITY + "," + UPDATED_MAIN_ACTIVITY,
            "mainActivity.in=" + UPDATED_MAIN_ACTIVITY
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByMainActivityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where mainActivity is not null
        defaultEconomicActivityFiltering("mainActivity.specified=true", "mainActivity.specified=false");
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where startDate equals to
        defaultEconomicActivityFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where startDate in
        defaultEconomicActivityFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where startDate is not null
        defaultEconomicActivityFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where startDate is greater than or equal to
        defaultEconomicActivityFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where startDate is less than or equal to
        defaultEconomicActivityFiltering(
            "startDate.lessThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.lessThanOrEqual=" + SMALLER_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where startDate is less than
        defaultEconomicActivityFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where startDate is greater than
        defaultEconomicActivityFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where endDate equals to
        defaultEconomicActivityFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where endDate in
        defaultEconomicActivityFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where endDate is not null
        defaultEconomicActivityFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where endDate is greater than or equal to
        defaultEconomicActivityFiltering(
            "endDate.greaterThanOrEqual=" + DEFAULT_END_DATE,
            "endDate.greaterThanOrEqual=" + UPDATED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where endDate is less than or equal to
        defaultEconomicActivityFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where endDate is less than
        defaultEconomicActivityFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where endDate is greater than
        defaultEconomicActivityFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByAnnualRevenueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where annualRevenue equals to
        defaultEconomicActivityFiltering(
            "annualRevenue.equals=" + DEFAULT_ANNUAL_REVENUE,
            "annualRevenue.equals=" + UPDATED_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByAnnualRevenueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where annualRevenue in
        defaultEconomicActivityFiltering(
            "annualRevenue.in=" + DEFAULT_ANNUAL_REVENUE + "," + UPDATED_ANNUAL_REVENUE,
            "annualRevenue.in=" + UPDATED_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByAnnualRevenueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where annualRevenue is not null
        defaultEconomicActivityFiltering("annualRevenue.specified=true", "annualRevenue.specified=false");
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByAnnualRevenueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where annualRevenue is greater than or equal to
        defaultEconomicActivityFiltering(
            "annualRevenue.greaterThanOrEqual=" + DEFAULT_ANNUAL_REVENUE,
            "annualRevenue.greaterThanOrEqual=" + UPDATED_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByAnnualRevenueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where annualRevenue is less than or equal to
        defaultEconomicActivityFiltering(
            "annualRevenue.lessThanOrEqual=" + DEFAULT_ANNUAL_REVENUE,
            "annualRevenue.lessThanOrEqual=" + SMALLER_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByAnnualRevenueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where annualRevenue is less than
        defaultEconomicActivityFiltering(
            "annualRevenue.lessThan=" + UPDATED_ANNUAL_REVENUE,
            "annualRevenue.lessThan=" + DEFAULT_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByAnnualRevenueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where annualRevenue is greater than
        defaultEconomicActivityFiltering(
            "annualRevenue.greaterThan=" + SMALLER_ANNUAL_REVENUE,
            "annualRevenue.greaterThan=" + DEFAULT_ANNUAL_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByMonthlyRevenueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where monthlyRevenue equals to
        defaultEconomicActivityFiltering(
            "monthlyRevenue.equals=" + DEFAULT_MONTHLY_REVENUE,
            "monthlyRevenue.equals=" + UPDATED_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByMonthlyRevenueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where monthlyRevenue in
        defaultEconomicActivityFiltering(
            "monthlyRevenue.in=" + DEFAULT_MONTHLY_REVENUE + "," + UPDATED_MONTHLY_REVENUE,
            "monthlyRevenue.in=" + UPDATED_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByMonthlyRevenueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where monthlyRevenue is not null
        defaultEconomicActivityFiltering("monthlyRevenue.specified=true", "monthlyRevenue.specified=false");
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByMonthlyRevenueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where monthlyRevenue is greater than or equal to
        defaultEconomicActivityFiltering(
            "monthlyRevenue.greaterThanOrEqual=" + DEFAULT_MONTHLY_REVENUE,
            "monthlyRevenue.greaterThanOrEqual=" + UPDATED_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByMonthlyRevenueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where monthlyRevenue is less than or equal to
        defaultEconomicActivityFiltering(
            "monthlyRevenue.lessThanOrEqual=" + DEFAULT_MONTHLY_REVENUE,
            "monthlyRevenue.lessThanOrEqual=" + SMALLER_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByMonthlyRevenueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where monthlyRevenue is less than
        defaultEconomicActivityFiltering(
            "monthlyRevenue.lessThan=" + UPDATED_MONTHLY_REVENUE,
            "monthlyRevenue.lessThan=" + DEFAULT_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByMonthlyRevenueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where monthlyRevenue is greater than
        defaultEconomicActivityFiltering(
            "monthlyRevenue.greaterThan=" + SMALLER_MONTHLY_REVENUE,
            "monthlyRevenue.greaterThan=" + DEFAULT_MONTHLY_REVENUE
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByNumberOfEmployeesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where numberOfEmployees equals to
        defaultEconomicActivityFiltering(
            "numberOfEmployees.equals=" + DEFAULT_NUMBER_OF_EMPLOYEES,
            "numberOfEmployees.equals=" + UPDATED_NUMBER_OF_EMPLOYEES
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByNumberOfEmployeesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where numberOfEmployees in
        defaultEconomicActivityFiltering(
            "numberOfEmployees.in=" + DEFAULT_NUMBER_OF_EMPLOYEES + "," + UPDATED_NUMBER_OF_EMPLOYEES,
            "numberOfEmployees.in=" + UPDATED_NUMBER_OF_EMPLOYEES
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByNumberOfEmployeesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where numberOfEmployees is not null
        defaultEconomicActivityFiltering("numberOfEmployees.specified=true", "numberOfEmployees.specified=false");
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByNumberOfEmployeesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where numberOfEmployees is greater than or equal to
        defaultEconomicActivityFiltering(
            "numberOfEmployees.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_EMPLOYEES,
            "numberOfEmployees.greaterThanOrEqual=" + UPDATED_NUMBER_OF_EMPLOYEES
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByNumberOfEmployeesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where numberOfEmployees is less than or equal to
        defaultEconomicActivityFiltering(
            "numberOfEmployees.lessThanOrEqual=" + DEFAULT_NUMBER_OF_EMPLOYEES,
            "numberOfEmployees.lessThanOrEqual=" + SMALLER_NUMBER_OF_EMPLOYEES
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByNumberOfEmployeesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where numberOfEmployees is less than
        defaultEconomicActivityFiltering(
            "numberOfEmployees.lessThan=" + UPDATED_NUMBER_OF_EMPLOYEES,
            "numberOfEmployees.lessThan=" + DEFAULT_NUMBER_OF_EMPLOYEES
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByNumberOfEmployeesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where numberOfEmployees is greater than
        defaultEconomicActivityFiltering(
            "numberOfEmployees.greaterThan=" + SMALLER_NUMBER_OF_EMPLOYEES,
            "numberOfEmployees.greaterThan=" + DEFAULT_NUMBER_OF_EMPLOYEES
        );
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where status equals to
        defaultEconomicActivityFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where status in
        defaultEconomicActivityFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        // Get all the economicActivityList where status is not null
        defaultEconomicActivityFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByAgriculturalActivityIsEqualToSomething() throws Exception {
        AgriculturalActivity agriculturalActivity;
        if (TestUtil.findAll(em, AgriculturalActivity.class).isEmpty()) {
            economicActivityRepository.saveAndFlush(economicActivity);
            agriculturalActivity = AgriculturalActivityResourceIT.createEntity();
        } else {
            agriculturalActivity = TestUtil.findAll(em, AgriculturalActivity.class).get(0);
        }
        em.persist(agriculturalActivity);
        em.flush();
        economicActivity.setAgriculturalActivity(agriculturalActivity);
        economicActivityRepository.saveAndFlush(economicActivity);
        Long agriculturalActivityId = agriculturalActivity.getId();
        // Get all the economicActivityList where agriculturalActivity equals to agriculturalActivityId
        defaultEconomicActivityShouldBeFound("agriculturalActivityId.equals=" + agriculturalActivityId);

        // Get all the economicActivityList where agriculturalActivity equals to (agriculturalActivityId + 1)
        defaultEconomicActivityShouldNotBeFound("agriculturalActivityId.equals=" + (agriculturalActivityId + 1));
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByLivestockActivityIsEqualToSomething() throws Exception {
        LivestockActivity livestockActivity;
        if (TestUtil.findAll(em, LivestockActivity.class).isEmpty()) {
            economicActivityRepository.saveAndFlush(economicActivity);
            livestockActivity = LivestockActivityResourceIT.createEntity();
        } else {
            livestockActivity = TestUtil.findAll(em, LivestockActivity.class).get(0);
        }
        em.persist(livestockActivity);
        em.flush();
        economicActivity.setLivestockActivity(livestockActivity);
        economicActivityRepository.saveAndFlush(economicActivity);
        Long livestockActivityId = livestockActivity.getId();
        // Get all the economicActivityList where livestockActivity equals to livestockActivityId
        defaultEconomicActivityShouldBeFound("livestockActivityId.equals=" + livestockActivityId);

        // Get all the economicActivityList where livestockActivity equals to (livestockActivityId + 1)
        defaultEconomicActivityShouldNotBeFound("livestockActivityId.equals=" + (livestockActivityId + 1));
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByAquacultureActivityIsEqualToSomething() throws Exception {
        AquacultureActivity aquacultureActivity;
        if (TestUtil.findAll(em, AquacultureActivity.class).isEmpty()) {
            economicActivityRepository.saveAndFlush(economicActivity);
            aquacultureActivity = AquacultureActivityResourceIT.createEntity();
        } else {
            aquacultureActivity = TestUtil.findAll(em, AquacultureActivity.class).get(0);
        }
        em.persist(aquacultureActivity);
        em.flush();
        economicActivity.setAquacultureActivity(aquacultureActivity);
        economicActivityRepository.saveAndFlush(economicActivity);
        Long aquacultureActivityId = aquacultureActivity.getId();
        // Get all the economicActivityList where aquacultureActivity equals to aquacultureActivityId
        defaultEconomicActivityShouldBeFound("aquacultureActivityId.equals=" + aquacultureActivityId);

        // Get all the economicActivityList where aquacultureActivity equals to (aquacultureActivityId + 1)
        defaultEconomicActivityShouldNotBeFound("aquacultureActivityId.equals=" + (aquacultureActivityId + 1));
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByMemberIsEqualToSomething() throws Exception {
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            economicActivityRepository.saveAndFlush(economicActivity);
            member = MemberResourceIT.createEntity();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        em.persist(member);
        em.flush();
        economicActivity.setMember(member);
        economicActivityRepository.saveAndFlush(economicActivity);
        Long memberId = member.getId();
        // Get all the economicActivityList where member equals to memberId
        defaultEconomicActivityShouldBeFound("memberId.equals=" + memberId);

        // Get all the economicActivityList where member equals to (memberId + 1)
        defaultEconomicActivityShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByActivityTypeIsEqualToSomething() throws Exception {
        EconomicActivityType activityType;
        if (TestUtil.findAll(em, EconomicActivityType.class).isEmpty()) {
            economicActivityRepository.saveAndFlush(economicActivity);
            activityType = EconomicActivityTypeResourceIT.createEntity();
        } else {
            activityType = TestUtil.findAll(em, EconomicActivityType.class).get(0);
        }
        em.persist(activityType);
        em.flush();
        economicActivity.setActivityType(activityType);
        economicActivityRepository.saveAndFlush(economicActivity);
        Long activityTypeId = activityType.getId();
        // Get all the economicActivityList where activityType equals to activityTypeId
        defaultEconomicActivityShouldBeFound("activityTypeId.equals=" + activityTypeId);

        // Get all the economicActivityList where activityType equals to (activityTypeId + 1)
        defaultEconomicActivityShouldNotBeFound("activityTypeId.equals=" + (activityTypeId + 1));
    }

    @Test
    @Transactional
    void getAllEconomicActivitiesByLocationIsEqualToSomething() throws Exception {
        Location location;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            economicActivityRepository.saveAndFlush(economicActivity);
            location = LocationResourceIT.createEntity();
        } else {
            location = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(location);
        em.flush();
        economicActivity.setLocation(location);
        economicActivityRepository.saveAndFlush(economicActivity);
        Long locationId = location.getId();
        // Get all the economicActivityList where location equals to locationId
        defaultEconomicActivityShouldBeFound("locationId.equals=" + locationId);

        // Get all the economicActivityList where location equals to (locationId + 1)
        defaultEconomicActivityShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    private void defaultEconomicActivityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEconomicActivityShouldBeFound(shouldBeFound);
        defaultEconomicActivityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEconomicActivityShouldBeFound(String filter) throws Exception {
        restEconomicActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(economicActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].mainActivity").value(hasItem(DEFAULT_MAIN_ACTIVITY)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].annualRevenue").value(hasItem(sameNumber(DEFAULT_ANNUAL_REVENUE))))
            .andExpect(jsonPath("$.[*].monthlyRevenue").value(hasItem(sameNumber(DEFAULT_MONTHLY_REVENUE))))
            .andExpect(jsonPath("$.[*].numberOfEmployees").value(hasItem(DEFAULT_NUMBER_OF_EMPLOYEES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restEconomicActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEconomicActivityShouldNotBeFound(String filter) throws Exception {
        restEconomicActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEconomicActivityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEconomicActivity() throws Exception {
        // Get the economicActivity
        restEconomicActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEconomicActivity() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the economicActivity
        EconomicActivity updatedEconomicActivity = economicActivityRepository.findById(economicActivity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEconomicActivity are not directly saved in db
        em.detach(updatedEconomicActivity);
        updatedEconomicActivity
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .mainActivity(UPDATED_MAIN_ACTIVITY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .annualRevenue(UPDATED_ANNUAL_REVENUE)
            .monthlyRevenue(UPDATED_MONTHLY_REVENUE)
            .numberOfEmployees(UPDATED_NUMBER_OF_EMPLOYEES)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);
        EconomicActivityDTO economicActivityDTO = economicActivityMapper.toDto(updatedEconomicActivity);

        restEconomicActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, economicActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(economicActivityDTO))
            )
            .andExpect(status().isOk());

        // Validate the EconomicActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEconomicActivityToMatchAllProperties(updatedEconomicActivity);
    }

    @Test
    @Transactional
    void putNonExistingEconomicActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        economicActivity.setId(longCount.incrementAndGet());

        // Create the EconomicActivity
        EconomicActivityDTO economicActivityDTO = economicActivityMapper.toDto(economicActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEconomicActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, economicActivityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(economicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EconomicActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEconomicActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        economicActivity.setId(longCount.incrementAndGet());

        // Create the EconomicActivity
        EconomicActivityDTO economicActivityDTO = economicActivityMapper.toDto(economicActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEconomicActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(economicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EconomicActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEconomicActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        economicActivity.setId(longCount.incrementAndGet());

        // Create the EconomicActivity
        EconomicActivityDTO economicActivityDTO = economicActivityMapper.toDto(economicActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEconomicActivityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(economicActivityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EconomicActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEconomicActivityWithPatch() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the economicActivity using partial update
        EconomicActivity partialUpdatedEconomicActivity = new EconomicActivity();
        partialUpdatedEconomicActivity.setId(economicActivity.getId());

        partialUpdatedEconomicActivity
            .name(UPDATED_NAME)
            .endDate(UPDATED_END_DATE)
            .annualRevenue(UPDATED_ANNUAL_REVENUE)
            .numberOfEmployees(UPDATED_NUMBER_OF_EMPLOYEES)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);

        restEconomicActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEconomicActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEconomicActivity))
            )
            .andExpect(status().isOk());

        // Validate the EconomicActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEconomicActivityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEconomicActivity, economicActivity),
            getPersistedEconomicActivity(economicActivity)
        );
    }

    @Test
    @Transactional
    void fullUpdateEconomicActivityWithPatch() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the economicActivity using partial update
        EconomicActivity partialUpdatedEconomicActivity = new EconomicActivity();
        partialUpdatedEconomicActivity.setId(economicActivity.getId());

        partialUpdatedEconomicActivity
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .mainActivity(UPDATED_MAIN_ACTIVITY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .annualRevenue(UPDATED_ANNUAL_REVENUE)
            .monthlyRevenue(UPDATED_MONTHLY_REVENUE)
            .numberOfEmployees(UPDATED_NUMBER_OF_EMPLOYEES)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);

        restEconomicActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEconomicActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEconomicActivity))
            )
            .andExpect(status().isOk());

        // Validate the EconomicActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEconomicActivityUpdatableFieldsEquals(
            partialUpdatedEconomicActivity,
            getPersistedEconomicActivity(partialUpdatedEconomicActivity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEconomicActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        economicActivity.setId(longCount.incrementAndGet());

        // Create the EconomicActivity
        EconomicActivityDTO economicActivityDTO = economicActivityMapper.toDto(economicActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEconomicActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, economicActivityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(economicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EconomicActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEconomicActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        economicActivity.setId(longCount.incrementAndGet());

        // Create the EconomicActivity
        EconomicActivityDTO economicActivityDTO = economicActivityMapper.toDto(economicActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEconomicActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(economicActivityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EconomicActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEconomicActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        economicActivity.setId(longCount.incrementAndGet());

        // Create the EconomicActivity
        EconomicActivityDTO economicActivityDTO = economicActivityMapper.toDto(economicActivity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEconomicActivityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(economicActivityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EconomicActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEconomicActivity() throws Exception {
        // Initialize the database
        insertedEconomicActivity = economicActivityRepository.saveAndFlush(economicActivity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the economicActivity
        restEconomicActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, economicActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return economicActivityRepository.count();
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

    protected EconomicActivity getPersistedEconomicActivity(EconomicActivity economicActivity) {
        return economicActivityRepository.findById(economicActivity.getId()).orElseThrow();
    }

    protected void assertPersistedEconomicActivityToMatchAllProperties(EconomicActivity expectedEconomicActivity) {
        assertEconomicActivityAllPropertiesEquals(expectedEconomicActivity, getPersistedEconomicActivity(expectedEconomicActivity));
    }

    protected void assertPersistedEconomicActivityToMatchUpdatableProperties(EconomicActivity expectedEconomicActivity) {
        assertEconomicActivityAllUpdatablePropertiesEquals(
            expectedEconomicActivity,
            getPersistedEconomicActivity(expectedEconomicActivity)
        );
    }
}
