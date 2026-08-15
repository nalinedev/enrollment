package com.naline.coopfull.service.mapper;

import static com.naline.coopfull.domain.MemberDocumentAsserts.*;
import static com.naline.coopfull.domain.MemberDocumentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberDocumentMapperTest {

    private MemberDocumentMapper memberDocumentMapper;

    @BeforeEach
    void setUp() {
        memberDocumentMapper = new MemberDocumentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMemberDocumentSample1();
        var actual = memberDocumentMapper.toEntity(memberDocumentMapper.toDto(expected));
        assertMemberDocumentAllPropertiesEquals(expected, actual);
    }
}
