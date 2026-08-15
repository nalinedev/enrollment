package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.AquaticSpeciesAsserts.*;
import static com.naline.coopfull.domain.AquaticSpeciesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AquaticSpeciesMapperTest {

    private AquaticSpeciesMapper aquaticSpeciesMapper;

    @BeforeEach
    void setUp() {
        aquaticSpeciesMapper = new AquaticSpeciesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAquaticSpeciesSample1();
        var actual = aquaticSpeciesMapper.toEntity(aquaticSpeciesMapper.toDto(expected));
        assertAquaticSpeciesAllPropertiesEquals(expected, actual);
    }
}
