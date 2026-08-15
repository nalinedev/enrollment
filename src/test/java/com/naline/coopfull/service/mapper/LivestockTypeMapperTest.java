package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.LivestockTypeAsserts.*;
import static com.naline.coopfull.domain.LivestockTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LivestockTypeMapperTest {

    private LivestockTypeMapper livestockTypeMapper;

    @BeforeEach
    void setUp() {
        livestockTypeMapper = new LivestockTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLivestockTypeSample1();
        var actual = livestockTypeMapper.toEntity(livestockTypeMapper.toDto(expected));
        assertLivestockTypeAllPropertiesEquals(expected, actual);
    }
}
