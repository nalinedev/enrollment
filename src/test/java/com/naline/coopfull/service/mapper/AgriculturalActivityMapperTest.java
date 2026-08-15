package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.AgriculturalActivityAsserts.*;
import static com.naline.coopfull.domain.AgriculturalActivityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgriculturalActivityMapperTest {

    private AgriculturalActivityMapper agriculturalActivityMapper;

    @BeforeEach
    void setUp() {
        agriculturalActivityMapper = new AgriculturalActivityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAgriculturalActivitySample1();
        var actual = agriculturalActivityMapper.toEntity(agriculturalActivityMapper.toDto(expected));
        assertAgriculturalActivityAllPropertiesEquals(expected, actual);
    }
}
