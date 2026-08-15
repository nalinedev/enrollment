package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.IdentityDocumentAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.IdentityDocument;
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.domain.enumeration.DocumentStatus;
import com.naline.coopfull.repository.IdentityDocumentRepository;
import com.naline.coopfull.service.IdentityDocumentService;
import com.naline.coopfull.service.dto.IdentityDocumentDTO;
import com.naline.coopfull.service.mapper.IdentityDocumentMapper;
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
 * Integration tests for the {@link IdentityDocumentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class IdentityDocumentResourceIT {

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ISSUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ISSUE_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_ISSUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_EXPIRY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ISSUING_AUTHORITY = "AAAAAAAAAA";
    private static final String UPDATED_ISSUING_AUTHORITY = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUING_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_ISSUING_COUNTRY = "BBBBBBBBBB";

    private static final DocumentStatus DEFAULT_STATUS = DocumentStatus.VALID;
    private static final DocumentStatus UPDATED_STATUS = DocumentStatus.EXPIRED;

    private static final Boolean DEFAULT_VERIFIED = false;
    private static final Boolean UPDATED_VERIFIED = true;

    private static final Instant DEFAULT_VERIFICATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VERIFICATION_DATE = Instant.ofEpochMilli(1786816059661L);

    private static final String DEFAULT_VERIFICATION_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_VERIFICATION_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/identity-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IdentityDocumentRepository identityDocumentRepository;

    @Mock
    private IdentityDocumentRepository identityDocumentRepositoryMock;

    @Autowired
    private IdentityDocumentMapper identityDocumentMapper;

    @Mock
    private IdentityDocumentService identityDocumentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIdentityDocumentMockMvc;

    private IdentityDocument identityDocument;

    private IdentityDocument insertedIdentityDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdentityDocument createEntity() {
        return new IdentityDocument()
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .documentNumber(DEFAULT_DOCUMENT_NUMBER)
            .issueDate(DEFAULT_ISSUE_DATE)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .issuingAuthority(DEFAULT_ISSUING_AUTHORITY)
            .issuingCountry(DEFAULT_ISSUING_COUNTRY)
            .status(DEFAULT_STATUS)
            .verified(DEFAULT_VERIFIED)
            .verificationDate(DEFAULT_VERIFICATION_DATE)
            .verificationComment(DEFAULT_VERIFICATION_COMMENT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IdentityDocument createUpdatedEntity() {
        return new IdentityDocument()
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .issuingAuthority(UPDATED_ISSUING_AUTHORITY)
            .issuingCountry(UPDATED_ISSUING_COUNTRY)
            .status(UPDATED_STATUS)
            .verified(UPDATED_VERIFIED)
            .verificationDate(UPDATED_VERIFICATION_DATE)
            .verificationComment(UPDATED_VERIFICATION_COMMENT);
    }

    @BeforeEach
    void initTest() {
        identityDocument = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIdentityDocument != null) {
            identityDocumentRepository.delete(insertedIdentityDocument);
            insertedIdentityDocument = null;
        }
    }

    @Test
    @Transactional
    void createIdentityDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);
        var returnedIdentityDocumentDTO = om.readValue(
            restIdentityDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IdentityDocumentDTO.class
        );

        // Validate the IdentityDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIdentityDocument = identityDocumentMapper.toEntity(returnedIdentityDocumentDTO);
        assertIdentityDocumentUpdatableFieldsEquals(returnedIdentityDocument, getPersistedIdentityDocument(returnedIdentityDocument));

        insertedIdentityDocument = returnedIdentityDocument;
    }

    @Test
    @Transactional
    void createIdentityDocumentWithExistingId() throws Exception {
        // Create the IdentityDocument with an existing ID
        identityDocument.setId(1L);
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIdentityDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        identityDocument.setDocumentType(null);

        // Create the IdentityDocument, which fails.
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        restIdentityDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        identityDocument.setDocumentNumber(null);

        // Create the IdentityDocument, which fails.
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        restIdentityDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        identityDocument.setStatus(null);

        // Create the IdentityDocument, which fails.
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        restIdentityDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVerifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        identityDocument.setVerified(null);

        // Create the IdentityDocument, which fails.
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        restIdentityDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIdentityDocuments() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList
        restIdentityDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(identityDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].issuingAuthority").value(hasItem(DEFAULT_ISSUING_AUTHORITY)))
            .andExpect(jsonPath("$.[*].issuingCountry").value(hasItem(DEFAULT_ISSUING_COUNTRY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED)))
            .andExpect(jsonPath("$.[*].verificationDate").value(hasItem(DEFAULT_VERIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].verificationComment").value(hasItem(DEFAULT_VERIFICATION_COMMENT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIdentityDocumentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(identityDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIdentityDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(identityDocumentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIdentityDocumentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(identityDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIdentityDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(identityDocumentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getIdentityDocument() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get the identityDocument
        restIdentityDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, identityDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(identityDocument.getId().intValue()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE))
            .andExpect(jsonPath("$.documentNumber").value(DEFAULT_DOCUMENT_NUMBER))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.issuingAuthority").value(DEFAULT_ISSUING_AUTHORITY))
            .andExpect(jsonPath("$.issuingCountry").value(DEFAULT_ISSUING_COUNTRY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.verified").value(DEFAULT_VERIFIED))
            .andExpect(jsonPath("$.verificationDate").value(DEFAULT_VERIFICATION_DATE.toString()))
            .andExpect(jsonPath("$.verificationComment").value(DEFAULT_VERIFICATION_COMMENT));
    }

    @Test
    @Transactional
    void getIdentityDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        Long id = identityDocument.getId();

        defaultIdentityDocumentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIdentityDocumentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIdentityDocumentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByDocumentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where documentType equals to
        defaultIdentityDocumentFiltering("documentType.equals=" + DEFAULT_DOCUMENT_TYPE, "documentType.equals=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByDocumentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where documentType in
        defaultIdentityDocumentFiltering(
            "documentType.in=" + DEFAULT_DOCUMENT_TYPE + "," + UPDATED_DOCUMENT_TYPE,
            "documentType.in=" + UPDATED_DOCUMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByDocumentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where documentType is not null
        defaultIdentityDocumentFiltering("documentType.specified=true", "documentType.specified=false");
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByDocumentTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where documentType contains
        defaultIdentityDocumentFiltering(
            "documentType.contains=" + DEFAULT_DOCUMENT_TYPE,
            "documentType.contains=" + UPDATED_DOCUMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByDocumentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where documentType does not contain
        defaultIdentityDocumentFiltering(
            "documentType.doesNotContain=" + UPDATED_DOCUMENT_TYPE,
            "documentType.doesNotContain=" + DEFAULT_DOCUMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByDocumentNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where documentNumber equals to
        defaultIdentityDocumentFiltering(
            "documentNumber.equals=" + DEFAULT_DOCUMENT_NUMBER,
            "documentNumber.equals=" + UPDATED_DOCUMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByDocumentNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where documentNumber in
        defaultIdentityDocumentFiltering(
            "documentNumber.in=" + DEFAULT_DOCUMENT_NUMBER + "," + UPDATED_DOCUMENT_NUMBER,
            "documentNumber.in=" + UPDATED_DOCUMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByDocumentNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where documentNumber is not null
        defaultIdentityDocumentFiltering("documentNumber.specified=true", "documentNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByDocumentNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where documentNumber contains
        defaultIdentityDocumentFiltering(
            "documentNumber.contains=" + DEFAULT_DOCUMENT_NUMBER,
            "documentNumber.contains=" + UPDATED_DOCUMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByDocumentNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where documentNumber does not contain
        defaultIdentityDocumentFiltering(
            "documentNumber.doesNotContain=" + UPDATED_DOCUMENT_NUMBER,
            "documentNumber.doesNotContain=" + DEFAULT_DOCUMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issueDate equals to
        defaultIdentityDocumentFiltering("issueDate.equals=" + DEFAULT_ISSUE_DATE, "issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issueDate in
        defaultIdentityDocumentFiltering(
            "issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE,
            "issueDate.in=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issueDate is not null
        defaultIdentityDocumentFiltering("issueDate.specified=true", "issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issueDate is greater than or equal to
        defaultIdentityDocumentFiltering(
            "issueDate.greaterThanOrEqual=" + DEFAULT_ISSUE_DATE,
            "issueDate.greaterThanOrEqual=" + UPDATED_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issueDate is less than or equal to
        defaultIdentityDocumentFiltering(
            "issueDate.lessThanOrEqual=" + DEFAULT_ISSUE_DATE,
            "issueDate.lessThanOrEqual=" + SMALLER_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issueDate is less than
        defaultIdentityDocumentFiltering("issueDate.lessThan=" + UPDATED_ISSUE_DATE, "issueDate.lessThan=" + DEFAULT_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issueDate is greater than
        defaultIdentityDocumentFiltering("issueDate.greaterThan=" + SMALLER_ISSUE_DATE, "issueDate.greaterThan=" + DEFAULT_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByExpiryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where expiryDate equals to
        defaultIdentityDocumentFiltering("expiryDate.equals=" + DEFAULT_EXPIRY_DATE, "expiryDate.equals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByExpiryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where expiryDate in
        defaultIdentityDocumentFiltering(
            "expiryDate.in=" + DEFAULT_EXPIRY_DATE + "," + UPDATED_EXPIRY_DATE,
            "expiryDate.in=" + UPDATED_EXPIRY_DATE
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByExpiryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where expiryDate is not null
        defaultIdentityDocumentFiltering("expiryDate.specified=true", "expiryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByExpiryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where expiryDate is greater than or equal to
        defaultIdentityDocumentFiltering(
            "expiryDate.greaterThanOrEqual=" + DEFAULT_EXPIRY_DATE,
            "expiryDate.greaterThanOrEqual=" + UPDATED_EXPIRY_DATE
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByExpiryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where expiryDate is less than or equal to
        defaultIdentityDocumentFiltering(
            "expiryDate.lessThanOrEqual=" + DEFAULT_EXPIRY_DATE,
            "expiryDate.lessThanOrEqual=" + SMALLER_EXPIRY_DATE
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByExpiryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where expiryDate is less than
        defaultIdentityDocumentFiltering("expiryDate.lessThan=" + UPDATED_EXPIRY_DATE, "expiryDate.lessThan=" + DEFAULT_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByExpiryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where expiryDate is greater than
        defaultIdentityDocumentFiltering("expiryDate.greaterThan=" + SMALLER_EXPIRY_DATE, "expiryDate.greaterThan=" + DEFAULT_EXPIRY_DATE);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssuingAuthorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issuingAuthority equals to
        defaultIdentityDocumentFiltering(
            "issuingAuthority.equals=" + DEFAULT_ISSUING_AUTHORITY,
            "issuingAuthority.equals=" + UPDATED_ISSUING_AUTHORITY
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssuingAuthorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issuingAuthority in
        defaultIdentityDocumentFiltering(
            "issuingAuthority.in=" + DEFAULT_ISSUING_AUTHORITY + "," + UPDATED_ISSUING_AUTHORITY,
            "issuingAuthority.in=" + UPDATED_ISSUING_AUTHORITY
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssuingAuthorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issuingAuthority is not null
        defaultIdentityDocumentFiltering("issuingAuthority.specified=true", "issuingAuthority.specified=false");
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssuingAuthorityContainsSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issuingAuthority contains
        defaultIdentityDocumentFiltering(
            "issuingAuthority.contains=" + DEFAULT_ISSUING_AUTHORITY,
            "issuingAuthority.contains=" + UPDATED_ISSUING_AUTHORITY
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssuingAuthorityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issuingAuthority does not contain
        defaultIdentityDocumentFiltering(
            "issuingAuthority.doesNotContain=" + UPDATED_ISSUING_AUTHORITY,
            "issuingAuthority.doesNotContain=" + DEFAULT_ISSUING_AUTHORITY
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssuingCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issuingCountry equals to
        defaultIdentityDocumentFiltering(
            "issuingCountry.equals=" + DEFAULT_ISSUING_COUNTRY,
            "issuingCountry.equals=" + UPDATED_ISSUING_COUNTRY
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssuingCountryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issuingCountry in
        defaultIdentityDocumentFiltering(
            "issuingCountry.in=" + DEFAULT_ISSUING_COUNTRY + "," + UPDATED_ISSUING_COUNTRY,
            "issuingCountry.in=" + UPDATED_ISSUING_COUNTRY
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssuingCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issuingCountry is not null
        defaultIdentityDocumentFiltering("issuingCountry.specified=true", "issuingCountry.specified=false");
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssuingCountryContainsSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issuingCountry contains
        defaultIdentityDocumentFiltering(
            "issuingCountry.contains=" + DEFAULT_ISSUING_COUNTRY,
            "issuingCountry.contains=" + UPDATED_ISSUING_COUNTRY
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByIssuingCountryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where issuingCountry does not contain
        defaultIdentityDocumentFiltering(
            "issuingCountry.doesNotContain=" + UPDATED_ISSUING_COUNTRY,
            "issuingCountry.doesNotContain=" + DEFAULT_ISSUING_COUNTRY
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where status equals to
        defaultIdentityDocumentFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where status in
        defaultIdentityDocumentFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where status is not null
        defaultIdentityDocumentFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByVerifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where verified equals to
        defaultIdentityDocumentFiltering("verified.equals=" + DEFAULT_VERIFIED, "verified.equals=" + UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByVerifiedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where verified in
        defaultIdentityDocumentFiltering("verified.in=" + DEFAULT_VERIFIED + "," + UPDATED_VERIFIED, "verified.in=" + UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByVerifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where verified is not null
        defaultIdentityDocumentFiltering("verified.specified=true", "verified.specified=false");
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByVerificationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where verificationDate equals to
        defaultIdentityDocumentFiltering(
            "verificationDate.equals=" + DEFAULT_VERIFICATION_DATE,
            "verificationDate.equals=" + UPDATED_VERIFICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByVerificationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where verificationDate in
        defaultIdentityDocumentFiltering(
            "verificationDate.in=" + DEFAULT_VERIFICATION_DATE + "," + UPDATED_VERIFICATION_DATE,
            "verificationDate.in=" + UPDATED_VERIFICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByVerificationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where verificationDate is not null
        defaultIdentityDocumentFiltering("verificationDate.specified=true", "verificationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByVerificationCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where verificationComment equals to
        defaultIdentityDocumentFiltering(
            "verificationComment.equals=" + DEFAULT_VERIFICATION_COMMENT,
            "verificationComment.equals=" + UPDATED_VERIFICATION_COMMENT
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByVerificationCommentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where verificationComment in
        defaultIdentityDocumentFiltering(
            "verificationComment.in=" + DEFAULT_VERIFICATION_COMMENT + "," + UPDATED_VERIFICATION_COMMENT,
            "verificationComment.in=" + UPDATED_VERIFICATION_COMMENT
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByVerificationCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where verificationComment is not null
        defaultIdentityDocumentFiltering("verificationComment.specified=true", "verificationComment.specified=false");
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByVerificationCommentContainsSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where verificationComment contains
        defaultIdentityDocumentFiltering(
            "verificationComment.contains=" + DEFAULT_VERIFICATION_COMMENT,
            "verificationComment.contains=" + UPDATED_VERIFICATION_COMMENT
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByVerificationCommentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        // Get all the identityDocumentList where verificationComment does not contain
        defaultIdentityDocumentFiltering(
            "verificationComment.doesNotContain=" + UPDATED_VERIFICATION_COMMENT,
            "verificationComment.doesNotContain=" + DEFAULT_VERIFICATION_COMMENT
        );
    }

    @Test
    @Transactional
    void getAllIdentityDocumentsByMemberIsEqualToSomething() throws Exception {
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            identityDocumentRepository.saveAndFlush(identityDocument);
            member = MemberResourceIT.createEntity();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        em.persist(member);
        em.flush();
        identityDocument.setMember(member);
        identityDocumentRepository.saveAndFlush(identityDocument);
        Long memberId = member.getId();
        // Get all the identityDocumentList where member equals to memberId
        defaultIdentityDocumentShouldBeFound("memberId.equals=" + memberId);

        // Get all the identityDocumentList where member equals to (memberId + 1)
        defaultIdentityDocumentShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    private void defaultIdentityDocumentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIdentityDocumentShouldBeFound(shouldBeFound);
        defaultIdentityDocumentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIdentityDocumentShouldBeFound(String filter) throws Exception {
        restIdentityDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(identityDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)))
            .andExpect(jsonPath("$.[*].documentNumber").value(hasItem(DEFAULT_DOCUMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].issuingAuthority").value(hasItem(DEFAULT_ISSUING_AUTHORITY)))
            .andExpect(jsonPath("$.[*].issuingCountry").value(hasItem(DEFAULT_ISSUING_COUNTRY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED)))
            .andExpect(jsonPath("$.[*].verificationDate").value(hasItem(DEFAULT_VERIFICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].verificationComment").value(hasItem(DEFAULT_VERIFICATION_COMMENT)));

        // Check, that the count call also returns 1
        restIdentityDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIdentityDocumentShouldNotBeFound(String filter) throws Exception {
        restIdentityDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIdentityDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIdentityDocument() throws Exception {
        // Get the identityDocument
        restIdentityDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIdentityDocument() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the identityDocument
        IdentityDocument updatedIdentityDocument = identityDocumentRepository.findById(identityDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIdentityDocument are not directly saved in db
        em.detach(updatedIdentityDocument);
        updatedIdentityDocument
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .issuingAuthority(UPDATED_ISSUING_AUTHORITY)
            .issuingCountry(UPDATED_ISSUING_COUNTRY)
            .status(UPDATED_STATUS)
            .verified(UPDATED_VERIFIED)
            .verificationDate(UPDATED_VERIFICATION_DATE)
            .verificationComment(UPDATED_VERIFICATION_COMMENT);
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(updatedIdentityDocument);

        restIdentityDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, identityDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(identityDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIdentityDocumentToMatchAllProperties(updatedIdentityDocument);
    }

    @Test
    @Transactional
    void putNonExistingIdentityDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdentityDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, identityDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(identityDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIdentityDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(identityDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIdentityDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIdentityDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the identityDocument using partial update
        IdentityDocument partialUpdatedIdentityDocument = new IdentityDocument();
        partialUpdatedIdentityDocument.setId(identityDocument.getId());

        partialUpdatedIdentityDocument
            .issueDate(UPDATED_ISSUE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .issuingAuthority(UPDATED_ISSUING_AUTHORITY)
            .verified(UPDATED_VERIFIED);

        restIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdentityDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdentityDocument))
            )
            .andExpect(status().isOk());

        // Validate the IdentityDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdentityDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIdentityDocument, identityDocument),
            getPersistedIdentityDocument(identityDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateIdentityDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the identityDocument using partial update
        IdentityDocument partialUpdatedIdentityDocument = new IdentityDocument();
        partialUpdatedIdentityDocument.setId(identityDocument.getId());

        partialUpdatedIdentityDocument
            .documentType(UPDATED_DOCUMENT_TYPE)
            .documentNumber(UPDATED_DOCUMENT_NUMBER)
            .issueDate(UPDATED_ISSUE_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .issuingAuthority(UPDATED_ISSUING_AUTHORITY)
            .issuingCountry(UPDATED_ISSUING_COUNTRY)
            .status(UPDATED_STATUS)
            .verified(UPDATED_VERIFIED)
            .verificationDate(UPDATED_VERIFICATION_DATE)
            .verificationComment(UPDATED_VERIFICATION_COMMENT);

        restIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIdentityDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIdentityDocument))
            )
            .andExpect(status().isOk());

        // Validate the IdentityDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIdentityDocumentUpdatableFieldsEquals(
            partialUpdatedIdentityDocument,
            getPersistedIdentityDocument(partialUpdatedIdentityDocument)
        );
    }

    @Test
    @Transactional
    void patchNonExistingIdentityDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, identityDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(identityDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIdentityDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(identityDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIdentityDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        identityDocument.setId(longCount.incrementAndGet());

        // Create the IdentityDocument
        IdentityDocumentDTO identityDocumentDTO = identityDocumentMapper.toDto(identityDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIdentityDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(identityDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IdentityDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIdentityDocument() throws Exception {
        // Initialize the database
        insertedIdentityDocument = identityDocumentRepository.saveAndFlush(identityDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the identityDocument
        restIdentityDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, identityDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return identityDocumentRepository.count();
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

    protected IdentityDocument getPersistedIdentityDocument(IdentityDocument identityDocument) {
        return identityDocumentRepository.findById(identityDocument.getId()).orElseThrow();
    }

    protected void assertPersistedIdentityDocumentToMatchAllProperties(IdentityDocument expectedIdentityDocument) {
        assertIdentityDocumentAllPropertiesEquals(expectedIdentityDocument, getPersistedIdentityDocument(expectedIdentityDocument));
    }

    protected void assertPersistedIdentityDocumentToMatchUpdatableProperties(IdentityDocument expectedIdentityDocument) {
        assertIdentityDocumentAllUpdatablePropertiesEquals(
            expectedIdentityDocument,
            getPersistedIdentityDocument(expectedIdentityDocument)
        );
    }
}
