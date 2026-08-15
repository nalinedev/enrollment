package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.IdentityDocumentAsserts.*;
import static com.naline.coopfull.domain.IdentityDocumentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IdentityDocumentMapperTest {

    private IdentityDocumentMapper identityDocumentMapper;

    @BeforeEach
    void setUp() {
        identityDocumentMapper = new IdentityDocumentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIdentityDocumentSample1();
        var actual = identityDocumentMapper.toEntity(identityDocumentMapper.toDto(expected));
        assertIdentityDocumentAllPropertiesEquals(expected, actual);
    }
}
