package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.IndividualMemberAsserts.*;
import static com.naline.coopfull.domain.IndividualMemberTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IndividualMemberMapperTest {

    private IndividualMemberMapper individualMemberMapper;

    @BeforeEach
    void setUp() {
        individualMemberMapper = new IndividualMemberMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIndividualMemberSample1();
        var actual = individualMemberMapper.toEntity(individualMemberMapper.toDto(expected));
        assertIndividualMemberAllPropertiesEquals(expected, actual);
    }
}
