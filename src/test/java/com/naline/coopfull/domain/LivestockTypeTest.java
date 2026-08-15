package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.LivestockTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LivestockTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LivestockType.class);
        LivestockType livestockType1 = getLivestockTypeSample1();
        LivestockType livestockType2 = new LivestockType();
        assertThat(livestockType1).isNotEqualTo(livestockType2);

        livestockType2.setId(livestockType1.getId());
        assertThat(livestockType1).isEqualTo(livestockType2);

        livestockType2 = getLivestockTypeSample2();
        assertThat(livestockType1).isNotEqualTo(livestockType2);
    }
}
