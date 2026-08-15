package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.EconomicActivityTypeAsserts.*;
import static com.naline.coopfull.domain.EconomicActivityTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EconomicActivityTypeMapperTest {

    private EconomicActivityTypeMapper economicActivityTypeMapper;

    @BeforeEach
    void setUp() {
        economicActivityTypeMapper = new EconomicActivityTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEconomicActivityTypeSample1();
        var actual = economicActivityTypeMapper.toEntity(economicActivityTypeMapper.toDto(expected));
        assertEconomicActivityTypeAllPropertiesEquals(expected, actual);
    }
}
