package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.AquacultureActivityAsserts.*;
import static com.naline.coopfull.domain.AquacultureActivityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AquacultureActivityMapperTest {

    private AquacultureActivityMapper aquacultureActivityMapper;

    @BeforeEach
    void setUp() {
        aquacultureActivityMapper = new AquacultureActivityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAquacultureActivitySample1();
        var actual = aquacultureActivityMapper.toEntity(aquacultureActivityMapper.toDto(expected));
        assertAquacultureActivityAllPropertiesEquals(expected, actual);
    }
}
