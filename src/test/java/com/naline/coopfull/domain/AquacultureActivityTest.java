package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.AquacultureActivityTestSamples.*;
import static com.naline.coopfull.domain.AquacultureProductionTestSamples.*;
import static com.naline.coopfull.domain.AquaticSpeciesTestSamples.*;
import static com.naline.coopfull.domain.EconomicActivityTestSamples.*;
import static com.naline.coopfull.domain.LocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AquacultureActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AquacultureActivity.class);
        AquacultureActivity aquacultureActivity1 = getAquacultureActivitySample1();
        AquacultureActivity aquacultureActivity2 = new AquacultureActivity();
        assertThat(aquacultureActivity1).isNotEqualTo(aquacultureActivity2);

        aquacultureActivity2.setId(aquacultureActivity1.getId());
        assertThat(aquacultureActivity1).isEqualTo(aquacultureActivity2);

        aquacultureActivity2 = getAquacultureActivitySample2();
        assertThat(aquacultureActivity1).isNotEqualTo(aquacultureActivity2);
    }

    @Test
    void locationTest() {
        AquacultureActivity aquacultureActivity = getAquacultureActivityRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        aquacultureActivity.setLocation(locationBack);
        assertThat(aquacultureActivity.getLocation()).isEqualTo(locationBack);

        aquacultureActivity.location(null);
        assertThat(aquacultureActivity.getLocation()).isNull();
    }

    @Test
    void aquaticSpeciesTest() {
        AquacultureActivity aquacultureActivity = getAquacultureActivityRandomSampleGenerator();
        AquaticSpecies aquaticSpeciesBack = getAquaticSpeciesRandomSampleGenerator();

        aquacultureActivity.setAquaticSpecies(aquaticSpeciesBack);
        assertThat(aquacultureActivity.getAquaticSpecies()).isEqualTo(aquaticSpeciesBack);

        aquacultureActivity.aquaticSpecies(null);
        assertThat(aquacultureActivity.getAquaticSpecies()).isNull();
    }

    @Test
    void economicActivityTest() {
        AquacultureActivity aquacultureActivity = getAquacultureActivityRandomSampleGenerator();
        EconomicActivity economicActivityBack = getEconomicActivityRandomSampleGenerator();

        aquacultureActivity.setEconomicActivity(economicActivityBack);
        assertThat(aquacultureActivity.getEconomicActivity()).isEqualTo(economicActivityBack);
        assertThat(economicActivityBack.getAquacultureActivity()).isEqualTo(aquacultureActivity);

        aquacultureActivity.economicActivity(null);
        assertThat(aquacultureActivity.getEconomicActivity()).isNull();
        assertThat(economicActivityBack.getAquacultureActivity()).isNull();
    }

    @Test
    void productionsTest() {
        AquacultureActivity aquacultureActivity = getAquacultureActivityRandomSampleGenerator();
        AquacultureProduction aquacultureProductionBack = getAquacultureProductionRandomSampleGenerator();

        aquacultureActivity.addProductions(aquacultureProductionBack);
        assertThat(aquacultureActivity.getProductionses()).containsOnly(aquacultureProductionBack);
        assertThat(aquacultureProductionBack.getAquacultureActivity()).isEqualTo(aquacultureActivity);

        aquacultureActivity.removeProductions(aquacultureProductionBack);
        assertThat(aquacultureActivity.getProductionses()).doesNotContain(aquacultureProductionBack);
        assertThat(aquacultureProductionBack.getAquacultureActivity()).isNull();

        aquacultureActivity.productionses(new HashSet<>(Set.of(aquacultureProductionBack)));
        assertThat(aquacultureActivity.getProductionses()).containsOnly(aquacultureProductionBack);
        assertThat(aquacultureProductionBack.getAquacultureActivity()).isEqualTo(aquacultureActivity);

        aquacultureActivity.setProductionses(new HashSet<>());
        assertThat(aquacultureActivity.getProductionses()).doesNotContain(aquacultureProductionBack);
        assertThat(aquacultureProductionBack.getAquacultureActivity()).isNull();
    }
}
