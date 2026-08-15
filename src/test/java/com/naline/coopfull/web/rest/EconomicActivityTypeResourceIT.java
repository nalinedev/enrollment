package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.EconomicActivityTypeAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.EconomicActivityType;
import com.naline.coopfull.repository.EconomicActivityTypeRepository;
import com.naline.coopfull.service.dto.EconomicActivityTypeDTO;
import com.naline.coopfull.service.mapper.EconomicActivityTypeMapper;
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
 * Integration tests for the {@link EconomicActivityTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EconomicActivityTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SECTOR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/economic-activity-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EconomicActivityTypeRepository economicActivityTypeRepository;

    @Autowired
    private EconomicActivityTypeMapper economicActivityTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEconomicActivityTypeMockMvc;

    private EconomicActivityType economicActivityType;

    private EconomicActivityType insertedEconomicActivityType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EconomicActivityType createEntity() {
        return new EconomicActivityType()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .sector(DEFAULT_SECTOR)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EconomicActivityType createUpdatedEntity() {
        return new EconomicActivityType()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .sector(UPDATED_SECTOR)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        economicActivityType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEconomicActivityType != null) {
            economicActivityTypeRepository.delete(insertedEconomicActivityType);
            insertedEconomicActivityType = null;
        }
    }

    @Test
    @Transactional
    void createEconomicActivityType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EconomicActivityType
        EconomicActivityTypeDTO economicActivityTypeDTO = economicActivityTypeMapper.toDto(economicActivityType);
        var returnedEconomicActivityTypeDTO = om.readValue(
            restEconomicActivityTypeMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(economicActivityTypeDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EconomicActivityTypeDTO.class
        );

        // Validate the EconomicActivityType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEconomicActivityType = economicActivityTypeMapper.toEntity(returnedEconomicActivityTypeDTO);
        assertEconomicActivityTypeUpdatableFieldsEquals(
            returnedEconomicActivityType,
            getPersistedEconomicActivityType(returnedEconomicActivityType)
        );

        insertedEconomicActivityType = returnedEconomicActivityType;
    }

    @Test
    @Transactional
    void createEconomicActivityTypeWithExistingId() throws Exception {
        // Create the EconomicActivityType with an existing ID
        economicActivityType.setId(1L);
        EconomicActivityTypeDTO economicActivityTypeDTO = economicActivityTypeMapper.toDto(economicActivityType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEconomicActivityTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(economicActivityTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EconomicActivityType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        economicActivityType.setCode(null);

        // Create the EconomicActivityType, which fails.
        EconomicActivityTypeDTO economicActivityTypeDTO = economicActivityTypeMapper.toDto(economicActivityType);

        restEconomicActivityTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(economicActivityTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        economicActivityType.setName(null);

        // Create the EconomicActivityType, which fails.
        EconomicActivityTypeDTO economicActivityTypeDTO = economicActivityTypeMapper.toDto(economicActivityType);

        restEconomicActivityTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(economicActivityTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        economicActivityType.setActive(null);

        // Create the EconomicActivityType, which fails.
        EconomicActivityTypeDTO economicActivityTypeDTO = economicActivityTypeMapper.toDto(economicActivityType);

        restEconomicActivityTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(economicActivityTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypes() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList
        restEconomicActivityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(economicActivityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getEconomicActivityType() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get the economicActivityType
        restEconomicActivityTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, economicActivityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(economicActivityType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sector").value(DEFAULT_SECTOR))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getEconomicActivityTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        Long id = economicActivityType.getId();

        defaultEconomicActivityTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEconomicActivityTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEconomicActivityTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where code equals to
        defaultEconomicActivityTypeFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where code in
        defaultEconomicActivityTypeFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where code is not null
        defaultEconomicActivityTypeFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where code contains
        defaultEconomicActivityTypeFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where code does not contain
        defaultEconomicActivityTypeFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where name equals to
        defaultEconomicActivityTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where name in
        defaultEconomicActivityTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where name is not null
        defaultEconomicActivityTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where name contains
        defaultEconomicActivityTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where name does not contain
        defaultEconomicActivityTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesBySectorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where sector equals to
        defaultEconomicActivityTypeFiltering("sector.equals=" + DEFAULT_SECTOR, "sector.equals=" + UPDATED_SECTOR);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesBySectorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where sector in
        defaultEconomicActivityTypeFiltering("sector.in=" + DEFAULT_SECTOR + "," + UPDATED_SECTOR, "sector.in=" + UPDATED_SECTOR);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesBySectorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where sector is not null
        defaultEconomicActivityTypeFiltering("sector.specified=true", "sector.specified=false");
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesBySectorContainsSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where sector contains
        defaultEconomicActivityTypeFiltering("sector.contains=" + DEFAULT_SECTOR, "sector.contains=" + UPDATED_SECTOR);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesBySectorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where sector does not contain
        defaultEconomicActivityTypeFiltering("sector.doesNotContain=" + UPDATED_SECTOR, "sector.doesNotContain=" + DEFAULT_SECTOR);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where active equals to
        defaultEconomicActivityTypeFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where active in
        defaultEconomicActivityTypeFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEconomicActivityTypesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        // Get all the economicActivityTypeList where active is not null
        defaultEconomicActivityTypeFiltering("active.specified=true", "active.specified=false");
    }

    private void defaultEconomicActivityTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEconomicActivityTypeShouldBeFound(shouldBeFound);
        defaultEconomicActivityTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEconomicActivityTypeShouldBeFound(String filter) throws Exception {
        restEconomicActivityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(economicActivityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restEconomicActivityTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEconomicActivityTypeShouldNotBeFound(String filter) throws Exception {
        restEconomicActivityTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEconomicActivityTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEconomicActivityType() throws Exception {
        // Get the economicActivityType
        restEconomicActivityTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEconomicActivityType() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the economicActivityType
        EconomicActivityType updatedEconomicActivityType = economicActivityTypeRepository
            .findById(economicActivityType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedEconomicActivityType are not directly saved in db
        em.detach(updatedEconomicActivityType);
        updatedEconomicActivityType
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .sector(UPDATED_SECTOR)
            .active(UPDATED_ACTIVE);
        EconomicActivityTypeDTO economicActivityTypeDTO = economicActivityTypeMapper.toDto(updatedEconomicActivityType);

        restEconomicActivityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, economicActivityTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(economicActivityTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the EconomicActivityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEconomicActivityTypeToMatchAllProperties(updatedEconomicActivityType);
    }

    @Test
    @Transactional
    void putNonExistingEconomicActivityType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        economicActivityType.setId(longCount.incrementAndGet());

        // Create the EconomicActivityType
        EconomicActivityTypeDTO economicActivityTypeDTO = economicActivityTypeMapper.toDto(economicActivityType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEconomicActivityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, economicActivityTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(economicActivityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EconomicActivityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEconomicActivityType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        economicActivityType.setId(longCount.incrementAndGet());

        // Create the EconomicActivityType
        EconomicActivityTypeDTO economicActivityTypeDTO = economicActivityTypeMapper.toDto(economicActivityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEconomicActivityTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(economicActivityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EconomicActivityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEconomicActivityType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        economicActivityType.setId(longCount.incrementAndGet());

        // Create the EconomicActivityType
        EconomicActivityTypeDTO economicActivityTypeDTO = economicActivityTypeMapper.toDto(economicActivityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEconomicActivityTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(economicActivityTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EconomicActivityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEconomicActivityTypeWithPatch() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the economicActivityType using partial update
        EconomicActivityType partialUpdatedEconomicActivityType = new EconomicActivityType();
        partialUpdatedEconomicActivityType.setId(economicActivityType.getId());

        partialUpdatedEconomicActivityType.description(UPDATED_DESCRIPTION).active(UPDATED_ACTIVE);

        restEconomicActivityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEconomicActivityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEconomicActivityType))
            )
            .andExpect(status().isOk());

        // Validate the EconomicActivityType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEconomicActivityTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEconomicActivityType, economicActivityType),
            getPersistedEconomicActivityType(economicActivityType)
        );
    }

    @Test
    @Transactional
    void fullUpdateEconomicActivityTypeWithPatch() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the economicActivityType using partial update
        EconomicActivityType partialUpdatedEconomicActivityType = new EconomicActivityType();
        partialUpdatedEconomicActivityType.setId(economicActivityType.getId());

        partialUpdatedEconomicActivityType
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .sector(UPDATED_SECTOR)
            .active(UPDATED_ACTIVE);

        restEconomicActivityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEconomicActivityType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEconomicActivityType))
            )
            .andExpect(status().isOk());

        // Validate the EconomicActivityType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEconomicActivityTypeUpdatableFieldsEquals(
            partialUpdatedEconomicActivityType,
            getPersistedEconomicActivityType(partialUpdatedEconomicActivityType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEconomicActivityType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        economicActivityType.setId(longCount.incrementAndGet());

        // Create the EconomicActivityType
        EconomicActivityTypeDTO economicActivityTypeDTO = economicActivityTypeMapper.toDto(economicActivityType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEconomicActivityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, economicActivityTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(economicActivityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EconomicActivityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEconomicActivityType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        economicActivityType.setId(longCount.incrementAndGet());

        // Create the EconomicActivityType
        EconomicActivityTypeDTO economicActivityTypeDTO = economicActivityTypeMapper.toDto(economicActivityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEconomicActivityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(economicActivityTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EconomicActivityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEconomicActivityType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        economicActivityType.setId(longCount.incrementAndGet());

        // Create the EconomicActivityType
        EconomicActivityTypeDTO economicActivityTypeDTO = economicActivityTypeMapper.toDto(economicActivityType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEconomicActivityTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(economicActivityTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EconomicActivityType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEconomicActivityType() throws Exception {
        // Initialize the database
        insertedEconomicActivityType = economicActivityTypeRepository.saveAndFlush(economicActivityType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the economicActivityType
        restEconomicActivityTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, economicActivityType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return economicActivityTypeRepository.count();
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

    protected EconomicActivityType getPersistedEconomicActivityType(EconomicActivityType economicActivityType) {
        return economicActivityTypeRepository.findById(economicActivityType.getId()).orElseThrow();
    }

    protected void assertPersistedEconomicActivityTypeToMatchAllProperties(EconomicActivityType expectedEconomicActivityType) {
        assertEconomicActivityTypeAllPropertiesEquals(
            expectedEconomicActivityType,
            getPersistedEconomicActivityType(expectedEconomicActivityType)
        );
    }

    protected void assertPersistedEconomicActivityTypeToMatchUpdatableProperties(EconomicActivityType expectedEconomicActivityType) {
        assertEconomicActivityTypeAllUpdatablePropertiesEquals(
            expectedEconomicActivityType,
            getPersistedEconomicActivityType(expectedEconomicActivityType)
        );
    }
}
