package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.AppUserTestSamples.*;
import static com.naline.coopfull.domain.BranchUserTestSamples.*;
import static com.naline.coopfull.domain.CooperativeBranchTestSamples.*;
import static com.naline.coopfull.domain.CooperativeRoleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BranchUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchUser.class);
        BranchUser branchUser1 = getBranchUserSample1();
        BranchUser branchUser2 = new BranchUser();
        assertThat(branchUser1).isNotEqualTo(branchUser2);

        branchUser2.setId(branchUser1.getId());
        assertThat(branchUser1).isEqualTo(branchUser2);

        branchUser2 = getBranchUserSample2();
        assertThat(branchUser1).isNotEqualTo(branchUser2);
    }

    @Test
    void appUserTest() {
        BranchUser branchUser = getBranchUserRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        branchUser.setAppUser(appUserBack);
        assertThat(branchUser.getAppUser()).isEqualTo(appUserBack);

        branchUser.appUser(null);
        assertThat(branchUser.getAppUser()).isNull();
    }

    @Test
    void branchTest() {
        BranchUser branchUser = getBranchUserRandomSampleGenerator();
        CooperativeBranch cooperativeBranchBack = getCooperativeBranchRandomSampleGenerator();

        branchUser.setBranch(cooperativeBranchBack);
        assertThat(branchUser.getBranch()).isEqualTo(cooperativeBranchBack);

        branchUser.branch(null);
        assertThat(branchUser.getBranch()).isNull();
    }

    @Test
    void roleTest() {
        BranchUser branchUser = getBranchUserRandomSampleGenerator();
        CooperativeRole cooperativeRoleBack = getCooperativeRoleRandomSampleGenerator();

        branchUser.setRole(cooperativeRoleBack);
        assertThat(branchUser.getRole()).isEqualTo(cooperativeRoleBack);

        branchUser.role(null);
        assertThat(branchUser.getRole()).isNull();
    }
}
