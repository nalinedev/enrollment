package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.LivestockActivityTestSamples.*;
import static com.naline.coopfull.domain.LivestockProductionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivestockProductionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LivestockProduction.class);
        LivestockProduction livestockProduction1 = getLivestockProductionSample1();
        LivestockProduction livestockProduction2 = new LivestockProduction();
        assertThat(livestockProduction1).isNotEqualTo(livestockProduction2);

        livestockProduction2.setId(livestockProduction1.getId());
        assertThat(livestockProduction1).isEqualTo(livestockProduction2);

        livestockProduction2 = getLivestockProductionSample2();
        assertThat(livestockProduction1).isNotEqualTo(livestockProduction2);
    }

    @Test
    void livestockActivityTest() {
        LivestockProduction livestockProduction = getLivestockProductionRandomSampleGenerator();
        LivestockActivity livestockActivityBack = getLivestockActivityRandomSampleGenerator();

        livestockProduction.setLivestockActivity(livestockActivityBack);
        assertThat(livestockProduction.getLivestockActivity()).isEqualTo(livestockActivityBack);

        livestockProduction.livestockActivity(null);
        assertThat(livestockProduction.getLivestockActivity()).isNull();
    }
}
