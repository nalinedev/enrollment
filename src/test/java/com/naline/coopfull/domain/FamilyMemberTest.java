package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.FamilyMemberTestSamples.*;
import static com.naline.coopfull.domain.MemberTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilyMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FamilyMember.class);
        FamilyMember familyMember1 = getFamilyMemberSample1();
        FamilyMember familyMember2 = new FamilyMember();
        assertThat(familyMember1).isNotEqualTo(familyMember2);

        familyMember2.setId(familyMember1.getId());
        assertThat(familyMember1).isEqualTo(familyMember2);

        familyMember2 = getFamilyMemberSample2();
        assertThat(familyMember1).isNotEqualTo(familyMember2);
    }

    @Test
    void memberTest() {
        FamilyMember familyMember = getFamilyMemberRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        familyMember.setMember(memberBack);
        assertThat(familyMember.getMember()).isEqualTo(memberBack);

        familyMember.member(null);
        assertThat(familyMember.getMember()).isNull();
    }
}
