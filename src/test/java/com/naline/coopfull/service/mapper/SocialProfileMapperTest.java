package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.SocialProfileAsserts.*;
import static com.naline.coopfull.domain.SocialProfileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SocialProfileMapperTest {

    private SocialProfileMapper socialProfileMapper;

    @BeforeEach
    void setUp() {
        socialProfileMapper = new SocialProfileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSocialProfileSample1();
        var actual = socialProfileMapper.toEntity(socialProfileMapper.toDto(expected));
        assertSocialProfileAllPropertiesEquals(expected, actual);
    }
}
