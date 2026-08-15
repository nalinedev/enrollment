package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.AgriculturalProductionAsserts.*;
import static com.naline.coopfull.domain.AgriculturalProductionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AgriculturalProductionMapperTest {

    private AgriculturalProductionMapper agriculturalProductionMapper;

    @BeforeEach
    void setUp() {
        agriculturalProductionMapper = new AgriculturalProductionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAgriculturalProductionSample1();
        var actual = agriculturalProductionMapper.toEntity(agriculturalProductionMapper.toDto(expected));
        assertAgriculturalProductionAllPropertiesEquals(expected, actual);
    }
}
