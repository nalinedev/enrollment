package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.BranchUserAsserts.*;
import static com.naline.coopfull.domain.BranchUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BranchUserMapperTest {

    private BranchUserMapper branchUserMapper;

    @BeforeEach
    void setUp() {
        branchUserMapper = new BranchUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBranchUserSample1();
        var actual = branchUserMapper.toEntity(branchUserMapper.toDto(expected));
        assertBranchUserAllPropertiesEquals(expected, actual);
    }
}
