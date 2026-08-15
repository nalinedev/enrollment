package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.MemberDocumentAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.AppUser;
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.domain.MemberDocument;
import com.naline.coopfull.domain.enumeration.DocumentVerificationStatus;
import com.naline.coopfull.domain.enumeration.MemberDocumentType;
import com.naline.coopfull.repository.MemberDocumentRepository;
import com.naline.coopfull.service.MemberDocumentService;
import com.naline.coopfull.service.dto.MemberDocumentDTO;
import com.naline.coopfull.service.mapper.MemberDocumentMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link MemberDocumentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MemberDocumentResourceIT {

    private static final MemberDocumentType DEFAULT_DOCUMENT_TYPE = MemberDocumentType.IDENTITY_CARD;
    private static final MemberDocumentType UPDATED_DOCUMENT_TYPE = MemberDocumentType.PASSPORT;

    private static final String DEFAULT_ORIGINAL_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STORED_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STORED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;
    private static final Long SMALLER_FILE_SIZE = 1L - 1L;

    private static final String DEFAULT_STORAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_CHECKSUM = "AAAAAAAAAA";
    private static final String UPDATED_CHECKSUM = "BBBBBBBBBB";

    private static final DocumentVerificationStatus DEFAULT_VERIFICATION_STATUS = DocumentVerificationStatus.PENDING;
    private static final DocumentVerificationStatus UPDATED_VERIFICATION_STATUS = DocumentVerificationStatus.VERIFIED;

    private static final Instant DEFAULT_UPLOADED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOADED_AT = Instant.ofEpochMilli(1786816059661L);

    private static final Instant DEFAULT_VERIFIED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VERIFIED_AT = Instant.ofEpochMilli(1786816059661L);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/member-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MemberDocumentRepository memberDocumentRepository;

    @Mock
    private MemberDocumentRepository memberDocumentRepositoryMock;

    @Autowired
    private MemberDocumentMapper memberDocumentMapper;

    @Mock
    private MemberDocumentService memberDocumentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberDocumentMockMvc;

    private MemberDocument memberDocument;

    private MemberDocument insertedMemberDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberDocument createEntity() {
        return new MemberDocument()
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .originalFileName(DEFAULT_ORIGINAL_FILE_NAME)
            .storedFileName(DEFAULT_STORED_FILE_NAME)
            .contentType(DEFAULT_CONTENT_TYPE)
            .fileSize(DEFAULT_FILE_SIZE)
            .storagePath(DEFAULT_STORAGE_PATH)
            .checksum(DEFAULT_CHECKSUM)
            .verificationStatus(DEFAULT_VERIFICATION_STATUS)
            .uploadedAt(DEFAULT_UPLOADED_AT)
            .verifiedAt(DEFAULT_VERIFIED_AT)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberDocument createUpdatedEntity() {
        return new MemberDocument()
            .documentType(UPDATED_DOCUMENT_TYPE)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .storedFileName(UPDATED_STORED_FILE_NAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .storagePath(UPDATED_STORAGE_PATH)
            .checksum(UPDATED_CHECKSUM)
            .verificationStatus(UPDATED_VERIFICATION_STATUS)
            .uploadedAt(UPDATED_UPLOADED_AT)
            .verifiedAt(UPDATED_VERIFIED_AT)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    void initTest() {
        memberDocument = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMemberDocument != null) {
            memberDocumentRepository.delete(insertedMemberDocument);
            insertedMemberDocument = null;
        }
    }

    @Test
    @Transactional
    void createMemberDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MemberDocument
        MemberDocumentDTO memberDocumentDTO = memberDocumentMapper.toDto(memberDocument);
        var returnedMemberDocumentDTO = om.readValue(
            restMemberDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDocumentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MemberDocumentDTO.class
        );

        // Validate the MemberDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMemberDocument = memberDocumentMapper.toEntity(returnedMemberDocumentDTO);
        assertMemberDocumentUpdatableFieldsEquals(returnedMemberDocument, getPersistedMemberDocument(returnedMemberDocument));

        insertedMemberDocument = returnedMemberDocument;
    }

    @Test
    @Transactional
    void createMemberDocumentWithExistingId() throws Exception {
        // Create the MemberDocument with an existing ID
        memberDocument.setId(1L);
        MemberDocumentDTO memberDocumentDTO = memberDocumentMapper.toDto(memberDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MemberDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        memberDocument.setDocumentType(null);

        // Create the MemberDocument, which fails.
        MemberDocumentDTO memberDocumentDTO = memberDocumentMapper.toDto(memberDocument);

        restMemberDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVerificationStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        memberDocument.setVerificationStatus(null);

        // Create the MemberDocument, which fails.
        MemberDocumentDTO memberDocumentDTO = memberDocumentMapper.toDto(memberDocument);

        restMemberDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUploadedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        memberDocument.setUploadedAt(null);

        // Create the MemberDocument, which fails.
        MemberDocumentDTO memberDocumentDTO = memberDocumentMapper.toDto(memberDocument);

        restMemberDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMemberDocuments() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList
        restMemberDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].originalFileName").value(hasItem(DEFAULT_ORIGINAL_FILE_NAME)))
            .andExpect(jsonPath("$.[*].storedFileName").value(hasItem(DEFAULT_STORED_FILE_NAME)))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].storagePath").value(hasItem(DEFAULT_STORAGE_PATH)))
            .andExpect(jsonPath("$.[*].checksum").value(hasItem(DEFAULT_CHECKSUM)))
            .andExpect(jsonPath("$.[*].verificationStatus").value(hasItem(DEFAULT_VERIFICATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].uploadedAt").value(hasItem(DEFAULT_UPLOADED_AT.toString())))
            .andExpect(jsonPath("$.[*].verifiedAt").value(hasItem(DEFAULT_VERIFIED_AT.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMemberDocumentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(memberDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMemberDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(memberDocumentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMemberDocumentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(memberDocumentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMemberDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(memberDocumentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMemberDocument() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get the memberDocument
        restMemberDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, memberDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memberDocument.getId().intValue()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE.toString()))
            .andExpect(jsonPath("$.originalFileName").value(DEFAULT_ORIGINAL_FILE_NAME))
            .andExpect(jsonPath("$.storedFileName").value(DEFAULT_STORED_FILE_NAME))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.storagePath").value(DEFAULT_STORAGE_PATH))
            .andExpect(jsonPath("$.checksum").value(DEFAULT_CHECKSUM))
            .andExpect(jsonPath("$.verificationStatus").value(DEFAULT_VERIFICATION_STATUS.toString()))
            .andExpect(jsonPath("$.uploadedAt").value(DEFAULT_UPLOADED_AT.toString()))
            .andExpect(jsonPath("$.verifiedAt").value(DEFAULT_VERIFIED_AT.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getMemberDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        Long id = memberDocument.getId();

        defaultMemberDocumentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMemberDocumentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMemberDocumentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByDocumentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where documentType equals to
        defaultMemberDocumentFiltering("documentType.equals=" + DEFAULT_DOCUMENT_TYPE, "documentType.equals=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByDocumentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where documentType in
        defaultMemberDocumentFiltering(
            "documentType.in=" + DEFAULT_DOCUMENT_TYPE + "," + UPDATED_DOCUMENT_TYPE,
            "documentType.in=" + UPDATED_DOCUMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByDocumentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where documentType is not null
        defaultMemberDocumentFiltering("documentType.specified=true", "documentType.specified=false");
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByOriginalFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where originalFileName equals to
        defaultMemberDocumentFiltering(
            "originalFileName.equals=" + DEFAULT_ORIGINAL_FILE_NAME,
            "originalFileName.equals=" + UPDATED_ORIGINAL_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByOriginalFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where originalFileName in
        defaultMemberDocumentFiltering(
            "originalFileName.in=" + DEFAULT_ORIGINAL_FILE_NAME + "," + UPDATED_ORIGINAL_FILE_NAME,
            "originalFileName.in=" + UPDATED_ORIGINAL_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByOriginalFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where originalFileName is not null
        defaultMemberDocumentFiltering("originalFileName.specified=true", "originalFileName.specified=false");
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByOriginalFileNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where originalFileName contains
        defaultMemberDocumentFiltering(
            "originalFileName.contains=" + DEFAULT_ORIGINAL_FILE_NAME,
            "originalFileName.contains=" + UPDATED_ORIGINAL_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByOriginalFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where originalFileName does not contain
        defaultMemberDocumentFiltering(
            "originalFileName.doesNotContain=" + UPDATED_ORIGINAL_FILE_NAME,
            "originalFileName.doesNotContain=" + DEFAULT_ORIGINAL_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByStoredFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where storedFileName equals to
        defaultMemberDocumentFiltering(
            "storedFileName.equals=" + DEFAULT_STORED_FILE_NAME,
            "storedFileName.equals=" + UPDATED_STORED_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByStoredFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where storedFileName in
        defaultMemberDocumentFiltering(
            "storedFileName.in=" + DEFAULT_STORED_FILE_NAME + "," + UPDATED_STORED_FILE_NAME,
            "storedFileName.in=" + UPDATED_STORED_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByStoredFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where storedFileName is not null
        defaultMemberDocumentFiltering("storedFileName.specified=true", "storedFileName.specified=false");
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByStoredFileNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where storedFileName contains
        defaultMemberDocumentFiltering(
            "storedFileName.contains=" + DEFAULT_STORED_FILE_NAME,
            "storedFileName.contains=" + UPDATED_STORED_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByStoredFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where storedFileName does not contain
        defaultMemberDocumentFiltering(
            "storedFileName.doesNotContain=" + UPDATED_STORED_FILE_NAME,
            "storedFileName.doesNotContain=" + DEFAULT_STORED_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByContentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where contentType equals to
        defaultMemberDocumentFiltering("contentType.equals=" + DEFAULT_CONTENT_TYPE, "contentType.equals=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByContentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where contentType in
        defaultMemberDocumentFiltering(
            "contentType.in=" + DEFAULT_CONTENT_TYPE + "," + UPDATED_CONTENT_TYPE,
            "contentType.in=" + UPDATED_CONTENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByContentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where contentType is not null
        defaultMemberDocumentFiltering("contentType.specified=true", "contentType.specified=false");
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByContentTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where contentType contains
        defaultMemberDocumentFiltering("contentType.contains=" + DEFAULT_CONTENT_TYPE, "contentType.contains=" + UPDATED_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByContentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where contentType does not contain
        defaultMemberDocumentFiltering(
            "contentType.doesNotContain=" + UPDATED_CONTENT_TYPE,
            "contentType.doesNotContain=" + DEFAULT_CONTENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where fileSize equals to
        defaultMemberDocumentFiltering("fileSize.equals=" + DEFAULT_FILE_SIZE, "fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where fileSize in
        defaultMemberDocumentFiltering("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE, "fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where fileSize is not null
        defaultMemberDocumentFiltering("fileSize.specified=true", "fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where fileSize is greater than or equal to
        defaultMemberDocumentFiltering(
            "fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE,
            "fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where fileSize is less than or equal to
        defaultMemberDocumentFiltering("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where fileSize is less than
        defaultMemberDocumentFiltering("fileSize.lessThan=" + UPDATED_FILE_SIZE, "fileSize.lessThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where fileSize is greater than
        defaultMemberDocumentFiltering("fileSize.greaterThan=" + SMALLER_FILE_SIZE, "fileSize.greaterThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByStoragePathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where storagePath equals to
        defaultMemberDocumentFiltering("storagePath.equals=" + DEFAULT_STORAGE_PATH, "storagePath.equals=" + UPDATED_STORAGE_PATH);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByStoragePathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where storagePath in
        defaultMemberDocumentFiltering(
            "storagePath.in=" + DEFAULT_STORAGE_PATH + "," + UPDATED_STORAGE_PATH,
            "storagePath.in=" + UPDATED_STORAGE_PATH
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByStoragePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where storagePath is not null
        defaultMemberDocumentFiltering("storagePath.specified=true", "storagePath.specified=false");
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByStoragePathContainsSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where storagePath contains
        defaultMemberDocumentFiltering("storagePath.contains=" + DEFAULT_STORAGE_PATH, "storagePath.contains=" + UPDATED_STORAGE_PATH);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByStoragePathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where storagePath does not contain
        defaultMemberDocumentFiltering(
            "storagePath.doesNotContain=" + UPDATED_STORAGE_PATH,
            "storagePath.doesNotContain=" + DEFAULT_STORAGE_PATH
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByChecksumIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where checksum equals to
        defaultMemberDocumentFiltering("checksum.equals=" + DEFAULT_CHECKSUM, "checksum.equals=" + UPDATED_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByChecksumIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where checksum in
        defaultMemberDocumentFiltering("checksum.in=" + DEFAULT_CHECKSUM + "," + UPDATED_CHECKSUM, "checksum.in=" + UPDATED_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByChecksumIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where checksum is not null
        defaultMemberDocumentFiltering("checksum.specified=true", "checksum.specified=false");
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByChecksumContainsSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where checksum contains
        defaultMemberDocumentFiltering("checksum.contains=" + DEFAULT_CHECKSUM, "checksum.contains=" + UPDATED_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByChecksumNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where checksum does not contain
        defaultMemberDocumentFiltering("checksum.doesNotContain=" + UPDATED_CHECKSUM, "checksum.doesNotContain=" + DEFAULT_CHECKSUM);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByVerificationStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where verificationStatus equals to
        defaultMemberDocumentFiltering(
            "verificationStatus.equals=" + DEFAULT_VERIFICATION_STATUS,
            "verificationStatus.equals=" + UPDATED_VERIFICATION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByVerificationStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where verificationStatus in
        defaultMemberDocumentFiltering(
            "verificationStatus.in=" + DEFAULT_VERIFICATION_STATUS + "," + UPDATED_VERIFICATION_STATUS,
            "verificationStatus.in=" + UPDATED_VERIFICATION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByVerificationStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where verificationStatus is not null
        defaultMemberDocumentFiltering("verificationStatus.specified=true", "verificationStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByUploadedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where uploadedAt equals to
        defaultMemberDocumentFiltering("uploadedAt.equals=" + DEFAULT_UPLOADED_AT, "uploadedAt.equals=" + UPDATED_UPLOADED_AT);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByUploadedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where uploadedAt in
        defaultMemberDocumentFiltering(
            "uploadedAt.in=" + DEFAULT_UPLOADED_AT + "," + UPDATED_UPLOADED_AT,
            "uploadedAt.in=" + UPDATED_UPLOADED_AT
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByUploadedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where uploadedAt is not null
        defaultMemberDocumentFiltering("uploadedAt.specified=true", "uploadedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByVerifiedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where verifiedAt equals to
        defaultMemberDocumentFiltering("verifiedAt.equals=" + DEFAULT_VERIFIED_AT, "verifiedAt.equals=" + UPDATED_VERIFIED_AT);
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByVerifiedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where verifiedAt in
        defaultMemberDocumentFiltering(
            "verifiedAt.in=" + DEFAULT_VERIFIED_AT + "," + UPDATED_VERIFIED_AT,
            "verifiedAt.in=" + UPDATED_VERIFIED_AT
        );
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByVerifiedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        // Get all the memberDocumentList where verifiedAt is not null
        defaultMemberDocumentFiltering("verifiedAt.specified=true", "verifiedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByMemberIsEqualToSomething() throws Exception {
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            memberDocumentRepository.saveAndFlush(memberDocument);
            member = MemberResourceIT.createEntity();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        em.persist(member);
        em.flush();
        memberDocument.setMember(member);
        memberDocumentRepository.saveAndFlush(memberDocument);
        Long memberId = member.getId();
        // Get all the memberDocumentList where member equals to memberId
        defaultMemberDocumentShouldBeFound("memberId.equals=" + memberId);

        // Get all the memberDocumentList where member equals to (memberId + 1)
        defaultMemberDocumentShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    @Test
    @Transactional
    void getAllMemberDocumentsByUploadedByIsEqualToSomething() throws Exception {
        AppUser uploadedBy;
        if (TestUtil.findAll(em, AppUser.class).isEmpty()) {
            memberDocumentRepository.saveAndFlush(memberDocument);
            uploadedBy = AppUserResourceIT.createEntity();
        } else {
            uploadedBy = TestUtil.findAll(em, AppUser.class).get(0);
        }
        em.persist(uploadedBy);
        em.flush();
        memberDocument.setUploadedBy(uploadedBy);
        memberDocumentRepository.saveAndFlush(memberDocument);
        Long uploadedById = uploadedBy.getId();
        // Get all the memberDocumentList where uploadedBy equals to uploadedById
        defaultMemberDocumentShouldBeFound("uploadedById.equals=" + uploadedById);

        // Get all the memberDocumentList where uploadedBy equals to (uploadedById + 1)
        defaultMemberDocumentShouldNotBeFound("uploadedById.equals=" + (uploadedById + 1));
    }

    private void defaultMemberDocumentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMemberDocumentShouldBeFound(shouldBeFound);
        defaultMemberDocumentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemberDocumentShouldBeFound(String filter) throws Exception {
        restMemberDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].originalFileName").value(hasItem(DEFAULT_ORIGINAL_FILE_NAME)))
            .andExpect(jsonPath("$.[*].storedFileName").value(hasItem(DEFAULT_STORED_FILE_NAME)))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].storagePath").value(hasItem(DEFAULT_STORAGE_PATH)))
            .andExpect(jsonPath("$.[*].checksum").value(hasItem(DEFAULT_CHECKSUM)))
            .andExpect(jsonPath("$.[*].verificationStatus").value(hasItem(DEFAULT_VERIFICATION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].uploadedAt").value(hasItem(DEFAULT_UPLOADED_AT.toString())))
            .andExpect(jsonPath("$.[*].verifiedAt").value(hasItem(DEFAULT_VERIFIED_AT.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restMemberDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemberDocumentShouldNotBeFound(String filter) throws Exception {
        restMemberDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemberDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMemberDocument() throws Exception {
        // Get the memberDocument
        restMemberDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMemberDocument() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memberDocument
        MemberDocument updatedMemberDocument = memberDocumentRepository.findById(memberDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMemberDocument are not directly saved in db
        em.detach(updatedMemberDocument);
        updatedMemberDocument
            .documentType(UPDATED_DOCUMENT_TYPE)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .storedFileName(UPDATED_STORED_FILE_NAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .storagePath(UPDATED_STORAGE_PATH)
            .checksum(UPDATED_CHECKSUM)
            .verificationStatus(UPDATED_VERIFICATION_STATUS)
            .uploadedAt(UPDATED_UPLOADED_AT)
            .verifiedAt(UPDATED_VERIFIED_AT)
            .notes(UPDATED_NOTES);
        MemberDocumentDTO memberDocumentDTO = memberDocumentMapper.toDto(updatedMemberDocument);

        restMemberDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the MemberDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMemberDocumentToMatchAllProperties(updatedMemberDocument);
    }

    @Test
    @Transactional
    void putNonExistingMemberDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberDocument.setId(longCount.incrementAndGet());

        // Create the MemberDocument
        MemberDocumentDTO memberDocumentDTO = memberDocumentMapper.toDto(memberDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMemberDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberDocument.setId(longCount.incrementAndGet());

        // Create the MemberDocument
        MemberDocumentDTO memberDocumentDTO = memberDocumentMapper.toDto(memberDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMemberDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberDocument.setId(longCount.incrementAndGet());

        // Create the MemberDocument
        MemberDocumentDTO memberDocumentDTO = memberDocumentMapper.toDto(memberDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MemberDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMemberDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memberDocument using partial update
        MemberDocument partialUpdatedMemberDocument = new MemberDocument();
        partialUpdatedMemberDocument.setId(memberDocument.getId());

        partialUpdatedMemberDocument
            .documentType(UPDATED_DOCUMENT_TYPE)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .storedFileName(UPDATED_STORED_FILE_NAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .storagePath(UPDATED_STORAGE_PATH)
            .checksum(UPDATED_CHECKSUM)
            .verificationStatus(UPDATED_VERIFICATION_STATUS)
            .uploadedAt(UPDATED_UPLOADED_AT);

        restMemberDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemberDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMemberDocument))
            )
            .andExpect(status().isOk());

        // Validate the MemberDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemberDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMemberDocument, memberDocument),
            getPersistedMemberDocument(memberDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateMemberDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the memberDocument using partial update
        MemberDocument partialUpdatedMemberDocument = new MemberDocument();
        partialUpdatedMemberDocument.setId(memberDocument.getId());

        partialUpdatedMemberDocument
            .documentType(UPDATED_DOCUMENT_TYPE)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .storedFileName(UPDATED_STORED_FILE_NAME)
            .contentType(UPDATED_CONTENT_TYPE)
            .fileSize(UPDATED_FILE_SIZE)
            .storagePath(UPDATED_STORAGE_PATH)
            .checksum(UPDATED_CHECKSUM)
            .verificationStatus(UPDATED_VERIFICATION_STATUS)
            .uploadedAt(UPDATED_UPLOADED_AT)
            .verifiedAt(UPDATED_VERIFIED_AT)
            .notes(UPDATED_NOTES);

        restMemberDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemberDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMemberDocument))
            )
            .andExpect(status().isOk());

        // Validate the MemberDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemberDocumentUpdatableFieldsEquals(partialUpdatedMemberDocument, getPersistedMemberDocument(partialUpdatedMemberDocument));
    }

    @Test
    @Transactional
    void patchNonExistingMemberDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberDocument.setId(longCount.incrementAndGet());

        // Create the MemberDocument
        MemberDocumentDTO memberDocumentDTO = memberDocumentMapper.toDto(memberDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, memberDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memberDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMemberDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberDocument.setId(longCount.incrementAndGet());

        // Create the MemberDocument
        MemberDocumentDTO memberDocumentDTO = memberDocumentMapper.toDto(memberDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memberDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMemberDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        memberDocument.setId(longCount.incrementAndGet());

        // Create the MemberDocument
        MemberDocumentDTO memberDocumentDTO = memberDocumentMapper.toDto(memberDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(memberDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MemberDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMemberDocument() throws Exception {
        // Initialize the database
        insertedMemberDocument = memberDocumentRepository.saveAndFlush(memberDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the memberDocument
        restMemberDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, memberDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return memberDocumentRepository.count();
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

    protected MemberDocument getPersistedMemberDocument(MemberDocument memberDocument) {
        return memberDocumentRepository.findById(memberDocument.getId()).orElseThrow();
    }

    protected void assertPersistedMemberDocumentToMatchAllProperties(MemberDocument expectedMemberDocument) {
        assertMemberDocumentAllPropertiesEquals(expectedMemberDocument, getPersistedMemberDocument(expectedMemberDocument));
    }

    protected void assertPersistedMemberDocumentToMatchUpdatableProperties(MemberDocument expectedMemberDocument) {
        assertMemberDocumentAllUpdatablePropertiesEquals(expectedMemberDocument, getPersistedMemberDocument(expectedMemberDocument));
    }
}
