package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.EconomicActivityTestSamples.*;
import static com.naline.coopfull.domain.LivestockActivityTestSamples.*;
import static com.naline.coopfull.domain.LivestockProductionTestSamples.*;
import static com.naline.coopfull.domain.LivestockTypeTestSamples.*;
import static com.naline.coopfull.domain.LocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LivestockActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LivestockActivity.class);
        LivestockActivity livestockActivity1 = getLivestockActivitySample1();
        LivestockActivity livestockActivity2 = new LivestockActivity();
        assertThat(livestockActivity1).isNotEqualTo(livestockActivity2);

        livestockActivity2.setId(livestockActivity1.getId());
        assertThat(livestockActivity1).isEqualTo(livestockActivity2);

        livestockActivity2 = getLivestockActivitySample2();
        assertThat(livestockActivity1).isNotEqualTo(livestockActivity2);
    }

    @Test
    void locationTest() {
        LivestockActivity livestockActivity = getLivestockActivityRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        livestockActivity.setLocation(locationBack);
        assertThat(livestockActivity.getLocation()).isEqualTo(locationBack);

        livestockActivity.location(null);
        assertThat(livestockActivity.getLocation()).isNull();
    }

    @Test
    void livestockTypeTest() {
        LivestockActivity livestockActivity = getLivestockActivityRandomSampleGenerator();
        LivestockType livestockTypeBack = getLivestockTypeRandomSampleGenerator();

        livestockActivity.setLivestockType(livestockTypeBack);
        assertThat(livestockActivity.getLivestockType()).isEqualTo(livestockTypeBack);

        livestockActivity.livestockType(null);
        assertThat(livestockActivity.getLivestockType()).isNull();
    }

    @Test
    void economicActivityTest() {
        LivestockActivity livestockActivity = getLivestockActivityRandomSampleGenerator();
        EconomicActivity economicActivityBack = getEconomicActivityRandomSampleGenerator();

        livestockActivity.setEconomicActivity(economicActivityBack);
        assertThat(livestockActivity.getEconomicActivity()).isEqualTo(economicActivityBack);
        assertThat(economicActivityBack.getLivestockActivity()).isEqualTo(livestockActivity);

        livestockActivity.economicActivity(null);
        assertThat(livestockActivity.getEconomicActivity()).isNull();
        assertThat(economicActivityBack.getLivestockActivity()).isNull();
    }

    @Test
    void productionsTest() {
        LivestockActivity livestockActivity = getLivestockActivityRandomSampleGenerator();
        LivestockProduction livestockProductionBack = getLivestockProductionRandomSampleGenerator();

        livestockActivity.addProductions(livestockProductionBack);
        assertThat(livestockActivity.getProductionses()).containsOnly(livestockProductionBack);
        assertThat(livestockProductionBack.getLivestockActivity()).isEqualTo(livestockActivity);

        livestockActivity.removeProductions(livestockProductionBack);
        assertThat(livestockActivity.getProductionses()).doesNotContain(livestockProductionBack);
        assertThat(livestockProductionBack.getLivestockActivity()).isNull();

        livestockActivity.productionses(new HashSet<>(Set.of(livestockProductionBack)));
        assertThat(livestockActivity.getProductionses()).containsOnly(livestockProductionBack);
        assertThat(livestockProductionBack.getLivestockActivity()).isEqualTo(livestockActivity);

        livestockActivity.setProductionses(new HashSet<>());
        assertThat(livestockActivity.getProductionses()).doesNotContain(livestockProductionBack);
        assertThat(livestockProductionBack.getLivestockActivity()).isNull();
    }
}
