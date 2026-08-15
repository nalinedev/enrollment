package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.CropTestSamples.*;
import static com.naline.coopfull.domain.CropVarietyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropVarietyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CropVariety.class);
        CropVariety cropVariety1 = getCropVarietySample1();
        CropVariety cropVariety2 = new CropVariety();
        assertThat(cropVariety1).isNotEqualTo(cropVariety2);

        cropVariety2.setId(cropVariety1.getId());
        assertThat(cropVariety1).isEqualTo(cropVariety2);

        cropVariety2 = getCropVarietySample2();
        assertThat(cropVariety1).isNotEqualTo(cropVariety2);
    }

    @Test
    void cropTest() {
        CropVariety cropVariety = getCropVarietyRandomSampleGenerator();
        Crop cropBack = getCropRandomSampleGenerator();

        cropVariety.setCrop(cropBack);
        assertThat(cropVariety.getCrop()).isEqualTo(cropBack);

        cropVariety.crop(null);
        assertThat(cropVariety.getCrop()).isNull();
    }
}
