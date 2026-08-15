package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.CooperativeBranchTestSamples.*;
import static com.naline.coopfull.domain.CooperativeTestSamples.*;
import static com.naline.coopfull.domain.MemberTestSamples.*;
import static com.naline.coopfull.domain.MembershipApplicationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembershipApplicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipApplication.class);
        MembershipApplication membershipApplication1 = getMembershipApplicationSample1();
        MembershipApplication membershipApplication2 = new MembershipApplication();
        assertThat(membershipApplication1).isNotEqualTo(membershipApplication2);

        membershipApplication2.setId(membershipApplication1.getId());
        assertThat(membershipApplication1).isEqualTo(membershipApplication2);

        membershipApplication2 = getMembershipApplicationSample2();
        assertThat(membershipApplication1).isNotEqualTo(membershipApplication2);
    }

    @Test
    void memberTest() {
        MembershipApplication membershipApplication = getMembershipApplicationRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        membershipApplication.setMember(memberBack);
        assertThat(membershipApplication.getMember()).isEqualTo(memberBack);

        membershipApplication.member(null);
        assertThat(membershipApplication.getMember()).isNull();
    }

    @Test
    void cooperativeTest() {
        MembershipApplication membershipApplication = getMembershipApplicationRandomSampleGenerator();
        Cooperative cooperativeBack = getCooperativeRandomSampleGenerator();

        membershipApplication.setCooperative(cooperativeBack);
        assertThat(membershipApplication.getCooperative()).isEqualTo(cooperativeBack);

        membershipApplication.cooperative(null);
        assertThat(membershipApplication.getCooperative()).isNull();
    }

    @Test
    void branchTest() {
        MembershipApplication membershipApplication = getMembershipApplicationRandomSampleGenerator();
        CooperativeBranch cooperativeBranchBack = getCooperativeBranchRandomSampleGenerator();

        membershipApplication.setBranch(cooperativeBranchBack);
        assertThat(membershipApplication.getBranch()).isEqualTo(cooperativeBranchBack);

        membershipApplication.branch(null);
        assertThat(membershipApplication.getBranch()).isNull();
    }
}
