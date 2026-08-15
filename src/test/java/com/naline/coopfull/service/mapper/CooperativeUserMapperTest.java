package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.CooperativeUserAsserts.*;
import static com.naline.coopfull.domain.CooperativeUserTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CooperativeUserMapperTest {

    private CooperativeUserMapper cooperativeUserMapper;

    @BeforeEach
    void setUp() {
        cooperativeUserMapper = new CooperativeUserMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCooperativeUserSample1();
        var actual = cooperativeUserMapper.toEntity(cooperativeUserMapper.toDto(expected));
        assertCooperativeUserAllPropertiesEquals(expected, actual);
    }
}
