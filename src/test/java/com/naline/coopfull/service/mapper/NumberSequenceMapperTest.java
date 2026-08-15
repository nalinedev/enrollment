package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.NumberSequenceAsserts.*;
import static com.naline.coopfull.domain.NumberSequenceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NumberSequenceMapperTest {

    private NumberSequenceMapper numberSequenceMapper;

    @BeforeEach
    void setUp() {
        numberSequenceMapper = new NumberSequenceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNumberSequenceSample1();
        var actual = numberSequenceMapper.toEntity(numberSequenceMapper.toDto(expected));
        assertNumberSequenceAllPropertiesEquals(expected, actual);
    }
}
