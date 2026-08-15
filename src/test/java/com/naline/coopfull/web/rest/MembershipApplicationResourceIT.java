package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.MembershipApplicationAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.Cooperative;
import com.naline.coopfull.domain.CooperativeBranch;
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.domain.MembershipApplication;
import com.naline.coopfull.domain.enumeration.MembershipApplicationStatus;
import com.naline.coopfull.repository.MembershipApplicationRepository;
import com.naline.coopfull.service.MembershipApplicationService;
import com.naline.coopfull.service.dto.MembershipApplicationDTO;
import com.naline.coopfull.service.mapper.MembershipApplicationMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link MembershipApplicationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MembershipApplicationResourceIT {

    private static final String DEFAULT_APPLICATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_NUMBER = "BBBBBBBBBB";

    private static final MembershipApplicationStatus DEFAULT_STATUS = MembershipApplicationStatus.DRAFT;
    private static final MembershipApplicationStatus UPDATED_STATUS = MembershipApplicationStatus.SUBMITTED;

    private static final LocalDate DEFAULT_APPLICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPLICATION_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_APPLICATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_SUBMITTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMITTED_AT = Instant.ofEpochMilli(1786816059661L);

    private static final Instant DEFAULT_REVIEWED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEWED_AT = Instant.ofEpochMilli(1786816059661L);

    private static final Instant DEFAULT_APPROVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPROVED_AT = Instant.ofEpochMilli(1786816059661L);

    private static final Instant DEFAULT_REJECTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REJECTED_AT = Instant.ofEpochMilli(1786816059661L);

    private static final String DEFAULT_REJECTION_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REJECTION_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_REVIEW_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW_COMMENTS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONFIRMATION = false;
    private static final Boolean UPDATED_CONFIRMATION = true;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/membership-applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MembershipApplicationRepository membershipApplicationRepository;

    @Mock
    private MembershipApplicationRepository membershipApplicationRepositoryMock;

    @Autowired
    private MembershipApplicationMapper membershipApplicationMapper;

    @Mock
    private MembershipApplicationService membershipApplicationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembershipApplicationMockMvc;

    private MembershipApplication membershipApplication;

    private MembershipApplication insertedMembershipApplication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipApplication createEntity() {
        return new MembershipApplication()
            .applicationNumber(DEFAULT_APPLICATION_NUMBER)
            .status(DEFAULT_STATUS)
            .applicationDate(DEFAULT_APPLICATION_DATE)
            .submittedAt(DEFAULT_SUBMITTED_AT)
            .reviewedAt(DEFAULT_REVIEWED_AT)
            .approvedAt(DEFAULT_APPROVED_AT)
            .rejectedAt(DEFAULT_REJECTED_AT)
            .rejectionReason(DEFAULT_REJECTION_REASON)
            .reviewComments(DEFAULT_REVIEW_COMMENTS)
            .confirmation(DEFAULT_CONFIRMATION)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipApplication createUpdatedEntity() {
        return new MembershipApplication()
            .applicationNumber(UPDATED_APPLICATION_NUMBER)
            .status(UPDATED_STATUS)
            .applicationDate(UPDATED_APPLICATION_DATE)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .reviewedAt(UPDATED_REVIEWED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .rejectedAt(UPDATED_REJECTED_AT)
            .rejectionReason(UPDATED_REJECTION_REASON)
            .reviewComments(UPDATED_REVIEW_COMMENTS)
            .confirmation(UPDATED_CONFIRMATION)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        membershipApplication = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMembershipApplication != null) {
            membershipApplicationRepository.delete(insertedMembershipApplication);
            insertedMembershipApplication = null;
        }
    }

    @Test
    @Transactional
    void createMembershipApplication() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MembershipApplication
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(membershipApplication);
        var returnedMembershipApplicationDTO = om.readValue(
            restMembershipApplicationMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipApplicationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MembershipApplicationDTO.class
        );

        // Validate the MembershipApplication in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMembershipApplication = membershipApplicationMapper.toEntity(returnedMembershipApplicationDTO);
        assertMembershipApplicationUpdatableFieldsEquals(
            returnedMembershipApplication,
            getPersistedMembershipApplication(returnedMembershipApplication)
        );

        insertedMembershipApplication = returnedMembershipApplication;
    }

    @Test
    @Transactional
    void createMembershipApplicationWithExistingId() throws Exception {
        // Create the MembershipApplication with an existing ID
        membershipApplication.setId(1L);
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(membershipApplication);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembershipApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipApplicationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MembershipApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkApplicationNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        membershipApplication.setApplicationNumber(null);

        // Create the MembershipApplication, which fails.
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(membershipApplication);

        restMembershipApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipApplicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        membershipApplication.setStatus(null);

        // Create the MembershipApplication, which fails.
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(membershipApplication);

        restMembershipApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipApplicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApplicationDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        membershipApplication.setApplicationDate(null);

        // Create the MembershipApplication, which fails.
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(membershipApplication);

        restMembershipApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipApplicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConfirmationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        membershipApplication.setConfirmation(null);

        // Create the MembershipApplication, which fails.
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(membershipApplication);

        restMembershipApplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipApplicationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMembershipApplications() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList
        restMembershipApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationNumber").value(hasItem(DEFAULT_APPLICATION_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].applicationDate").value(hasItem(DEFAULT_APPLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].submittedAt").value(hasItem(DEFAULT_SUBMITTED_AT.toString())))
            .andExpect(jsonPath("$.[*].reviewedAt").value(hasItem(DEFAULT_REVIEWED_AT.toString())))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(DEFAULT_APPROVED_AT.toString())))
            .andExpect(jsonPath("$.[*].rejectedAt").value(hasItem(DEFAULT_REJECTED_AT.toString())))
            .andExpect(jsonPath("$.[*].rejectionReason").value(hasItem(DEFAULT_REJECTION_REASON)))
            .andExpect(jsonPath("$.[*].reviewComments").value(hasItem(DEFAULT_REVIEW_COMMENTS)))
            .andExpect(jsonPath("$.[*].confirmation").value(hasItem(DEFAULT_CONFIRMATION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMembershipApplicationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(membershipApplicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMembershipApplicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(membershipApplicationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMembershipApplicationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(membershipApplicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMembershipApplicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(membershipApplicationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMembershipApplication() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get the membershipApplication
        restMembershipApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, membershipApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membershipApplication.getId().intValue()))
            .andExpect(jsonPath("$.applicationNumber").value(DEFAULT_APPLICATION_NUMBER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.applicationDate").value(DEFAULT_APPLICATION_DATE.toString()))
            .andExpect(jsonPath("$.submittedAt").value(DEFAULT_SUBMITTED_AT.toString()))
            .andExpect(jsonPath("$.reviewedAt").value(DEFAULT_REVIEWED_AT.toString()))
            .andExpect(jsonPath("$.approvedAt").value(DEFAULT_APPROVED_AT.toString()))
            .andExpect(jsonPath("$.rejectedAt").value(DEFAULT_REJECTED_AT.toString()))
            .andExpect(jsonPath("$.rejectionReason").value(DEFAULT_REJECTION_REASON))
            .andExpect(jsonPath("$.reviewComments").value(DEFAULT_REVIEW_COMMENTS))
            .andExpect(jsonPath("$.confirmation").value(DEFAULT_CONFIRMATION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getMembershipApplicationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        Long id = membershipApplication.getId();

        defaultMembershipApplicationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMembershipApplicationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMembershipApplicationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApplicationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where applicationNumber equals to
        defaultMembershipApplicationFiltering(
            "applicationNumber.equals=" + DEFAULT_APPLICATION_NUMBER,
            "applicationNumber.equals=" + UPDATED_APPLICATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApplicationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where applicationNumber in
        defaultMembershipApplicationFiltering(
            "applicationNumber.in=" + DEFAULT_APPLICATION_NUMBER + "," + UPDATED_APPLICATION_NUMBER,
            "applicationNumber.in=" + UPDATED_APPLICATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApplicationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where applicationNumber is not null
        defaultMembershipApplicationFiltering("applicationNumber.specified=true", "applicationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApplicationNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where applicationNumber contains
        defaultMembershipApplicationFiltering(
            "applicationNumber.contains=" + DEFAULT_APPLICATION_NUMBER,
            "applicationNumber.contains=" + UPDATED_APPLICATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApplicationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where applicationNumber does not contain
        defaultMembershipApplicationFiltering(
            "applicationNumber.doesNotContain=" + UPDATED_APPLICATION_NUMBER,
            "applicationNumber.doesNotContain=" + DEFAULT_APPLICATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where status equals to
        defaultMembershipApplicationFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where status in
        defaultMembershipApplicationFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where status is not null
        defaultMembershipApplicationFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApplicationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where applicationDate equals to
        defaultMembershipApplicationFiltering(
            "applicationDate.equals=" + DEFAULT_APPLICATION_DATE,
            "applicationDate.equals=" + UPDATED_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApplicationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where applicationDate in
        defaultMembershipApplicationFiltering(
            "applicationDate.in=" + DEFAULT_APPLICATION_DATE + "," + UPDATED_APPLICATION_DATE,
            "applicationDate.in=" + UPDATED_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApplicationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where applicationDate is not null
        defaultMembershipApplicationFiltering("applicationDate.specified=true", "applicationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApplicationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where applicationDate is greater than or equal to
        defaultMembershipApplicationFiltering(
            "applicationDate.greaterThanOrEqual=" + DEFAULT_APPLICATION_DATE,
            "applicationDate.greaterThanOrEqual=" + UPDATED_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApplicationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where applicationDate is less than or equal to
        defaultMembershipApplicationFiltering(
            "applicationDate.lessThanOrEqual=" + DEFAULT_APPLICATION_DATE,
            "applicationDate.lessThanOrEqual=" + SMALLER_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApplicationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where applicationDate is less than
        defaultMembershipApplicationFiltering(
            "applicationDate.lessThan=" + UPDATED_APPLICATION_DATE,
            "applicationDate.lessThan=" + DEFAULT_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApplicationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where applicationDate is greater than
        defaultMembershipApplicationFiltering(
            "applicationDate.greaterThan=" + SMALLER_APPLICATION_DATE,
            "applicationDate.greaterThan=" + DEFAULT_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsBySubmittedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where submittedAt equals to
        defaultMembershipApplicationFiltering("submittedAt.equals=" + DEFAULT_SUBMITTED_AT, "submittedAt.equals=" + UPDATED_SUBMITTED_AT);
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsBySubmittedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where submittedAt in
        defaultMembershipApplicationFiltering(
            "submittedAt.in=" + DEFAULT_SUBMITTED_AT + "," + UPDATED_SUBMITTED_AT,
            "submittedAt.in=" + UPDATED_SUBMITTED_AT
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsBySubmittedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where submittedAt is not null
        defaultMembershipApplicationFiltering("submittedAt.specified=true", "submittedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByReviewedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where reviewedAt equals to
        defaultMembershipApplicationFiltering("reviewedAt.equals=" + DEFAULT_REVIEWED_AT, "reviewedAt.equals=" + UPDATED_REVIEWED_AT);
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByReviewedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where reviewedAt in
        defaultMembershipApplicationFiltering(
            "reviewedAt.in=" + DEFAULT_REVIEWED_AT + "," + UPDATED_REVIEWED_AT,
            "reviewedAt.in=" + UPDATED_REVIEWED_AT
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByReviewedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where reviewedAt is not null
        defaultMembershipApplicationFiltering("reviewedAt.specified=true", "reviewedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApprovedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where approvedAt equals to
        defaultMembershipApplicationFiltering("approvedAt.equals=" + DEFAULT_APPROVED_AT, "approvedAt.equals=" + UPDATED_APPROVED_AT);
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApprovedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where approvedAt in
        defaultMembershipApplicationFiltering(
            "approvedAt.in=" + DEFAULT_APPROVED_AT + "," + UPDATED_APPROVED_AT,
            "approvedAt.in=" + UPDATED_APPROVED_AT
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByApprovedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where approvedAt is not null
        defaultMembershipApplicationFiltering("approvedAt.specified=true", "approvedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByRejectedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where rejectedAt equals to
        defaultMembershipApplicationFiltering("rejectedAt.equals=" + DEFAULT_REJECTED_AT, "rejectedAt.equals=" + UPDATED_REJECTED_AT);
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByRejectedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where rejectedAt in
        defaultMembershipApplicationFiltering(
            "rejectedAt.in=" + DEFAULT_REJECTED_AT + "," + UPDATED_REJECTED_AT,
            "rejectedAt.in=" + UPDATED_REJECTED_AT
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByRejectedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where rejectedAt is not null
        defaultMembershipApplicationFiltering("rejectedAt.specified=true", "rejectedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByConfirmationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where confirmation equals to
        defaultMembershipApplicationFiltering("confirmation.equals=" + DEFAULT_CONFIRMATION, "confirmation.equals=" + UPDATED_CONFIRMATION);
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByConfirmationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where confirmation in
        defaultMembershipApplicationFiltering(
            "confirmation.in=" + DEFAULT_CONFIRMATION + "," + UPDATED_CONFIRMATION,
            "confirmation.in=" + UPDATED_CONFIRMATION
        );
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByConfirmationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        // Get all the membershipApplicationList where confirmation is not null
        defaultMembershipApplicationFiltering("confirmation.specified=true", "confirmation.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByMemberIsEqualToSomething() throws Exception {
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            membershipApplicationRepository.saveAndFlush(membershipApplication);
            member = MemberResourceIT.createEntity();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        em.persist(member);
        em.flush();
        membershipApplication.setMember(member);
        membershipApplicationRepository.saveAndFlush(membershipApplication);
        Long memberId = member.getId();
        // Get all the membershipApplicationList where member equals to memberId
        defaultMembershipApplicationShouldBeFound("memberId.equals=" + memberId);

        // Get all the membershipApplicationList where member equals to (memberId + 1)
        defaultMembershipApplicationShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByCooperativeIsEqualToSomething() throws Exception {
        Cooperative cooperative;
        if (TestUtil.findAll(em, Cooperative.class).isEmpty()) {
            membershipApplicationRepository.saveAndFlush(membershipApplication);
            cooperative = CooperativeResourceIT.createEntity();
        } else {
            cooperative = TestUtil.findAll(em, Cooperative.class).get(0);
        }
        em.persist(cooperative);
        em.flush();
        membershipApplication.setCooperative(cooperative);
        membershipApplicationRepository.saveAndFlush(membershipApplication);
        Long cooperativeId = cooperative.getId();
        // Get all the membershipApplicationList where cooperative equals to cooperativeId
        defaultMembershipApplicationShouldBeFound("cooperativeId.equals=" + cooperativeId);

        // Get all the membershipApplicationList where cooperative equals to (cooperativeId + 1)
        defaultMembershipApplicationShouldNotBeFound("cooperativeId.equals=" + (cooperativeId + 1));
    }

    @Test
    @Transactional
    void getAllMembershipApplicationsByBranchIsEqualToSomething() throws Exception {
        CooperativeBranch branch;
        if (TestUtil.findAll(em, CooperativeBranch.class).isEmpty()) {
            membershipApplicationRepository.saveAndFlush(membershipApplication);
            branch = CooperativeBranchResourceIT.createEntity();
        } else {
            branch = TestUtil.findAll(em, CooperativeBranch.class).get(0);
        }
        em.persist(branch);
        em.flush();
        membershipApplication.setBranch(branch);
        membershipApplicationRepository.saveAndFlush(membershipApplication);
        Long branchId = branch.getId();
        // Get all the membershipApplicationList where branch equals to branchId
        defaultMembershipApplicationShouldBeFound("branchId.equals=" + branchId);

        // Get all the membershipApplicationList where branch equals to (branchId + 1)
        defaultMembershipApplicationShouldNotBeFound("branchId.equals=" + (branchId + 1));
    }

    private void defaultMembershipApplicationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMembershipApplicationShouldBeFound(shouldBeFound);
        defaultMembershipApplicationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMembershipApplicationShouldBeFound(String filter) throws Exception {
        restMembershipApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationNumber").value(hasItem(DEFAULT_APPLICATION_NUMBER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].applicationDate").value(hasItem(DEFAULT_APPLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].submittedAt").value(hasItem(DEFAULT_SUBMITTED_AT.toString())))
            .andExpect(jsonPath("$.[*].reviewedAt").value(hasItem(DEFAULT_REVIEWED_AT.toString())))
            .andExpect(jsonPath("$.[*].approvedAt").value(hasItem(DEFAULT_APPROVED_AT.toString())))
            .andExpect(jsonPath("$.[*].rejectedAt").value(hasItem(DEFAULT_REJECTED_AT.toString())))
            .andExpect(jsonPath("$.[*].rejectionReason").value(hasItem(DEFAULT_REJECTION_REASON)))
            .andExpect(jsonPath("$.[*].reviewComments").value(hasItem(DEFAULT_REVIEW_COMMENTS)))
            .andExpect(jsonPath("$.[*].confirmation").value(hasItem(DEFAULT_CONFIRMATION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restMembershipApplicationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMembershipApplicationShouldNotBeFound(String filter) throws Exception {
        restMembershipApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMembershipApplicationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMembershipApplication() throws Exception {
        // Get the membershipApplication
        restMembershipApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMembershipApplication() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the membershipApplication
        MembershipApplication updatedMembershipApplication = membershipApplicationRepository
            .findById(membershipApplication.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedMembershipApplication are not directly saved in db
        em.detach(updatedMembershipApplication);
        updatedMembershipApplication
            .applicationNumber(UPDATED_APPLICATION_NUMBER)
            .status(UPDATED_STATUS)
            .applicationDate(UPDATED_APPLICATION_DATE)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .reviewedAt(UPDATED_REVIEWED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .rejectedAt(UPDATED_REJECTED_AT)
            .rejectionReason(UPDATED_REJECTION_REASON)
            .reviewComments(UPDATED_REVIEW_COMMENTS)
            .confirmation(UPDATED_CONFIRMATION)
            .notes(UPDATED_NOTES);
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(updatedMembershipApplication);

        restMembershipApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, membershipApplicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(membershipApplicationDTO))
            )
            .andExpect(status().isOk());

        // Validate the MembershipApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMembershipApplicationToMatchAllProperties(updatedMembershipApplication);
    }

    @Test
    @Transactional
    void putNonExistingMembershipApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        membershipApplication.setId(longCount.incrementAndGet());

        // Create the MembershipApplication
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(membershipApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, membershipApplicationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(membershipApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMembershipApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        membershipApplication.setId(longCount.incrementAndGet());

        // Create the MembershipApplication
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(membershipApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(membershipApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMembershipApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        membershipApplication.setId(longCount.incrementAndGet());

        // Create the MembershipApplication
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(membershipApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipApplicationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(membershipApplicationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MembershipApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMembershipApplicationWithPatch() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the membershipApplication using partial update
        MembershipApplication partialUpdatedMembershipApplication = new MembershipApplication();
        partialUpdatedMembershipApplication.setId(membershipApplication.getId());

        partialUpdatedMembershipApplication
            .status(UPDATED_STATUS)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .reviewedAt(UPDATED_REVIEWED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .rejectedAt(UPDATED_REJECTED_AT)
            .rejectionReason(UPDATED_REJECTION_REASON);

        restMembershipApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembershipApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMembershipApplication))
            )
            .andExpect(status().isOk());

        // Validate the MembershipApplication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMembershipApplicationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMembershipApplication, membershipApplication),
            getPersistedMembershipApplication(membershipApplication)
        );
    }

    @Test
    @Transactional
    void fullUpdateMembershipApplicationWithPatch() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the membershipApplication using partial update
        MembershipApplication partialUpdatedMembershipApplication = new MembershipApplication();
        partialUpdatedMembershipApplication.setId(membershipApplication.getId());

        partialUpdatedMembershipApplication
            .applicationNumber(UPDATED_APPLICATION_NUMBER)
            .status(UPDATED_STATUS)
            .applicationDate(UPDATED_APPLICATION_DATE)
            .submittedAt(UPDATED_SUBMITTED_AT)
            .reviewedAt(UPDATED_REVIEWED_AT)
            .approvedAt(UPDATED_APPROVED_AT)
            .rejectedAt(UPDATED_REJECTED_AT)
            .rejectionReason(UPDATED_REJECTION_REASON)
            .reviewComments(UPDATED_REVIEW_COMMENTS)
            .confirmation(UPDATED_CONFIRMATION)
            .notes(UPDATED_NOTES);

        restMembershipApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembershipApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMembershipApplication))
            )
            .andExpect(status().isOk());

        // Validate the MembershipApplication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMembershipApplicationUpdatableFieldsEquals(
            partialUpdatedMembershipApplication,
            getPersistedMembershipApplication(partialUpdatedMembershipApplication)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMembershipApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        membershipApplication.setId(longCount.incrementAndGet());

        // Create the MembershipApplication
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(membershipApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, membershipApplicationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(membershipApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMembershipApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        membershipApplication.setId(longCount.incrementAndGet());

        // Create the MembershipApplication
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(membershipApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(membershipApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMembershipApplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        membershipApplication.setId(longCount.incrementAndGet());

        // Create the MembershipApplication
        MembershipApplicationDTO membershipApplicationDTO = membershipApplicationMapper.toDto(membershipApplication);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(membershipApplicationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MembershipApplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMembershipApplication() throws Exception {
        // Initialize the database
        insertedMembershipApplication = membershipApplicationRepository.saveAndFlush(membershipApplication);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the membershipApplication
        restMembershipApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, membershipApplication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return membershipApplicationRepository.count();
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

    protected MembershipApplication getPersistedMembershipApplication(MembershipApplication membershipApplication) {
        return membershipApplicationRepository.findById(membershipApplication.getId()).orElseThrow();
    }

    protected void assertPersistedMembershipApplicationToMatchAllProperties(MembershipApplication expectedMembershipApplication) {
        assertMembershipApplicationAllPropertiesEquals(
            expectedMembershipApplication,
            getPersistedMembershipApplication(expectedMembershipApplication)
        );
    }

    protected void assertPersistedMembershipApplicationToMatchUpdatableProperties(MembershipApplication expectedMembershipApplication) {
        assertMembershipApplicationAllUpdatablePropertiesEquals(
            expectedMembershipApplication,
            getPersistedMembershipApplication(expectedMembershipApplication)
        );
    }
}
