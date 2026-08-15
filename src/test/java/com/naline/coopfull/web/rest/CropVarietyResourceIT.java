package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.CropVarietyAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static com.naline.coopfull.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.Crop;
import com.naline.coopfull.domain.CropVariety;
import com.naline.coopfull.repository.CropVarietyRepository;
import com.naline.coopfull.service.CropVarietyService;
import com.naline.coopfull.service.dto.CropVarietyDTO;
import com.naline.coopfull.service.mapper.CropVarietyMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CropVarietyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CropVarietyResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGIN = "AAAAAAAAAA";
    private static final String UPDATED_ORIGIN = "BBBBBBBBBB";

    private static final Integer DEFAULT_MATURITY_DAYS = 1;
    private static final Integer UPDATED_MATURITY_DAYS = 2;
    private static final Integer SMALLER_MATURITY_DAYS = 1 - 1;

    private static final BigDecimal DEFAULT_YIELD_POTENTIAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_YIELD_POTENTIAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_YIELD_POTENTIAL = new BigDecimal(1 - 1);

    private static final String DEFAULT_DISEASE_RESISTANCE = "AAAAAAAAAA";
    private static final String UPDATED_DISEASE_RESISTANCE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/crop-varieties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CropVarietyRepository cropVarietyRepository;

    @Mock
    private CropVarietyRepository cropVarietyRepositoryMock;

    @Autowired
    private CropVarietyMapper cropVarietyMapper;

    @Mock
    private CropVarietyService cropVarietyServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCropVarietyMockMvc;

    private CropVariety cropVariety;

    private CropVariety insertedCropVariety;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropVariety createEntity() {
        return new CropVariety()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .origin(DEFAULT_ORIGIN)
            .maturityDays(DEFAULT_MATURITY_DAYS)
            .yieldPotential(DEFAULT_YIELD_POTENTIAL)
            .diseaseResistance(DEFAULT_DISEASE_RESISTANCE)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CropVariety createUpdatedEntity() {
        return new CropVariety()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .origin(UPDATED_ORIGIN)
            .maturityDays(UPDATED_MATURITY_DAYS)
            .yieldPotential(UPDATED_YIELD_POTENTIAL)
            .diseaseResistance(UPDATED_DISEASE_RESISTANCE)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        cropVariety = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCropVariety != null) {
            cropVarietyRepository.delete(insertedCropVariety);
            insertedCropVariety = null;
        }
    }

    @Test
    @Transactional
    void createCropVariety() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CropVariety
        CropVarietyDTO cropVarietyDTO = cropVarietyMapper.toDto(cropVariety);
        var returnedCropVarietyDTO = om.readValue(
            restCropVarietyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropVarietyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CropVarietyDTO.class
        );

        // Validate the CropVariety in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCropVariety = cropVarietyMapper.toEntity(returnedCropVarietyDTO);
        assertCropVarietyUpdatableFieldsEquals(returnedCropVariety, getPersistedCropVariety(returnedCropVariety));

        insertedCropVariety = returnedCropVariety;
    }

    @Test
    @Transactional
    void createCropVarietyWithExistingId() throws Exception {
        // Create the CropVariety with an existing ID
        cropVariety.setId(1L);
        CropVarietyDTO cropVarietyDTO = cropVarietyMapper.toDto(cropVariety);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCropVarietyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropVarietyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cropVariety.setCode(null);

        // Create the CropVariety, which fails.
        CropVarietyDTO cropVarietyDTO = cropVarietyMapper.toDto(cropVariety);

        restCropVarietyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropVarietyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cropVariety.setName(null);

        // Create the CropVariety, which fails.
        CropVarietyDTO cropVarietyDTO = cropVarietyMapper.toDto(cropVariety);

        restCropVarietyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropVarietyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cropVariety.setActive(null);

        // Create the CropVariety, which fails.
        CropVarietyDTO cropVarietyDTO = cropVarietyMapper.toDto(cropVariety);

        restCropVarietyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropVarietyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCropVarieties() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList
        restCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropVariety.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN)))
            .andExpect(jsonPath("$.[*].maturityDays").value(hasItem(DEFAULT_MATURITY_DAYS)))
            .andExpect(jsonPath("$.[*].yieldPotential").value(hasItem(sameNumber(DEFAULT_YIELD_POTENTIAL))))
            .andExpect(jsonPath("$.[*].diseaseResistance").value(hasItem(DEFAULT_DISEASE_RESISTANCE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCropVarietiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(cropVarietyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCropVarietyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cropVarietyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCropVarietiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cropVarietyServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCropVarietyMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cropVarietyRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCropVariety() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get the cropVariety
        restCropVarietyMockMvc
            .perform(get(ENTITY_API_URL_ID, cropVariety.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cropVariety.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN))
            .andExpect(jsonPath("$.maturityDays").value(DEFAULT_MATURITY_DAYS))
            .andExpect(jsonPath("$.yieldPotential").value(sameNumber(DEFAULT_YIELD_POTENTIAL)))
            .andExpect(jsonPath("$.diseaseResistance").value(DEFAULT_DISEASE_RESISTANCE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getCropVarietiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        Long id = cropVariety.getId();

        defaultCropVarietyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCropVarietyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCropVarietyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where code equals to
        defaultCropVarietyFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where code in
        defaultCropVarietyFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where code is not null
        defaultCropVarietyFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where code contains
        defaultCropVarietyFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where code does not contain
        defaultCropVarietyFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where name equals to
        defaultCropVarietyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where name in
        defaultCropVarietyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where name is not null
        defaultCropVarietyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where name contains
        defaultCropVarietyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where name does not contain
        defaultCropVarietyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByOriginIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where origin equals to
        defaultCropVarietyFiltering("origin.equals=" + DEFAULT_ORIGIN, "origin.equals=" + UPDATED_ORIGIN);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByOriginIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where origin in
        defaultCropVarietyFiltering("origin.in=" + DEFAULT_ORIGIN + "," + UPDATED_ORIGIN, "origin.in=" + UPDATED_ORIGIN);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByOriginIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where origin is not null
        defaultCropVarietyFiltering("origin.specified=true", "origin.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByOriginContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where origin contains
        defaultCropVarietyFiltering("origin.contains=" + DEFAULT_ORIGIN, "origin.contains=" + UPDATED_ORIGIN);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByOriginNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where origin does not contain
        defaultCropVarietyFiltering("origin.doesNotContain=" + UPDATED_ORIGIN, "origin.doesNotContain=" + DEFAULT_ORIGIN);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByMaturityDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where maturityDays equals to
        defaultCropVarietyFiltering("maturityDays.equals=" + DEFAULT_MATURITY_DAYS, "maturityDays.equals=" + UPDATED_MATURITY_DAYS);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByMaturityDaysIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where maturityDays in
        defaultCropVarietyFiltering(
            "maturityDays.in=" + DEFAULT_MATURITY_DAYS + "," + UPDATED_MATURITY_DAYS,
            "maturityDays.in=" + UPDATED_MATURITY_DAYS
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByMaturityDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where maturityDays is not null
        defaultCropVarietyFiltering("maturityDays.specified=true", "maturityDays.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByMaturityDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where maturityDays is greater than or equal to
        defaultCropVarietyFiltering(
            "maturityDays.greaterThanOrEqual=" + DEFAULT_MATURITY_DAYS,
            "maturityDays.greaterThanOrEqual=" + UPDATED_MATURITY_DAYS
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByMaturityDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where maturityDays is less than or equal to
        defaultCropVarietyFiltering(
            "maturityDays.lessThanOrEqual=" + DEFAULT_MATURITY_DAYS,
            "maturityDays.lessThanOrEqual=" + SMALLER_MATURITY_DAYS
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByMaturityDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where maturityDays is less than
        defaultCropVarietyFiltering("maturityDays.lessThan=" + UPDATED_MATURITY_DAYS, "maturityDays.lessThan=" + DEFAULT_MATURITY_DAYS);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByMaturityDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where maturityDays is greater than
        defaultCropVarietyFiltering(
            "maturityDays.greaterThan=" + SMALLER_MATURITY_DAYS,
            "maturityDays.greaterThan=" + DEFAULT_MATURITY_DAYS
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByYieldPotentialIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where yieldPotential equals to
        defaultCropVarietyFiltering("yieldPotential.equals=" + DEFAULT_YIELD_POTENTIAL, "yieldPotential.equals=" + UPDATED_YIELD_POTENTIAL);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByYieldPotentialIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where yieldPotential in
        defaultCropVarietyFiltering(
            "yieldPotential.in=" + DEFAULT_YIELD_POTENTIAL + "," + UPDATED_YIELD_POTENTIAL,
            "yieldPotential.in=" + UPDATED_YIELD_POTENTIAL
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByYieldPotentialIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where yieldPotential is not null
        defaultCropVarietyFiltering("yieldPotential.specified=true", "yieldPotential.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByYieldPotentialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where yieldPotential is greater than or equal to
        defaultCropVarietyFiltering(
            "yieldPotential.greaterThanOrEqual=" + DEFAULT_YIELD_POTENTIAL,
            "yieldPotential.greaterThanOrEqual=" + UPDATED_YIELD_POTENTIAL
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByYieldPotentialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where yieldPotential is less than or equal to
        defaultCropVarietyFiltering(
            "yieldPotential.lessThanOrEqual=" + DEFAULT_YIELD_POTENTIAL,
            "yieldPotential.lessThanOrEqual=" + SMALLER_YIELD_POTENTIAL
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByYieldPotentialIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where yieldPotential is less than
        defaultCropVarietyFiltering(
            "yieldPotential.lessThan=" + UPDATED_YIELD_POTENTIAL,
            "yieldPotential.lessThan=" + DEFAULT_YIELD_POTENTIAL
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByYieldPotentialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where yieldPotential is greater than
        defaultCropVarietyFiltering(
            "yieldPotential.greaterThan=" + SMALLER_YIELD_POTENTIAL,
            "yieldPotential.greaterThan=" + DEFAULT_YIELD_POTENTIAL
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByDiseaseResistanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where diseaseResistance equals to
        defaultCropVarietyFiltering(
            "diseaseResistance.equals=" + DEFAULT_DISEASE_RESISTANCE,
            "diseaseResistance.equals=" + UPDATED_DISEASE_RESISTANCE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByDiseaseResistanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where diseaseResistance in
        defaultCropVarietyFiltering(
            "diseaseResistance.in=" + DEFAULT_DISEASE_RESISTANCE + "," + UPDATED_DISEASE_RESISTANCE,
            "diseaseResistance.in=" + UPDATED_DISEASE_RESISTANCE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByDiseaseResistanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where diseaseResistance is not null
        defaultCropVarietyFiltering("diseaseResistance.specified=true", "diseaseResistance.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByDiseaseResistanceContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where diseaseResistance contains
        defaultCropVarietyFiltering(
            "diseaseResistance.contains=" + DEFAULT_DISEASE_RESISTANCE,
            "diseaseResistance.contains=" + UPDATED_DISEASE_RESISTANCE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByDiseaseResistanceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where diseaseResistance does not contain
        defaultCropVarietyFiltering(
            "diseaseResistance.doesNotContain=" + UPDATED_DISEASE_RESISTANCE,
            "diseaseResistance.doesNotContain=" + DEFAULT_DISEASE_RESISTANCE
        );
    }

    @Test
    @Transactional
    void getAllCropVarietiesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where active equals to
        defaultCropVarietyFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where active in
        defaultCropVarietyFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCropVarietiesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        // Get all the cropVarietyList where active is not null
        defaultCropVarietyFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllCropVarietiesByCropIsEqualToSomething() throws Exception {
        Crop crop;
        if (TestUtil.findAll(em, Crop.class).isEmpty()) {
            cropVarietyRepository.saveAndFlush(cropVariety);
            crop = CropResourceIT.createEntity();
        } else {
            crop = TestUtil.findAll(em, Crop.class).get(0);
        }
        em.persist(crop);
        em.flush();
        cropVariety.setCrop(crop);
        cropVarietyRepository.saveAndFlush(cropVariety);
        Long cropId = crop.getId();
        // Get all the cropVarietyList where crop equals to cropId
        defaultCropVarietyShouldBeFound("cropId.equals=" + cropId);

        // Get all the cropVarietyList where crop equals to (cropId + 1)
        defaultCropVarietyShouldNotBeFound("cropId.equals=" + (cropId + 1));
    }

    private void defaultCropVarietyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCropVarietyShouldBeFound(shouldBeFound);
        defaultCropVarietyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCropVarietyShouldBeFound(String filter) throws Exception {
        restCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cropVariety.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN)))
            .andExpect(jsonPath("$.[*].maturityDays").value(hasItem(DEFAULT_MATURITY_DAYS)))
            .andExpect(jsonPath("$.[*].yieldPotential").value(hasItem(sameNumber(DEFAULT_YIELD_POTENTIAL))))
            .andExpect(jsonPath("$.[*].diseaseResistance").value(hasItem(DEFAULT_DISEASE_RESISTANCE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCropVarietyShouldNotBeFound(String filter) throws Exception {
        restCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCropVarietyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCropVariety() throws Exception {
        // Get the cropVariety
        restCropVarietyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCropVariety() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropVariety
        CropVariety updatedCropVariety = cropVarietyRepository.findById(cropVariety.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCropVariety are not directly saved in db
        em.detach(updatedCropVariety);
        updatedCropVariety
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .origin(UPDATED_ORIGIN)
            .maturityDays(UPDATED_MATURITY_DAYS)
            .yieldPotential(UPDATED_YIELD_POTENTIAL)
            .diseaseResistance(UPDATED_DISEASE_RESISTANCE)
            .active(UPDATED_ACTIVE);
        CropVarietyDTO cropVarietyDTO = cropVarietyMapper.toDto(updatedCropVariety);

        restCropVarietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropVarietyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropVarietyDTO))
            )
            .andExpect(status().isOk());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCropVarietyToMatchAllProperties(updatedCropVariety);
    }

    @Test
    @Transactional
    void putNonExistingCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVariety.setId(longCount.incrementAndGet());

        // Create the CropVariety
        CropVarietyDTO cropVarietyDTO = cropVarietyMapper.toDto(cropVariety);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropVarietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cropVarietyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropVarietyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVariety.setId(longCount.incrementAndGet());

        // Create the CropVariety
        CropVarietyDTO cropVarietyDTO = cropVarietyMapper.toDto(cropVariety);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropVarietyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVariety.setId(longCount.incrementAndGet());

        // Create the CropVariety
        CropVarietyDTO cropVarietyDTO = cropVarietyMapper.toDto(cropVariety);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropVarietyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCropVarietyWithPatch() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropVariety using partial update
        CropVariety partialUpdatedCropVariety = new CropVariety();
        partialUpdatedCropVariety.setId(cropVariety.getId());

        partialUpdatedCropVariety
            .code(UPDATED_CODE)
            .description(UPDATED_DESCRIPTION)
            .origin(UPDATED_ORIGIN)
            .maturityDays(UPDATED_MATURITY_DAYS)
            .yieldPotential(UPDATED_YIELD_POTENTIAL);

        restCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropVariety.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropVariety))
            )
            .andExpect(status().isOk());

        // Validate the CropVariety in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropVarietyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCropVariety, cropVariety),
            getPersistedCropVariety(cropVariety)
        );
    }

    @Test
    @Transactional
    void fullUpdateCropVarietyWithPatch() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cropVariety using partial update
        CropVariety partialUpdatedCropVariety = new CropVariety();
        partialUpdatedCropVariety.setId(cropVariety.getId());

        partialUpdatedCropVariety
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .origin(UPDATED_ORIGIN)
            .maturityDays(UPDATED_MATURITY_DAYS)
            .yieldPotential(UPDATED_YIELD_POTENTIAL)
            .diseaseResistance(UPDATED_DISEASE_RESISTANCE)
            .active(UPDATED_ACTIVE);

        restCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCropVariety.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCropVariety))
            )
            .andExpect(status().isOk());

        // Validate the CropVariety in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropVarietyUpdatableFieldsEquals(partialUpdatedCropVariety, getPersistedCropVariety(partialUpdatedCropVariety));
    }

    @Test
    @Transactional
    void patchNonExistingCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVariety.setId(longCount.incrementAndGet());

        // Create the CropVariety
        CropVarietyDTO cropVarietyDTO = cropVarietyMapper.toDto(cropVariety);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cropVarietyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropVarietyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVariety.setId(longCount.incrementAndGet());

        // Create the CropVariety
        CropVarietyDTO cropVarietyDTO = cropVarietyMapper.toDto(cropVariety);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropVarietyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCropVariety() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cropVariety.setId(longCount.incrementAndGet());

        // Create the CropVariety
        CropVarietyDTO cropVarietyDTO = cropVarietyMapper.toDto(cropVariety);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropVarietyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cropVarietyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CropVariety in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCropVariety() throws Exception {
        // Initialize the database
        insertedCropVariety = cropVarietyRepository.saveAndFlush(cropVariety);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cropVariety
        restCropVarietyMockMvc
            .perform(delete(ENTITY_API_URL_ID, cropVariety.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cropVarietyRepository.count();
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

    protected CropVariety getPersistedCropVariety(CropVariety cropVariety) {
        return cropVarietyRepository.findById(cropVariety.getId()).orElseThrow();
    }

    protected void assertPersistedCropVarietyToMatchAllProperties(CropVariety expectedCropVariety) {
        assertCropVarietyAllPropertiesEquals(expectedCropVariety, getPersistedCropVariety(expectedCropVariety));
    }

    protected void assertPersistedCropVarietyToMatchUpdatableProperties(CropVariety expectedCropVariety) {
        assertCropVarietyAllUpdatablePropertiesEquals(expectedCropVariety, getPersistedCropVariety(expectedCropVariety));
    }
}
