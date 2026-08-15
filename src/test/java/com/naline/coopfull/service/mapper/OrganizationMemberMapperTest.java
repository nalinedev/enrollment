package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.OrganizationMemberAsserts.*;
import static com.naline.coopfull.domain.OrganizationMemberTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrganizationMemberMapperTest {

    private OrganizationMemberMapper organizationMemberMapper;

    @BeforeEach
    void setUp() {
        organizationMemberMapper = new OrganizationMemberMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOrganizationMemberSample1();
        var actual = organizationMemberMapper.toEntity(organizationMemberMapper.toDto(expected));
        assertOrganizationMemberAllPropertiesEquals(expected, actual);
    }
}
