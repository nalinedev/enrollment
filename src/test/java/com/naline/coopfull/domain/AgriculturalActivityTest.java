package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.AgriculturalActivityTestSamples.*;
import static com.naline.coopfull.domain.AgriculturalProductionTestSamples.*;
import static com.naline.coopfull.domain.EconomicActivityTestSamples.*;
import static com.naline.coopfull.domain.LocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AgriculturalActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgriculturalActivity.class);
        AgriculturalActivity agriculturalActivity1 = getAgriculturalActivitySample1();
        AgriculturalActivity agriculturalActivity2 = new AgriculturalActivity();
        assertThat(agriculturalActivity1).isNotEqualTo(agriculturalActivity2);

        agriculturalActivity2.setId(agriculturalActivity1.getId());
        assertThat(agriculturalActivity1).isEqualTo(agriculturalActivity2);

        agriculturalActivity2 = getAgriculturalActivitySample2();
        assertThat(agriculturalActivity1).isNotEqualTo(agriculturalActivity2);
    }

    @Test
    void locationTest() {
        AgriculturalActivity agriculturalActivity = getAgriculturalActivityRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        agriculturalActivity.setLocation(locationBack);
        assertThat(agriculturalActivity.getLocation()).isEqualTo(locationBack);

        agriculturalActivity.location(null);
        assertThat(agriculturalActivity.getLocation()).isNull();
    }

    @Test
    void economicActivityTest() {
        AgriculturalActivity agriculturalActivity = getAgriculturalActivityRandomSampleGenerator();
        EconomicActivity economicActivityBack = getEconomicActivityRandomSampleGenerator();

        agriculturalActivity.setEconomicActivity(economicActivityBack);
        assertThat(agriculturalActivity.getEconomicActivity()).isEqualTo(economicActivityBack);
        assertThat(economicActivityBack.getAgriculturalActivity()).isEqualTo(agriculturalActivity);

        agriculturalActivity.economicActivity(null);
        assertThat(agriculturalActivity.getEconomicActivity()).isNull();
        assertThat(economicActivityBack.getAgriculturalActivity()).isNull();
    }

    @Test
    void productionsTest() {
        AgriculturalActivity agriculturalActivity = getAgriculturalActivityRandomSampleGenerator();
        AgriculturalProduction agriculturalProductionBack = getAgriculturalProductionRandomSampleGenerator();

        agriculturalActivity.addProductions(agriculturalProductionBack);
        assertThat(agriculturalActivity.getProductionses()).containsOnly(agriculturalProductionBack);
        assertThat(agriculturalProductionBack.getAgriculturalActivity()).isEqualTo(agriculturalActivity);

        agriculturalActivity.removeProductions(agriculturalProductionBack);
        assertThat(agriculturalActivity.getProductionses()).doesNotContain(agriculturalProductionBack);
        assertThat(agriculturalProductionBack.getAgriculturalActivity()).isNull();

        agriculturalActivity.productionses(new HashSet<>(Set.of(agriculturalProductionBack)));
        assertThat(agriculturalActivity.getProductionses()).containsOnly(agriculturalProductionBack);
        assertThat(agriculturalProductionBack.getAgriculturalActivity()).isEqualTo(agriculturalActivity);

        agriculturalActivity.setProductionses(new HashSet<>());
        assertThat(agriculturalActivity.getProductionses()).doesNotContain(agriculturalProductionBack);
        assertThat(agriculturalProductionBack.getAgriculturalActivity()).isNull();
    }
}
