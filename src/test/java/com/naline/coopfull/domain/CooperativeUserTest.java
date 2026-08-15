package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.AppUserTestSamples.*;
import static com.naline.coopfull.domain.CooperativeRoleTestSamples.*;
import static com.naline.coopfull.domain.CooperativeTestSamples.*;
import static com.naline.coopfull.domain.CooperativeUserTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CooperativeUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CooperativeUser.class);
        CooperativeUser cooperativeUser1 = getCooperativeUserSample1();
        CooperativeUser cooperativeUser2 = new CooperativeUser();
        assertThat(cooperativeUser1).isNotEqualTo(cooperativeUser2);

        cooperativeUser2.setId(cooperativeUser1.getId());
        assertThat(cooperativeUser1).isEqualTo(cooperativeUser2);

        cooperativeUser2 = getCooperativeUserSample2();
        assertThat(cooperativeUser1).isNotEqualTo(cooperativeUser2);
    }

    @Test
    void appUserTest() {
        CooperativeUser cooperativeUser = getCooperativeUserRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        cooperativeUser.setAppUser(appUserBack);
        assertThat(cooperativeUser.getAppUser()).isEqualTo(appUserBack);

        cooperativeUser.appUser(null);
        assertThat(cooperativeUser.getAppUser()).isNull();
    }

    @Test
    void cooperativeTest() {
        CooperativeUser cooperativeUser = getCooperativeUserRandomSampleGenerator();
        Cooperative cooperativeBack = getCooperativeRandomSampleGenerator();

        cooperativeUser.setCooperative(cooperativeBack);
        assertThat(cooperativeUser.getCooperative()).isEqualTo(cooperativeBack);

        cooperativeUser.cooperative(null);
        assertThat(cooperativeUser.getCooperative()).isNull();
    }

    @Test
    void roleTest() {
        CooperativeUser cooperativeUser = getCooperativeUserRandomSampleGenerator();
        CooperativeRole cooperativeRoleBack = getCooperativeRoleRandomSampleGenerator();

        cooperativeUser.setRole(cooperativeRoleBack);
        assertThat(cooperativeUser.getRole()).isEqualTo(cooperativeRoleBack);

        cooperativeUser.role(null);
        assertThat(cooperativeUser.getRole()).isNull();
    }
}
