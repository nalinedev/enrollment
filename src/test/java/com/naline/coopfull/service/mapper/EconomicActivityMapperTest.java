package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.EconomicActivityAsserts.*;
import static com.naline.coopfull.domain.EconomicActivityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EconomicActivityMapperTest {

    private EconomicActivityMapper economicActivityMapper;

    @BeforeEach
    void setUp() {
        economicActivityMapper = new EconomicActivityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEconomicActivitySample1();
        var actual = economicActivityMapper.toEntity(economicActivityMapper.toDto(expected));
        assertEconomicActivityAllPropertiesEquals(expected, actual);
    }
}
