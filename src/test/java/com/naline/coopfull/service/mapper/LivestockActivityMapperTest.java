package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.LivestockActivityAsserts.*;
import static com.naline.coopfull.domain.LivestockActivityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LivestockActivityMapperTest {

    private LivestockActivityMapper livestockActivityMapper;

    @BeforeEach
    void setUp() {
        livestockActivityMapper = new LivestockActivityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLivestockActivitySample1();
        var actual = livestockActivityMapper.toEntity(livestockActivityMapper.toDto(expected));
        assertLivestockActivityAllPropertiesEquals(expected, actual);
    }
}
