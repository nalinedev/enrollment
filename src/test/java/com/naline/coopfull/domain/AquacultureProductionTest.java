package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.AquacultureActivityTestSamples.*;
import static com.naline.coopfull.domain.AquacultureProductionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AquacultureProductionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AquacultureProduction.class);
        AquacultureProduction aquacultureProduction1 = getAquacultureProductionSample1();
        AquacultureProduction aquacultureProduction2 = new AquacultureProduction();
        assertThat(aquacultureProduction1).isNotEqualTo(aquacultureProduction2);

        aquacultureProduction2.setId(aquacultureProduction1.getId());
        assertThat(aquacultureProduction1).isEqualTo(aquacultureProduction2);

        aquacultureProduction2 = getAquacultureProductionSample2();
        assertThat(aquacultureProduction1).isNotEqualTo(aquacultureProduction2);
    }

    @Test
    void aquacultureActivityTest() {
        AquacultureProduction aquacultureProduction = getAquacultureProductionRandomSampleGenerator();
        AquacultureActivity aquacultureActivityBack = getAquacultureActivityRandomSampleGenerator();

        aquacultureProduction.setAquacultureActivity(aquacultureActivityBack);
        assertThat(aquacultureProduction.getAquacultureActivity()).isEqualTo(aquacultureActivityBack);

        aquacultureProduction.aquacultureActivity(null);
        assertThat(aquacultureProduction.getAquacultureActivity()).isNull();
    }
}
