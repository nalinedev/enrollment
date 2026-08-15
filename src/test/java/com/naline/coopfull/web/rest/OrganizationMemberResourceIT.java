package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.OrganizationMemberAsserts.*;
import static com.naline.coopfull.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naline.coopfull.IntegrationTest;
import com.naline.coopfull.domain.OrganizationMember;
import com.naline.coopfull.repository.OrganizationMemberRepository;
import com.naline.coopfull.service.dto.OrganizationMemberDTO;
import com.naline.coopfull.service.mapper.OrganizationMemberMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
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
 * Integration tests for the {@link OrganizationMemberResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganizationMemberResourceIT {

    private static final String DEFAULT_LEGAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TRADE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TAX_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_LEGAL_FORM = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_FORM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGISTRATION_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_REGISTRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organization-members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    @Autowired
    private OrganizationMemberMapper organizationMemberMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationMemberMockMvc;

    private OrganizationMember organizationMember;

    private OrganizationMember insertedOrganizationMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationMember createEntity() {
        return new OrganizationMember()
            .legalName(DEFAULT_LEGAL_NAME)
            .tradeName(DEFAULT_TRADE_NAME)
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER)
            .taxNumber(DEFAULT_TAX_NUMBER)
            .legalForm(DEFAULT_LEGAL_FORM)
            .registrationDate(DEFAULT_REGISTRATION_DATE)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .website(DEFAULT_WEBSITE)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationMember createUpdatedEntity() {
        return new OrganizationMember()
            .legalName(UPDATED_LEGAL_NAME)
            .tradeName(UPDATED_TRADE_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .taxNumber(UPDATED_TAX_NUMBER)
            .legalForm(UPDATED_LEGAL_FORM)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .website(UPDATED_WEBSITE)
            .description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        organizationMember = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOrganizationMember != null) {
            organizationMemberRepository.delete(insertedOrganizationMember);
            insertedOrganizationMember = null;
        }
    }

    @Test
    @Transactional
    void createOrganizationMember() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OrganizationMember
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);
        var returnedOrganizationMemberDTO = om.readValue(
            restOrganizationMemberMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organizationMemberDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrganizationMemberDTO.class
        );

        // Validate the OrganizationMember in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrganizationMember = organizationMemberMapper.toEntity(returnedOrganizationMemberDTO);
        assertOrganizationMemberUpdatableFieldsEquals(
            returnedOrganizationMember,
            getPersistedOrganizationMember(returnedOrganizationMember)
        );

        insertedOrganizationMember = returnedOrganizationMember;
    }

    @Test
    @Transactional
    void createOrganizationMemberWithExistingId() throws Exception {
        // Create the OrganizationMember with an existing ID
        organizationMember.setId(1L);
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organizationMemberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrganizationMember in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLegalNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        organizationMember.setLegalName(null);

        // Create the OrganizationMember, which fails.
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);

        restOrganizationMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organizationMemberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganizationMembers() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList
        restOrganizationMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].legalName").value(hasItem(DEFAULT_LEGAL_NAME)))
            .andExpect(jsonPath("$.[*].tradeName").value(hasItem(DEFAULT_TRADE_NAME)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].legalForm").value(hasItem(DEFAULT_LEGAL_FORM)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getOrganizationMember() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get the organizationMember
        restOrganizationMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, organizationMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organizationMember.getId().intValue()))
            .andExpect(jsonPath("$.legalName").value(DEFAULT_LEGAL_NAME))
            .andExpect(jsonPath("$.tradeName").value(DEFAULT_TRADE_NAME))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER))
            .andExpect(jsonPath("$.taxNumber").value(DEFAULT_TAX_NUMBER))
            .andExpect(jsonPath("$.legalForm").value(DEFAULT_LEGAL_FORM))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getOrganizationMembersByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        Long id = organizationMember.getId();

        defaultOrganizationMemberFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrganizationMemberFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrganizationMemberFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByLegalNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where legalName equals to
        defaultOrganizationMemberFiltering("legalName.equals=" + DEFAULT_LEGAL_NAME, "legalName.equals=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByLegalNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where legalName in
        defaultOrganizationMemberFiltering(
            "legalName.in=" + DEFAULT_LEGAL_NAME + "," + UPDATED_LEGAL_NAME,
            "legalName.in=" + UPDATED_LEGAL_NAME
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByLegalNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where legalName is not null
        defaultOrganizationMemberFiltering("legalName.specified=true", "legalName.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByLegalNameContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where legalName contains
        defaultOrganizationMemberFiltering("legalName.contains=" + DEFAULT_LEGAL_NAME, "legalName.contains=" + UPDATED_LEGAL_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByLegalNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where legalName does not contain
        defaultOrganizationMemberFiltering(
            "legalName.doesNotContain=" + UPDATED_LEGAL_NAME,
            "legalName.doesNotContain=" + DEFAULT_LEGAL_NAME
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByTradeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where tradeName equals to
        defaultOrganizationMemberFiltering("tradeName.equals=" + DEFAULT_TRADE_NAME, "tradeName.equals=" + UPDATED_TRADE_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByTradeNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where tradeName in
        defaultOrganizationMemberFiltering(
            "tradeName.in=" + DEFAULT_TRADE_NAME + "," + UPDATED_TRADE_NAME,
            "tradeName.in=" + UPDATED_TRADE_NAME
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByTradeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where tradeName is not null
        defaultOrganizationMemberFiltering("tradeName.specified=true", "tradeName.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByTradeNameContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where tradeName contains
        defaultOrganizationMemberFiltering("tradeName.contains=" + DEFAULT_TRADE_NAME, "tradeName.contains=" + UPDATED_TRADE_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByTradeNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where tradeName does not contain
        defaultOrganizationMemberFiltering(
            "tradeName.doesNotContain=" + UPDATED_TRADE_NAME,
            "tradeName.doesNotContain=" + DEFAULT_TRADE_NAME
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByRegistrationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where registrationNumber equals to
        defaultOrganizationMemberFiltering(
            "registrationNumber.equals=" + DEFAULT_REGISTRATION_NUMBER,
            "registrationNumber.equals=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByRegistrationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where registrationNumber in
        defaultOrganizationMemberFiltering(
            "registrationNumber.in=" + DEFAULT_REGISTRATION_NUMBER + "," + UPDATED_REGISTRATION_NUMBER,
            "registrationNumber.in=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByRegistrationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where registrationNumber is not null
        defaultOrganizationMemberFiltering("registrationNumber.specified=true", "registrationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByRegistrationNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where registrationNumber contains
        defaultOrganizationMemberFiltering(
            "registrationNumber.contains=" + DEFAULT_REGISTRATION_NUMBER,
            "registrationNumber.contains=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByRegistrationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where registrationNumber does not contain
        defaultOrganizationMemberFiltering(
            "registrationNumber.doesNotContain=" + UPDATED_REGISTRATION_NUMBER,
            "registrationNumber.doesNotContain=" + DEFAULT_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByTaxNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where taxNumber equals to
        defaultOrganizationMemberFiltering("taxNumber.equals=" + DEFAULT_TAX_NUMBER, "taxNumber.equals=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByTaxNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where taxNumber in
        defaultOrganizationMemberFiltering(
            "taxNumber.in=" + DEFAULT_TAX_NUMBER + "," + UPDATED_TAX_NUMBER,
            "taxNumber.in=" + UPDATED_TAX_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByTaxNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where taxNumber is not null
        defaultOrganizationMemberFiltering("taxNumber.specified=true", "taxNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByTaxNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where taxNumber contains
        defaultOrganizationMemberFiltering("taxNumber.contains=" + DEFAULT_TAX_NUMBER, "taxNumber.contains=" + UPDATED_TAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByTaxNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where taxNumber does not contain
        defaultOrganizationMemberFiltering(
            "taxNumber.doesNotContain=" + UPDATED_TAX_NUMBER,
            "taxNumber.doesNotContain=" + DEFAULT_TAX_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByLegalFormIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where legalForm equals to
        defaultOrganizationMemberFiltering("legalForm.equals=" + DEFAULT_LEGAL_FORM, "legalForm.equals=" + UPDATED_LEGAL_FORM);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByLegalFormIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where legalForm in
        defaultOrganizationMemberFiltering(
            "legalForm.in=" + DEFAULT_LEGAL_FORM + "," + UPDATED_LEGAL_FORM,
            "legalForm.in=" + UPDATED_LEGAL_FORM
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByLegalFormIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where legalForm is not null
        defaultOrganizationMemberFiltering("legalForm.specified=true", "legalForm.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByLegalFormContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where legalForm contains
        defaultOrganizationMemberFiltering("legalForm.contains=" + DEFAULT_LEGAL_FORM, "legalForm.contains=" + UPDATED_LEGAL_FORM);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByLegalFormNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where legalForm does not contain
        defaultOrganizationMemberFiltering(
            "legalForm.doesNotContain=" + UPDATED_LEGAL_FORM,
            "legalForm.doesNotContain=" + DEFAULT_LEGAL_FORM
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByRegistrationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where registrationDate equals to
        defaultOrganizationMemberFiltering(
            "registrationDate.equals=" + DEFAULT_REGISTRATION_DATE,
            "registrationDate.equals=" + UPDATED_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByRegistrationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where registrationDate in
        defaultOrganizationMemberFiltering(
            "registrationDate.in=" + DEFAULT_REGISTRATION_DATE + "," + UPDATED_REGISTRATION_DATE,
            "registrationDate.in=" + UPDATED_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByRegistrationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where registrationDate is not null
        defaultOrganizationMemberFiltering("registrationDate.specified=true", "registrationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByRegistrationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where registrationDate is greater than or equal to
        defaultOrganizationMemberFiltering(
            "registrationDate.greaterThanOrEqual=" + DEFAULT_REGISTRATION_DATE,
            "registrationDate.greaterThanOrEqual=" + UPDATED_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByRegistrationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where registrationDate is less than or equal to
        defaultOrganizationMemberFiltering(
            "registrationDate.lessThanOrEqual=" + DEFAULT_REGISTRATION_DATE,
            "registrationDate.lessThanOrEqual=" + SMALLER_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByRegistrationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where registrationDate is less than
        defaultOrganizationMemberFiltering(
            "registrationDate.lessThan=" + UPDATED_REGISTRATION_DATE,
            "registrationDate.lessThan=" + DEFAULT_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByRegistrationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where registrationDate is greater than
        defaultOrganizationMemberFiltering(
            "registrationDate.greaterThan=" + SMALLER_REGISTRATION_DATE,
            "registrationDate.greaterThan=" + DEFAULT_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where email equals to
        defaultOrganizationMemberFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where email in
        defaultOrganizationMemberFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where email is not null
        defaultOrganizationMemberFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where email contains
        defaultOrganizationMemberFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where email does not contain
        defaultOrganizationMemberFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where phoneNumber equals to
        defaultOrganizationMemberFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where phoneNumber in
        defaultOrganizationMemberFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where phoneNumber is not null
        defaultOrganizationMemberFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where phoneNumber contains
        defaultOrganizationMemberFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where phoneNumber does not contain
        defaultOrganizationMemberFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where website equals to
        defaultOrganizationMemberFiltering("website.equals=" + DEFAULT_WEBSITE, "website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where website in
        defaultOrganizationMemberFiltering("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE, "website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where website is not null
        defaultOrganizationMemberFiltering("website.specified=true", "website.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where website contains
        defaultOrganizationMemberFiltering("website.contains=" + DEFAULT_WEBSITE, "website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllOrganizationMembersByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        // Get all the organizationMemberList where website does not contain
        defaultOrganizationMemberFiltering("website.doesNotContain=" + UPDATED_WEBSITE, "website.doesNotContain=" + DEFAULT_WEBSITE);
    }

    private void defaultOrganizationMemberFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrganizationMemberShouldBeFound(shouldBeFound);
        defaultOrganizationMemberShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganizationMemberShouldBeFound(String filter) throws Exception {
        restOrganizationMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].legalName").value(hasItem(DEFAULT_LEGAL_NAME)))
            .andExpect(jsonPath("$.[*].tradeName").value(hasItem(DEFAULT_TRADE_NAME)))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].taxNumber").value(hasItem(DEFAULT_TAX_NUMBER)))
            .andExpect(jsonPath("$.[*].legalForm").value(hasItem(DEFAULT_LEGAL_FORM)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restOrganizationMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganizationMemberShouldNotBeFound(String filter) throws Exception {
        restOrganizationMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganizationMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrganizationMember() throws Exception {
        // Get the organizationMember
        restOrganizationMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrganizationMember() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organizationMember
        OrganizationMember updatedOrganizationMember = organizationMemberRepository.findById(organizationMember.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrganizationMember are not directly saved in db
        em.detach(updatedOrganizationMember);
        updatedOrganizationMember
            .legalName(UPDATED_LEGAL_NAME)
            .tradeName(UPDATED_TRADE_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .taxNumber(UPDATED_TAX_NUMBER)
            .legalForm(UPDATED_LEGAL_FORM)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .website(UPDATED_WEBSITE)
            .description(UPDATED_DESCRIPTION);
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(updatedOrganizationMember);

        restOrganizationMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organizationMemberDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrganizationMemberToMatchAllProperties(updatedOrganizationMember);
    }

    @Test
    @Transactional
    void putNonExistingOrganizationMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organizationMember.setId(longCount.incrementAndGet());

        // Create the OrganizationMember
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationMemberDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organizationMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganizationMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organizationMember.setId(longCount.incrementAndGet());

        // Create the OrganizationMember
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(organizationMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganizationMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organizationMember.setId(longCount.incrementAndGet());

        // Create the OrganizationMember
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(organizationMemberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganizationMemberWithPatch() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organizationMember using partial update
        OrganizationMember partialUpdatedOrganizationMember = new OrganizationMember();
        partialUpdatedOrganizationMember.setId(organizationMember.getId());

        partialUpdatedOrganizationMember
            .tradeName(UPDATED_TRADE_NAME)
            .legalForm(UPDATED_LEGAL_FORM)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .website(UPDATED_WEBSITE);

        restOrganizationMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrganizationMember))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrganizationMemberUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrganizationMember, organizationMember),
            getPersistedOrganizationMember(organizationMember)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrganizationMemberWithPatch() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the organizationMember using partial update
        OrganizationMember partialUpdatedOrganizationMember = new OrganizationMember();
        partialUpdatedOrganizationMember.setId(organizationMember.getId());

        partialUpdatedOrganizationMember
            .legalName(UPDATED_LEGAL_NAME)
            .tradeName(UPDATED_TRADE_NAME)
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .taxNumber(UPDATED_TAX_NUMBER)
            .legalForm(UPDATED_LEGAL_FORM)
            .registrationDate(UPDATED_REGISTRATION_DATE)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .website(UPDATED_WEBSITE)
            .description(UPDATED_DESCRIPTION);

        restOrganizationMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrganizationMember))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationMember in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrganizationMemberUpdatableFieldsEquals(
            partialUpdatedOrganizationMember,
            getPersistedOrganizationMember(partialUpdatedOrganizationMember)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOrganizationMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organizationMember.setId(longCount.incrementAndGet());

        // Create the OrganizationMember
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organizationMemberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(organizationMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganizationMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organizationMember.setId(longCount.incrementAndGet());

        // Create the OrganizationMember
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(organizationMemberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganizationMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        organizationMember.setId(longCount.incrementAndGet());

        // Create the OrganizationMember
        OrganizationMemberDTO organizationMemberDTO = organizationMemberMapper.toDto(organizationMember);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMemberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(organizationMemberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationMember in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganizationMember() throws Exception {
        // Initialize the database
        insertedOrganizationMember = organizationMemberRepository.saveAndFlush(organizationMember);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the organizationMember
        restOrganizationMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, organizationMember.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return organizationMemberRepository.count();
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

    protected OrganizationMember getPersistedOrganizationMember(OrganizationMember organizationMember) {
        return organizationMemberRepository.findById(organizationMember.getId()).orElseThrow();
    }

    protected void assertPersistedOrganizationMemberToMatchAllProperties(OrganizationMember expectedOrganizationMember) {
        assertOrganizationMemberAllPropertiesEquals(expectedOrganizationMember, getPersistedOrganizationMember(expectedOrganizationMember));
    }

    protected void assertPersistedOrganizationMemberToMatchUpdatableProperties(OrganizationMember expectedOrganizationMember) {
        assertOrganizationMemberAllUpdatablePropertiesEquals(
            expectedOrganizationMember,
            getPersistedOrganizationMember(expectedOrganizationMember)
        );
    }
}
