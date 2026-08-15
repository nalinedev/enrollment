package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.LocationAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static com.naline.coopfull.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.domain.Location;
import com.naline.coopfull.domain.enumeration.LocationType;
import com.naline.coopfull.repository.LocationRepository;
import com.naline.coopfull.service.LocationService;
import com.naline.coopfull.service.dto.LocationDTO;
import com.naline.coopfull.service.mapper.LocationMapper;
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
 * Integration tests for the {@link LocationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LocationResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocationType DEFAULT_TYPE = LocationType.COUNTRY;
    private static final LocationType UPDATED_TYPE = LocationType.REGION;

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LATITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LONGITUDE = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LocationRepository locationRepository;

    @Mock
    private LocationRepository locationRepositoryMock;

    @Autowired
    private LocationMapper locationMapper;

    @Mock
    private LocationService locationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationMockMvc;

    private Location location;

    private Location insertedLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createEntity() {
        return new Location()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .postalCode(DEFAULT_POSTAL_CODE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Location createUpdatedEntity() {
        return new Location()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .postalCode(UPDATED_POSTAL_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        location = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLocation != null) {
            locationRepository.delete(insertedLocation);
            insertedLocation = null;
        }
    }

    @Test
    @Transactional
    void createLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);
        var returnedLocationDTO = om.readValue(
            restLocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LocationDTO.class
        );

        // Validate the Location in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLocation = locationMapper.toEntity(returnedLocationDTO);
        assertLocationUpdatableFieldsEquals(returnedLocation, getPersistedLocation(returnedLocation));

        insertedLocation = returnedLocation;
    }

    @Test
    @Transactional
    void createLocationWithExistingId() throws Exception {
        // Create the Location with an existing ID
        location.setId(1L);
        LocationDTO locationDTO = locationMapper.toDto(location);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        location.setCode(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        location.setName(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        location.setType(null);

        // Create the Location, which fails.
        LocationDTO locationDTO = locationMapper.toDto(location);

        restLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocations() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(locationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(locationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(locationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(locationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLocation() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get the location
        restLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, location.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(location.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.latitude").value(sameNumber(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.longitude").value(sameNumber(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getLocationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        Long id = location.getId();

        defaultLocationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLocationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLocationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLocationsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where code equals to
        defaultLocationFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where code in
        defaultLocationFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where code is not null
        defaultLocationFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where code contains
        defaultLocationFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where code does not contain
        defaultLocationFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where name equals to
        defaultLocationFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLocationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where name in
        defaultLocationFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLocationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where name is not null
        defaultLocationFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where name contains
        defaultLocationFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLocationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where name does not contain
        defaultLocationFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllLocationsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where type equals to
        defaultLocationFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllLocationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where type in
        defaultLocationFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllLocationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where type is not null
        defaultLocationFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine1 equals to
        defaultLocationFiltering("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1, "addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllLocationsByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine1 in
        defaultLocationFiltering(
            "addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1,
            "addressLine1.in=" + UPDATED_ADDRESS_LINE_1
        );
    }

    @Test
    @Transactional
    void getAllLocationsByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine1 is not null
        defaultLocationFiltering("addressLine1.specified=true", "addressLine1.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine1 contains
        defaultLocationFiltering("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1, "addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllLocationsByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine1 does not contain
        defaultLocationFiltering(
            "addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1,
            "addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1
        );
    }

    @Test
    @Transactional
    void getAllLocationsByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine2 equals to
        defaultLocationFiltering("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2, "addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllLocationsByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine2 in
        defaultLocationFiltering(
            "addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2,
            "addressLine2.in=" + UPDATED_ADDRESS_LINE_2
        );
    }

    @Test
    @Transactional
    void getAllLocationsByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine2 is not null
        defaultLocationFiltering("addressLine2.specified=true", "addressLine2.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine2 contains
        defaultLocationFiltering("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2, "addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllLocationsByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where addressLine2 does not contain
        defaultLocationFiltering(
            "addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2,
            "addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2
        );
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode equals to
        defaultLocationFiltering("postalCode.equals=" + DEFAULT_POSTAL_CODE, "postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode in
        defaultLocationFiltering(
            "postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE,
            "postalCode.in=" + UPDATED_POSTAL_CODE
        );
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode is not null
        defaultLocationFiltering("postalCode.specified=true", "postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode contains
        defaultLocationFiltering("postalCode.contains=" + DEFAULT_POSTAL_CODE, "postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where postalCode does not contain
        defaultLocationFiltering("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE, "postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude equals to
        defaultLocationFiltering("latitude.equals=" + DEFAULT_LATITUDE, "latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude in
        defaultLocationFiltering("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE, "latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude is not null
        defaultLocationFiltering("latitude.specified=true", "latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude is greater than or equal to
        defaultLocationFiltering("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE, "latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude is less than or equal to
        defaultLocationFiltering("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE, "latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude is less than
        defaultLocationFiltering("latitude.lessThan=" + UPDATED_LATITUDE, "latitude.lessThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where latitude is greater than
        defaultLocationFiltering("latitude.greaterThan=" + SMALLER_LATITUDE, "latitude.greaterThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude equals to
        defaultLocationFiltering("longitude.equals=" + DEFAULT_LONGITUDE, "longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude in
        defaultLocationFiltering("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE, "longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude is not null
        defaultLocationFiltering("longitude.specified=true", "longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude is greater than or equal to
        defaultLocationFiltering("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude is less than or equal to
        defaultLocationFiltering("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude is less than
        defaultLocationFiltering("longitude.lessThan=" + UPDATED_LONGITUDE, "longitude.lessThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where longitude is greater than
        defaultLocationFiltering("longitude.greaterThan=" + SMALLER_LONGITUDE, "longitude.greaterThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllLocationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where description equals to
        defaultLocationFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLocationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where description in
        defaultLocationFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllLocationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where description is not null
        defaultLocationFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllLocationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where description contains
        defaultLocationFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLocationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        // Get all the locationList where description does not contain
        defaultLocationFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllLocationsByParentIsEqualToSomething() throws Exception {
        Location parent;
        if (TestUtil.findAll(em, Location.class).isEmpty()) {
            locationRepository.saveAndFlush(location);
            parent = LocationResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, Location.class).get(0);
        }
        em.persist(parent);
        em.flush();
        location.setParent(parent);
        locationRepository.saveAndFlush(location);
        Long parentId = parent.getId();
        // Get all the locationList where parent equals to parentId
        defaultLocationShouldBeFound("parentId.equals=" + parentId);

        // Get all the locationList where parent equals to (parentId + 1)
        defaultLocationShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    private void defaultLocationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLocationShouldBeFound(shouldBeFound);
        defaultLocationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocationShouldBeFound(String filter) throws Exception {
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(location.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocationShouldNotBeFound(String filter) throws Exception {
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLocation() throws Exception {
        // Get the location
        restLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocation() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the location
        Location updatedLocation = locationRepository.findById(location.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLocation are not directly saved in db
        em.detach(updatedLocation);
        updatedLocation
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .postalCode(UPDATED_POSTAL_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .description(UPDATED_DESCRIPTION);
        LocationDTO locationDTO = locationMapper.toDto(updatedLocation);

        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(locationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLocationToMatchAllProperties(updatedLocation);
    }

    @Test
    @Transactional
    void putNonExistingLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(locationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation.code(UPDATED_CODE).latitude(UPDATED_LATITUDE).description(UPDATED_DESCRIPTION);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocationUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLocation, location), getPersistedLocation(location));
    }

    @Test
    @Transactional
    void fullUpdateLocationWithPatch() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the location using partial update
        Location partialUpdatedLocation = new Location();
        partialUpdatedLocation.setId(location.getId());

        partialUpdatedLocation
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .postalCode(UPDATED_POSTAL_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .description(UPDATED_DESCRIPTION);

        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocation))
            )
            .andExpect(status().isOk());

        // Validate the Location in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocationUpdatableFieldsEquals(partialUpdatedLocation, getPersistedLocation(partialUpdatedLocation));
    }

    @Test
    @Transactional
    void patchNonExistingLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(locationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        location.setId(longCount.incrementAndGet());

        // Create the Location
        LocationDTO locationDTO = locationMapper.toDto(location);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(locationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Location in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocation() throws Exception {
        // Initialize the database
        insertedLocation = locationRepository.saveAndFlush(location);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the location
        restLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, location.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return locationRepository.count();
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

    protected Location getPersistedLocation(Location location) {
        return locationRepository.findById(location.getId()).orElseThrow();
    }

    protected void assertPersistedLocationToMatchAllProperties(Location expectedLocation) {
        assertLocationAllPropertiesEquals(expectedLocation, getPersistedLocation(expectedLocation));
    }

    protected void assertPersistedLocationToMatchUpdatableProperties(Location expectedLocation) {
        assertLocationAllUpdatablePropertiesEquals(expectedLocation, getPersistedLocation(expectedLocation));
    }
}
