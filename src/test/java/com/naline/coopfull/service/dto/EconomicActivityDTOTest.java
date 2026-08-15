package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EconomicActivityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EconomicActivityDTO.class);
        EconomicActivityDTO economicActivityDTO1 = new EconomicActivityDTO();
        economicActivityDTO1.setId(1L);
        EconomicActivityDTO economicActivityDTO2 = new EconomicActivityDTO();
        assertThat(economicActivityDTO1).isNotEqualTo(economicActivityDTO2);
        economicActivityDTO2.setId(economicActivityDTO1.getId());
        assertThat(economicActivityDTO1).isEqualTo(economicActivityDTO2);
        economicActivityDTO2.setId(2L);
        assertThat(economicActivityDTO1).isNotEqualTo(economicActivityDTO2);
        economicActivityDTO1.setId(null);
        assertThat(economicActivityDTO1).isNotEqualTo(economicActivityDTO2);
    }
}
