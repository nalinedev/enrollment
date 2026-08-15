package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.AppUserTestSamples.*;
import static com.naline.coopfull.domain.MemberDocumentTestSamples.*;
import static com.naline.coopfull.domain.MemberTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberDocument.class);
        MemberDocument memberDocument1 = getMemberDocumentSample1();
        MemberDocument memberDocument2 = new MemberDocument();
        assertThat(memberDocument1).isNotEqualTo(memberDocument2);

        memberDocument2.setId(memberDocument1.getId());
        assertThat(memberDocument1).isEqualTo(memberDocument2);

        memberDocument2 = getMemberDocumentSample2();
        assertThat(memberDocument1).isNotEqualTo(memberDocument2);
    }

    @Test
    void memberTest() {
        MemberDocument memberDocument = getMemberDocumentRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        memberDocument.setMember(memberBack);
        assertThat(memberDocument.getMember()).isEqualTo(memberBack);

        memberDocument.member(null);
        assertThat(memberDocument.getMember()).isNull();
    }

    @Test
    void uploadedByTest() {
        MemberDocument memberDocument = getMemberDocumentRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        memberDocument.setUploadedBy(appUserBack);
        assertThat(memberDocument.getUploadedBy()).isEqualTo(appUserBack);

        memberDocument.uploadedBy(null);
        assertThat(memberDocument.getUploadedBy()).isNull();
    }
}
