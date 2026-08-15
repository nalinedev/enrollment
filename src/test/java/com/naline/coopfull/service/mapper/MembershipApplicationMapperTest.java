package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.MembershipApplicationAsserts.*;
import static com.naline.coopfull.domain.MembershipApplicationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MembershipApplicationMapperTest {

    private MembershipApplicationMapper membershipApplicationMapper;

    @BeforeEach
    void setUp() {
        membershipApplicationMapper = new MembershipApplicationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMembershipApplicationSample1();
        var actual = membershipApplicationMapper.toEntity(membershipApplicationMapper.toDto(expected));
        assertMembershipApplicationAllPropertiesEquals(expected, actual);
    }
}
