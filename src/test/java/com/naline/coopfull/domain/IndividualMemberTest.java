package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.IndividualMemberTestSamples.*;
import static com.naline.coopfull.domain.MemberTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IndividualMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndividualMember.class);
        IndividualMember individualMember1 = getIndividualMemberSample1();
        IndividualMember individualMember2 = new IndividualMember();
        assertThat(individualMember1).isNotEqualTo(individualMember2);

        individualMember2.setId(individualMember1.getId());
        assertThat(individualMember1).isEqualTo(individualMember2);

        individualMember2 = getIndividualMemberSample2();
        assertThat(individualMember1).isNotEqualTo(individualMember2);
    }

    @Test
    void memberTest() {
        IndividualMember individualMember = getIndividualMemberRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        individualMember.setMember(memberBack);
        assertThat(individualMember.getMember()).isEqualTo(memberBack);
        assertThat(memberBack.getIndividualMember()).isEqualTo(individualMember);

        individualMember.member(null);
        assertThat(individualMember.getMember()).isNull();
        assertThat(memberBack.getIndividualMember()).isNull();
    }
}
