package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.LivestockProductionAsserts.*;
import static com.naline.coopfull.domain.LivestockProductionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LivestockProductionMapperTest {

    private LivestockProductionMapper livestockProductionMapper;

    @BeforeEach
    void setUp() {
        livestockProductionMapper = new LivestockProductionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLivestockProductionSample1();
        var actual = livestockProductionMapper.toEntity(livestockProductionMapper.toDto(expected));
        assertLivestockProductionAllPropertiesEquals(expected, actual);
    }
}
