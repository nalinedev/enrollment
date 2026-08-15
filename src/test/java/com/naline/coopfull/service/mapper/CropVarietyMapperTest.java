package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.CropVarietyAsserts.*;
import static com.naline.coopfull.domain.CropVarietyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CropVarietyMapperTest {

    private CropVarietyMapper cropVarietyMapper;

    @BeforeEach
    void setUp() {
        cropVarietyMapper = new CropVarietyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCropVarietySample1();
        var actual = cropVarietyMapper.toEntity(cropVarietyMapper.toDto(expected));
        assertCropVarietyAllPropertiesEquals(expected, actual);
    }
}
