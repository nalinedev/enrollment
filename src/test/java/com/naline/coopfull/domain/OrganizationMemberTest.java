package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.MemberTestSamples.*;
import static com.naline.coopfull.domain.OrganizationMemberTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationMemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationMember.class);
        OrganizationMember organizationMember1 = getOrganizationMemberSample1();
        OrganizationMember organizationMember2 = new OrganizationMember();
        assertThat(organizationMember1).isNotEqualTo(organizationMember2);

        organizationMember2.setId(organizationMember1.getId());
        assertThat(organizationMember1).isEqualTo(organizationMember2);

        organizationMember2 = getOrganizationMemberSample2();
        assertThat(organizationMember1).isNotEqualTo(organizationMember2);
    }

    @Test
    void memberTest() {
        OrganizationMember organizationMember = getOrganizationMemberRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        organizationMember.setMember(memberBack);
        assertThat(organizationMember.getMember()).isEqualTo(memberBack);
        assertThat(memberBack.getOrganizationMember()).isEqualTo(organizationMember);

        organizationMember.member(null);
        assertThat(organizationMember.getMember()).isNull();
        assertThat(memberBack.getOrganizationMember()).isNull();
    }
}
