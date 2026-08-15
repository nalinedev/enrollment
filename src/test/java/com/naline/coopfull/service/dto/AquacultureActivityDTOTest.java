package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AquacultureActivityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AquacultureActivityDTO.class);
        AquacultureActivityDTO aquacultureActivityDTO1 = new AquacultureActivityDTO();
        aquacultureActivityDTO1.setId(1L);
        AquacultureActivityDTO aquacultureActivityDTO2 = new AquacultureActivityDTO();
        assertThat(aquacultureActivityDTO1).isNotEqualTo(aquacultureActivityDTO2);
        aquacultureActivityDTO2.setId(aquacultureActivityDTO1.getId());
        assertThat(aquacultureActivityDTO1).isEqualTo(aquacultureActivityDTO2);
        aquacultureActivityDTO2.setId(2L);
        assertThat(aquacultureActivityDTO1).isNotEqualTo(aquacultureActivityDTO2);
        aquacultureActivityDTO1.setId(null);
        assertThat(aquacultureActivityDTO1).isNotEqualTo(aquacultureActivityDTO2);
    }
}
