package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.CropTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CropTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Crop.class);
        Crop crop1 = getCropSample1();
        Crop crop2 = new Crop();
        assertThat(crop1).isNotEqualTo(crop2);

        crop2.setId(crop1.getId());
        assertThat(crop1).isEqualTo(crop2);

        crop2 = getCropSample2();
        assertThat(crop1).isNotEqualTo(crop2);
    }
}
