package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.AquacultureProductionAsserts.*;
import static com.naline.coopfull.domain.AquacultureProductionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AquacultureProductionMapperTest {

    private AquacultureProductionMapper aquacultureProductionMapper;

    @BeforeEach
    void setUp() {
        aquacultureProductionMapper = new AquacultureProductionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAquacultureProductionSample1();
        var actual = aquacultureProductionMapper.toEntity(aquacultureProductionMapper.toDto(expected));
        assertAquacultureProductionAllPropertiesEquals(expected, actual);
    }
}
