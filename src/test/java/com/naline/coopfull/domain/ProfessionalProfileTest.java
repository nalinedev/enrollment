package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.MemberTestSamples.*;
import static com.naline.coopfull.domain.ProfessionalProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProfessionalProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfessionalProfile.class);
        ProfessionalProfile professionalProfile1 = getProfessionalProfileSample1();
        ProfessionalProfile professionalProfile2 = new ProfessionalProfile();
        assertThat(professionalProfile1).isNotEqualTo(professionalProfile2);

        professionalProfile2.setId(professionalProfile1.getId());
        assertThat(professionalProfile1).isEqualTo(professionalProfile2);

        professionalProfile2 = getProfessionalProfileSample2();
        assertThat(professionalProfile1).isNotEqualTo(professionalProfile2);
    }

    @Test
    void memberTest() {
        ProfessionalProfile professionalProfile = getProfessionalProfileRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        professionalProfile.setMember(memberBack);
        assertThat(professionalProfile.getMember()).isEqualTo(memberBack);
        assertThat(memberBack.getProfessionalProfile()).isEqualTo(professionalProfile);

        professionalProfile.member(null);
        assertThat(professionalProfile.getMember()).isNull();
        assertThat(memberBack.getProfessionalProfile()).isNull();
    }
}
