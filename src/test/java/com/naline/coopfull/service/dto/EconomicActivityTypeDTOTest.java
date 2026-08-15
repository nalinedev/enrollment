package com.naline.coopfull.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.naline.coopfull.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EconomicActivityTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EconomicActivityTypeDTO.class);
        EconomicActivityTypeDTO economicActivityTypeDTO1 = new EconomicActivityTypeDTO();
        economicActivityTypeDTO1.setId(1L);
        EconomicActivityTypeDTO economicActivityTypeDTO2 = new EconomicActivityTypeDTO();
        assertThat(economicActivityTypeDTO1).isNotEqualTo(economicActivityTypeDTO2);
        economicActivityTypeDTO2.setId(economicActivityTypeDTO1.getId());
        assertThat(economicActivityTypeDTO1).isEqualTo(economicActivityTypeDTO2);
        economicActivityTypeDTO2.setId(2L);
        assertThat(economicActivityTypeDTO1).isNotEqualTo(economicActivityTypeDTO2);
        economicActivityTypeDTO1.setId(null);
        assertThat(economicActivityTypeDTO1).isNotEqualTo(economicActivityTypeDTO2);
    }
}
