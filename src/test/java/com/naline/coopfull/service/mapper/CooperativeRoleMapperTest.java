package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.CooperativeRoleAsserts.*;
import static com.naline.coopfull.domain.CooperativeRoleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CooperativeRoleMapperTest {

    private CooperativeRoleMapper cooperativeRoleMapper;

    @BeforeEach
    void setUp() {
        cooperativeRoleMapper = new CooperativeRoleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCooperativeRoleSample1();
        var actual = cooperativeRoleMapper.toEntity(cooperativeRoleMapper.toDto(expected));
        assertCooperativeRoleAllPropertiesEquals(expected, actual);
    }
}
