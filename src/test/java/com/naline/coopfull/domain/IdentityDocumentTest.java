package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.IdentityDocumentTestSamples.*;
import static com.naline.coopfull.domain.MemberTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdentityDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IdentityDocument.class);
        IdentityDocument identityDocument1 = getIdentityDocumentSample1();
        IdentityDocument identityDocument2 = new IdentityDocument();
        assertThat(identityDocument1).isNotEqualTo(identityDocument2);

        identityDocument2.setId(identityDocument1.getId());
        assertThat(identityDocument1).isEqualTo(identityDocument2);

        identityDocument2 = getIdentityDocumentSample2();
        assertThat(identityDocument1).isNotEqualTo(identityDocument2);
    }

    @Test
    void memberTest() {
        IdentityDocument identityDocument = getIdentityDocumentRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        identityDocument.setMember(memberBack);
        assertThat(identityDocument.getMember()).isEqualTo(memberBack);

        identityDocument.member(null);
        assertThat(identityDocument.getMember()).isNull();
    }
}
