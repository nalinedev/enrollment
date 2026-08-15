package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.AgriculturalActivityTestSamples.*;
import static com.naline.coopfull.domain.AquacultureActivityTestSamples.*;
import static com.naline.coopfull.domain.EconomicActivityTestSamples.*;
import static com.naline.coopfull.domain.EconomicActivityTypeTestSamples.*;
import static com.naline.coopfull.domain.LivestockActivityTestSamples.*;
import static com.naline.coopfull.domain.LocationTestSamples.*;
import static com.naline.coopfull.domain.MemberTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EconomicActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EconomicActivity.class);
        EconomicActivity economicActivity1 = getEconomicActivitySample1();
        EconomicActivity economicActivity2 = new EconomicActivity();
        assertThat(economicActivity1).isNotEqualTo(economicActivity2);

        economicActivity2.setId(economicActivity1.getId());
        assertThat(economicActivity1).isEqualTo(economicActivity2);

        economicActivity2 = getEconomicActivitySample2();
        assertThat(economicActivity1).isNotEqualTo(economicActivity2);
    }

    @Test
    void agriculturalActivityTest() {
        EconomicActivity economicActivity = getEconomicActivityRandomSampleGenerator();
        AgriculturalActivity agriculturalActivityBack = getAgriculturalActivityRandomSampleGenerator();

        economicActivity.setAgriculturalActivity(agriculturalActivityBack);
        assertThat(economicActivity.getAgriculturalActivity()).isEqualTo(agriculturalActivityBack);

        economicActivity.agriculturalActivity(null);
        assertThat(economicActivity.getAgriculturalActivity()).isNull();
    }

    @Test
    void livestockActivityTest() {
        EconomicActivity economicActivity = getEconomicActivityRandomSampleGenerator();
        LivestockActivity livestockActivityBack = getLivestockActivityRandomSampleGenerator();

        economicActivity.setLivestockActivity(livestockActivityBack);
        assertThat(economicActivity.getLivestockActivity()).isEqualTo(livestockActivityBack);

        economicActivity.livestockActivity(null);
        assertThat(economicActivity.getLivestockActivity()).isNull();
    }

    @Test
    void aquacultureActivityTest() {
        EconomicActivity economicActivity = getEconomicActivityRandomSampleGenerator();
        AquacultureActivity aquacultureActivityBack = getAquacultureActivityRandomSampleGenerator();

        economicActivity.setAquacultureActivity(aquacultureActivityBack);
        assertThat(economicActivity.getAquacultureActivity()).isEqualTo(aquacultureActivityBack);

        economicActivity.aquacultureActivity(null);
        assertThat(economicActivity.getAquacultureActivity()).isNull();
    }

    @Test
    void memberTest() {
        EconomicActivity economicActivity = getEconomicActivityRandomSampleGenerator();
        Member memberBack = getMemberRandomSampleGenerator();

        economicActivity.setMember(memberBack);
        assertThat(economicActivity.getMember()).isEqualTo(memberBack);

        economicActivity.member(null);
        assertThat(economicActivity.getMember()).isNull();
    }

    @Test
    void activityTypeTest() {
        EconomicActivity economicActivity = getEconomicActivityRandomSampleGenerator();
        EconomicActivityType economicActivityTypeBack = getEconomicActivityTypeRandomSampleGenerator();

        economicActivity.setActivityType(economicActivityTypeBack);
        assertThat(economicActivity.getActivityType()).isEqualTo(economicActivityTypeBack);

        economicActivity.activityType(null);
        assertThat(economicActivity.getActivityType()).isNull();
    }

    @Test
    void locationTest() {
        EconomicActivity economicActivity = getEconomicActivityRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        economicActivity.setLocation(locationBack);
        assertThat(economicActivity.getLocation()).isEqualTo(locationBack);

        economicActivity.location(null);
        assertThat(economicActivity.getLocation()).isNull();
    }
}
