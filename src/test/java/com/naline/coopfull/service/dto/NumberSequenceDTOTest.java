package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NumberSequenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NumberSequenceDTO.class);
        NumberSequenceDTO numberSequenceDTO1 = new NumberSequenceDTO();
        numberSequenceDTO1.setId(1L);
        NumberSequenceDTO numberSequenceDTO2 = new NumberSequenceDTO();
        assertThat(numberSequenceDTO1).isNotEqualTo(numberSequenceDTO2);
        numberSequenceDTO2.setId(numberSequenceDTO1.getId());
        assertThat(numberSequenceDTO1).isEqualTo(numberSequenceDTO2);
        numberSequenceDTO2.setId(2L);
        assertThat(numberSequenceDTO1).isNotEqualTo(numberSequenceDTO2);
        numberSequenceDTO1.setId(null);
        assertThat(numberSequenceDTO1).isNotEqualTo(numberSequenceDTO2);
    }
}
