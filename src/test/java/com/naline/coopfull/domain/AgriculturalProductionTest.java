package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.AgriculturalActivityTestSamples.*;
import static com.naline.coopfull.domain.AgriculturalProductionTestSamples.*;
import static com.naline.coopfull.domain.CropTestSamples.*;
import static com.naline.coopfull.domain.CropVarietyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AgriculturalProductionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgriculturalProduction.class);
        AgriculturalProduction agriculturalProduction1 = getAgriculturalProductionSample1();
        AgriculturalProduction agriculturalProduction2 = new AgriculturalProduction();
        assertThat(agriculturalProduction1).isNotEqualTo(agriculturalProduction2);

        agriculturalProduction2.setId(agriculturalProduction1.getId());
        assertThat(agriculturalProduction1).isEqualTo(agriculturalProduction2);

        agriculturalProduction2 = getAgriculturalProductionSample2();
        assertThat(agriculturalProduction1).isNotEqualTo(agriculturalProduction2);
    }

    @Test
    void agriculturalActivityTest() {
        AgriculturalProduction agriculturalProduction = getAgriculturalProductionRandomSampleGenerator();
        AgriculturalActivity agriculturalActivityBack = getAgriculturalActivityRandomSampleGenerator();

        agriculturalProduction.setAgriculturalActivity(agriculturalActivityBack);
        assertThat(agriculturalProduction.getAgriculturalActivity()).isEqualTo(agriculturalActivityBack);

        agriculturalProduction.agriculturalActivity(null);
        assertThat(agriculturalProduction.getAgriculturalActivity()).isNull();
    }

    @Test
    void cropTest() {
        AgriculturalProduction agriculturalProduction = getAgriculturalProductionRandomSampleGenerator();
        Crop cropBack = getCropRandomSampleGenerator();

        agriculturalProduction.setCrop(cropBack);
        assertThat(agriculturalProduction.getCrop()).isEqualTo(cropBack);

        agriculturalProduction.crop(null);
        assertThat(agriculturalProduction.getCrop()).isNull();
    }

    @Test
    void cropVarietyTest() {
        AgriculturalProduction agriculturalProduction = getAgriculturalProductionRandomSampleGenerator();
        CropVariety cropVarietyBack = getCropVarietyRandomSampleGenerator();

        agriculturalProduction.setCropVariety(cropVarietyBack);
        assertThat(agriculturalProduction.getCropVariety()).isEqualTo(cropVarietyBack);

        agriculturalProduction.cropVariety(null);
        assertThat(agriculturalProduction.getCropVariety()).isNull();
    }
}
