package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.CooperativeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CooperativeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cooperative.class);
        Cooperative cooperative1 = getCooperativeSample1();
        Cooperative cooperative2 = new Cooperative();
        assertThat(cooperative1).isNotEqualTo(cooperative2);

        cooperative2.setId(cooperative1.getId());
        assertThat(cooperative1).isEqualTo(cooperative2);

        cooperative2 = getCooperativeSample2();
        assertThat(cooperative1).isNotEqualTo(cooperative2);
    }
}
