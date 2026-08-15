package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.CropAsserts.*;
import static com.naline.coopfull.domain.CropTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CropMapperTest {

    private CropMapper cropMapper;

    @BeforeEach
    void setUp() {
        cropMapper = new CropMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCropSample1();
        var actual = cropMapper.toEntity(cropMapper.toDto(expected));
        assertCropAllPropertiesEquals(expected, actual);
    }
}
