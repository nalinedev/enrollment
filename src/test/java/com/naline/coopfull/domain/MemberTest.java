package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.CooperativeBranchTestSamples.*;
import static com.naline.coopfull.domain.CooperativeTestSamples.*;
import static com.naline.coopfull.domain.IndividualMemberTestSamples.*;
import static com.naline.coopfull.domain.MemberTestSamples.*;
import static com.naline.coopfull.domain.OrganizationMemberTestSamples.*;
import static com.naline.coopfull.domain.ProfessionalProfileTestSamples.*;
import static com.naline.coopfull.domain.SocialProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Member.class);
        Member member1 = getMemberSample1();
        Member member2 = new Member();
        assertThat(member1).isNotEqualTo(member2);

        member2.setId(member1.getId());
        assertThat(member1).isEqualTo(member2);

        member2 = getMemberSample2();
        assertThat(member1).isNotEqualTo(member2);
    }

    @Test
    void individualMemberTest() {
        Member member = getMemberRandomSampleGenerator();
        IndividualMember individualMemberBack = getIndividualMemberRandomSampleGenerator();

        member.setIndividualMember(individualMemberBack);
        assertThat(member.getIndividualMember()).isEqualTo(individualMemberBack);

        member.individualMember(null);
        assertThat(member.getIndividualMember()).isNull();
    }

    @Test
    void organizationMemberTest() {
        Member member = getMemberRandomSampleGenerator();
        OrganizationMember organizationMemberBack = getOrganizationMemberRandomSampleGenerator();

        member.setOrganizationMember(organizationMemberBack);
        assertThat(member.getOrganizationMember()).isEqualTo(organizationMemberBack);

        member.organizationMember(null);
        assertThat(member.getOrganizationMember()).isNull();
    }

    @Test
    void socialProfileTest() {
        Member member = getMemberRandomSampleGenerator();
        SocialProfile socialProfileBack = getSocialProfileRandomSampleGenerator();

        member.setSocialProfile(socialProfileBack);
        assertThat(member.getSocialProfile()).isEqualTo(socialProfileBack);

        member.socialProfile(null);
        assertThat(member.getSocialProfile()).isNull();
    }

    @Test
    void professionalProfileTest() {
        Member member = getMemberRandomSampleGenerator();
        ProfessionalProfile professionalProfileBack = getProfessionalProfileRandomSampleGenerator();

        member.setProfessionalProfile(professionalProfileBack);
        assertThat(member.getProfessionalProfile()).isEqualTo(professionalProfileBack);

        member.professionalProfile(null);
        assertThat(member.getProfessionalProfile()).isNull();
    }

    @Test
    void cooperativeTest() {
        Member member = getMemberRandomSampleGenerator();
        Cooperative cooperativeBack = getCooperativeRandomSampleGenerator();

        member.setCooperative(cooperativeBack);
        assertThat(member.getCooperative()).isEqualTo(cooperativeBack);

        member.cooperative(null);
        assertThat(member.getCooperative()).isNull();
    }

    @Test
    void branchTest() {
        Member member = getMemberRandomSampleGenerator();
        CooperativeBranch cooperativeBranchBack = getCooperativeBranchRandomSampleGenerator();

        member.setBranch(cooperativeBranchBack);
        assertThat(member.getBranch()).isEqualTo(cooperativeBranchBack);

        member.branch(null);
        assertThat(member.getBranch()).isNull();
    }
}
