package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.FamilyMemberAsserts.*;
import static com.naline.coopfull.domain.FamilyMemberTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FamilyMemberMapperTest {

    private FamilyMemberMapper familyMemberMapper;

    @BeforeEach
    void setUp() {
        familyMemberMapper = new FamilyMemberMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFamilyMemberSample1();
        var actual = familyMemberMapper.toEntity(familyMemberMapper.toDto(expected));
        assertFamilyMemberAllPropertiesEquals(expected, actual);
    }
}
