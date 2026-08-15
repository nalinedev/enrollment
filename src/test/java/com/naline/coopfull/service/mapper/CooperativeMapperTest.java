package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.CooperativeAsserts.*;
import static com.naline.coopfull.domain.CooperativeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CooperativeMapperTest {

    private CooperativeMapper cooperativeMapper;

    @BeforeEach
    void setUp() {
        cooperativeMapper = new CooperativeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCooperativeSample1();
        var actual = cooperativeMapper.toEntity(cooperativeMapper.toDto(expected));
        assertCooperativeAllPropertiesEquals(expected, actual);
    }
}
