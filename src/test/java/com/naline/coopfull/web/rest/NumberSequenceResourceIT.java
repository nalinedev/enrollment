package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.NumberSequenceAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.domain.NumberSequence;
import com.naline.coopfull.domain.enumeration.SequenceType;
import com.naline.coopfull.repository.NumberSequenceRepository;
import com.naline.coopfull.service.NumberSequenceService;
import com.naline.coopfull.service.dto.NumberSequenceDTO;
import com.naline.coopfull.service.mapper.NumberSequenceMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link NumberSequenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NumberSequenceResourceIT {

    private static final SequenceType DEFAULT_SEQUENCE_TYPE = SequenceType.MEMBER;
    private static final SequenceType UPDATED_SEQUENCE_TYPE = SequenceType.APPLICATION;

    private static final String DEFAULT_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_PREFIX = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;
    private static final Integer SMALLER_YEAR = 1 - 1;

    private static final Long DEFAULT_CURRENT_VALUE = 1L;
    private static final Long UPDATED_CURRENT_VALUE = 2L;
    private static final Long SMALLER_CURRENT_VALUE = 1L - 1L;

    private static final Integer DEFAULT_PADDING = 1;
    private static final Integer UPDATED_PADDING = 2;
    private static final Integer SMALLER_PADDING = 1 - 1;

    private static final String ENTITY_API_URL = "/api/number-sequences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NumberSequenceRepository numberSequenceRepository;

    @Mock
    private NumberSequenceRepository numberSequenceRepositoryMock;

    @Autowired
    private NumberSequenceMapper numberSequenceMapper;

    @Mock
    private NumberSequenceService numberSequenceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNumberSequenceMockMvc;

    private NumberSequence numberSequence;

    private NumberSequence insertedNumberSequence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NumberSequence createEntity() {
        return new NumberSequence()
            .sequenceType(DEFAULT_SEQUENCE_TYPE)
            .prefix(DEFAULT_PREFIX)
            .year(DEFAULT_YEAR)
            .currentValue(DEFAULT_CURRENT_VALUE)
            .padding(DEFAULT_PADDING);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NumberSequence createUpdatedEntity() {
        return new NumberSequence()
            .sequenceType(UPDATED_SEQUENCE_TYPE)
            .prefix(UPDATED_PREFIX)
            .year(UPDATED_YEAR)
            .currentValue(UPDATED_CURRENT_VALUE)
            .padding(UPDATED_PADDING);
    }

    @BeforeEach
    void initTest() {
        numberSequence = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNumberSequence != null) {
            numberSequenceRepository.delete(insertedNumberSequence);
            insertedNumberSequence = null;
        }
    }

    @Test
    @Transactional
    void createNumberSequence() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NumberSequence
        NumberSequenceDTO numberSequenceDTO = numberSequenceMapper.toDto(numberSequence);
        var returnedNumberSequenceDTO = om.readValue(
            restNumberSequenceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(numberSequenceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NumberSequenceDTO.class
        );

        // Validate the NumberSequence in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNumberSequence = numberSequenceMapper.toEntity(returnedNumberSequenceDTO);
        assertNumberSequenceUpdatableFieldsEquals(returnedNumberSequence, getPersistedNumberSequence(returnedNumberSequence));

        insertedNumberSequence = returnedNumberSequence;
    }

    @Test
    @Transactional
    void createNumberSequenceWithExistingId() throws Exception {
        // Create the NumberSequence with an existing ID
        numberSequence.setId(1L);
        NumberSequenceDTO numberSequenceDTO = numberSequenceMapper.toDto(numberSequence);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNumberSequenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(numberSequenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NumberSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSequenceTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        numberSequence.setSequenceType(null);

        // Create the NumberSequence, which fails.
        NumberSequenceDTO numberSequenceDTO = numberSequenceMapper.toDto(numberSequence);

        restNumberSequenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(numberSequenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrentValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        numberSequence.setCurrentValue(null);

        // Create the NumberSequence, which fails.
        NumberSequenceDTO numberSequenceDTO = numberSequenceMapper.toDto(numberSequence);

        restNumberSequenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(numberSequenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPaddingIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        numberSequence.setPadding(null);

        // Create the NumberSequence, which fails.
        NumberSequenceDTO numberSequenceDTO = numberSequenceMapper.toDto(numberSequence);

        restNumberSequenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(numberSequenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNumberSequences() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList
        restNumberSequenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(numberSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceType").value(hasItem(DEFAULT_SEQUENCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].currentValue").value(hasItem(DEFAULT_CURRENT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].padding").value(hasItem(DEFAULT_PADDING)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNumberSequencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(numberSequenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNumberSequenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(numberSequenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNumberSequencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(numberSequenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNumberSequenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(numberSequenceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNumberSequence() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get the numberSequence
        restNumberSequenceMockMvc
            .perform(get(ENTITY_API_URL_ID, numberSequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(numberSequence.getId().intValue()))
            .andExpect(jsonPath("$.sequenceType").value(DEFAULT_SEQUENCE_TYPE.toString()))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.currentValue").value(DEFAULT_CURRENT_VALUE.intValue()))
            .andExpect(jsonPath("$.padding").value(DEFAULT_PADDING));
    }

    @Test
    @Transactional
    void getNumberSequencesByIdFiltering() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        Long id = numberSequence.getId();

        defaultNumberSequenceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNumberSequenceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNumberSequenceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNumberSequencesBySequenceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where sequenceType equals to
        defaultNumberSequenceFiltering("sequenceType.equals=" + DEFAULT_SEQUENCE_TYPE, "sequenceType.equals=" + UPDATED_SEQUENCE_TYPE);
    }

    @Test
    @Transactional
    void getAllNumberSequencesBySequenceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where sequenceType in
        defaultNumberSequenceFiltering(
            "sequenceType.in=" + DEFAULT_SEQUENCE_TYPE + "," + UPDATED_SEQUENCE_TYPE,
            "sequenceType.in=" + UPDATED_SEQUENCE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNumberSequencesBySequenceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where sequenceType is not null
        defaultNumberSequenceFiltering("sequenceType.specified=true", "sequenceType.specified=false");
    }

    @Test
    @Transactional
    void getAllNumberSequencesByPrefixIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where prefix equals to
        defaultNumberSequenceFiltering("prefix.equals=" + DEFAULT_PREFIX, "prefix.equals=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByPrefixIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where prefix in
        defaultNumberSequenceFiltering("prefix.in=" + DEFAULT_PREFIX + "," + UPDATED_PREFIX, "prefix.in=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByPrefixIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where prefix is not null
        defaultNumberSequenceFiltering("prefix.specified=true", "prefix.specified=false");
    }

    @Test
    @Transactional
    void getAllNumberSequencesByPrefixContainsSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where prefix contains
        defaultNumberSequenceFiltering("prefix.contains=" + DEFAULT_PREFIX, "prefix.contains=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByPrefixNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where prefix does not contain
        defaultNumberSequenceFiltering("prefix.doesNotContain=" + UPDATED_PREFIX, "prefix.doesNotContain=" + DEFAULT_PREFIX);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where year equals to
        defaultNumberSequenceFiltering("year.equals=" + DEFAULT_YEAR, "year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where year in
        defaultNumberSequenceFiltering("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR, "year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where year is not null
        defaultNumberSequenceFiltering("year.specified=true", "year.specified=false");
    }

    @Test
    @Transactional
    void getAllNumberSequencesByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where year is greater than or equal to
        defaultNumberSequenceFiltering("year.greaterThanOrEqual=" + DEFAULT_YEAR, "year.greaterThanOrEqual=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where year is less than or equal to
        defaultNumberSequenceFiltering("year.lessThanOrEqual=" + DEFAULT_YEAR, "year.lessThanOrEqual=" + SMALLER_YEAR);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where year is less than
        defaultNumberSequenceFiltering("year.lessThan=" + UPDATED_YEAR, "year.lessThan=" + DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where year is greater than
        defaultNumberSequenceFiltering("year.greaterThan=" + SMALLER_YEAR, "year.greaterThan=" + DEFAULT_YEAR);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByCurrentValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where currentValue equals to
        defaultNumberSequenceFiltering("currentValue.equals=" + DEFAULT_CURRENT_VALUE, "currentValue.equals=" + UPDATED_CURRENT_VALUE);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByCurrentValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where currentValue in
        defaultNumberSequenceFiltering(
            "currentValue.in=" + DEFAULT_CURRENT_VALUE + "," + UPDATED_CURRENT_VALUE,
            "currentValue.in=" + UPDATED_CURRENT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllNumberSequencesByCurrentValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where currentValue is not null
        defaultNumberSequenceFiltering("currentValue.specified=true", "currentValue.specified=false");
    }

    @Test
    @Transactional
    void getAllNumberSequencesByCurrentValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where currentValue is greater than or equal to
        defaultNumberSequenceFiltering(
            "currentValue.greaterThanOrEqual=" + DEFAULT_CURRENT_VALUE,
            "currentValue.greaterThanOrEqual=" + UPDATED_CURRENT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllNumberSequencesByCurrentValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where currentValue is less than or equal to
        defaultNumberSequenceFiltering(
            "currentValue.lessThanOrEqual=" + DEFAULT_CURRENT_VALUE,
            "currentValue.lessThanOrEqual=" + SMALLER_CURRENT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllNumberSequencesByCurrentValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where currentValue is less than
        defaultNumberSequenceFiltering("currentValue.lessThan=" + UPDATED_CURRENT_VALUE, "currentValue.lessThan=" + DEFAULT_CURRENT_VALUE);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByCurrentValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where currentValue is greater than
        defaultNumberSequenceFiltering(
            "currentValue.greaterThan=" + SMALLER_CURRENT_VALUE,
            "currentValue.greaterThan=" + DEFAULT_CURRENT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllNumberSequencesByPaddingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where padding equals to
        defaultNumberSequenceFiltering("padding.equals=" + DEFAULT_PADDING, "padding.equals=" + UPDATED_PADDING);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByPaddingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where padding in
        defaultNumberSequenceFiltering("padding.in=" + DEFAULT_PADDING + "," + UPDATED_PADDING, "padding.in=" + UPDATED_PADDING);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByPaddingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where padding is not null
        defaultNumberSequenceFiltering("padding.specified=true", "padding.specified=false");
    }

    @Test
    @Transactional
    void getAllNumberSequencesByPaddingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where padding is greater than or equal to
        defaultNumberSequenceFiltering("padding.greaterThanOrEqual=" + DEFAULT_PADDING, "padding.greaterThanOrEqual=" + UPDATED_PADDING);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByPaddingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where padding is less than or equal to
        defaultNumberSequenceFiltering("padding.lessThanOrEqual=" + DEFAULT_PADDING, "padding.lessThanOrEqual=" + SMALLER_PADDING);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByPaddingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where padding is less than
        defaultNumberSequenceFiltering("padding.lessThan=" + UPDATED_PADDING, "padding.lessThan=" + DEFAULT_PADDING);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByPaddingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        // Get all the numberSequenceList where padding is greater than
        defaultNumberSequenceFiltering("padding.greaterThan=" + SMALLER_PADDING, "padding.greaterThan=" + DEFAULT_PADDING);
    }

    @Test
    @Transactional
    void getAllNumberSequencesByCooperativeIsEqualToSomething() throws Exception {
        Cooperative cooperative;
        if (TestUtil.findAll(em, Cooperative.class).isEmpty()) {
            numberSequenceRepository.saveAndFlush(numberSequence);
            cooperative = CooperativeResourceIT.createEntity();
        } else {
            cooperative = TestUtil.findAll(em, Cooperative.class).get(0);
        }
        em.persist(cooperative);
        em.flush();
        numberSequence.setCooperative(cooperative);
        numberSequenceRepository.saveAndFlush(numberSequence);
        Long cooperativeId = cooperative.getId();
        // Get all the numberSequenceList where cooperative equals to cooperativeId
        defaultNumberSequenceShouldBeFound("cooperativeId.equals=" + cooperativeId);

        // Get all the numberSequenceList where cooperative equals to (cooperativeId + 1)
        defaultNumberSequenceShouldNotBeFound("cooperativeId.equals=" + (cooperativeId + 1));
    }

    private void defaultNumberSequenceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNumberSequenceShouldBeFound(shouldBeFound);
        defaultNumberSequenceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNumberSequenceShouldBeFound(String filter) throws Exception {
        restNumberSequenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(numberSequence.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequenceType").value(hasItem(DEFAULT_SEQUENCE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].currentValue").value(hasItem(DEFAULT_CURRENT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].padding").value(hasItem(DEFAULT_PADDING)));

        // Check, that the count call also returns 1
        restNumberSequenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNumberSequenceShouldNotBeFound(String filter) throws Exception {
        restNumberSequenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNumberSequenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNumberSequence() throws Exception {
        // Get the numberSequence
        restNumberSequenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNumberSequence() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the numberSequence
        NumberSequence updatedNumberSequence = numberSequenceRepository.findById(numberSequence.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNumberSequence are not directly saved in db
        em.detach(updatedNumberSequence);
        updatedNumberSequence
            .sequenceType(UPDATED_SEQUENCE_TYPE)
            .prefix(UPDATED_PREFIX)
            .year(UPDATED_YEAR)
            .currentValue(UPDATED_CURRENT_VALUE)
            .padding(UPDATED_PADDING);
        NumberSequenceDTO numberSequenceDTO = numberSequenceMapper.toDto(updatedNumberSequence);

        restNumberSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, numberSequenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(numberSequenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the NumberSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNumberSequenceToMatchAllProperties(updatedNumberSequence);
    }

    @Test
    @Transactional
    void putNonExistingNumberSequence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        numberSequence.setId(longCount.incrementAndGet());

        // Create the NumberSequence
        NumberSequenceDTO numberSequenceDTO = numberSequenceMapper.toDto(numberSequence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNumberSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, numberSequenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(numberSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NumberSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNumberSequence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        numberSequence.setId(longCount.incrementAndGet());

        // Create the NumberSequence
        NumberSequenceDTO numberSequenceDTO = numberSequenceMapper.toDto(numberSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNumberSequenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(numberSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NumberSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNumberSequence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        numberSequence.setId(longCount.incrementAndGet());

        // Create the NumberSequence
        NumberSequenceDTO numberSequenceDTO = numberSequenceMapper.toDto(numberSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNumberSequenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(numberSequenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NumberSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNumberSequenceWithPatch() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the numberSequence using partial update
        NumberSequence partialUpdatedNumberSequence = new NumberSequence();
        partialUpdatedNumberSequence.setId(numberSequence.getId());

        partialUpdatedNumberSequence.prefix(UPDATED_PREFIX).year(UPDATED_YEAR).currentValue(UPDATED_CURRENT_VALUE);

        restNumberSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNumberSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNumberSequence))
            )
            .andExpect(status().isOk());

        // Validate the NumberSequence in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNumberSequenceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNumberSequence, numberSequence),
            getPersistedNumberSequence(numberSequence)
        );
    }

    @Test
    @Transactional
    void fullUpdateNumberSequenceWithPatch() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the numberSequence using partial update
        NumberSequence partialUpdatedNumberSequence = new NumberSequence();
        partialUpdatedNumberSequence.setId(numberSequence.getId());

        partialUpdatedNumberSequence
            .sequenceType(UPDATED_SEQUENCE_TYPE)
            .prefix(UPDATED_PREFIX)
            .year(UPDATED_YEAR)
            .currentValue(UPDATED_CURRENT_VALUE)
            .padding(UPDATED_PADDING);

        restNumberSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNumberSequence.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNumberSequence))
            )
            .andExpect(status().isOk());

        // Validate the NumberSequence in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNumberSequenceUpdatableFieldsEquals(partialUpdatedNumberSequence, getPersistedNumberSequence(partialUpdatedNumberSequence));
    }

    @Test
    @Transactional
    void patchNonExistingNumberSequence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        numberSequence.setId(longCount.incrementAndGet());

        // Create the NumberSequence
        NumberSequenceDTO numberSequenceDTO = numberSequenceMapper.toDto(numberSequence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNumberSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, numberSequenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(numberSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NumberSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNumberSequence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        numberSequence.setId(longCount.incrementAndGet());

        // Create the NumberSequence
        NumberSequenceDTO numberSequenceDTO = numberSequenceMapper.toDto(numberSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNumberSequenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(numberSequenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NumberSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNumberSequence() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        numberSequence.setId(longCount.incrementAndGet());

        // Create the NumberSequence
        NumberSequenceDTO numberSequenceDTO = numberSequenceMapper.toDto(numberSequence);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNumberSequenceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(numberSequenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NumberSequence in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNumberSequence() throws Exception {
        // Initialize the database
        insertedNumberSequence = numberSequenceRepository.saveAndFlush(numberSequence);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the numberSequence
        restNumberSequenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, numberSequence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return numberSequenceRepository.count();
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

    protected NumberSequence getPersistedNumberSequence(NumberSequence numberSequence) {
        return numberSequenceRepository.findById(numberSequence.getId()).orElseThrow();
    }

    protected void assertPersistedNumberSequenceToMatchAllProperties(NumberSequence expectedNumberSequence) {
        assertNumberSequenceAllPropertiesEquals(expectedNumberSequence, getPersistedNumberSequence(expectedNumberSequence));
    }

    protected void assertPersistedNumberSequenceToMatchUpdatableProperties(NumberSequence expectedNumberSequence) {
        assertNumberSequenceAllUpdatablePropertiesEquals(expectedNumberSequence, getPersistedNumberSequence(expectedNumberSequence));
    }
}
