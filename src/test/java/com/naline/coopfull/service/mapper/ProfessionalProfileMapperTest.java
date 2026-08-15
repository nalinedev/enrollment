package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.ProfessionalProfileAsserts.*;
import static com.naline.coopfull.domain.ProfessionalProfileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessionalProfileMapperTest {

    private ProfessionalProfileMapper professionalProfileMapper;

    @BeforeEach
    void setUp() {
        professionalProfileMapper = new ProfessionalProfileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProfessionalProfileSample1();
        var actual = professionalProfileMapper.toEntity(professionalProfileMapper.toDto(expected));
        assertProfessionalProfileAllPropertiesEquals(expected, actual);
    }
}
