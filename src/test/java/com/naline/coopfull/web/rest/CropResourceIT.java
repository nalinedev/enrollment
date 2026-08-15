package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.CropAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.Crop;
import com.naline.coopfull.repository.CropRepository;
import com.naline.coopfull.service.dto.CropDTO;
import com.naline.coopfull.service.mapper.CropMapper;
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
 * Integration tests for the {@link CropResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CropResourceIT {

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

    private static final Boolean DEFAULT_PERENNIAL = false;
    private static final Boolean UPDATED_PERENNIAL = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/crops";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private CropMapper cropMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCropMockMvc;

    private Crop crop;

    private Crop insertedCrop;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Crop createEntity() {
        return new Crop()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .scientificName(DEFAULT_SCIENTIFIC_NAME)
            .category(DEFAULT_CATEGORY)
            .description(DEFAULT_DESCRIPTION)
            .perennial(DEFAULT_PERENNIAL)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Crop createUpdatedEntity() {
        return new Crop()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .perennial(UPDATED_PERENNIAL)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    void initTest() {
        crop = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCrop != null) {
            cropRepository.delete(insertedCrop);
            insertedCrop = null;
        }
    }

    @Test
    @Transactional
    void createCrop() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Crop
        CropDTO cropDTO = cropMapper.toDto(crop);
        var returnedCropDTO = om.readValue(
            restCropMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CropDTO.class
        );

        // Validate the Crop in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCrop = cropMapper.toEntity(returnedCropDTO);
        assertCropUpdatableFieldsEquals(returnedCrop, getPersistedCrop(returnedCrop));

        insertedCrop = returnedCrop;
    }

    @Test
    @Transactional
    void createCropWithExistingId() throws Exception {
        // Create the Crop with an existing ID
        crop.setId(1L);
        CropDTO cropDTO = cropMapper.toDto(crop);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCropMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Crop in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        crop.setCode(null);

        // Create the Crop, which fails.
        CropDTO cropDTO = cropMapper.toDto(crop);

        restCropMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        crop.setName(null);

        // Create the Crop, which fails.
        CropDTO cropDTO = cropMapper.toDto(crop);

        restCropMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        crop.setActive(null);

        // Create the Crop, which fails.
        CropDTO cropDTO = cropMapper.toDto(crop);

        restCropMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrops() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList
        restCropMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crop.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].scientificName").value(hasItem(DEFAULT_SCIENTIFIC_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].perennial").value(hasItem(DEFAULT_PERENNIAL)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getCrop() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get the crop
        restCropMockMvc
            .perform(get(ENTITY_API_URL_ID, crop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crop.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.scientificName").value(DEFAULT_SCIENTIFIC_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.perennial").value(DEFAULT_PERENNIAL))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getCropsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        Long id = crop.getId();

        defaultCropFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCropFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCropFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCropsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where code equals to
        defaultCropFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCropsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where code in
        defaultCropFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCropsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where code is not null
        defaultCropFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllCropsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where code contains
        defaultCropFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllCropsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where code does not contain
        defaultCropFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllCropsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where name equals to
        defaultCropFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where name in
        defaultCropFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where name is not null
        defaultCropFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCropsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where name contains
        defaultCropFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCropsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where name does not contain
        defaultCropFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCropsByScientificNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where scientificName equals to
        defaultCropFiltering("scientificName.equals=" + DEFAULT_SCIENTIFIC_NAME, "scientificName.equals=" + UPDATED_SCIENTIFIC_NAME);
    }

    @Test
    @Transactional
    void getAllCropsByScientificNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where scientificName in
        defaultCropFiltering(
            "scientificName.in=" + DEFAULT_SCIENTIFIC_NAME + "," + UPDATED_SCIENTIFIC_NAME,
            "scientificName.in=" + UPDATED_SCIENTIFIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllCropsByScientificNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where scientificName is not null
        defaultCropFiltering("scientificName.specified=true", "scientificName.specified=false");
    }

    @Test
    @Transactional
    void getAllCropsByScientificNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where scientificName contains
        defaultCropFiltering("scientificName.contains=" + DEFAULT_SCIENTIFIC_NAME, "scientificName.contains=" + UPDATED_SCIENTIFIC_NAME);
    }

    @Test
    @Transactional
    void getAllCropsByScientificNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where scientificName does not contain
        defaultCropFiltering(
            "scientificName.doesNotContain=" + UPDATED_SCIENTIFIC_NAME,
            "scientificName.doesNotContain=" + DEFAULT_SCIENTIFIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllCropsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where category equals to
        defaultCropFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCropsByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where category in
        defaultCropFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCropsByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where category is not null
        defaultCropFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllCropsByCategoryContainsSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where category contains
        defaultCropFiltering("category.contains=" + DEFAULT_CATEGORY, "category.contains=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCropsByCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where category does not contain
        defaultCropFiltering("category.doesNotContain=" + UPDATED_CATEGORY, "category.doesNotContain=" + DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void getAllCropsByPerennialIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where perennial equals to
        defaultCropFiltering("perennial.equals=" + DEFAULT_PERENNIAL, "perennial.equals=" + UPDATED_PERENNIAL);
    }

    @Test
    @Transactional
    void getAllCropsByPerennialIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where perennial in
        defaultCropFiltering("perennial.in=" + DEFAULT_PERENNIAL + "," + UPDATED_PERENNIAL, "perennial.in=" + UPDATED_PERENNIAL);
    }

    @Test
    @Transactional
    void getAllCropsByPerennialIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where perennial is not null
        defaultCropFiltering("perennial.specified=true", "perennial.specified=false");
    }

    @Test
    @Transactional
    void getAllCropsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where active equals to
        defaultCropFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCropsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where active in
        defaultCropFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllCropsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        // Get all the cropList where active is not null
        defaultCropFiltering("active.specified=true", "active.specified=false");
    }

    private void defaultCropFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCropShouldBeFound(shouldBeFound);
        defaultCropShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCropShouldBeFound(String filter) throws Exception {
        restCropMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crop.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].scientificName").value(hasItem(DEFAULT_SCIENTIFIC_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].perennial").value(hasItem(DEFAULT_PERENNIAL)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));

        // Check, that the count call also returns 1
        restCropMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCropShouldNotBeFound(String filter) throws Exception {
        restCropMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCropMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrop() throws Exception {
        // Get the crop
        restCropMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCrop() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the crop
        Crop updatedCrop = cropRepository.findById(crop.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCrop are not directly saved in db
        em.detach(updatedCrop);
        updatedCrop
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .perennial(UPDATED_PERENNIAL)
            .active(UPDATED_ACTIVE);
        CropDTO cropDTO = cropMapper.toDto(updatedCrop);

        restCropMockMvc
            .perform(put(ENTITY_API_URL_ID, cropDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDTO)))
            .andExpect(status().isOk());

        // Validate the Crop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCropToMatchAllProperties(updatedCrop);
    }

    @Test
    @Transactional
    void putNonExistingCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crop.setId(longCount.incrementAndGet());

        // Create the Crop
        CropDTO cropDTO = cropMapper.toDto(crop);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropMockMvc
            .perform(put(ENTITY_API_URL_ID, cropDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Crop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crop.setId(longCount.incrementAndGet());

        // Create the Crop
        CropDTO cropDTO = cropMapper.toDto(crop);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cropDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crop.setId(longCount.incrementAndGet());

        // Create the Crop
        CropDTO cropDTO = cropMapper.toDto(crop);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cropDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Crop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCropWithPatch() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the crop using partial update
        Crop partialUpdatedCrop = new Crop();
        partialUpdatedCrop.setId(crop.getId());

        partialUpdatedCrop.category(UPDATED_CATEGORY).description(UPDATED_DESCRIPTION).perennial(UPDATED_PERENNIAL).active(UPDATED_ACTIVE);

        restCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrop.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCrop))
            )
            .andExpect(status().isOk());

        // Validate the Crop in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCrop, crop), getPersistedCrop(crop));
    }

    @Test
    @Transactional
    void fullUpdateCropWithPatch() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the crop using partial update
        Crop partialUpdatedCrop = new Crop();
        partialUpdatedCrop.setId(crop.getId());

        partialUpdatedCrop
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .perennial(UPDATED_PERENNIAL)
            .active(UPDATED_ACTIVE);

        restCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrop.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCrop))
            )
            .andExpect(status().isOk());

        // Validate the Crop in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCropUpdatableFieldsEquals(partialUpdatedCrop, getPersistedCrop(partialUpdatedCrop));
    }

    @Test
    @Transactional
    void patchNonExistingCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crop.setId(longCount.incrementAndGet());

        // Create the Crop
        CropDTO cropDTO = cropMapper.toDto(crop);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cropDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cropDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crop.setId(longCount.incrementAndGet());

        // Create the Crop
        CropDTO cropDTO = cropMapper.toDto(crop);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cropDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrop() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        crop.setId(longCount.incrementAndGet());

        // Create the Crop
        CropDTO cropDTO = cropMapper.toDto(crop);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCropMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cropDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Crop in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCrop() throws Exception {
        // Initialize the database
        insertedCrop = cropRepository.saveAndFlush(crop);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the crop
        restCropMockMvc
            .perform(delete(ENTITY_API_URL_ID, crop.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cropRepository.count();
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

    protected Crop getPersistedCrop(Crop crop) {
        return cropRepository.findById(crop.getId()).orElseThrow();
    }

    protected void assertPersistedCropToMatchAllProperties(Crop expectedCrop) {
        assertCropAllPropertiesEquals(expectedCrop, getPersistedCrop(expectedCrop));
    }

    protected void assertPersistedCropToMatchUpdatableProperties(Crop expectedCrop) {
        assertCropAllUpdatablePropertiesEquals(expectedCrop, getPersistedCrop(expectedCrop));
    }
}
