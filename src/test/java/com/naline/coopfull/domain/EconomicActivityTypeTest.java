package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.EconomicActivityTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EconomicActivityTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EconomicActivityType.class);
        EconomicActivityType economicActivityType1 = getEconomicActivityTypeSample1();
        EconomicActivityType economicActivityType2 = new EconomicActivityType();
        assertThat(economicActivityType1).isNotEqualTo(economicActivityType2);

        economicActivityType2.setId(economicActivityType1.getId());
        assertThat(economicActivityType1).isEqualTo(economicActivityType2);

        economicActivityType2 = getEconomicActivityTypeSample2();
        assertThat(economicActivityType1).isNotEqualTo(economicActivityType2);
    }
}
