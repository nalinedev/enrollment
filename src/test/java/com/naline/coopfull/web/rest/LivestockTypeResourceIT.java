package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.LivestockTypeAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.LivestockType;
import com.naline.coopfull.repository.LivestockTypeRepository;
import com.naline.coopfull.service.dto.LivestockTypeDTO;
import com.naline.coopfull.service.mapper.LivestockTypeMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link LivestockTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LivestockTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SCIENTIFIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SCIENTIFIC_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/livestock-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LivestockTypeRepository livestockTypeRepository;

    @Autowired
    private LivestockTypeMapper livestockTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLivestockTypeMockMvc;

    private LivestockType livestockType;

    private LivestockType insertedLivestockType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LivestockType createEntity() {
        return new LivestockType()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .scientificName(DEFAULT_SCIENTIFIC_NAME)
            .category(DEFAULT_CATEGORY)
            .description(DEFAULT_DESCRIPTION)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LivestockType createUpdatedEntity() {
        return new LivestockType()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        livestockType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLivestockType != null) {
            livestockTypeRepository.delete(insertedLivestockType);
            insertedLivestockType = null;
        }
    }

    @Test
    @Transactional
    void createLivestockType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LivestockType
        LivestockTypeDTO livestockTypeDTO = livestockTypeMapper.toDto(livestockType);
        var returnedLivestockTypeDTO = om.readValue(
            restLivestockTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LivestockTypeDTO.class
        );

        // Validate the LivestockType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLivestockType = livestockTypeMapper.toEntity(returnedLivestockTypeDTO);
        assertLivestockTypeUpdatableFieldsEquals(returnedLivestockType, getPersistedLivestockType(returnedLivestockType));

        insertedLivestockType = returnedLivestockType;
    }

    @Test
    @Transactional
    void createLivestockTypeWithExistingId() throws Exception {
        // Create the LivestockType with an existing ID
        livestockType.setId(1L);
        LivestockTypeDTO livestockTypeDTO = livestockTypeMapper.toDto(livestockType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivestockTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LivestockType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        livestockType.setCode(null);

        // Create the LivestockType, which fails.
        LivestockTypeDTO livestockTypeDTO = livestockTypeMapper.toDto(livestockType);

        restLivestockTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        livestockType.setName(null);

        // Create the LivestockType, which fails.
        LivestockTypeDTO livestockTypeDTO = livestockTypeMapper.toDto(livestockType);

        restLivestockTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        livestockType.setActive(null);

        // Create the LivestockType, which fails.
        LivestockTypeDTO livestockTypeDTO = livestockTypeMapper.toDto(livestockType);

        restLivestockTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLivestockTypes() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList
        restLivestockTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livestockType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].scientificName").value(hasItem(DEFAULT_SCIENTIFIC_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getLivestockType() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get the livestockType
        restLivestockTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, livestockType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(livestockType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.scientificName").value(DEFAULT_SCIENTIFIC_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getLivestockTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        Long id = livestockType.getId();

        defaultLivestockTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLivestockTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLivestockTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where code equals to
        defaultLivestockTypeFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where code in
        defaultLivestockTypeFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where code is not null
        defaultLivestockTypeFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where code contains
        defaultLivestockTypeFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where code does not contain
        defaultLivestockTypeFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where name equals to
        defaultLivestockTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where name in
        defaultLivestockTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where name is not null
        defaultLivestockTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where name contains
        defaultLivestockTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where name does not contain
        defaultLivestockTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByScientificNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where scientificName equals to
        defaultLivestockTypeFiltering(
            "scientificName.equals=" + DEFAULT_SCIENTIFIC_NAME,
            "scientificName.equals=" + UPDATED_SCIENTIFIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllLivestockTypesByScientificNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where scientificName in
        defaultLivestockTypeFiltering(
            "scientificName.in=" + DEFAULT_SCIENTIFIC_NAME + "," + UPDATED_SCIENTIFIC_NAME,
            "scientificName.in=" + UPDATED_SCIENTIFIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllLivestockTypesByScientificNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where scientificName is not null
        defaultLivestockTypeFiltering("scientificName.specified=true", "scientificName.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockTypesByScientificNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where scientificName contains
        defaultLivestockTypeFiltering(
            "scientificName.contains=" + DEFAULT_SCIENTIFIC_NAME,
            "scientificName.contains=" + UPDATED_SCIENTIFIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllLivestockTypesByScientificNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where scientificName does not contain
        defaultLivestockTypeFiltering(
            "scientificName.doesNotContain=" + UPDATED_SCIENTIFIC_NAME,
            "scientificName.doesNotContain=" + DEFAULT_SCIENTIFIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllLivestockTypesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where category equals to
        defaultLivestockTypeFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where category in
        defaultLivestockTypeFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where category is not null
        defaultLivestockTypeFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllLivestockTypesByCategoryContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where category contains
        defaultLivestockTypeFiltering("category.contains=" + DEFAULT_CATEGORY, "category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where category does not contain
        defaultLivestockTypeFiltering("category.doesNotContain=" + UPDATED_CATEGORY, "category.doesNotContain=" + DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where active equals to
        defaultLivestockTypeFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where active in
        defaultLivestockTypeFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllLivestockTypesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        // Get all the livestockTypeList where active is not null
        defaultLivestockTypeFiltering("active.specified=true", "active.specified=false");
    }

    private void defaultLivestockTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLivestockTypeShouldBeFound(shouldBeFound);
        defaultLivestockTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLivestockTypeShouldBeFound(String filter) throws Exception {
        restLivestockTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livestockType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].scientificName").value(hasItem(DEFAULT_SCIENTIFIC_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restLivestockTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLivestockTypeShouldNotBeFound(String filter) throws Exception {
        restLivestockTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLivestockTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLivestockType() throws Exception {
        // Get the livestockType
        restLivestockTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLivestockType() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the livestockType
        LivestockType updatedLivestockType = livestockTypeRepository.findById(livestockType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLivestockType are not directly saved in db
        em.detach(updatedLivestockType);
        updatedLivestockType
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE);
        LivestockTypeDTO livestockTypeDTO = livestockTypeMapper.toDto(updatedLivestockType);

        restLivestockTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livestockTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(livestockTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the LivestockType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLivestockTypeToMatchAllProperties(updatedLivestockType);
    }

    @Test
    @Transactional
    void putNonExistingLivestockType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockType.setId(longCount.incrementAndGet());

        // Create the LivestockType
        LivestockTypeDTO livestockTypeDTO = livestockTypeMapper.toDto(livestockType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivestockTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livestockTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(livestockTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivestockType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLivestockType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockType.setId(longCount.incrementAndGet());

        // Create the LivestockType
        LivestockTypeDTO livestockTypeDTO = livestockTypeMapper.toDto(livestockType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivestockTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(livestockTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivestockType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLivestockType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockType.setId(longCount.incrementAndGet());

        // Create the LivestockType
        LivestockTypeDTO livestockTypeDTO = livestockTypeMapper.toDto(livestockType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivestockTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(livestockTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LivestockType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLivestockTypeWithPatch() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the livestockType using partial update
        LivestockType partialUpdatedLivestockType = new LivestockType();
        partialUpdatedLivestockType.setId(livestockType.getId());

        partialUpdatedLivestockType.code(UPDATED_CODE).name(UPDATED_NAME);

        restLivestockTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivestockType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLivestockType))
            )
            .andExpect(status().isOk());

        // Validate the LivestockType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLivestockTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLivestockType, livestockType),
            getPersistedLivestockType(livestockType)
        );
    }

    @Test
    @Transactional
    void fullUpdateLivestockTypeWithPatch() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the livestockType using partial update
        LivestockType partialUpdatedLivestockType = new LivestockType();
        partialUpdatedLivestockType.setId(livestockType.getId());

        partialUpdatedLivestockType
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE);

        restLivestockTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivestockType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLivestockType))
            )
            .andExpect(status().isOk());

        // Validate the LivestockType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLivestockTypeUpdatableFieldsEquals(partialUpdatedLivestockType, getPersistedLivestockType(partialUpdatedLivestockType));
    }

    @Test
    @Transactional
    void patchNonExistingLivestockType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockType.setId(longCount.incrementAndGet());

        // Create the LivestockType
        LivestockTypeDTO livestockTypeDTO = livestockTypeMapper.toDto(livestockType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivestockTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, livestockTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(livestockTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivestockType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLivestockType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockType.setId(longCount.incrementAndGet());

        // Create the LivestockType
        LivestockTypeDTO livestockTypeDTO = livestockTypeMapper.toDto(livestockType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivestockTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(livestockTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LivestockType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLivestockType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        livestockType.setId(longCount.incrementAndGet());

        // Create the LivestockType
        LivestockTypeDTO livestockTypeDTO = livestockTypeMapper.toDto(livestockType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivestockTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(livestockTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LivestockType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLivestockType() throws Exception {
        // Initialize the database
        insertedLivestockType = livestockTypeRepository.saveAndFlush(livestockType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the livestockType
        restLivestockTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, livestockType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return livestockTypeRepository.count();
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

    protected LivestockType getPersistedLivestockType(LivestockType livestockType) {
        return livestockTypeRepository.findById(livestockType.getId()).orElseThrow();
    }

    protected void assertPersistedLivestockTypeToMatchAllProperties(LivestockType expectedLivestockType) {
        assertLivestockTypeAllPropertiesEquals(expectedLivestockType, getPersistedLivestockType(expectedLivestockType));
    }

    protected void assertPersistedLivestockTypeToMatchUpdatableProperties(LivestockType expectedLivestockType) {
        assertLivestockTypeAllUpdatablePropertiesEquals(expectedLivestockType, getPersistedLivestockType(expectedLivestockType));
    }
}
