package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.AuditLogAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.AppUser;
import com.naline.coopfull.domain.AuditLog;
import com.naline.coopfull.domain.enumeration.AuditAction;
import com.naline.coopfull.repository.AuditLogRepository;
import com.naline.coopfull.service.dto.AuditLogDTO;
import com.naline.coopfull.service.mapper.AuditLogMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link AuditLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AuditLogResourceIT {

    private static final AuditAction DEFAULT_ACTION = AuditAction.CREATE;
    private static final AuditAction UPDATED_ACTION = AuditAction.UPDATE;

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_ID = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final Long DEFAULT_COOPERATIVE_ID = 1L;
    private static final Long UPDATED_COOPERATIVE_ID = 2L;
    private static final Long SMALLER_COOPERATIVE_ID = 1L - 1L;

    private static final Long DEFAULT_BRANCH_ID = 1L;
    private static final Long UPDATED_BRANCH_ID = 2L;
    private static final Long SMALLER_BRANCH_ID = 1L - 1L;

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.ofEpochMilli(1786816059661L);

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_USER_AGENT = "AAAAAAAAAA";
    private static final String UPDATED_USER_AGENT = "BBBBBBBBBB";

    private static final String DEFAULT_OLD_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_OLD_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_NEW_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_NEW_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/audit-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuditLogMockMvc;

    private AuditLog auditLog;

    private AuditLog insertedAuditLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditLog createEntity() {
        return new AuditLog()
            .action(DEFAULT_ACTION)
            .entityName(DEFAULT_ENTITY_NAME)
            .entityId(DEFAULT_ENTITY_ID)
            .username(DEFAULT_USERNAME)
            .cooperativeId(DEFAULT_COOPERATIVE_ID)
            .branchId(DEFAULT_BRANCH_ID)
            .timestamp(DEFAULT_TIMESTAMP)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .userAgent(DEFAULT_USER_AGENT)
            .oldValue(DEFAULT_OLD_VALUE)
            .newValue(DEFAULT_NEW_VALUE)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditLog createUpdatedEntity() {
        return new AuditLog()
            .action(UPDATED_ACTION)
            .entityName(UPDATED_ENTITY_NAME)
            .entityId(UPDATED_ENTITY_ID)
            .username(UPDATED_USERNAME)
            .cooperativeId(UPDATED_COOPERATIVE_ID)
            .branchId(UPDATED_BRANCH_ID)
            .timestamp(UPDATED_TIMESTAMP)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        auditLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAuditLog != null) {
            auditLogRepository.delete(insertedAuditLog);
            insertedAuditLog = null;
        }
    }

    @Test
    @Transactional
    void createAuditLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);
        var returnedAuditLogDTO = om.readValue(
            restAuditLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AuditLogDTO.class
        );

        // Validate the AuditLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAuditLog = auditLogMapper.toEntity(returnedAuditLogDTO);
        assertAuditLogUpdatableFieldsEquals(returnedAuditLog, getPersistedAuditLog(returnedAuditLog));

        insertedAuditLog = returnedAuditLog;
    }

    @Test
    @Transactional
    void createAuditLogWithExistingId() throws Exception {
        // Create the AuditLog with an existing ID
        auditLog.setId(1L);
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auditLog.setAction(null);

        // Create the AuditLog, which fails.
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auditLog.setEntityName(null);

        // Create the AuditLog, which fails.
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        auditLog.setTimestamp(null);

        // Create the AuditLog, which fails.
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        restAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAuditLogs() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].cooperativeId").value(hasItem(DEFAULT_COOPERATIVE_ID.intValue())))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID.intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].userAgent").value(hasItem(DEFAULT_USER_AGENT)))
            .andExpect(jsonPath("$.[*].oldValue").value(hasItem(DEFAULT_OLD_VALUE)))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getAuditLog() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get the auditLog
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL_ID, auditLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auditLog.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.cooperativeId").value(DEFAULT_COOPERATIVE_ID.intValue()))
            .andExpect(jsonPath("$.branchId").value(DEFAULT_BRANCH_ID.intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.userAgent").value(DEFAULT_USER_AGENT))
            .andExpect(jsonPath("$.oldValue").value(DEFAULT_OLD_VALUE))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getAuditLogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        Long id = auditLog.getId();

        defaultAuditLogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAuditLogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAuditLogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAuditLogsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where action equals to
        defaultAuditLogFiltering("action.equals=" + DEFAULT_ACTION, "action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAuditLogsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where action in
        defaultAuditLogFiltering("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION, "action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllAuditLogsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where action is not null
        defaultAuditLogFiltering("action.specified=true", "action.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where entityName equals to
        defaultAuditLogFiltering("entityName.equals=" + DEFAULT_ENTITY_NAME, "entityName.equals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllAuditLogsByEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where entityName in
        defaultAuditLogFiltering(
            "entityName.in=" + DEFAULT_ENTITY_NAME + "," + UPDATED_ENTITY_NAME,
            "entityName.in=" + UPDATED_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllAuditLogsByEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where entityName is not null
        defaultAuditLogFiltering("entityName.specified=true", "entityName.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByEntityNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where entityName contains
        defaultAuditLogFiltering("entityName.contains=" + DEFAULT_ENTITY_NAME, "entityName.contains=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllAuditLogsByEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where entityName does not contain
        defaultAuditLogFiltering("entityName.doesNotContain=" + UPDATED_ENTITY_NAME, "entityName.doesNotContain=" + DEFAULT_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllAuditLogsByEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where entityId equals to
        defaultAuditLogFiltering("entityId.equals=" + DEFAULT_ENTITY_ID, "entityId.equals=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllAuditLogsByEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where entityId in
        defaultAuditLogFiltering("entityId.in=" + DEFAULT_ENTITY_ID + "," + UPDATED_ENTITY_ID, "entityId.in=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllAuditLogsByEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where entityId is not null
        defaultAuditLogFiltering("entityId.specified=true", "entityId.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByEntityIdContainsSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where entityId contains
        defaultAuditLogFiltering("entityId.contains=" + DEFAULT_ENTITY_ID, "entityId.contains=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllAuditLogsByEntityIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where entityId does not contain
        defaultAuditLogFiltering("entityId.doesNotContain=" + UPDATED_ENTITY_ID, "entityId.doesNotContain=" + DEFAULT_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where username equals to
        defaultAuditLogFiltering("username.equals=" + DEFAULT_USERNAME, "username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where username in
        defaultAuditLogFiltering("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME, "username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where username is not null
        defaultAuditLogFiltering("username.specified=true", "username.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where username contains
        defaultAuditLogFiltering("username.contains=" + DEFAULT_USERNAME, "username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where username does not contain
        defaultAuditLogFiltering("username.doesNotContain=" + UPDATED_USERNAME, "username.doesNotContain=" + DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCooperativeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cooperativeId equals to
        defaultAuditLogFiltering("cooperativeId.equals=" + DEFAULT_COOPERATIVE_ID, "cooperativeId.equals=" + UPDATED_COOPERATIVE_ID);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCooperativeIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cooperativeId in
        defaultAuditLogFiltering(
            "cooperativeId.in=" + DEFAULT_COOPERATIVE_ID + "," + UPDATED_COOPERATIVE_ID,
            "cooperativeId.in=" + UPDATED_COOPERATIVE_ID
        );
    }

    @Test
    @Transactional
    void getAllAuditLogsByCooperativeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cooperativeId is not null
        defaultAuditLogFiltering("cooperativeId.specified=true", "cooperativeId.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByCooperativeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cooperativeId is greater than or equal to
        defaultAuditLogFiltering(
            "cooperativeId.greaterThanOrEqual=" + DEFAULT_COOPERATIVE_ID,
            "cooperativeId.greaterThanOrEqual=" + UPDATED_COOPERATIVE_ID
        );
    }

    @Test
    @Transactional
    void getAllAuditLogsByCooperativeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cooperativeId is less than or equal to
        defaultAuditLogFiltering(
            "cooperativeId.lessThanOrEqual=" + DEFAULT_COOPERATIVE_ID,
            "cooperativeId.lessThanOrEqual=" + SMALLER_COOPERATIVE_ID
        );
    }

    @Test
    @Transactional
    void getAllAuditLogsByCooperativeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cooperativeId is less than
        defaultAuditLogFiltering("cooperativeId.lessThan=" + UPDATED_COOPERATIVE_ID, "cooperativeId.lessThan=" + DEFAULT_COOPERATIVE_ID);
    }

    @Test
    @Transactional
    void getAllAuditLogsByCooperativeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where cooperativeId is greater than
        defaultAuditLogFiltering(
            "cooperativeId.greaterThan=" + SMALLER_COOPERATIVE_ID,
            "cooperativeId.greaterThan=" + DEFAULT_COOPERATIVE_ID
        );
    }

    @Test
    @Transactional
    void getAllAuditLogsByBranchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where branchId equals to
        defaultAuditLogFiltering("branchId.equals=" + DEFAULT_BRANCH_ID, "branchId.equals=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAuditLogsByBranchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where branchId in
        defaultAuditLogFiltering("branchId.in=" + DEFAULT_BRANCH_ID + "," + UPDATED_BRANCH_ID, "branchId.in=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAuditLogsByBranchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where branchId is not null
        defaultAuditLogFiltering("branchId.specified=true", "branchId.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByBranchIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where branchId is greater than or equal to
        defaultAuditLogFiltering("branchId.greaterThanOrEqual=" + DEFAULT_BRANCH_ID, "branchId.greaterThanOrEqual=" + UPDATED_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAuditLogsByBranchIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where branchId is less than or equal to
        defaultAuditLogFiltering("branchId.lessThanOrEqual=" + DEFAULT_BRANCH_ID, "branchId.lessThanOrEqual=" + SMALLER_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAuditLogsByBranchIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where branchId is less than
        defaultAuditLogFiltering("branchId.lessThan=" + UPDATED_BRANCH_ID, "branchId.lessThan=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAuditLogsByBranchIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where branchId is greater than
        defaultAuditLogFiltering("branchId.greaterThan=" + SMALLER_BRANCH_ID, "branchId.greaterThan=" + DEFAULT_BRANCH_ID);
    }

    @Test
    @Transactional
    void getAllAuditLogsByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where timestamp equals to
        defaultAuditLogFiltering("timestamp.equals=" + DEFAULT_TIMESTAMP, "timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllAuditLogsByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where timestamp in
        defaultAuditLogFiltering("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP, "timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllAuditLogsByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where timestamp is not null
        defaultAuditLogFiltering("timestamp.specified=true", "timestamp.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByIpAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where ipAddress equals to
        defaultAuditLogFiltering("ipAddress.equals=" + DEFAULT_IP_ADDRESS, "ipAddress.equals=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAuditLogsByIpAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where ipAddress in
        defaultAuditLogFiltering("ipAddress.in=" + DEFAULT_IP_ADDRESS + "," + UPDATED_IP_ADDRESS, "ipAddress.in=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAuditLogsByIpAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where ipAddress is not null
        defaultAuditLogFiltering("ipAddress.specified=true", "ipAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByIpAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where ipAddress contains
        defaultAuditLogFiltering("ipAddress.contains=" + DEFAULT_IP_ADDRESS, "ipAddress.contains=" + UPDATED_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAuditLogsByIpAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where ipAddress does not contain
        defaultAuditLogFiltering("ipAddress.doesNotContain=" + UPDATED_IP_ADDRESS, "ipAddress.doesNotContain=" + DEFAULT_IP_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserAgentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userAgent equals to
        defaultAuditLogFiltering("userAgent.equals=" + DEFAULT_USER_AGENT, "userAgent.equals=" + UPDATED_USER_AGENT);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserAgentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userAgent in
        defaultAuditLogFiltering("userAgent.in=" + DEFAULT_USER_AGENT + "," + UPDATED_USER_AGENT, "userAgent.in=" + UPDATED_USER_AGENT);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserAgentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userAgent is not null
        defaultAuditLogFiltering("userAgent.specified=true", "userAgent.specified=false");
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserAgentContainsSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userAgent contains
        defaultAuditLogFiltering("userAgent.contains=" + DEFAULT_USER_AGENT, "userAgent.contains=" + UPDATED_USER_AGENT);
    }

    @Test
    @Transactional
    void getAllAuditLogsByUserAgentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        // Get all the auditLogList where userAgent does not contain
        defaultAuditLogFiltering("userAgent.doesNotContain=" + UPDATED_USER_AGENT, "userAgent.doesNotContain=" + DEFAULT_USER_AGENT);
    }

    @Test
    @Transactional
    void getAllAuditLogsByAppUserIsEqualToSomething() throws Exception {
        AppUser appUser;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            auditLogRepository.saveAndFlush(auditLog);
            appUser = AppUserResourceIT.createEntity();
        } else {
            appUser = TestUtil.findAll(em, AppUser.class).get(0);
        }
        em.persist(appUser);
        em.flush();
        auditLog.setAppUser(appUser);
        auditLogRepository.saveAndFlush(auditLog);
        Long appUserId = appUser.getId();
        // Get all the auditLogList where appUser equals to appUserId
        defaultAuditLogShouldBeFound("appUserId.equals=" + appUserId);

        // Get all the auditLogList where appUser equals to (appUserId + 1)
        defaultAuditLogShouldNotBeFound("appUserId.equals=" + (appUserId + 1));
    }

    private void defaultAuditLogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAuditLogShouldBeFound(shouldBeFound);
        defaultAuditLogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAuditLogShouldBeFound(String filter) throws Exception {
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].cooperativeId").value(hasItem(DEFAULT_COOPERATIVE_ID.intValue())))
            .andExpect(jsonPath("$.[*].branchId").value(hasItem(DEFAULT_BRANCH_ID.intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].userAgent").value(hasItem(DEFAULT_USER_AGENT)))
            .andExpect(jsonPath("$.[*].oldValue").value(hasItem(DEFAULT_OLD_VALUE)))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAuditLogShouldNotBeFound(String filter) throws Exception {
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAuditLog() throws Exception {
        // Get the auditLog
        restAuditLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAuditLog() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the auditLog
        AuditLog updatedAuditLog = auditLogRepository.findById(auditLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAuditLog are not directly saved in db
        em.detach(updatedAuditLog);
        updatedAuditLog
            .action(UPDATED_ACTION)
            .entityName(UPDATED_ENTITY_NAME)
            .entityId(UPDATED_ENTITY_ID)
            .username(UPDATED_USERNAME)
            .cooperativeId(UPDATED_COOPERATIVE_ID)
            .branchId(UPDATED_BRANCH_ID)
            .timestamp(UPDATED_TIMESTAMP)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .description(UPDATED_DESCRIPTION);
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(updatedAuditLog);

        restAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, auditLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(auditLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAuditLogToMatchAllProperties(updatedAuditLog);
    }

    @Test
    @Transactional
    void putNonExistingAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auditLog.setId(longCount.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, auditLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(auditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auditLog.setId(longCount.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(auditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auditLog.setId(longCount.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAuditLogWithPatch() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the auditLog using partial update
        AuditLog partialUpdatedAuditLog = new AuditLog();
        partialUpdatedAuditLog.setId(auditLog.getId());

        partialUpdatedAuditLog
            .action(UPDATED_ACTION)
            .entityId(UPDATED_ENTITY_ID)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT);

        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuditLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuditLog))
            )
            .andExpect(status().isOk());

        // Validate the AuditLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuditLogUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAuditLog, auditLog), getPersistedAuditLog(auditLog));
    }

    @Test
    @Transactional
    void fullUpdateAuditLogWithPatch() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the auditLog using partial update
        AuditLog partialUpdatedAuditLog = new AuditLog();
        partialUpdatedAuditLog.setId(auditLog.getId());

        partialUpdatedAuditLog
            .action(UPDATED_ACTION)
            .entityName(UPDATED_ENTITY_NAME)
            .entityId(UPDATED_ENTITY_ID)
            .username(UPDATED_USERNAME)
            .cooperativeId(UPDATED_COOPERATIVE_ID)
            .branchId(UPDATED_BRANCH_ID)
            .timestamp(UPDATED_TIMESTAMP)
            .ipAddress(UPDATED_IP_ADDRESS)
            .userAgent(UPDATED_USER_AGENT)
            .oldValue(UPDATED_OLD_VALUE)
            .newValue(UPDATED_NEW_VALUE)
            .description(UPDATED_DESCRIPTION);

        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuditLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAuditLog))
            )
            .andExpect(status().isOk());

        // Validate the AuditLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAuditLogUpdatableFieldsEquals(partialUpdatedAuditLog, getPersistedAuditLog(partialUpdatedAuditLog));
    }

    @Test
    @Transactional
    void patchNonExistingAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auditLog.setId(longCount.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, auditLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(auditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auditLog.setId(longCount.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(auditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        auditLog.setId(longCount.incrementAndGet());

        // Create the AuditLog
        AuditLogDTO auditLogDTO = auditLogMapper.toDto(auditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuditLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(auditLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAuditLog() throws Exception {
        // Initialize the database
        insertedAuditLog = auditLogRepository.saveAndFlush(auditLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the auditLog
        restAuditLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, auditLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return auditLogRepository.count();
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

    protected AuditLog getPersistedAuditLog(AuditLog auditLog) {
        return auditLogRepository.findById(auditLog.getId()).orElseThrow();
    }

    protected void assertPersistedAuditLogToMatchAllProperties(AuditLog expectedAuditLog) {
        assertAuditLogAllPropertiesEquals(expectedAuditLog, getPersistedAuditLog(expectedAuditLog));
    }

    protected void assertPersistedAuditLogToMatchUpdatableProperties(AuditLog expectedAuditLog) {
        assertAuditLogAllUpdatablePropertiesEquals(expectedAuditLog, getPersistedAuditLog(expectedAuditLog));
    }
}
