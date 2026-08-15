package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.CooperativeBranchAsserts.*;
import static com.naline.coopfull.domain.CooperativeBranchTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CooperativeBranchMapperTest {

    private CooperativeBranchMapper cooperativeBranchMapper;

    @BeforeEach
    void setUp() {
        cooperativeBranchMapper = new CooperativeBranchMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCooperativeBranchSample1();
        var actual = cooperativeBranchMapper.toEntity(cooperativeBranchMapper.toDto(expected));
        assertCooperativeBranchAllPropertiesEquals(expected, actual);
    }
}
