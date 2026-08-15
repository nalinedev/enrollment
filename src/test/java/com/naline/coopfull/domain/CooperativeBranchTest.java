package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.CooperativeBranchTestSamples.*;
import static com.naline.coopfull.domain.CooperativeTestSamples.*;
import static com.naline.coopfull.domain.LocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CooperativeBranchTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CooperativeBranch.class);
        CooperativeBranch cooperativeBranch1 = getCooperativeBranchSample1();
        CooperativeBranch cooperativeBranch2 = new CooperativeBranch();
        assertThat(cooperativeBranch1).isNotEqualTo(cooperativeBranch2);

        cooperativeBranch2.setId(cooperativeBranch1.getId());
        assertThat(cooperativeBranch1).isEqualTo(cooperativeBranch2);

        cooperativeBranch2 = getCooperativeBranchSample2();
        assertThat(cooperativeBranch1).isNotEqualTo(cooperativeBranch2);
    }

    @Test
    void cooperativeTest() {
        CooperativeBranch cooperativeBranch = getCooperativeBranchRandomSampleGenerator();
        Cooperative cooperativeBack = getCooperativeRandomSampleGenerator();

        cooperativeBranch.setCooperative(cooperativeBack);
        assertThat(cooperativeBranch.getCooperative()).isEqualTo(cooperativeBack);

        cooperativeBranch.cooperative(null);
        assertThat(cooperativeBranch.getCooperative()).isNull();
    }

    @Test
    void locationTest() {
        CooperativeBranch cooperativeBranch = getCooperativeBranchRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        cooperativeBranch.setLocation(locationBack);
        assertThat(cooperativeBranch.getLocation()).isEqualTo(locationBack);

        cooperativeBranch.location(null);
        assertThat(cooperativeBranch.getLocation()).isNull();
    }
}
