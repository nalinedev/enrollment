package com.naline.coopfull.web.rest;

import static com.naline.coopfull.domain.MemberAsserts.*;
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
import com.naline.coopfull.domain.IndividualMember;
import com.naline.coopfull.domain.Member;
import com.naline.coopfull.domain.OrganizationMember;
import com.naline.coopfull.domain.ProfessionalProfile;
import com.naline.coopfull.domain.SocialProfile;
import com.naline.coopfull.domain.enumeration.MemberStatus;
import com.naline.coopfull.domain.enumeration.MemberType;
import com.naline.coopfull.repository.MemberRepository;
import com.naline.coopfull.service.MemberService;
import com.naline.coopfull.service.dto.MemberDTO;
import com.naline.coopfull.service.mapper.MemberMapper;
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
 * Integration tests for the {@link MemberResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MemberResourceIT {

    private static final String DEFAULT_MEMBER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MEMBER_NUMBER = "BBBBBBBBBB";

    private static final MemberType DEFAULT_MEMBER_TYPE = MemberType.INDIVIDUAL;
    private static final MemberType UPDATED_MEMBER_TYPE = MemberType.ORGANIZATION;

    private static final MemberStatus DEFAULT_STATUS = MemberStatus.PENDING;
    private static final MemberStatus UPDATED_STATUS = MemberStatus.ACTIVE;

    private static final LocalDate DEFAULT_ADMISSION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ADMISSION_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_ADMISSION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_EXIT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXIT_DATE = LocalDate.parse("2026-08-15");
    private static final LocalDate SMALLER_EXIT_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_EXIT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_EXIT_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1786816059661L);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1786816059661L);

    private static final String ENTITY_API_URL = "/api/members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MemberRepository memberRepository;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Autowired
    private MemberMapper memberMapper;

    @Mock
    private MemberService memberServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberMockMvc;

    private Member member;

    private Member insertedMember;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createEntity() {
        return new Member()
            .memberNumber(DEFAULT_MEMBER_NUMBER)
            .memberType(DEFAULT_MEMBER_TYPE)
            .status(DEFAULT_STATUS)
            .admissionDate(DEFAULT_ADMISSION_DATE)
            .exitDate(DEFAULT_EXIT_DATE)
            .exitReason(DEFAULT_EXIT_REASON)
            .notes(DEFAULT_NOTES)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createUpdatedEntity() {
        return new Member()
            .memberNumber(UPDATED_MEMBER_NUMBER)
            .memberType(UPDATED_MEMBER_TYPE)
            .status(UPDATED_STATUS)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .exitDate(UPDATED_EXIT_DATE)
            .exitReason(UPDATED_EXIT_REASON)
            .notes(UPDATED_NOTES)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        member = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMember != null) {
            memberRepository.delete(insertedMember);
            insertedMember = null;
        }
    }

    @Test
    @Transactional
    void createMember() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);
        var returnedMemberDTO = om.readValue(
            restMemberMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MemberDTO.class
        );

        // Validate the Member in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMember = memberMapper.toEntity(returnedMemberDTO);
        assertMemberUpdatableFieldsEquals(returnedMember, getPersistedMember(returnedMember));

        insertedMember = returnedMember;
    }

    @Test
    @Transactional
    void createMemberWithExistingId() throws Exception {
        // Create the Member with an existing ID
        member.setId(1L);
        MemberDTO memberDTO = memberMapper.toDto(member);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMemberNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        member.setMemberNumber(null);

        // Create the Member, which fails.
        MemberDTO memberDTO = memberMapper.toDto(member);

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMemberTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        member.setMemberType(null);

        // Create the Member, which fails.
        MemberDTO memberDTO = memberMapper.toDto(member);

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        member.setStatus(null);

        // Create the Member, which fails.
        MemberDTO memberDTO = memberMapper.toDto(member);

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        member.setCreatedDate(null);

        // Create the Member, which fails.
        MemberDTO memberDTO = memberMapper.toDto(member);

        restMemberMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMembers() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].memberNumber").value(hasItem(DEFAULT_MEMBER_NUMBER)))
            .andExpect(jsonPath("$.[*].memberType").value(hasItem(DEFAULT_MEMBER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].admissionDate").value(hasItem(DEFAULT_ADMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].exitDate").value(hasItem(DEFAULT_EXIT_DATE.toString())))
            .andExpect(jsonPath("$.[*].exitReason").value(hasItem(DEFAULT_EXIT_REASON)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMembersWithEagerRelationshipsIsEnabled() throws Exception {
        when(memberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(memberServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMembersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(memberServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMemberMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(memberRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMember() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get the member
        restMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.memberNumber").value(DEFAULT_MEMBER_NUMBER))
            .andExpect(jsonPath("$.memberType").value(DEFAULT_MEMBER_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.admissionDate").value(DEFAULT_ADMISSION_DATE.toString()))
            .andExpect(jsonPath("$.exitDate").value(DEFAULT_EXIT_DATE.toString()))
            .andExpect(jsonPath("$.exitReason").value(DEFAULT_EXIT_REASON))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMembersByIdFiltering() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        Long id = member.getId();

        defaultMemberFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMemberFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMemberFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMembersByMemberNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where memberNumber equals to
        defaultMemberFiltering("memberNumber.equals=" + DEFAULT_MEMBER_NUMBER, "memberNumber.equals=" + UPDATED_MEMBER_NUMBER);
    }

    @Test
    @Transactional
    void getAllMembersByMemberNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where memberNumber in
        defaultMemberFiltering(
            "memberNumber.in=" + DEFAULT_MEMBER_NUMBER + "," + UPDATED_MEMBER_NUMBER,
            "memberNumber.in=" + UPDATED_MEMBER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMembersByMemberNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where memberNumber is not null
        defaultMemberFiltering("memberNumber.specified=true", "memberNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByMemberNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where memberNumber contains
        defaultMemberFiltering("memberNumber.contains=" + DEFAULT_MEMBER_NUMBER, "memberNumber.contains=" + UPDATED_MEMBER_NUMBER);
    }

    @Test
    @Transactional
    void getAllMembersByMemberNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where memberNumber does not contain
        defaultMemberFiltering(
            "memberNumber.doesNotContain=" + UPDATED_MEMBER_NUMBER,
            "memberNumber.doesNotContain=" + DEFAULT_MEMBER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMembersByMemberTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where memberType equals to
        defaultMemberFiltering("memberType.equals=" + DEFAULT_MEMBER_TYPE, "memberType.equals=" + UPDATED_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void getAllMembersByMemberTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where memberType in
        defaultMemberFiltering("memberType.in=" + DEFAULT_MEMBER_TYPE + "," + UPDATED_MEMBER_TYPE, "memberType.in=" + UPDATED_MEMBER_TYPE);
    }

    @Test
    @Transactional
    void getAllMembersByMemberTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where memberType is not null
        defaultMemberFiltering("memberType.specified=true", "memberType.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where status equals to
        defaultMemberFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMembersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where status in
        defaultMemberFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMembersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where status is not null
        defaultMemberFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByAdmissionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where admissionDate equals to
        defaultMemberFiltering("admissionDate.equals=" + DEFAULT_ADMISSION_DATE, "admissionDate.equals=" + UPDATED_ADMISSION_DATE);
    }

    @Test
    @Transactional
    void getAllMembersByAdmissionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where admissionDate in
        defaultMemberFiltering(
            "admissionDate.in=" + DEFAULT_ADMISSION_DATE + "," + UPDATED_ADMISSION_DATE,
            "admissionDate.in=" + UPDATED_ADMISSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembersByAdmissionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where admissionDate is not null
        defaultMemberFiltering("admissionDate.specified=true", "admissionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByAdmissionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where admissionDate is greater than or equal to
        defaultMemberFiltering(
            "admissionDate.greaterThanOrEqual=" + DEFAULT_ADMISSION_DATE,
            "admissionDate.greaterThanOrEqual=" + UPDATED_ADMISSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembersByAdmissionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where admissionDate is less than or equal to
        defaultMemberFiltering(
            "admissionDate.lessThanOrEqual=" + DEFAULT_ADMISSION_DATE,
            "admissionDate.lessThanOrEqual=" + SMALLER_ADMISSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembersByAdmissionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where admissionDate is less than
        defaultMemberFiltering("admissionDate.lessThan=" + UPDATED_ADMISSION_DATE, "admissionDate.lessThan=" + DEFAULT_ADMISSION_DATE);
    }

    @Test
    @Transactional
    void getAllMembersByAdmissionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where admissionDate is greater than
        defaultMemberFiltering(
            "admissionDate.greaterThan=" + SMALLER_ADMISSION_DATE,
            "admissionDate.greaterThan=" + DEFAULT_ADMISSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembersByExitDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where exitDate equals to
        defaultMemberFiltering("exitDate.equals=" + DEFAULT_EXIT_DATE, "exitDate.equals=" + UPDATED_EXIT_DATE);
    }

    @Test
    @Transactional
    void getAllMembersByExitDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where exitDate in
        defaultMemberFiltering("exitDate.in=" + DEFAULT_EXIT_DATE + "," + UPDATED_EXIT_DATE, "exitDate.in=" + UPDATED_EXIT_DATE);
    }

    @Test
    @Transactional
    void getAllMembersByExitDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where exitDate is not null
        defaultMemberFiltering("exitDate.specified=true", "exitDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByExitDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where exitDate is greater than or equal to
        defaultMemberFiltering("exitDate.greaterThanOrEqual=" + DEFAULT_EXIT_DATE, "exitDate.greaterThanOrEqual=" + UPDATED_EXIT_DATE);
    }

    @Test
    @Transactional
    void getAllMembersByExitDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where exitDate is less than or equal to
        defaultMemberFiltering("exitDate.lessThanOrEqual=" + DEFAULT_EXIT_DATE, "exitDate.lessThanOrEqual=" + SMALLER_EXIT_DATE);
    }

    @Test
    @Transactional
    void getAllMembersByExitDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where exitDate is less than
        defaultMemberFiltering("exitDate.lessThan=" + UPDATED_EXIT_DATE, "exitDate.lessThan=" + DEFAULT_EXIT_DATE);
    }

    @Test
    @Transactional
    void getAllMembersByExitDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where exitDate is greater than
        defaultMemberFiltering("exitDate.greaterThan=" + SMALLER_EXIT_DATE, "exitDate.greaterThan=" + DEFAULT_EXIT_DATE);
    }

    @Test
    @Transactional
    void getAllMembersByExitReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where exitReason equals to
        defaultMemberFiltering("exitReason.equals=" + DEFAULT_EXIT_REASON, "exitReason.equals=" + UPDATED_EXIT_REASON);
    }

    @Test
    @Transactional
    void getAllMembersByExitReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where exitReason in
        defaultMemberFiltering("exitReason.in=" + DEFAULT_EXIT_REASON + "," + UPDATED_EXIT_REASON, "exitReason.in=" + UPDATED_EXIT_REASON);
    }

    @Test
    @Transactional
    void getAllMembersByExitReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where exitReason is not null
        defaultMemberFiltering("exitReason.specified=true", "exitReason.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByExitReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where exitReason contains
        defaultMemberFiltering("exitReason.contains=" + DEFAULT_EXIT_REASON, "exitReason.contains=" + UPDATED_EXIT_REASON);
    }

    @Test
    @Transactional
    void getAllMembersByExitReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where exitReason does not contain
        defaultMemberFiltering("exitReason.doesNotContain=" + UPDATED_EXIT_REASON, "exitReason.doesNotContain=" + DEFAULT_EXIT_REASON);
    }

    @Test
    @Transactional
    void getAllMembersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where createdDate equals to
        defaultMemberFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMembersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where createdDate in
        defaultMemberFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where createdDate is not null
        defaultMemberFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where lastModifiedDate equals to
        defaultMemberFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembersByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where lastModifiedDate in
        defaultMemberFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMembersByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        // Get all the memberList where lastModifiedDate is not null
        defaultMemberFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMembersByIndividualMemberIsEqualToSomething() throws Exception {
        IndividualMember individualMember;
        if (TestUtil.findAll(em, IndividualMember.class).isEmpty()) {
            memberRepository.saveAndFlush(member);
            individualMember = IndividualMemberResourceIT.createEntity();
        } else {
            individualMember = TestUtil.findAll(em, IndividualMember.class).get(0);
        }
        em.persist(individualMember);
        em.flush();
        member.setIndividualMember(individualMember);
        memberRepository.saveAndFlush(member);
        Long individualMemberId = individualMember.getId();
        // Get all the memberList where individualMember equals to individualMemberId
        defaultMemberShouldBeFound("individualMemberId.equals=" + individualMemberId);

        // Get all the memberList where individualMember equals to (individualMemberId + 1)
        defaultMemberShouldNotBeFound("individualMemberId.equals=" + (individualMemberId + 1));
    }

    @Test
    @Transactional
    void getAllMembersByOrganizationMemberIsEqualToSomething() throws Exception {
        OrganizationMember organizationMember;
        if (TestUtil.findAll(em, OrganizationMember.class).isEmpty()) {
            memberRepository.saveAndFlush(member);
            organizationMember = OrganizationMemberResourceIT.createEntity();
        } else {
            organizationMember = TestUtil.findAll(em, OrganizationMember.class).get(0);
        }
        em.persist(organizationMember);
        em.flush();
        member.setOrganizationMember(organizationMember);
        memberRepository.saveAndFlush(member);
        Long organizationMemberId = organizationMember.getId();
        // Get all the memberList where organizationMember equals to organizationMemberId
        defaultMemberShouldBeFound("organizationMemberId.equals=" + organizationMemberId);

        // Get all the memberList where organizationMember equals to (organizationMemberId + 1)
        defaultMemberShouldNotBeFound("organizationMemberId.equals=" + (organizationMemberId + 1));
    }

    @Test
    @Transactional
    void getAllMembersBySocialProfileIsEqualToSomething() throws Exception {
        SocialProfile socialProfile;
        if (TestUtil.findAll(em, SocialProfile.class).isEmpty()) {
            memberRepository.saveAndFlush(member);
            socialProfile = SocialProfileResourceIT.createEntity();
        } else {
            socialProfile = TestUtil.findAll(em, SocialProfile.class).get(0);
        }
        em.persist(socialProfile);
        em.flush();
        member.setSocialProfile(socialProfile);
        memberRepository.saveAndFlush(member);
        Long socialProfileId = socialProfile.getId();
        // Get all the memberList where socialProfile equals to socialProfileId
        defaultMemberShouldBeFound("socialProfileId.equals=" + socialProfileId);

        // Get all the memberList where socialProfile equals to (socialProfileId + 1)
        defaultMemberShouldNotBeFound("socialProfileId.equals=" + (socialProfileId + 1));
    }

    @Test
    @Transactional
    void getAllMembersByProfessionalProfileIsEqualToSomething() throws Exception {
        ProfessionalProfile professionalProfile;
        if (TestUtil.findAll(em, ProfessionalProfile.class).isEmpty()) {
            memberRepository.saveAndFlush(member);
            professionalProfile = ProfessionalProfileResourceIT.createEntity();
        } else {
            professionalProfile = TestUtil.findAll(em, ProfessionalProfile.class).get(0);
        }
        em.persist(professionalProfile);
        em.flush();
        member.setProfessionalProfile(professionalProfile);
        memberRepository.saveAndFlush(member);
        Long professionalProfileId = professionalProfile.getId();
        // Get all the memberList where professionalProfile equals to professionalProfileId
        defaultMemberShouldBeFound("professionalProfileId.equals=" + professionalProfileId);

        // Get all the memberList where professionalProfile equals to (professionalProfileId + 1)
        defaultMemberShouldNotBeFound("professionalProfileId.equals=" + (professionalProfileId + 1));
    }

    @Test
    @Transactional
    void getAllMembersByCooperativeIsEqualToSomething() throws Exception {
        Cooperative cooperative;
        if (TestUtil.findAll(em, Cooperative.class).isEmpty()) {
            memberRepository.saveAndFlush(member);
            cooperative = CooperativeResourceIT.createEntity();
        } else {
            cooperative = TestUtil.findAll(em, Cooperative.class).get(0);
        }
        em.persist(cooperative);
        em.flush();
        member.setCooperative(cooperative);
        memberRepository.saveAndFlush(member);
        Long cooperativeId = cooperative.getId();
        // Get all the memberList where cooperative equals to cooperativeId
        defaultMemberShouldBeFound("cooperativeId.equals=" + cooperativeId);

        // Get all the memberList where cooperative equals to (cooperativeId + 1)
        defaultMemberShouldNotBeFound("cooperativeId.equals=" + (cooperativeId + 1));
    }

    @Test
    @Transactional
    void getAllMembersByBranchIsEqualToSomething() throws Exception {
        CooperativeBranch branch;
        if (TestUtil.findAll(em, CooperativeBranch.class).isEmpty()) {
            memberRepository.saveAndFlush(member);
            branch = CooperativeBranchResourceIT.createEntity();
        } else {
            branch = TestUtil.findAll(em, CooperativeBranch.class).get(0);
        }
        em.persist(branch);
        em.flush();
        member.setBranch(branch);
        memberRepository.saveAndFlush(member);
        Long branchId = branch.getId();
        // Get all the memberList where branch equals to branchId
        defaultMemberShouldBeFound("branchId.equals=" + branchId);

        // Get all the memberList where branch equals to (branchId + 1)
        defaultMemberShouldNotBeFound("branchId.equals=" + (branchId + 1));
    }

    private void defaultMemberFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMemberShouldBeFound(shouldBeFound);
        defaultMemberShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemberShouldBeFound(String filter) throws Exception {
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].memberNumber").value(hasItem(DEFAULT_MEMBER_NUMBER)))
            .andExpect(jsonPath("$.[*].memberType").value(hasItem(DEFAULT_MEMBER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].admissionDate").value(hasItem(DEFAULT_ADMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].exitDate").value(hasItem(DEFAULT_EXIT_DATE.toString())))
            .andExpect(jsonPath("$.[*].exitReason").value(hasItem(DEFAULT_EXIT_REASON)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemberShouldNotBeFound(String filter) throws Exception {
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMember() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the member
        Member updatedMember = memberRepository.findById(member.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMember are not directly saved in db
        em.detach(updatedMember);
        updatedMember
            .memberNumber(UPDATED_MEMBER_NUMBER)
            .memberType(UPDATED_MEMBER_TYPE)
            .status(UPDATED_STATUS)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .exitDate(UPDATED_EXIT_DATE)
            .exitReason(UPDATED_EXIT_REASON)
            .notes(UPDATED_NOTES)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        MemberDTO memberDTO = memberMapper.toDto(updatedMember);

        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMemberToMatchAllProperties(updatedMember);
    }

    @Test
    @Transactional
    void putNonExistingMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        member.setId(longCount.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        member.setId(longCount.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(memberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        member.setId(longCount.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMemberWithPatch() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the member using partial update
        Member partialUpdatedMember = new Member();
        partialUpdatedMember.setId(member.getId());

        partialUpdatedMember
            .memberNumber(UPDATED_MEMBER_NUMBER)
            .memberType(UPDATED_MEMBER_TYPE)
            .status(UPDATED_STATUS)
            .notes(UPDATED_NOTES);

        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMember))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemberUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMember, member), getPersistedMember(member));
    }

    @Test
    @Transactional
    void fullUpdateMemberWithPatch() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the member using partial update
        Member partialUpdatedMember = new Member();
        partialUpdatedMember.setId(member.getId());

        partialUpdatedMember
            .memberNumber(UPDATED_MEMBER_NUMBER)
            .memberType(UPDATED_MEMBER_TYPE)
            .status(UPDATED_STATUS)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .exitDate(UPDATED_EXIT_DATE)
            .exitReason(UPDATED_EXIT_REASON)
            .notes(UPDATED_NOTES)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMember.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMember))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMemberUpdatableFieldsEquals(partialUpdatedMember, getPersistedMember(partialUpdatedMember));
    }

    @Test
    @Transactional
    void patchNonExistingMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        member.setId(longCount.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, memberDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        member.setId(longCount.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(memberDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMember() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        member.setId(longCount.incrementAndGet());

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(memberDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Member in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMember() throws Exception {
        // Initialize the database
        insertedMember = memberRepository.saveAndFlush(member);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the member
        restMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, member.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return memberRepository.count();
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

    protected Member getPersistedMember(Member member) {
        return memberRepository.findById(member.getId()).orElseThrow();
    }

    protected void assertPersistedMemberToMatchAllProperties(Member expectedMember) {
        assertMemberAllPropertiesEquals(expectedMember, getPersistedMember(expectedMember));
    }

    protected void assertPersistedMemberToMatchUpdatableProperties(Member expectedMember) {
        assertMemberAllUpdatablePropertiesEquals(expectedMember, getPersistedMember(expectedMember));
    }
}
