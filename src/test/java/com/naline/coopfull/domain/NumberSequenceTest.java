package com.naline.coopfull.domain;

import static com.naline.coopfull.domain.CooperativeTestSamples.*;
import static com.naline.coopfull.domain.NumberSequenceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NumberSequenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NumberSequence.class);
        NumberSequence numberSequence1 = getNumberSequenceSample1();
        NumberSequence numberSequence2 = new NumberSequence();
        assertThat(numberSequence1).isNotEqualTo(numberSequence2);

        numberSequence2.setId(numberSequence1.getId());
        assertThat(numberSequence1).isEqualTo(numberSequence2);

        numberSequence2 = getNumberSequenceSample2();
        assertThat(numberSequence1).isNotEqualTo(numberSequence2);
    }

    @Test
    void cooperativeTest() {
        NumberSequence numberSequence = getNumberSequenceRandomSampleGenerator();
        Cooperative cooperativeBack = getCooperativeRandomSampleGenerator();

        numberSequence.setCooperative(cooperativeBack);
        assertThat(numberSequence.getCooperative()).isEqualTo(cooperativeBack);

        numberSequence.cooperative(null);
        assertThat(numberSequence.getCooperative()).isNull();
    }
}
