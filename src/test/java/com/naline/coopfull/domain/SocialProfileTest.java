package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.MemberTestSamples.*;
import static com.naline.coopfull.domain.SocialProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SocialProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SocialProfile.class);
        SocialProfile socialProfile1 = getSocialProfileSample1();
        SocialProfile socialProfile2 = new SocialProfile();
        assertThat(socialProfile1).isNotEqualTo(socialProfile2);

        socialProfile2.setId(socialProfile1.getId());
        assertThat(socialProfile1).isEqualTo(socialProfile2);

        socialProfile2 = getSocialProfileSample2();
        assertThat(socialProfile1).isNotEqualTo(socialProfile2);
    }

    @Test
    void memberTest() {
        SocialProfile socialProfile = getSocialProfileRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        socialProfile.setMember(memberBack);
        assertThat(socialProfile.getMember()).isEqualTo(memberBack);
        assertThat(memberBack.getSocialProfile()).isEqualTo(socialProfile);

        socialProfile.member(null);
        assertThat(socialProfile.getMember()).isNull();
        assertThat(memberBack.getSocialProfile()).isNull();
    }
}
